# Synchronization-and-Mutex, Spring 2018
This is a project for my Spring 2018 Operating Systems class to preform synchronization and mutual exclusion for multiple consumers and producers accessing the same resource buffer using both semaphores and Java synchronized classes.

The program is two have two parent threads producing money and putting it into a bank account, and a number of child threads withdrawing from the same bank account. We were to do this once using semaphores and once using Java's synchronized classes. I did both in the same program, allowing the user to select which implementation to use from the GUI.

A full description of the assignment is available in Assignment4Description.pdf

I made a simple GUI for the program using Netbean's GUI maker. An example image of the GUI running is available in SynchronizationGUIExample.png

Assignment4.jar is a jar version of the program to run if anyone wants to see it in action.

There are multiple classes, but they are all in the same file for simplicity's sake.
