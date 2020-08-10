# TourGuide
## Technical:

1. Framework: Spring Boot v2.0.4
2. Java 14

## What is it
![alt text](https://raw.githubusercontent.com/Kerbetth/OCR-08/feature/src/main/resources/tourguide_flyer.png "Presentation")

Tourguide is an application whose repertoriate attractions worldwide, and propose interaction between these and the user.
## Database
1. At the state of the application, the data is generate manually inside the services in order to test the different functions.

## MicroService architecture
1. The application is divide in three microservices for the three following task:
-managing of the User data
-managing of the attraction data and calculate distances
-managing of userRewards and generation of provider list according to userPreferences

## Implement a Feature
1. Create mapping domain class and place in package com.medic.mediscreen.domain
2. Create repository class and place in package com.medic.mediscreen.repositories
3. Create controller class and place in package com.medic.mediscreen.controllers
4. Create view files and place in src/main/resource/templates

## Tests
1. Create unit test and place in package com.medic.mediscreen in folder test > java
2. Each microservice have its own tests corresponding to the service they provide (Integration and Unit tests)
