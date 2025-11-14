import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Bank {
    public static final int NUM_TELLERS = 3;
    public static final int NUM_CUSTOMERS = 50;

    public static final int BEFORE_MIN = 0, BEFORE_MAX = 100;
    public static final int MGR_MIN = 5, MGR_MAX = 30;
    public static final int SAFE_MIN = 10, SAFE_MAX = 50;

    public final CountDownLatch bankOpen = new CountDownLatch(1);
    public final AtomicInteger tellersReady = new AtomicInteger(0);

    public final Semaphore doorEnter = new Semaphore(2);
    public final Semaphore manager = new Semaphore(1);
    public final Semaphore safe = new Semaphore(2);

    public final Queue<Integer> readyTellersQ = new ConcurrentLinkedQueue<>();
    public final Semaphore readyTellersCount = new Semaphore(0);

    public final Semaphore[] waitForCustomer = new Semaphore[NUM_TELLERS];
    public final Semaphore[] introduced = new Semaphore[NUM_TELLERS];
    public final Semaphore[] askedForTxn = new Semaphore[NUM_TELLERS];
    public final Semaphore[] txnProvided = new Semaphore[NUM_TELLERS];
    public final Semaphore[] doneSignal = new Semaphore[NUM_TELLERS];
    public final Semaphore[] customerLeft = new Semaphore[NUM_TELLERS];

    public final int[] currentCustomerId = new int[NUM_TELLERS];
    public final Customer.TxnType[] currentTxn = new Customer.TxnType[NUM_TELLERS];

    public final AtomicInteger served = new AtomicInteger(0);
    public final AtomicBoolean closing = new AtomicBoolean(false);

    public Bank()
    {
        for (int i=0; i<NUM_TELLERS; i++)
        {
            waitForCustomer[i] = new Semaphore(0);
            introduced[i] = new Semaphore(0);
            askedForTxn[i] = new Semaphore(0);
            txnProvided[i] = new Semaphore(0);
            doneSignal[i] = new Semaphore(0);
            customerLeft[i] = new Semaphore(0);
            currentCustomerId[i] = -1;
            currentTxn[i] = Customer.TxnType.DEPOSIT;
        }
    }

    public void announceTellerReady(int tellerId)
    {
        readyTellersQ.add(tellerId);
        readyTellersCount.release();
    }

    public Integer nextReadyTeller() throws InterruptedException
    {
        readyTellersCount.acquire();
        return readyTellersQ.poll();
    }

    public static void sleepQuiet(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException ignored) {}
    }

    public static int rand(int a, int b)
    {
        return ThreadLocalRandom.current().nextInt(a, b + 1);
    }
}
