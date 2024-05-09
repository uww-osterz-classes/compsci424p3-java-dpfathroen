[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/0Arn_6wA)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-7f7980b617ed060a017424585567c406b6ee15c891e84e1186181d67ecf80aa0.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=15017322)
# UWW COMPSCI 424 Program 3 in Java

## Your notes for me

*If you have notes or messages for me, please put them here so I can see them.*

## About this repository

Use this template for Operating Systems Program 3 in Java.

To help you get started quickly, the starter code file includes code to process the two command-line arguments and begin processing the setup file. You may use, modify, or delete this code. There are also hints about other steps to complete in the main program.

Setup files for the two provided test cases are included in the repository's top-level directory as `424-p3-test1.txt` and `424-p3-test2.txt`. If you are using GitHub Codespaces or Visual Studio Code, I have included predefined run configurations for these test cases using manual or automatic mode.

Please follow these rules to make it easier to compile and run correctly. This template is set up with these rules in mind.

* **Your main class must be named `Program3`** because GitHub will use the run command `java Program3` to run your code. You can add, rename, modify, or combine other files as needed. 
* You will need to add other classes to complete the program, but there's more than one way to do this. Create a class structure that works for you. If you want hints, please ask me; I can give you some.
* Your Java source code files (`.java` files) must be stored in the `app/src/main/java/compsci424/p3/java` directory within your project environment. The Gradle build system will compile all `.java` files in this directory when you issue a `gradle run` command. (On Windows, use backslashes '\\' instead of forward slashes '/'.)
* Your Java source code files must also include the package declaration `package compsci424.p3;`.

This template uses the Gradle build system to build and run your Java code. All of the major Java IDEs (Eclipse, IntelliJ, and NetBeans) support Gradle, and so does Visual Studio Code. You might need to install a Gradle plugin or change a "Build System" setting in your project settings to enable Gradle. Try it and see what happens.

You may use any standard Java class or feature that is supported in Java 17.

If you encounter problems, please [email me](osterz@uww.edu) with a description of what's happening. I will look at your repository on GitHub to try to help you debug.

## Advice for Java programmers

This advice is also posted on the Program 3 page on Canvas. Updates, if any, will be posted there.

*   Use the standard Java [Thread class](https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/lang/Thread.html), and possibly the [Runnable interface](https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/lang/Runnable.html), to make your program multithreaded.
*   There is a a built-in Semaphore class (`java.util.concurrent.Semaphore`) that you can use in this program, just like in Program 2.
*   Oracle's Java tutorials provide a good quick introduction to [Synchronized Methods](https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html) along with [Intrinsic Locks and Synchronization](https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html) in Java. You might also want to take a look at other classes in the [java.util.concurrent package](https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/util/concurrent/package-summary.html) of the Java API.
*   Oracle Java tutorial page on [accepting command-line arguments in Java](https://docs.oracle.com/javase/tutorial/essential/environment/cmdLineArgs.html). You may also need to set up some custom build settings in your IDE to provide command-line arguments when you run your code.
