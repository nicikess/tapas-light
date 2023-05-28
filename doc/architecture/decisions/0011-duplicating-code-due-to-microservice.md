# 11. Duplicating code due to microservice

Date: 2021-10-17

## Status

Accepted

Superseded by [16. Code sharing](0016-code-sharing.md)

## Context

Due to microservice architecture, the goal is that each service runs by itself and hence, code duplications are necessary. Every service has some identical parts due to the functionality requirements and other requirements.
When establishing the Roster and Executor Pool / Executor services, many modules were duplicated. 

## Decision

Although code duplications are not desired by itself, the context given with the microservice architecture leads to the decision that this architectural outcome is accepted due to the many advantages of the microservice architecture.

## Consequences

When modifying a part of the code that is duplicated, all services / modules need to be identified that contain the specific part and need to be updated. This may lead to inconsistencies in the different services over time. However, the microservice architecture offers many advantages so this code duplication is accepted. 
