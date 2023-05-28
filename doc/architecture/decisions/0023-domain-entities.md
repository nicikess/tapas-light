# 23. Domain Entities Structural Decision

Date: 2021-12-13

## Status

Accepted

## Context

There were 2 options evaluated, having the domain entities in the respective services as they have slightly different attributes and having them as a shared library. The main advantage for having a shared libary is the reduction in code duplications. In both cases the code needs to be changed in multiple places.

## Decision

We have decided that the domain entities are saved in a shared library to due the code duplications otherwise necessary. 

## Consequences

X
