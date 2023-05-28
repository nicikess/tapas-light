# Event-based Interaction with W3C WebSub and MQTT

## Setting up a local WebSub Hub

W3C WebSub Recommendation: [https://www.w3.org/TR/websub/](https://www.w3.org/TR/websub/)

There are several implementations of W3C WebSub available.  One implementation that is easy to set up is:
[https://github.com/hemerajs/websub-hub](https://github.com/hemerajs/websub-hub)

Running this WebSub Hub implementation requires Docker, Node.js, and npm:
* installation instructions for Docker: [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/)
* installation instructions for Node.js and npm: [https://docs.npmjs.com/downloading-and-installing-node-js-and-npm](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)

### How to run

```shell
docker run -d -p 27017:27017 -p 28017:28017 -e AUTH=no mongo:latest
npm i -g websub-hub
websub-hub -l info -m mongodb://localhost:27017/hub
```

The third command launches a Docker container with a [MongoDB](https://www.mongodb.com/) instance,
which is used to persist all subscriptions made with the hub. See the README of the original project
for further details: [https://github.com/hemerajs/websub-hub](https://github.com/hemerajs/websub-hub)

### Implementation note on verification of intents

There are 3 main W3C WebSub hub implementations out of which:
* 2 are public but closed-source: [Google' WebHub hub](http://pubsubhubbub.appspot.com/)
and [Superfeedr's WebSub hub](https://websub.superfeedr.com/)
* 1 is public and open-source, but it requires additional overhead to set up for local development:
  [Switchboard](https://switchboard.p3k.io/)

The project we recommend above provides by far one of the easiest ways to run a W3C WebSub Hub locally. However,
**interoperability is hard**: this project diverges in one significant way from the [W3C WebSub Recommendation](https://www.w3.org/TR/websub/)
and the main [WebSub Hub](http://pubsubhubbub.appspot.com/) implementations. To save you from the
interoperability headaches, we document this divergence  below.

#### Verifying subscriber intents

When a Subscriber registers with a WebSub Hub, the Hub is required to verify the intent of the subscriber
in order to prevent an attacker from creating unwanted subscriptions.

To verify the Subscriber's intent, the Hub sends an HTTP GET request to the subscriber's callback
URL. The HTTP GET request includes several parameters, one of which is a hub-generated random string
with the name `hub.challenge`. To confirm the subscription, a Susbcriber must then respond with an
HTTP 2xx status code and a response body equal to the `hub.challenge` parameter. For more details on
the verification of intents, see [Section 5.3 in the W3C WebSub Recommendation](https://www.w3.org/TR/websub/#hub-verifies-intent).

#### Verifying subscriber intents with the Hemerajs implementation

The above Hemerajs implementation differs from the W3C WebSub Recommendation in that the Hub expects the
response body confirming the intent to be in the JSON format with the `application/json` media type.

Sample HTTP request and the HTTP response expected by the Hemerajs WeSub Hub:

```shell
curl -i --location --request GET 'localhost:8084/websub/?hub.mode=subscribe&hub.topic=http://example.org&hub.challenge=hub-generated-challenge'

HTTP/1.1 200
Content-Type: application/json
Content-Length: 46
Date: Fri, 22 Oct 2021 10:14:03 GMT

{
  "hub.challenge": "hub-generated-challenge"
}
```

#### Verifying subscriber intents in a standard W3C WebSub implementation

In contrast to the Hemerajs implementation, a standard WebSub Hub implementation would require the reponse body to be exactly equal to the
hub-generated challenge parameter. Here is an example of a standard response, as expected by WebSub Hubs
that are fully standard-compliant:

```shell
curl -i --location --request GET 'https://websub.flus.io/dummy-subscriber?hub.mode=subscribe&hub.topic=http://example.org&hub.challenge=hub-generated-challenge'

HTTP/2 200
server: nginx
date: Fri, 22 Oct 2021 10:15:58 GMT
content-type: text/plain;charset=UTF-8
content-security-policy: default-src 'self'
strict-transport-security: max-age=63072000

hub-generated-challenge
```

#### What this means for your TAPAS application

We recommend using the Hemerajs WebSub Hub implementation for local development.

In our deployment, however, we will use one of the publicly available W3C WebSub hubs. You will then
have to change the response for intent verification to match the standard response: that is, to return
directly the `hub.challenge` parameter as shown above.


## Setting up a local MQTT broker

An easy way to set up a local MQTT broker is with HiveMQ and Docker:
[https://www.hivemq.com/downloads/docker/](https://www.hivemq.com/downloads/docker/)

```shell
docker run -p 8080:8080 -p 1883:1883 hivemq/hivemq4
```

The above command launches a Docker container with a HiveMQT broker and binds to the container on 2 ports:
* port `1883` is used by the MQTT protocol
* port `8080` is used for the HiveMQ dashboard; point your browser to: [http://localhost:8080/](http://localhost:8080/)

To bind the Docker container to a different HTTP port, you can configure the first parameter. E.g.,
this command binds the HiveMQT dashboard to port `8085`:

```shell
docker run -p 8085:8080 -p 1883:1883 hivemq/hivemq4
```

For development and debugging, it might help to install an MQTT client as well. HiveMQ provides an MQTT
Command-Line Interface (CLI) that may help: [https://hivemq.github.io/mqtt-cli/](https://hivemq.github.io/mqtt-cli/)
