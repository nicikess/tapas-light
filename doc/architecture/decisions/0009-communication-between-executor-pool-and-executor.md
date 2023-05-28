# 9. Communication between Executor Pool and Executor

Date: 2021-10-17

## Status

Accepted

## Context

The Executor Pool finds available Executors and forwards the Tasks directly to the Executors, based on the type of the Task. The current Executors are a Calculator Service, a Robot Service and a Temperature Service.

## Decision

An Executor can register himself to the Executor Pool, where then a Task could be solved. Due to the implementation, the Executor does not need to communicate directly with the Roster.

## Consequences

If an Executor finishes a Task, he notifies the Executor Pool.
