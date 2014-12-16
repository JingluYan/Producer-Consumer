Producer-Consumer
=================
Java program to im- plement the following producer-consumer situation with the use of semaphores: 
- The manager of a company employs 3 secretaries who are of varying ability, but who all work extremely fast.
- Secretary A is the most experienced secretary and is capable of typing up a letter in 1 second.
- Secretary B is less experienced and is capable of typing up a letter in 2 seconds.
- Secretary C is the junior secretary and is capable of typing up a letter in 4 seconds.

When a secretary has typed up a letter he leaves it in the manager’s tray forher to remove and sign. 
The manager removes and signs a letter from the tray once every 2 seconds.
The tray can hold a maximum of 5 letters at a time. The tray’s limited capacity sometimes causes the 
various workers to be delayed. For example, if the tray is full after a letter has been typed, the 
secretaries must wait until the manager makes a space available before they can add another letter 
to the tray. Similarly, the manager must wait for at least one letter to appear in the tray before 
she can take it out and sign it.
