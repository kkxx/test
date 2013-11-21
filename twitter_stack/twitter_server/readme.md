mvn exec:java -Dexec.mainClass=andy.BasicServer

mvn clean compile assembly:single
java -jar target/ts-1.0-SNAPSHOT-jar-with-dependencies.jar