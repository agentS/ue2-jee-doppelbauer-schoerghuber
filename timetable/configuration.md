# Initializing the Configuration Database

In order to initialize the configuration database you need to run the following commands from Redis CLI:

```
SET timetable:ordinal 600
HMSET timetable quarkus.http.port 8082 quarkus.http.cors "true" quarkus.http.cors.origins "http://localhost:3000,http://127.0.0.1:3000"
HMSET timetable openapi.server.url "http://127.0.0.1:8082"
HMSET timetable quarkus.datasource.db-kind "postgresql" quarkus.datasource.username "lukas" quarkus.datasource.password "Cisco0" quarkus.datasource.jdbc.url "jdbc:tracing:postgresql://127.0.0.1:5432/timetable" quarkus.datasource.jdbc.driver "io.opentracing.contrib.jdbc.TracingDriver" quarkus.hibernate-orm.dialect "org.hibernate.dialect.PostgreSQLDialect" quarkus.hibernate-orm.database.generation "drop-and-create"
HMSET timetable quarkus.jaeger.service-name "timetable" quarkus.jaeger.sampler-type "const" quarkus.jaeger.sampler-param 1 quarkus.jaeger.propagation "b3" quarkus.log.console.format "%d{HH:mm:ss} %-5p traceId=%X{traceId}, spanId=%X{spanId}, sampled=%X{sampled} [%c{2.}] (%t) %s%e%n"
```

# Initializing the Configuration File for Accessing the Configuration Database

This issue is on quite a meta level, but the configuration source for reading the configuration from Redis uses a configuration file containing the information required to access Redis.
The configuration file has to reside in the resource directory (`src/main/resources`) and has to be named `redisConfiguration.json`.
The following example outlines the file's structure:

```json
{
  "hostname": "127.0.0.1",
  "port": 6379,
  "timeout": 3000,
  "maximumPoolSize": 5,

  "prefix": "timetable"
}
```
