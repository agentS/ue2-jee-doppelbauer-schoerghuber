$bin = Join-Path $([Environment]::GetEnvironmentVariable("JBOSS_HOME", "User")) "bin"

$cmd1='module add --name=org.postgres --resources=postgresql-42.2.11.jar --dependencies=javax.api,javax.transaction.api'
& $bin\jboss-cli.ps1 --connect --command="$cmd1"

$cmd2='/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)'
& $bin\jboss-cli.ps1 --connect --command="$cmd2"

$cmd3='data-source add --jndi-name=java:/PostGreDS --name=PostgrePool --connection-url=jdbc:postgresql://localhost:5432/booking --driver-name=postgres --user-name=postgres --password=postgres'
& $bin\jboss-cli.ps1 --connect --command="$cmd3"

#use option --controller=127.0.0.1:<port> if jboss management runs under a different port.
#module add --name=org.postgres --resources=/tmp/postgresql-9.3-1101.jdbc41.jar --dependencies=javax.api,javax.transaction.api
#data-source add --jndi-name=java:/PostGreDS --name=PostgrePool --connection-url=jdbc:postgresql://localhost/postgres --driver-name=postgres --user-name=postgres --password=postgres