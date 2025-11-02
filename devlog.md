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

