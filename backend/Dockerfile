FROM maven:3.8.5-jdk-11

COPY . /var/local/WheelOfFortune/backend/
WORKDIR /var/local/WheelOfFortune/backend/

ENTRYPOINT mvn clean install -DskipTests=true -Doutput.jar.name=WheelOfFortune-DOCKER -Dmaven.repo.local=/var/local/.m2 spring-boot:run