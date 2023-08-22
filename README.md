# DDD-9-Android3-Server
DDD 9기 안드로이드 3팀 서버

## About Service

- 수많은 아이템 속 고민하는 당신을 위한 익명 투표 + 위시리스트 아카이브 서비스

## Development Environment

- Language: Java 17
- Framework: SpringBoot, Spring Security
- Database: MySQL 8.0, Spring Data JPA
- CI/CD: Github Actions, Docker, Docker Hub, Elastic Beanstalk
- Docs: Swagger

## Folder Structure

```
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── nexters
    │   │           └── buyornot
    │   │               ├── BuyornotApplication.java
    │   │               ├── HealthController.java
    │   │               ├── domain
    │   │               │   ├── archive
    │   │               │   │   ├── api
    │   │               │   │   ├── application
    │   │               │   │   ├── dao
    │   │               │   │   ├── domain
    │   │               │   │   ├── dto
    │   │               │   │   └── exception
    │   │               │   ├── item
    │   │               │   │   ├── api
    │   │               │   │   ├── application
    │   │               │   │   ├── dao
    │   │               │   │   ├── domain
    │   │               │   │   ├── dto
    │   │               │   │   └── exception
    │   │               │   ├── model
    │   │               │   │   └── BaseEntity.java
    │   │               │   ├── post
    │   │               │   │   ├── api
    │   │               │   │   ├── application
    │   │               │   │   ├── dao
    │   │               │   │   ├── domain
    │   │               │   │   ├── dto
    │   │               │   │   └── exception
    │   │               │   ├── user
    │   │               │   │   ├── api
    │   │               │   │   ├── application
    │   │               │   │   ├── dao
    │   │               │   │   ├── domain
    │   │               │   │   ├── dto
    │   │               │   │   └── exception
    │   │               │   ├── voteItem
    │   │               │   │   ├── api
    │   │               │   │   ├── application
    │   │               │   │   ├── dao
    │   │               │   │   ├── domain
    │   │               │   │   ├── dto
    │   │               │   │   └── exception
    │   │               │   └── vote
    │   │               │       ├── api
    │   │               │       ├── application
    │   │               │       ├── dao
    │   │               │       ├── domain
    │   │               │       ├── dto
    │   │               │       └── exception
    │   │               │ 
    │   │               ├── global
    │   │               │   ├── common
    │   │               │   │   ├── codes
    │   │               │   │   ├── request
    │   │               │   │   └── response
    │   │               │   ├── config
    │   │               │   │   ├── Swagger
    │   │               │   │   ├── properties
    │   │               │   │   └── security
    │   │               │   ├── exception
    │   │               │   │   ├── BsinessExceptionHandler.java
    │   │               │   │   └── GlobalExceptionHandler.java
    │   │               │   └── util
    │   │               └── infra
    │   └── resources
    │       ├── application-aws.yml
    │       ├── application-oauth.yml
    │       └── application.yml

```

## Project architecture

<img src = "https://github.com/DDD-Community/DDD-9-Android3-Server/assets/70634740/5c7ea336-cbbe-4621-93cd-d8f07e0f464d" height = 300/>


## ERD

