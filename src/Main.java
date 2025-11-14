public class Main {
    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();
        Teller[] tellers = new Teller[Bank.NUM_TELLERS];
        for (int i=0; i<Bank.NUM_TELLERS; i++)
        {
            tellers[i] = new Teller(i, bank);
            tellers[i].start();
        }

        Customer[] customers = new Customer[Bank.NUM_CUSTOMERS];
        for (int i=0; i<Bank.NUM_CUSTOMERS; i++)
        {
            customers[i] = new Customer(i, bank);
            customers[i].start();
        }

        for (Customer c : customers)
        {
            c.join();
        }

        bank.closing.set(true);
        for(int i=0; i<Bank.NUM_TELLERS; i++)
        {
            bank.waitForCustomer[i].release();
        }
        for(Teller t : tellers)
        {
            t.join();
        }

        System.out.println("The bank closes for the day.");
    }
}