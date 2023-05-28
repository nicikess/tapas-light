# 15. Communication among Auction Houses

Date: 2021-11-14

## Status

Accepted

## Context

The Auction House must be able to communicate with the other Auction Houses to exchange information regarding the bids.

## Decision

The communication between the Auction Houses is led by the MQTT and WebSub protocols. This allows to share information about bidding, publishing auctions and sharing information of auction wins over a uniform interface with other Tapas applications.
More details and the comparison of MQTT and WebSub are specified and a dedicated document submitted on 14.11.21.

## Consequences

Information about started auctions are shared over the WebSub protocol or MQTT protocol. It is crusial to implement the standard set with the other groups to exchange messages seamlessly.
