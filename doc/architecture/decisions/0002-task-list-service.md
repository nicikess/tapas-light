# 2. Task List service

Date: 2021-09-29

## Status

Accepted

## Context

Tasks are added to the Task List at the time they are created. The Task List Domain communicates with the Roster Domain for the execution of the Task.

## Decision

The Task List service is a standalone service and responsible for handling everything related to Tasks. The service has to be able to communicate with the Roster Service for the execution of Tasks. Additionally, we have to ensure that Tasks that are forwarded from other Auction Houses can be executed. The system has to be able to handle hundreds of Tasks - we assured this with JMeter testing. 

## Consequences

We need a standalone Task Service which fulfills the throughput and availability requirements.