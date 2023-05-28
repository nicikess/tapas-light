# 14. Auction House forwards Task to Task List

Date: 2021-11-14

## Status

Accepted

## Context

The Auction House bids on auctions from other Auction Houses. In case the Auction House wins the bid, it must be able to forward the Task to the Task List.

## Decision

The Auction House forwards the Task to the Task List where the Task gets forwarded to the Roster which then checks for an Executor in the Executor Pool. Another option would have been to forward the Task directly to the Roster Service. However, this has the disadvantage that you no longer have any knowledge about the Task after the execution, since it is not stored in the Task List.

## Consequences

The Auction House must be able to communicate to the Task List and forward the Task.
