# 7. Communication between Task List and Roster

Date: 2021-10-17

## Status

Accepted

Superseded by [17. Executor capabilities](0017-executor-capabilities.md)

## Context

The Roster has to be notified if a new Task is created. He has the responsibility to either forward a Task to the Auction Service or the Executor Pool. The decision on where to forward the Task is based on the capabilities of the Executor Pool.

## Decision

It is crucial to notify the Roster if a new Task is added to the Task List. Adding a Task to the Task List triggers a task created event. The Roster then decides to forward the Task either to the Auction House or to the Executor Pool. The Roster Service asks the Executor Pool for the different Task types that the Executors of the Pool are able to execute. If the Task is of a type which the Pool is not able to run, he gets forwarded to the Auction Service.  

## Consequences

An event handler has to be implemented in the Roster microservice. As soon as a new Task is added to the Task List, an event is published and the event handler in the Roster microservice handles the new event. The logic on where to forward the Tasks has to be implemented.
