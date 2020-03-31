# Initializing the Configuration Database

In order to initialize the configuration database you need to run the following commands from Redis CLI:

```
SET booking:ordinal 600
HMSET booking timetableService/mp-rest/uri "http://localhost:8082"
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

  "prefix": "booking"
}
```

# Run Redis on docker

```
docker run --name redis-sve2 -p 6379:6379 -d redis
docker exec -it redis-sve2 redis-cli
```

# Run Postgresql on docker
```
docker run --name postgres-docker -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```

# Setup Postgresql Datasource on Wildfly (powershell)
Add a user to Wildfly.
```
$bin = Join-Path $([Environment]::GetEnvironmentVariable("JBOSS_HOME", "User")) "bin"

& $bin\add-user.ps1 -u admin -p admin123!
```

Add a DataDriver and DataSource to Wildfly.
```
$bin = Join-Path $([Environment]::GetEnvironmentVariable("JBOSS_HOME", "User")) "bin"

$cmd1='module add --name=org.postgres --resources=postgresql-42.2.11.jar --dependencies=javax.api,javax.transaction.api'
& $bin\jboss-cli.ps1 --connect --command="$cmd1"

$cmd2='/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)'
& $bin\jboss-cli.ps1 --connect --command="$cmd2"

$cmd3='data-source add --jndi-name=java:/PostGreDS --name=PostgrePool --connection-url=jdbc:postgresql://localhost:5432/booking --driver-name=postgres --user-name=postgres --password=postgres'
& $bin\jboss-cli.ps1 --connect --command="$cmd3"
```
