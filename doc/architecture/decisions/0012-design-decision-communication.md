# 12. Design Decision Communication

Date: 2021-10-17
Updated: 2021-12-22

## Status

Accepted

## Context

For the different parts of the system a communication decision needs to be taken. On a high level the communication is planned to look like as follows. 
As soon as a new Task is submitted to the Task List Service, the Roster Service will be notified, which forwards a newly created task to the Executor Pool which then searches for a suitable Executor. If no Executor can be found, the Roster Service will be notified and an external Executor search will be performed.

## Decision

The API uses mainly synchronous communication because tasks are slow and are executed quickly. We used asynchronous communication for the WebSub hub notification because those notification requests should not block the main API request during the time of execution.

## Consequences

Complexity of the whole architecture increases, since we are using different communication styles. 
