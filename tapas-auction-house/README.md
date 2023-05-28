# tapas-auction-house

The Auction House is the part of your TAPAS application that is largely responsible for the interactions
with the TAPAS applications developed by the other groups. More precisely, it is responsible for
launching and managing auctions and it is implemented following the Hexagonal Architecture (based on
examples from book "Get Your Hands Dirty on Clean Architecture" by Tom Hombergs).

Technologies: Spring Boot, Maven

**Note:** this repository contains an [EditorConfig](https://editorconfig.org/) file (`.editorconfig`)
with default editor settings. EditorConfig is supported out-of-the-box by the IntelliJ IDE. To help maintain
consistent code styles, we recommend to reuse this editor configuration file in all your services.

## Project Overview

This project provides a partial implementation of the Auction House. The code is documented in detail,
here we only include a summary of implemented features:
* running and managing auctions:
  * each auction has a deadline by which it is open for bids
  * once the deadline has passed, the auction house closes the auction and selects a random bid
* starting an auction using a command via an HTTP adapter (see sample request below)
* retrieving the list of open auctions via an HTTP adapter, i.e. auctions accepting bids (see sample
  request below)
* receiving events when executors are added to the TAPAS application (both via HTTP and MQTT adapters)
* the logic for automatic placement of bids in auctions: the auction house will place a bid in every
  auction for which there is at least one executor that can handle the type of task
  being auctioned
* discovery of auction houses via a provided resource directory (see assignment sheet for
  Exercises 5 & 6 for more details)

## Overview of Adapters

In addition to the overall skeleton of the auction house, the current partial implementation provides
several adapters to help you get started.

### HTTP Adapters

Sample HTTP request for launching an auction:

```shell
curl -i --location --request POST 'http://localhost:8083/auctions/' \
--header 'Content-Type: application/json' \
--data-raw '{
  "taskUri" : "http://example.org",
  "taskType" : "taskType1",
  "deadline" : 10000
}'

HTTP/1.1 201
Content-Type: application/json
Content-Length: 131
Date: Sun, 17 Oct 2021 22:34:13 GMT

{
  "auctionId":"1",
  "auctionHouseUri":"http://localhost:8083/",
  "taskUri":"http://example.org",
  "taskType":"taskType1",
  "deadline":10000
}
```

Sample HTTP request for retrieving auctions currently open for bids:

```shell
curl -i --location --request GET 'http://localhost:8083/auctions/'

HTTP/1.1 200
Content-Type: application/json
Content-Length: 133
Date: Sun, 17 Oct 2021 22:34:20 GMT

[
  {
    "auctionId":"1",
    "auctionHouseUri":"http://localhost:8083/",
    "taskUri":"http://example.org",
    "taskType":"taskType1",
    "deadline":10000
  }
]
```

Sending an [ExecutorAddedEvent](src/main/java/ch/unisg/tapas/auctionhouse/application/port/in/ExecutorAddedEvent.java)
via an HTTP request:

```shell
curl -i --location --request POST 'http://localhost:8083/executors/taskType1/executor1'

HTTP/1.1 204
Date: Sun, 17 Oct 2021 22:38:45 GMT
```

### MQTT Adapters

Sending an [ExecutorAddedEvent](src/main/java/ch/unisg/tapas/auctionhouse/application/port/in/ExecutorAddedEvent.java)
via an MQTT message via HiveMQ's [MQTT CLI](https://hivemq.github.io/mqtt-cli/):

```shell
 mqtt pub -t ch/unisg/tapas-group1/executors -m '{ "taskType" : "taskType1", "executorId" : "executor1" }'
```
