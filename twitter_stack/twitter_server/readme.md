mvn exec:java -Dexec.mainClass=andy.BasicServer



mvn clean compile assembly:single
java -jar target/ts-1.0-SNAPSHOT-jar-with-dependencies.jar

java -jar target\ts-1.0-SNAPSHOT-jar-with-dependencies.jar -help

curl -D - http://localhost:9990/admin
curl -D - http://localhost:9990/admin/metrics.json?pretty=true

java -jar target\ts-1.0-SNAPSHOT-jar-with-dependencies.jar -admin.port=:8080

mvn exec:java -Dexec.mainClass=andy.AdvancedServer -Dexec.arguments="-admin.port=:8080"
