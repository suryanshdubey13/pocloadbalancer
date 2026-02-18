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
                            │       Target Group         │
                            │       (HTTP : 8080)        │
                            │ Health Check: /hello/hello │
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

    example- 

## Sample Response

    Hello from container: ip-adress.ap-south-1.compute.internal (container env).

# Steps for Deployment using ALB

## Dockerfile


    


