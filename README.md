# pocloadbalancer

# Java + Docker + Amazon ECS + Application Load Balancer

# 📌 Overview

This Proof of Concept (POC) demonstrates deploying a Spring Boot application on:

Amazon Web Services
Amazon ECS (Fargate Launch Type)
Application Load Balancer
Amazon CloudWatch (for Auto Scaling metrics)

The application is containerized using Docker and deployed to ECS Fargate. Traffic is routed through an ALB, and Service Auto Scaling dynamically adjusts the number of running tasks based on CPU utilization.
1. Horizontal scaling
2. High availability
3. Health-based routing
4. Fault tolerance
5. Zero downtime rolling deployments

# Architecture
Flow 
1. User sends request to ALB public DNS.
2. ALB forwards request to Target Group.
3. Target Group routes traffic only to healthy ECS tasks.
4. ECS Service ensures desired number of tasks are always running.
5. Each task runs a Spring Boot container exposing port 8080.
6. If health check fails:
    a. Target marked unhealthy
    b. Traffic stopped
    c. ECS launches replacement task

                            ┌────────────────────────────┐
                            │        Internet Users      │
                            └──────────────┬─────────────┘
                                           │
                                           ▼
                            ┌────────────────────────────┐
                            │  Application Load Balancer │
                            │        (HTTP : 80)         │
                            │  SG: Allow 80 from 0.0.0.0 │
                            └──────────────┬─────────────┘
                                           │
                                           ▼
                            ┌────────────────────────────┐
                            │       Target Group         | 
                            │       (HTTP : 8080)        |
                            │ Health Check: /acuator/    |
                            |           health           │
                            └──────────────┬─────────────┘
                                           │
                        ┌──────────────────┴──────────────────┐
                        ▼                                     ▼
            ┌──────────────────────┐              ┌──────────────────────┐
            │ ECS Fargate Task 1   │              │ ECS Fargate Task 2   │
            │ Spring Boot App      │              │ Spring Boot App      │
            │ Port: 8080           │              │ Port: 8080           │
            │ SG: Allow 8080       │              │ SG: Allow 8080       │
            │ From ALB SG only     │              │ From ALB SG only     │
            └──────────────────────┘              └──────────────────────┘
                            ┌────────────────────────────┐
                            │      Auto Scaling Policy   | 
                            │     Scale Out/ IN :50%     |
                            │ MIN CPU : 2 | MAX CPU : 4  |
                            |                            │
                            └──────────────┬─────────────┘

   
## flowchart TD
    A[User] --> B[Application Load Balancer]
    
    B --> C[Target Group<br/>Health Check Enabled]
    
    C --> D[ECS Service - Fargate]
    
    D --> E[Spring Boot Task 1]
    
    D --> F[Spring Boot Task 2]

# Tech Stack
1. Backend: Spring Boot
2. Containerization: Docker
3. Container Orchestration: Amazon Elastic Container Service
4. Load Balancing: Application Load Balancer
5. Image Registry: Docker Hub


# Application Details

## Endpoint

    GET /api/hello

    example- http://pocloadbalancer-1509606686.ap-south-1.elb.amazonaws.com/hello/hello

## Sample Response

    Hello from container: ip-adress.ap-south-1.compute.internal (container env).

## Health Endpoint
      GET /actuator/heath

      example- http://pocloadbalancer-1509606686.ap-south-1.elb.amazonaws.com/actuator/health
      
# Steps in configuring service and ALB

## Dockerize the application

    1. Build Jar file using -->  mvn clean package --> generate target/pocloadbalancer.jar
    2. Configure Docker file 
    3. docker login ( login to your docker hub account)
    4. create docker repository
    5. build Docker image 
      eg :  docker build -t pocloadbalancer .
    6. docker push

## Create Application Load Balancer

    1. Go to EC2 → Load Balancers
    2. Click Create Load Balancer
    3. Select Application Load Balancer
    4. Choose:
      Internet-facing
      HTTP (Port 80)
      At least 2 Availability Zones

## Create Target Group

    1. Target Type → IP (Required for Fargate)
    2. Protocol → HTTP
    3. Port → 8080
    4. Health Check Path: /actuator/health

## Create ECS Cluster

    1. Go to ECS (Elastic Container Service).
    2. Create Cluster
    3. Select Fargate (Networking Only)

## Create Task Definition

    Configure task definition
    Launch type: Fargate
    CPU: 256
    Memory: 512
    add Container Image URI from docker hub
    container port : 8080
    
## Create ECS Service
1. Launch type → Fargate
2. Desired tasks → 2
3. Select VPC and subnets
4. Enable Public IP
5. Attach existing ALB
6. Select Target Group
7. Configure ECS Auto Scaling (Min CPU :2 , Max CPU : 4 , Scale : 50% )
8. Map container port 8080

## Test Load Balancing
     Hit Endpoint on browser

# Common Issues Faced During POC

| Issue                  | Cause                           | Fix                        |
| ---------------------- | ------------------------------- | -------------------------- |
| ERR_CONNECTION_REFUSED | ALB listening on wrong port     | Change listener to port 80 |
| Target Unhealthy       | Health check path incorrect     | Update health check path   |
| Connection timeout     | Security group misconfiguration | Allow 8080 from ALB SG     |

# Future Improvements

1. Add HTTPS (SSL via ACM)
2. Add CloudWatch logging
3. Add CI/CD using GitHub Actions
4. Use Infrastructure as Code (Terraform / CloudFormation)

# Author
Suryansh Dubey
ALB POC (Flow + traffic distribution). 


    


    


