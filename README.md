## Fawry Technical Task 

Below is a list of the major changes made to improve the readability, and maintainability of the project:
 - Re-wrote most of the code base to be in a more modern, functional style.
 - Changed the Date class to the modern and recommended alternative: ZonedDateTime to preserve the zone-id so that the application can be multi-regional.
 - Majorly simplified many if-else statements and completely replaced others to use loops instead when applicable.
 - Made an Enum type to hold the vehicle type value, to prevent writing the type name twice (ones in the child-classes and other in the toll-free vehicles) which prevents typos from affecting the correctness of the program.

All of this cannot be done comfortably and confidently without first writing a comprehensive test suite that makes sure every change I make adheres to business rules.
Below is a list of all the test scenarios that can be found in the file "src/test/java/cam/fawry/task/services/TollCalculatorTest.java":
 - Calculating fees for all holidays in the year 2013, and making sure all of them are 0.
 - Calculating fees for weekends, and making sure all of them are 0.
 - Calculating fees for all fee-free vehicle types, and making sure all of them are 0.
 - Calculating fees for all predetermined time slots with different fees, making sure they adhere to business rules.
 - Calculating fees for two dates with less than an hour in between them, to show it returns the bigger fare.

And as with any important application, there must exist comprehensive, easy-to-understand, and dynamic logs. 
Making me introduce a whole new vertical in the application solely for logging purposes, with an extensible interface and two implementations out-of-the-box, one that writes logs to the application console, and the other writes the logs in an embedded .txt file.
All of this is supported by a "Loggers" class that has final static variables for global, shared, and easy access for all predefined logging classes in the application.

I would also like to share some of the "pet peeves" that I faced and rationalize some of my decisions and why I chose one way over the other:
 - Should/Can I change the public contracts?
         This was the first question that came to my mind when I saw the heavy usage of the "Date" class, which there is a more modern alternative to (the java.time package).
         It is known that changing a public contract is a big no-no in application developments especially if other systems depend on your abstractions.
         In this case, however, I decided to change the contract since it stated in the README.md file that the application is not in production yet, allowing me to make breaking changes.
 -  Should the getTollFee method be a static method since it doesn't depend on any instance-specific state?
         This is a question I stayed with for a long time because I am a fan of avoiding making objects when possible and this looked like a case where this method can be just a static method.
         But when the logging abstraction was introduced I had to choose between making the method accept a Logger in the parameter list, which I wasn't a fan of, or putting this dependency in the constructor list making the method depend on an instance variable, so it cannot be static.
         I chose the second option with the rationale that we can get the benefits of both approaches by making the TollCalculator class a singleton with a shared, global, access point.
 - The Logging implementations, as they stand now, are public final static variables, but the issue with that is they both are eagerly loaded and if I only used the console logging implementation, then I also have the file logger taking precious memory space without ever being used, a solution to this is to make the implementations singleton as well with a lazy-loading approach, but to avoid over-engineering at this small scale, I decided it is better to leave it that way.
