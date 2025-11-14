public class Teller extends Thread{
    private final int id;
    private final Bank bank;

    public Teller(int id, Bank bank)
    {
        super("Teller-" + id);
        this.id = id;
        this.bank = bank;
    }

    @Override
    public void run()
    {
        Log.p("Teller", id, "", "ready to serve");
        if (bank.tellersReady.incrementAndGet() == Bank.NUM_TELLERS)
        {
            bank.bankOpen.countDown();
        }

        while (!bank.closing.get())
        {
            Log.p("Teller", id, "", "waiting for a customer");
            bank.announceTellerReady(id);

            try
            {
                bank.waitForCustomer[id].acquire();
            } catch (InterruptedException ignored) {}
            if (bank.closing.get()) break;

            try
            {
                bank.introduced[id].acquire();
            } catch (InterruptedException ignored) {}

            int custId = bank.currentCustomerId[id];
            Log.p("Teller", id, "Customer " + custId, "serving a customer");
            Log.p("Teller", id, "Customer " + custId, "asks for transaction");
            bank.askedForTxn[id].release();

            try
            {
                bank.txnProvided[id].acquire();
            } catch (InterruptedException ignored) {}

            Customer.TxnType txn = bank.currentTxn[id];
            if (txn == Customer.TxnType.WITHDRAW)
            {
                Log.p("Teller", id, "Customer " + custId, "handling withdrawal transaction");
                Log.p("Teller", id, "Customer " + custId, "going to the manager");
                try
                {
                    bank.manager.acquire();
                    Log.p("Teller", id, "Customer " + custId, "getting manager's permission");
                    Bank.sleepQuiet(Bank.rand(Bank.MGR_MIN, Bank.MGR_MAX));
                    Log.p("Teller", id, "Customer " + custId, "got manager's permission");
                } catch (InterruptedException ignored) {}
                finally
                {
                    bank.manager.release();
                }
            }
            else
            {
                Log.p("Teller", id, "Customer " + custId, "handling deposit transaction");
            }

            Log.p("Teller", id, "Customer " + custId, "going to safe");
            try
            {
                bank.safe.acquire();
                Log.p("Teller", id, "Customer " + custId, "enter safe");
                Bank.sleepQuiet(Bank.rand(Bank.SAFE_MIN, Bank.SAFE_MAX));
                Log.p("Teller", id, "Customer " + custId, "leaving safe");
            } catch (InterruptedException ignored) {}
            finally
            {
                bank.safe.release();
            }

            if (txn == Customer.TxnType.WITHDRAW)
            {
                Log.p("Teller", id, "Customer " + custId, "finishes withdrawal transaction");
            }
            else
            {
                Log.p("Teller", id, "Customer " + custId, "finishes deposit transaction");
            }

            Log.p("Teller", id, "Customer " + custId, "wait for customer to leave");
            bank.doneSignal[id].release();
            try
            {
                bank.customerLeft[id].acquire();
            } catch (InterruptedException ignored) {}
        }
        Log.p("Teller", id, "", "leaving for the day");
    }
}
