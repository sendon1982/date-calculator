# date-calculator

## How to run the application
1. Go the command line and run `mvn clean package` to generate the fat jar file

2. Then run command `java -jar target/date-calculator-0.0.1-SNAPSHOT.jar` to start the application

3. Access URL at `http://localhost:8080/uno/api/date/daysBetween?fromDate=01.01.2020&toDate=05.01.2020` which should
response as json like:
````
{
    status: "SUCCESS",
    data: {
    daysBetween: 3
    },
    message: null
}
````

## How to build a docker image
1. Run command `./mvnw com.google.cloud.tools:jib-maven-plugin:dockerBuild -Dimage=${username}/date-calculator-service` and
`usernanme` should be your docker hub login username

2. Login into docker hub first by using `docker login` and type username and password

3. Then run command `./mvnw com.google.cloud.tools:jib-maven-plugin:build -Dimage=${username}/date-calculator-service` 
to push the image to the docker hub repository


# How to run with CloudFormation

## Create DynamoDB stack
1. Create a new AWS account in Sydney region and get its accesskey and secretkey for access DynamoDB, as well as SubnetId
2. Go to Amazon AWS console to import `dynamodb-stack.yml` which ned SubnetId from VPC
3. Verify DynamoDB is created with a table called `date_calculation_info`

## Update application properties
1. Update value for key `amazon.aws.accesskey` and `amazon.aws.secretkey`
2. Run `mvn clean package` to create fat jar file and build docker image and push into docker hub
Build Image: `./mvnw com.google.cloud.tools:jib-maven-plugin:dockerBuild -Dimage=${username}/date-calculator-service`
Push Image: `./mvnw com.google.cloud.tools:jib-maven-plugin:build -Dimage=${username}/date-calculator-service` 

## Create ECS stack
1. If you push image under different user at DockerHub then, update `ecs-stack.yml` to put correct image name
2. Go to Amazon AWS console to import `ecs-stack.yml` which ned SubnetId from VPC
3. This template will create ECS instance and pull the image from docker hub and run inside this ECS
