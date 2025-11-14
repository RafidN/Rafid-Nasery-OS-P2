### Bank.java
- CountDownLatch bankOpen: customers wait until all tellers ready
- Semaphore doorEnter: door capacity of 2
- Semaphore manager: 1 manager to approve withdrawals
- Semaphore safe: safe capacity of 2
- ConcurrentLinkedQueue<Integer> readyTellersQ / Semaphore readyTellersCount: customers can grab available teller
- Per-teller Semaphores include:
  - waitForCustomer, introduced, askedForTxn, txnProvided, doneSignal, customerLeft
- Atomics:
  - tellersReady, served, closing

### Teller.java
- Thread representing Teller
- Loop through availability -> wait for customer, ask transaction, get manager for withdraw, enter safe, finish,
wait for customer to leave, repeat until closing

### Customer.java
- Thread representing Customer
- Wait for bank open -> pass the door -> select ready teller -> introduce -> withdraw or depo -> wait for completion, leave

### Log.java
- Logging utility to print out following output-example.txt format

### Main.java
- Boots up the objects/threads and joins the threads

## Running Steps:

```bash
javac -d out src/*.java  
java -cp out Main > run.log
```

The output will be in the run.log file generated

## Public repo link:

https://github.com/RafidN/Rafid-Nasery-OS-P2