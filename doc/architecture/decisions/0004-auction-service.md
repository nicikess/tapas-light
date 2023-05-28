# 4. Auction service

Date: 2021-09-29

## Status

Accepted

## Context

If no internal Executor has been found, an Auction will be placed to allow external Auction Houses to bid on a specific Task. The creation of an Auction is initiated by the Executor Pool Service where the coordination of Task assignment happens.

## Decision

The Auction Domain is modeled as an own Auction Service, that handles independently the needed actions to find a suitable external Executor for a specific Task. If an Executor can be found the Auction House Service gets notified.

## Consequences

The separation from the other services guarantees fault-tolerance, scalability, extensibility and availability. For example if the Auction Service is unavailable the remainder of the system stays intact and allows further planning of Tasks with capabilities for an internal Executor.
