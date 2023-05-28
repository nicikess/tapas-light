# 21. Executor registration

Date: 2021-11-25

## Status

Accepted

Superseds [17. Executor capabilities](0017-executor-capabilities.md)

## Context

Updates the ADR 0017 from 2021-11-21:
The Executor Pool needs to be aware of which Executors are available for executing.

## Decision

The Executors have to register them self with the Executor Pool. For this we use our Executor Base service. After the registration the Executor Pool knows of their existence and can assign Tasks to the Executors.

## Consequences

The Executors have to be able to register them self via HTTP.