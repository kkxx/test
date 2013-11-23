mvn exec:java -Dexec.mainClass=andy.BasicServer
mvn package
mvn clean compile assembly:single
java -jar target/ts-1.0-SNAPSHOT-jar-with-dependencies.jar

curl -D - http://localhost:9990/admin
curl -D - http://localhost:9990/admin/metrics.json?pretty=true

java -jar target\ts-1.0-SNAPSHOT-jar-with-dependencies.jar -help
java -jar target\ts-1.0-SNAPSHOT-jar-with-dependencies.jar -admin.port=:8080

mvn exec:java -Dexec.mainClass=andy.AdvancedServer -Dexec.arguments="-admin.port=:8080,-what='*',-log.output=log/server.log"


mvn compile -P development
mvn compile -P development exec:java -Dexec.mainClass=andy.AdvancedServer
curl -D - http://localhost:9990/echo/
curl -D - http://localhost:9990/admin/tracing?enable=true


curl -D -  http://172.19.108.98:9990/admin/tracing?enable=true
I 1122 10:25:49.511 THREAD13 com.twitter.server.handler.TracingHandler.apply: Enabled Finagle tracing


mvn compile -P development exec:java -Dexec.mainClass=andy.AdvancedServer -Dexec.arguments="-log.level=DEBUG,-log.output=/dev/stdout,-com.twitter.finagle.zipkin.host=172.19.108.98:9410,-com.twitter.finagle.zipkin.initialSampleRate=1"
mvn compile -P development exec:java -Dexec.mainClass=andy.AdvancedServer -Dexec.arguments="-com.twitter.finagle.zipkin.host=172.19.108.98:9410,-com.twitter.finagle.zipkin.initialSampleRate=1"
