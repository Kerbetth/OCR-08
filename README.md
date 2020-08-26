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
1. Create mapping domain class and place in package com.medic.tourguide.domain
2. Tracker and Util class place in package com.medic.tourguide.repositories
3. controllers class place in package com.medic.tourguide.controllers
4. services class place in package com.medic.tourguide.service


## Tests
1. Performance tests
2. Integration tests
3. Unit tests
