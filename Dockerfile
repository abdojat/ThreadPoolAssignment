FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY src/ ./src/
RUN mkdir -p out && javac -encoding UTF-8 -d out src/*.java src/handler/*.java src/singleton/*.java src/task/*.java
EXPOSE 8000
CMD ["java", "-cp", "out", "WebServerSocket"]
