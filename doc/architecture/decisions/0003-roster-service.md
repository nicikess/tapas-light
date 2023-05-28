# 3. Roster service

Date: 2021-09-29

## Status

Accepted

## Context

The Roster Service finds a suitable internal Executor based on the requirements of the Task. If no internal Executor can be found the Roster Service communicates with the Auction Service to find an external Executor.

## Decision

We will create a Roster Service, which handles the forwarding of Tasks to either the Auction House Service or the Executor Pool based on the type of the Task.

## Consequences

The separation from the other services guarantees availability, scalability and fault-tolerance. If the Roster Service has any failure, Tasks and Executors can still be managed through the other services.
