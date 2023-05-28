# 18. Executor base service

Date: 2021-11-21

## Status

Accepted

## Context

The three Executor Services rely a lot on the same functionalities. This leads to a lot of code duplication.

## Decision

Our Executor Base library bundles functionalities that are used in the Robot Executor, Computation Executor and Temperature Executor. We use this library to manage changes in the state of Tasks and Executors and to register Executors to the pool. This library reduces our code and simplifies the application.

## Consequences

The functionalities must be outsourced to the new library.
