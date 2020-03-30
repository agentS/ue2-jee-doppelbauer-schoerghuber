# wildfly jaeger configuration
$bin = Join-Path $([Environment]::GetEnvironmentVariable("JBOSS_HOME", "User")) "bin"

# using udp: define an outbound socket binding towards the Jaeger tracer.
$cmd1='/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=jaeger:add(host=localhost, port=6831)'
& $bin\jboss-cli.ps1 --connect --command="$cmd1"

# define MPOT tracer configuration
$cmd2='/subsystem=microprofile-opentracing-smallrye/jaeger-tracer=jaeger-demo:add(sampler-type=const, sampler-param=1, reporter-log-spans=true, sender-binding=jaeger)'
& $bin\jboss-cli.ps1 --connect --command="$cmd2"

#setting the default tracer
$cmd3='/subsystem=microprofile-opentracing-smallrye:write-attribute(name=default-tracer, value=jaeger-demo)'
& $bin\jboss-cli.ps1 --connect --command="$cmd3"