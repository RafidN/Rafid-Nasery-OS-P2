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

