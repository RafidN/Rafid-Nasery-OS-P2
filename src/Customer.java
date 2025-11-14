public class Customer extends Thread{
    public enum TxnType {DEPOSIT, WITHDRAW}

    private final int id;
    private final Bank bank;

    public Customer(int id, Bank bank)
    {
        super("Customer-" + id);
        this.id = id;
        this.bank = bank;
    }

    @Override
    public void run()
    {
        TxnType txn = (Bank.rand(0, 1) == 0) ? TxnType.DEPOSIT : TxnType.WITHDRAW;
        Log.p("Customer", id, "", "wants to perform a " + txn.name().toLowerCase() + " transaction");

        Bank.sleepQuiet(Bank.rand(Bank.BEFORE_MIN, Bank.BEFORE_MAX));

        try
        {
            bank.bankOpen.await();
        } catch (InterruptedException ignored) {}

        Log.p("Customer", id, "Door", "going to bank.");
        try
        {
            bank.doorEnter.acquire();
            Log.p("Customer", id, "Door", "entering bank.");
        } catch (InterruptedException ignored) {}
        finally
        {
            bank.doorEnter.release();
        }

        Log.p("Customer", id, "Line", "getting in line");

        Integer tellerId = null;
        try
        {
            tellerId = bank.nextReadyTeller();
        } catch (InterruptedException ignored) {}
        if (tellerId == null) return;

        Log.p("Customer", id, "Teller " + tellerId, "selecting a teller");
        bank.currentCustomerId[tellerId] = id;

        bank.waitForCustomer[tellerId].release();
        Log.p("Customer", id, "Teller " + tellerId, "introduces itself");
        bank.introduced[tellerId].release();

        try
        {
            bank.askedForTxn[tellerId].acquire();
        } catch (InterruptedException ignored) {}
        Log.p("Customer", id, "Teller " + tellerId, "asks for " + txn.name().toLowerCase() + " transaction");
        bank.currentTxn[tellerId] = txn;
        bank.txnProvided[tellerId].release();

        try
        {
            bank.doneSignal[tellerId].acquire();
        } catch (InterruptedException ignored) {}
        Log.p("Customer", id, "Teller " + tellerId, "leaves teller");
        bank.customerLeft[tellerId].release();

        Log.p("Customer", id, "Door", "leaves the bank");
        bank.served.incrementAndGet();

    }
}
