# Demo

## Logs

- https://tapas-tasks.86-119-35-199.nip.io/logs
- https://tapas-roster.86-119-35-199.nip.io/logs
- https://tapas-executor-pool.86-119-35-199.nip.io/logs
- https://tapas-executor-bigrobot.86-119-35-199.nip.io/logs
- https://tapas-executor-computation.86-119-35-199.nip.io/logs
- https://tapas-executor-temperature.86-119-35-199.nip.io/logs
- https://tapas-auction-house.86-119-35-199.nip.io/logs

## Create Executors

```
GET https://tapas-executor-bigrobot.86-119-35-199.nip.io/register
GET https://tapas-executor-computation.86-119-35-199.nip.io/register
GET https://tapas-executor-temperature.86-119-35-199.nip.io/register
```

## Create Task

```
POST https://tapas-tasks.86-119-35-199.nip.io/tasks/

Content-Type application/task+json

{
    "taskName": "Task",
    "taskType": "COMPUTATION",
    "inputData": "5 + 5"
}
```

## Internally Assign Executors

- https://dbadmin.86-119-35-199.nip.io/db/tapas-executor-pool/task_assignments

## Discover Auction Houses through hypermedia

```
GET https://tapas-auction-house.86-119-35-199.nip.io/discover?url=https://tapas-auction-house.86-119-35-213.nip.io
GET https://tapas-auction-house.86-119-35-199.nip.io/discovery
```

## Launch Auction

```
POST https://tapas-tasks.86-119-35-199.nip.io/tasks/

Content-Type application/task+json

{
    "taskName": "Task",
    "taskType": "RANDOMTEXT"
}
```
