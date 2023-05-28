# 22. Semantic hypermedia

Date: 2021-12-05

## Status

Accepted

## Context

Using a hypermedia-driven interaction we can further decentralize our tapas application. The directory-based discovery of Auction Houses gets replaced with hypermedia-based discovery via crawling.

## Decision

For crawling the semantic hypermedia overlay we discussed two options. Either we use a dedicated service to crawl the other Auction Houses, or we include the crawling functionalities into our Auction House service. We decided use to the second option. The decision was based on the discussion of disintegrators and integrators. Since extensibility and scalability are not mandatory in this case, we decided to integrate the functionality in the Auction House service. The fact that we use shared code made the decision easier for us.

## Consequences

There is no need for a service, and we integrate the crawling of the semantic hypermedia overlay in the Auction House.
