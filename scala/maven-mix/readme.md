mvn compile
mvn exec:java -Dexec.mainClass=andy.test.JavaBootstrap
mvn exec:java -Dexec.mainClass=andy.test.ScalaBootstrap

mvn clean compile assembly:single
java -jar target\scala-java-mix-1.0-SNAPSHOT-jar-with-dependencies.jar