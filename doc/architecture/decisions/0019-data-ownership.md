# 19. Data ownership

Date: 2021-11-21

## Status

Accepted

## Context

As seen in the lecture it is advisable that the data ownership is clearly regulated and that a service is the data owner of a table it writes to. If multiple services write to the same table things may get complicated.

## Decision

As a result, and our approach to keep things as simple as possible, clear data ownership and hence allocating a table to a service is a goal of our approach. We have decided to add the tables to the respective bounded contexts of a service and having a clear data ownership structure.

Since we value Availability und Partition tolerance, we designed the system around BASE transactions, therefore we assume that all services within the scope of a distributed transaction have some basic level of availability. Therefore we accept soft states - which could lead to inconsistency between data sources. Additionally we have to ensure that the data is synchronised into a consistent state at some unspecified point in time.

## Consequences

Some additional communication between services may result from this decision as a single service is responsible for writing to its table, however this results in many advantages and also simplifies the data persistence implementation.
