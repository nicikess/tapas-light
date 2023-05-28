# 6. Executor Domain service

Date: 2021-09-29

## Status

Accepted

## Context

An Executor starts executing a Task. If the Executor finishes his Task, he is available for other tasks.

## Decision

The Executor domain is a service on its own in our application. Reasons for that are service functionality and fault tolerance. The service functionality disintegrator is important since Executors do different Tasks e.g. printing or rotating an arm of a robot. Additionally, fault tolerance is crucial for our system. If an Executor is blocked or fails, the other Executors must not be affected by those malfunctions.

## Consequences

Since we found two disintegrators for our Executor domain, this domain is embedded in its own microservice.
