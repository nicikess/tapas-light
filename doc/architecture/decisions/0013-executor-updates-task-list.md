# 13. Executor updates Task List

Date: 2021-11-14

## Status

Accepted

## Context

Task status needs to be updated in the Task List. A Task can have the open, assigned, running or executed status.

## Decision

We need to update the status of a Task when the execution is finished. We decided that the Executor himself updates a Task List when he finishes the execution of a Task. Initially we intended the Executor to give feedback to the Pool which then informs the Roster which finally updates the Task Status in the Task list. However, this detour does not offer any advantages because the Executor Pool and the Roster do not need to know the status of the task. That is why we decided to use the straightforward variant.

## Consequences

An Executor must be able to update the Task List. The Task List can either be the internal List or an external List of another group.
