# Coding challenge
**Carpark Ubi**

## Domain vocabulary:
EV - electric vehicle.
CP - charging point, an element in an infrastructure that supplies electric energy for the recharging of electric vehicles.

## Problem details:
The task is to implement a simple application to manage the charging points installed at Carpark Ubi.
Carpark Ubi has 10 charging points installed. When a car is connected it consumes either 20 Amperes (fast charging) or 10 Amperes (slow charging). 
Carpark Ubi installation has an overall current input of 100 Amperes so it can support fast charging for a maximum of 5 cars or slow charging for a maximum of 10 cars at one time.
A charge point notifies the application when a car is plugged or unplugged.
The application must distribute the available current of 100 Amperes among the charging points so that when possible all cars use fast charging and when the current is not sufficient some cars are switched to slow charging. 
Cars which were connected earlier have lower priority than those which were connected later.
The application must also provide a report with a current state of each charging point returning a list of charging point, status (free or occupied) and - if occupied – the consumed current.

## Constraints:
The solution must be implemented as a Spring Boot application exposing a REST API.
Include at least one unit test and one integration test.

## Examples:

```
CP1 sends a notification that a car is plugged
Report: 
CP1 OCCUPIED 20A
CP2 AVAILABLE
...
CP10 AVAILABLE
```

```
CP1, CP2, CP3, CP4, CP5 and CP6 send notification that a car is plugged
Report:
CP1 OCCUPIED 10A
CP2 OCCUPIED 10A
CP3 OCCUPIED 20A
CP4 OCCUPIED 20A
CP5 OCCUPIED 20A
CP6 OCCUPIED 20A
CP7 AVAILABLE
...
CP10 AVAILABLE
```

## Deliverables:
Zipped archive with the implementation and the documentation on how to call the API (Swagger/Postman collection/text description).
Please add any details about your ideas and considerations to this README and add it to the repository.


## How to run:
1) run mvn clean install 
2) cd target 
3) java -jar ubicity-1.0-SNAPSHOT.jar 
4) go to http://localhost:8080/swagger-ui.html
 There you will encounter swagger page where you can play with the API
 
## Implementation Idea
* The code uses a hashmap to keep track of the charging points and their status . 
* For the cases where cars have to be changed from slow to fast or reverse two queues are used to track the order .
* The FastQueue is a FIFO while the slow queue is a LIFO .
* Together they make sure that the cars entering last have the priority for fast charge.
* The code uses thread safe implementation by using data structures from concurrent package  and syncronized functions
* Care has been taken to keep the system in stable phase while handling car state changes

## Future Scalabilty tips.
The code can be applied to real time scenario by using an event driven mechanisms using Reactive Spring,and Kafka.
* The data in the repository  can be stored in No-SQL database (redis ).
* The client Service will only be responsible for its own car.
* In  case it needs power from other cars it can send a messsage to the routing system kafka topic  to allocate power to it.
* Similarly if the car is leaving the system it will send a message to the routing system saying that it is relinquishing power .
The routing system  will then allocate this to someone else 
* this project can be made scalable using spring cloud/docker on cloud systems