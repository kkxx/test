set SCRIPT_DIR=%~dp0
java -Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=8087 -Dsbt.global.base=d:\sbt\.sbt -Dsbt.ivy.home=d:\sbt\.ivy2 -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar "%SCRIPT_DIR%sbt-launch.jar" %*

rem java  -Dsbt.global.base=d:\sbt\.sbt -Dsbt.ivy.home=d:\sbt\.ivy2 -Xmx512M -jar "%SCRIPT_DIR%sbt-launch.jar" %*