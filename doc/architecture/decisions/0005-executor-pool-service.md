# 5. Executor Pool service

Date: 2021-09-29

## Status

Accepted

## Context

The Executor Pool holds, in its own database, a list of executors along their status and a list of their capabilities. Executors can be added or removed from the Executor Pool. 

## Decision

The Executor Pool is its own service due to the fact that it should be scalable and the other parts of the system, especially the Executors, can fail without affecting the Executor Pool.

## Consequences

This allows being able to add and remove Executors from the Executor Pool without having to redeploy. If an Executor fails, this only affects this specific Executor along the Task being executed. If the Executor Pool Service fails, no new allocations of Executors to Tasks can take place, however the ongoing Tasks will continue.