#Build stage
FROM maven:3-amazoncorretto-21 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

#Runtime stage
FROM amazoncorretto:21
ARG PROFILE=dev
ARG APP_VERSION=1.0.0
#Define few things

WORKDIR /app
COPY --from=build /build/target/book-network-*.jar /app/

#Expose on wich port should the container work
EXPOSE 8088

ENV DB_URL=jdbc:postgresql://postgres-sql-bsn:5432/book_social_network
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} book-network-${JAR_VERSION}.jar

