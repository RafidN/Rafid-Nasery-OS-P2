### Nov 2 2025, 2:04 PM

#### First Look at the Project

I was dealing with a fever during the class discussion on the project and so I was absent
during lecture. Today I just want to get the git repo set up for the project so I'm able to work
on it probably next week. I also want to give the project document a read and will leave my 
understanding of the project below.

#### Notes for planning etc.

- Using 3 Java threads for the Teller, 50 Customers
- Bank will probably be a class with reusable functions
- Will use semaphores most likely 
  - Only 2 customers may enter the door at once
  - Only 1 teller can consult the manager at a time
  - At most 2 tellers inside the Safe at once
  - Can open the gate only if all 3 tellers are ready
- Matching customers to tellers is a queue (FIFO)
  - thread-safe queue storing teller IDs, have a counting semaphore
  - Customers just pop an ID off the queue
- Tellers have semaphores for stages of interactions almost like a handshake with the Customer
  - waitForCustomer, introduced, askedForTxn, txnProvided, done, left
  - Probably best to leave logs before and after each interaction stage

### Nov 14 2025, 10:37 AM

#### This session's goals:

- I have had exams all week including the OS exam, and so haven't been able to work on the project.
- Will try to complete the entire project today so I can free up my Sunday.
- Checklist of items to do:
- Think about how package structure/classes should be organized, use OOP where it makes sense
- Include logging throughout and match the output example provided
- Create Bank, Teller, and Customer
- Implement Main to begin the simulation for the project of 3 tellers and 50 customers

#### Notes during Development:

- I forgot about the Door, need to make sure that exists and limits 2 entries at a time
- Bank works as a producer/consumer system, Customers produce work and the Tellers consume 
- Used parallel arrays indexed by TellerId for ease of access and simplicity
- Used CountDownLatch for bankOpen so customers can start being generated early, wait, and then
rush to the door when the tellers are ready/set up, to match the output-example provided.
- Kept the main thread responsible for just start/stop and Bank setup.
- Used semaphores as primary primitive for doorEnter, manager, and safe
- Tellers have per-teller semaphores like waitForCustomer, introduced, askedForTxn, txnProvided,
doneSignal, and customerLeft to represent the step-by-step process a Teller-Customer relatioship
goes through
- ignored nearly every InterruptException to allow for Interrupts
- Used a ConcurrentLinkedQueue<Integer> for readyTellersQ with a counting Semaphore
readyTellersCount to implement a BlockingQueue without any extra libraries
  - Because of this we only need to pass int tellerId and not some object
- readyTellersCount.acquire() makes sure that customers block until at least 1 teller is ready
- Used AtomicInteger for tellersReady, served, and closing objects, as it lets tellers increment
and detect when all tellers are ready, is thread safe without global locks, and a good shutdown
flag
- Semaphore acquire -> release pairs make it easy to see what happens before whatever action 
between a customer and teller step
  - Atomics here guarantee visibility of closing flips
- Using basic arrays for teller fields like ids/txn/semaphores for simplicity
- Made TxnType an enum for DEPOSIT and WITHDRAWAL for ease
- Created Log class just to format print statements, this will be input redirected to a file
  - Tried my best to copy format from output-example given
- Used "rand(a, b)" to get thread-safe randomness on threads 
- Main thread sets closing = true before unblocking the tellers 
- Releases one waitForCustomer per teller to break them out of blocking "acquire()" on shutdown
- Could've just used BlockingQueue<Integer> but would be much harder to get teller Logs
- Tested on 10 customers 2 tellers, seemed good so assuming it works for 50x3
- Used ConcurrentLinkedQueue for Lock-free ready-teller queue
- Could've added more objects for OOP like a TellerContext object with all the parallel array info

### Nov 14 2025, 2:07PM

#### SESSION REFLECTION

Did I encounter any problems you have not already wrote about?
- The ordering of messages between teller and customer were out of order a lot of the times, had 
to experiment with try catch blocks and throw that everywhere to get the order correct
- Was very hard to understand if things were working properly especially the Door so just added and
removed some error logs in the console for myself.

Did I accomplish my goal for this session?
- Yes the program is complete.