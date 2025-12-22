# AWS Deployment Documentation

## Deployed Service
Event Service

## Deployment Goal
Demonstrate ability to deploy a Spring Boot microservice to AWS using Docker
without complex infrastructure.

## AWS Components Used
- EC2 (Amazon Linux 2)
- Docker
- Amazon ECR
- Security Group (port 8080 open)

## Step 1: Build Docker Image
```bash
docker build -t event-service .
```

## Step 2: Create ECR Repository
```bash
aws ecr create-repository --repository-name event-service
```

### Step 3: Authenticate Docker to ECR
```bash
aws ecr get-login-password --region ap-south-1 | \
docker login --username AWS --password-stdin <account-id>.dkr.ecr.ap-south-1.amazonaws.com
```

### Step 4: Authenticate Docker to ECR
```bash
docker tag event-service:latest <ecr-uri>:latest
docker push <ecr-uri>:latest
```

### Step 5: Launch EC2 Instance
```
Instance type: t2.micro

OS: Amazon Linux 2

Security group:

Allow inbound TCP 8080 from your IP
```

### Step 6: Install Docker on EC2
```bash
sudo yum install docker -y
sudo service docker start
sudo usermod -a -G docker ec2-user
```
### Step 7: Install Docker on EC2
```bash
docker run -d -p 8080:8080 \
-e SPRING_PROFILES_ACTIVE=prod \
-e KAFKA_BOOTSTRAP_SERVERS=<kafka-ip>:9092 \
<ecr-uri>:latest
```

### Step 8: Verify Access
```bash
curl http://<public-ip>:8080/actuator/health

```




