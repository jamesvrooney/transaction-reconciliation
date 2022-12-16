FROM maven:3.8-openjdk-17 as build

ENV HOME=/usr/app

RUN mkdir -p $HOME

WORKDIR $HOME

ADD . $HOME

RUN mvn package

FROM openjdk:17-jdk

WORKDIR /app

RUN mkdir -p /Users/jamesrooney/logs

RUN touch /Users/jamesrooney/cash-transactions.json


COPY --from=build /usr/app/target/transaction-reconciliation-0.0.1-SNAPSHOT.jar /app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "transaction-reconciliation-0.0.1-SNAPSHOT.jar"]