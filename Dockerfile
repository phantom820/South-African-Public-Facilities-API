FROM alpine:latest

# Install OpenJDK 11 and maven
RUN apk add --no-cache openjdk11 maven

# Set JAVA_HOME environment variable
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk

# Add JAVA_HOME to PATH
ENV PATH=${PATH}:${JAVA_HOME}/bin

# Create app directory
RUN mkdir app

# Create src directory within app directory
RUN mkdir app/src

# Copy pom.xml and checkstyle.xml to root of app directory
COPY pom.xml checkstyle.xml /app

# Copy .env to root of app directory
COPY .env /app

# Copy build.sh to root of app directory
COPY build.sh /app

# Copy run.sh to root of app directory
COPY run.sh /app

# Copy src directory to root of app directory
COPY  src /app/src

# Install dependencies from pom.xml so things get cached
RUN mvn -f app/pom.xml install -Dcheckstyle.skip


