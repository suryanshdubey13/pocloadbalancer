# pocloadbalancer

# Java + Spring Boot+ Load Balancer

# 📌 Overview

This Proof of concept demonstrates a Client-Side Static Load Balancer implemented using Spring Boot.

The load balancer distributes traffic between multiple backend services using a Round Robin algorithm, and retry if failed.

This POC simulates how load balancing works internally in microservices architecture.

✅ Round Robin Load Balancing

✅ Static Server Configuration

✅ Health Check Endpoint

✅ Fault Tolerance Handling

# Architecture

              Client
                  │
          http://localhost:8080
                  │
        ┌────────────────────┐
        │  Load Balancer App │  (Port 8080)
        └────────────────────┘
             │            │
      http://localhost:8081
      http://localhost:8082

        ┌──────────┐   ┌──────────┐
        │ Service1 │   │ Service2 │
        │ 8081     │   │ 8082     │
        └──────────┘   └──────────┘
   
🔄 How Load Balancing Works

    The Load Balancer service:

    Maintains a static list of backend servers
    Uses Round Robin strategy
    health validation
    Rotates requests between:
       http://localhost:8081
       http://localhost:8082
    Example:
    | Request | Routed To |
    | ------- | --------- |
    | 1st     | 8081      |
    | 2nd     | 8082      |
    | 3rd     | 8081      |
    | 4th     | 8082      |

🛠 Health-Aware Routing Logic
Before routing request, the load balancer:
1. Checks if backend service is healthy
2. Skips unhealthy service
3. Routes to available healthy service
This ensures high availability.

# Tech Stack
1. Java 17
2. Spring Boot
3. RestTemplate
4. Maven
5. IntelliJ IDEA
6. Git

# Features Demonstrated
    
1. Client-side load balancing
2. Static server configuration
3. Round Robin strategy
4. Health Monitoring
5. Microservice communication
6. Basic fault handling

# Advantages of This Approach
 1. Simple to implement
 2. No external infrastructure required
 3.  Good for POC and local testing

Demonstrates load balancing concept clearly
# Future Improvements

1. Integrate Spring Cloud LoadBalancer
2. Add Service Registry (Eureka)
3.  Dockerize all services
4.  Add Circuit Breaker (Resilience4j)

# Author
## Suryansh Dubey 
Java Developer 


    


    


