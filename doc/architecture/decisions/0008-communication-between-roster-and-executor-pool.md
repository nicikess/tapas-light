# 8. Communication between Roster and Executor Pool

Date: 2021-10-17

## Status

Accepted

Superseded by [17. Executor capabilities](0017-executor-capabilities.md)

## Context

The Roster can forward a new Task to the Executor Pool. The Executor Pool has to find a suitable internal executor, which then gets assigned to the Task.

## Decision

Due to the implementation of an Executor Pool, the Roster does not need to communicate with an Executor directly. It also makes it easier to differentiate between an internal and an external Executor.

## Consequences

Communication between Executors and the Roster service has to be back propagated through the Executor Pool.
