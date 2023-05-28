# tapas-tasks

Micro-service for Managing Tasks in a Task List implemented following Hexagonal Architecture.

Based on examples from book "Get Your Hands Dirty on Clean Architecture" by Tom Hombergs

Technologies: Java, Spring Boot, Maven

**Note:** this repository contains an [EditorConfig](https://editorconfig.org/) file (`.editorconfig`)
with default editor settings. EditorConfig is supported out-of-the-box by the IntelliJ IDE. To help maintain
consistent code styles, we recommend to reuse this editor configuration file in all your services.

## HTTP API Overview
The code we provide includes a minimalistic uniform HTTP API for (i) creating a new task, (ii) retrieving
a representation of the current state of a task, and (iii) patching the representation of a task, which
is mapped to a domain/integration event.

The representations exchanged with the API use two media types:
* a JSON-based format for task with the media type `application/task+json`; this media type is defined
  in the context of our project, but could be [registered with IANA](https://www.iana.org/assignments/media-types)
  to promote interoperability (see
  [TaskJsonRepresentation](src/main/java/ch/unisg/tapastasks/tasks/adapter/in/formats/TaskJsonRepresentation.java)
  for more details)
* the [JSON Patch](http://jsonpatch.com/) format with the registered media type `application/json-patch+json`, which is also a
  JSON-based format (see sample HTTP requests below).

For further developing and working with your HTTP API, we recommend to use [Postman](https://www.postman.com/).

### Creating a new task

A new task is created via an `HTTP POST` request to the `/tasks/` endpoint. The body of the request
must include a representation of the task to be created using the content type `application/task+json`
defined in the context of this project. A valid representation must include at least two required fields
(see [TaskJsonRepresentation](src/main/java/ch/unisg/tapastasks/tasks/adapter/in/formats/TaskJsonRepresentation.java)
for more details):
* `taskName`: a string that represents the name of the task to be created
* `taskType`: a string that represents the type of the task to be created

A sample HTTP request with `curl`:
```shell
curl -i --location --request POST 'http://localhost:8081/tasks/' \
--header 'Content-Type: application/task+json' \
--data-raw '{
  "taskName" : "task1",
  "taskType" : "computation",
  "originalTaskUri" : "http://example.org",
  "inputData" : "1+1"
}'

HTTP/1.1 201
Location: http://localhost:8081/tasks/cef2fa9d-367b-4e7f-bf06-3b1fea35f354
Content-Type: application/task+json
Content-Length: 170
Date: Sun, 17 Oct 2021 21:03:34 GMT

{
  "taskId":"cef2fa9d-367b-4e7f-bf06-3b1fea35f354",
  "taskName":"task1",
  "taskType":"computation",
  "taskStatus":"OPEN",
  "originalTaskUri":"http://example.org",
  "inputData":"1+1"
}
```

If the task is created successfuly, a `201 Created` status code is returned together with a
representation of the created task. The response also includes a `Location` header filed that points
to the URI of the newly created task.

### Retrieving a task

The representation of a task is retrieved via an `HTTP GET` request to the URI of task.

A sample HTTP request with `curl`:
```shell
curl -i --location --request GET 'http://localhost:8081/tasks/cef2fa9d-367b-4e7f-bf06-3b1fea35f354'

HTTP/1.1 200
Content-Type: application/task+json
Content-Length: 170
Date: Sun, 17 Oct 2021 21:07:04 GMT

{
  "taskId":"cef2fa9d-367b-4e7f-bf06-3b1fea35f354",
  "taskName":"task1",
  "taskType":"computation",
  "taskStatus":"OPEN",
  "originalTaskUri":"http://example.org",
  "inputData":"1+1"
}
```

### Patching a task

REST emphasizes the generality of interfaces to promote uniform interaction. For instance, we can use
the `HTTP PATCH` method to implement fine-grained updates to the representational state of a task, which
may translate to various domain/integration events. However, to conform to the uniform interface
contraint in REST, any such updates have to rely on standard knowledge — and thus to hide away the
implementation details of our service.

In addition to the `application/task+json` media type we defined for our uniform HTTP API, a standard
representation format we can use to specify fine-grained updates to the representation of tasks
is [JSON Patch](http://jsonpatch.com/). In what follow, we provide a few examples of `HTTP PATCH` requests.
For further details on the JSON Patch format, see also [RFC 6902](https://datatracker.ietf.org/doc/html/rfc6902)).

#### Changing the status of a task from OPEN to ASSIGNED

Sample HTTP request that assigns the previously created task to group `tapas-group1`:

```shell
curl -i --location --request PATCH 'http://localhost:8081/tasks/cef2fa9d-367b-4e7f-bf06-3b1fea35f354' \
--header 'Content-Type: application/json-patch+json' \
--data-raw '[ {"op" : "replace", "path": "/taskStatus", "value" : "ASSIGNED" },
  {"op" : "add", "path": "/serviceProvider", "value" : "tapas-group1" } ]'

HTTP/1.1 200
Content-Type: application/task+json
Content-Length: 207
Date: Sun, 17 Oct 2021 21:20:58 GMT

{
  "taskId":"cef2fa9d-367b-4e7f-bf06-3b1fea35f354",
  "taskName":"task1",
  "taskType":"computation",
  "taskStatus":"ASSIGNED",
  "originalTaskUri":"http://example.org",
  "serviceProvider":"tapas-group1",
  "inputData":"1+1"
}
```

In this example, the requested patch includes two JSON Patch operations:
* an operation to `replace` the `taskStatus` already in the task's representation with the value `ASSIGNED`
* an operation to `add` to the task's representation a `serviceProvider` with the value `tapas-group1`

Internally, this request is mapped to a
[TaskAssignedEvent](src/main/java/ch/unisg/tapastasks/tasks/application/port/in/TaskAssignedEvent.java).
The HTTP response returns a `200 OK` status code together with the updated representation of the task.

#### Changing the status of a task from to EXECUTED

Sample HTTP request that changes the status of the task to `EXECUTED` and adds an output result:

```shell
curl -i --location --request PATCH 'http://localhost:8081/tasks/cef2fa9d-367b-4e7f-bf06-3b1fea35f354' \
--header 'Content-Type: application/json-patch+json' \
--data-raw '[ {"op" : "replace", "path": "/taskStatus", "value" : "EXECUTED" },
  {"op" : "add", "path": "/outputData", "value" : "2" } ]'

HTTP/1.1 200
Content-Type: application/task+json
Content-Length: 224
Date: Sun, 17 Oct 2021 21:32:25 GMT

{
  "taskId":"cef2fa9d-367b-4e7f-bf06-3b1fea35f354",
  "taskName":"task1",
  "taskType":"computation",
  "taskStatus":"EXECUTED",
  "originalTaskUri":"http://example.org",
  "serviceProvider":"tapas-group1",
  "inputData":"1+1",
  "outputData":"2"
}
```

Internally, this request is mapped to a
[TaskExecutedEvent](src/main/java/ch/unisg/tapastasks/tasks/application/port/in/TaskExecutedEvent.java).
The HTTP response returns a `200 OK` status code together with the updated representation of the task.

## Working with MongoDB
The provided TAPAS Tasks service is connected to a MongoDB as a repository for persisting data.

Here are some pointers to start integrating the MongoDB with the other microservices:
* [application.properties](src/main/resources/application.properties) defines the
  * URI of the DB server that Spring will connect to (`mongodb`service running in Docker container). Username and password for the server can be found in [docker-compose.yml](../docker-compose.yml).
  * Name of the database for the microservice (`tapas-tasks`)
* [docker-compose.yml](../docker-compose.yml) defines
  * in lines 74-82: the configuration of the mongodb service based on the mongodb container including the root username and password (once deployed this cannot be changed anymore!)
  * in lines 84-102: the configuration of a web application called `mongo-express` to manage the MongoDB server. The web app can be reached via the URI: [http://dbadmin.${PUB_IP}.nip.io]([http://dbadmin.${PUB_IP}.nip.io]). Login credentials for  mongo-express can be found in lines 89 and 90.
  * in lines 104-105: the volume to be used by the mongodb service for writing and storing data (do not forget!).
* The [pom.xml](./pom.xml) needs to have `spring-boot-starter-data-mongodb` and `spring-data-mongodb` as new dependencies.
* The [TapasTasksApplication.java](/src/main/java/ch/unisg/tapastasks/TapasTasksApplication.java) specifies in line 9 the location of the MongoRepository classes for the microservice.
* The [persistence.mongodb](/src/main/java/ch/unisg/tapastasks/tasks/adapter/out/persistence/mongodb) package has the relevant classes to work with MongoDB:
  * The [MongoTaskDocument.java](/src/main/java/ch/unisg/tapastasks/tasks/adapter/out/persistence/mongodb/MongoTaskDocument.java) class defines the attributes of a Document for storing a task in the collection `tasks`.
  * The [TaskRepository.java](/src/main/java/ch/unisg/tapastasks/tasks/adapter/out/persistence/mongodb/TaskRepository.java) class specifies the MongoRepository.
  * The [TaskPersistenceAdapter.java](/src/main/java/ch/unisg/tapastasks/tasks/adapter/out/persistence/mongodb/TaskPersistenceAdapter.java) implements the two ports to add a new task ([AddTaskPort](/src/main/java/ch/unisg/tapastasks/tasks/application/port/out/AddTaskPort.java)) and retrieve a task ([LoadTaskPort](/src/main/java/ch/unisg/tapastasks/tasks/application/port/out/LoadTaskPort.java)). These ports are used in the classes [AddNewTaskToTaskListService.java](/src/main/java/ch/unisg/tapastasks/tasks/application/service/AddNewTaskToTaskListService.java) and [RetrieveTaskFromTaskListService.java](/src/main/java/ch/unisg/tapastasks/tasks/application/service/RetrieveTaskFromTaskListService.java).

#### General hints:
* To not overload the VMs we recommend to use only one MongoDB server that all microservices connect to. Per microservice you could use one database or one collection (discuss in your ADRs!). To use more than one MongoDB server you have to extend the [docker-compose.yml](../docker-compose.yml) file by basically replicating lines 74-105 and changing the names of the services and volumes to be unique (ask your tutors!).
* For local testing you have to install the MongoDB server locally on your computers and change the `spring.data.mongodb.uri` String in [application.properties](./src/main/resources/application.properties).
