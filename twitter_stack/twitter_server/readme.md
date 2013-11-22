mvn exec:java -Dexec.mainClass=andy.BasicServer
mvn package
mvn clean compile assembly:single
java -jar target/ts-1.0-SNAPSHOT-jar-with-dependencies.jar

curl -D - http://localhost:9990/admin
curl -D - http://localhost:9990/admin/metrics.json?pretty=true

java -jar target\ts-1.0-SNAPSHOT-jar-with-dependencies.jar -help
java -jar target\ts-1.0-SNAPSHOT-jar-with-dependencies.jar -admin.port=:8080

mvn exec:java -Dexec.mainClass=andy.AdvancedServer -Dexec.arguments="-help"
mvn exec:java -Dexec.mainClass=andy.AdvancedServer -Dexec.arguments="-admin.port=:8080"
mvn exec:java -Dexec.mainClass=andy.AdvancedServer -Dexec.arguments="-admin.port=:8080,-what='*',-log.output=log/server.log"


mvn compile -P development
mvn compile -P development exec:java -Dexec.mainClass=andy.AdvancedServer
