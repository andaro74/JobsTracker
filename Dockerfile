# Use a base image with Java runtime
FROM amazoncorretto:23-alpine-jdk AS builder

# Set the working directory inside the container
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests


FROM amazoncorretto:23-alpine-jdk
WORKDIR /app
# Copy the built JAR file into the container
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080
EXPOSE 5005

# Run the application
#ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]

