# 20. Task assignment

Date: 2021-11-25

## Status

Accepted

## Context

A Task is forwarded to the Executor Pool, where the Task gets assigned to an Executor.

## Decision

We assign a Task to an Executor. The Executor Pool is responsible to inform the other parts of the application when a Task gets assigned. In our application the Roster only has the responsibility to forward Task either to the Executor Pool for internal or to the Auction House for external execution. Therefore, we decided not to persist the status of the Roster, but the assignments of Tasks in the Executor Pool.

## Consequences

The Executor Pool has to be able to persist assignments of Tasks.