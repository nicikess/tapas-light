# 16. Code sharing

Date: 2021-11-19

## Status

Accepted

Superseds [11. Duplicating code due to microservice](0011-duplicating-code-due-to-microservice.md)

## Context

This ADR updates the outdated 0010 ARD

In our Tapas application we have different services that use the same code. Options are to use either a shared service or a shared library.

## Decision

We decided to use a shared library for our tapas application. After handing in the first two assignments we noticed that we had to adjust our code to fulfill the requirements for the application. Using a shared library minimizes these adjustments. Compared to the shared service we have better performance, fault tolerance and scalability. This is because shared libraries are bound to services at compile time, which reduces runtime errors as well. We only included classes that don’t contain any domain logic in our library. The only exception is the task class which we included as well, since the class is used in all services and therefore we wanted a uniform representation of the tasks.


## Consequences

In our common library we outsourced functionalities that are used by several other services. Task and Executor classes and representations are located in this library. Besides, we also manage the addresses of our services in a dedicated class. This enables us the switch easier to our local addresses. Additionally, we have a Webclient class which simplifies the use of HTTP request methods in our adapters. Since the SelfValidating class is also used in all of our services we externalized this class to the common library as well.