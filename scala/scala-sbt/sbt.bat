set SCRIPT_DIR=%~dp0
rem java  -Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=8087 -Dsbt.global.base=d:\sbt\.sbt -Dsbt.ivy.home=d:\sbt\.ivy2 -Xmx512M -jar "%SCRIPT_DIR%sbt-launch.jar" %*

java  -Dsbt.global.base=d:\sbt\.sbt -Dsbt.ivy.home=d:\sbt\.ivy2 -Xmx512M -jar "%SCRIPT_DIR%sbt-launch.jar" %*