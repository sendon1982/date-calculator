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
1. Run command `./mvnw com.google.cloud.tools:jib-maven-plugin:dockerBuild -Dimage=${username}/date-calculator` and
`usernanme` should be your docker hub login username

2. Login into docker hub first by using `docker login` and type username and password

3. Then run command `./mvnw com.google.cloud.tools:jib-maven-plugin:build -Dimage=sendon1982/date-calculator` 
to push the image to the docker hub repository
