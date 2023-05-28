# 1. Design decision Task structure

Date: 2021-09-29

## Status

Accepted

## Context

Our discussion has resolved around two architectural styles, namely the service based architecture and the microservice architecture. Naturally we have also evaluated the hybrid form of the two, the service based microservice architecture.

## Decision

Due to requirements of individual and independent functionality of the services, we have decided to go for the microservice architecture.

## Consequences

The microservice architecture allows independent working of the individual components, even if one or multiple services fail. Evidently the system won’t provide the full functionality if one microservice is unavailable, but the remainder of the system stays accessible and running properly.
