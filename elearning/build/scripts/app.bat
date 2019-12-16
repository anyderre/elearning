@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  app startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and APP_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\ProyectoFinalServer-0.0.1-SNAPSHOT.jar;%APP_HOME%\lib\spring-boot-starter-freemarker-1.5.4.RELEASE.jar;%APP_HOME%\lib\jsoup-1.9.1.jar;%APP_HOME%\lib\spring-boot-starter-web-1.5.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-data-jpa-1.5.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-thymeleaf-1.5.4.RELEASE.jar;%APP_HOME%\lib\jquery-3.2.1.jar;%APP_HOME%\lib\bootstrap-3.2.0.jar;%APP_HOME%\lib\gson-2.8.1.jar;%APP_HOME%\lib\postgresql-9.4.1211.jar;%APP_HOME%\lib\spring-boot-devtools-1.5.4.RELEASE.jar;%APP_HOME%\lib\spring-security-config-4.2.3.RELEASE.jar;%APP_HOME%\lib\spring-security-web-4.2.3.RELEASE.jar;%APP_HOME%\lib\thymeleaf-extras-springsecurity4-3.0.2.RELEASE.jar;%APP_HOME%\lib\json-20090211.jar;%APP_HOME%\lib\quartz-2.3.0.jar;%APP_HOME%\lib\spring-boot-starter-1.5.4.RELEASE.jar;%APP_HOME%\lib\freemarker-2.3.26-incubating.jar;%APP_HOME%\lib\spring-context-support-4.3.9.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-tomcat-1.5.4.RELEASE.jar;%APP_HOME%\lib\hibernate-validator-5.3.5.Final.jar;%APP_HOME%\lib\jackson-databind-2.8.8.jar;%APP_HOME%\lib\spring-web-4.3.9.RELEASE.jar;%APP_HOME%\lib\spring-webmvc-4.3.9.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-aop-1.5.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-jdbc-1.5.4.RELEASE.jar;%APP_HOME%\lib\hibernate-core-5.0.12.Final.jar;%APP_HOME%\lib\hibernate-entitymanager-5.0.12.Final.jar;%APP_HOME%\lib\javax.transaction-api-1.2.jar;%APP_HOME%\lib\spring-data-jpa-1.11.4.RELEASE.jar;%APP_HOME%\lib\spring-aspects-4.3.9.RELEASE.jar;%APP_HOME%\lib\thymeleaf-spring4-2.1.5.RELEASE.jar;%APP_HOME%\lib\thymeleaf-layout-dialect-1.4.0.jar;%APP_HOME%\lib\spring-boot-1.5.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-autoconfigure-1.5.4.RELEASE.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\spring-security-core-4.2.3.RELEASE.jar;%APP_HOME%\lib\spring-aop-4.3.9.RELEASE.jar;%APP_HOME%\lib\spring-beans-4.3.9.RELEASE.jar;%APP_HOME%\lib\spring-context-4.3.9.RELEASE.jar;%APP_HOME%\lib\spring-core-4.3.9.RELEASE.jar;%APP_HOME%\lib\spring-expression-4.3.9.RELEASE.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\c3p0-0.9.5.2.jar;%APP_HOME%\lib\mchange-commons-java-0.2.11.jar;%APP_HOME%\lib\HikariCP-java6-2.3.13.jar;%APP_HOME%\lib\spring-boot-starter-logging-1.5.4.RELEASE.jar;%APP_HOME%\lib\snakeyaml-1.17.jar;%APP_HOME%\lib\tomcat-embed-core-8.5.15.jar;%APP_HOME%\lib\tomcat-embed-el-8.5.15.jar;%APP_HOME%\lib\tomcat-embed-websocket-8.5.15.jar;%APP_HOME%\lib\validation-api-1.1.0.Final.jar;%APP_HOME%\lib\jboss-logging-3.3.1.Final.jar;%APP_HOME%\lib\classmate-1.3.3.jar;%APP_HOME%\lib\jackson-annotations-2.8.0.jar;%APP_HOME%\lib\jackson-core-2.8.8.jar;%APP_HOME%\lib\aspectjweaver-1.8.10.jar;%APP_HOME%\lib\tomcat-jdbc-8.5.15.jar;%APP_HOME%\lib\spring-jdbc-4.3.9.RELEASE.jar;%APP_HOME%\lib\hibernate-jpa-2.1-api-1.0.0.Final.jar;%APP_HOME%\lib\javassist-3.21.0-GA.jar;%APP_HOME%\lib\antlr-2.7.7.jar;%APP_HOME%\lib\jandex-2.0.0.Final.jar;%APP_HOME%\lib\dom4j-1.6.1.jar;%APP_HOME%\lib\hibernate-commons-annotations-5.0.1.Final.jar;%APP_HOME%\lib\spring-data-commons-1.13.4.RELEASE.jar;%APP_HOME%\lib\spring-orm-4.3.9.RELEASE.jar;%APP_HOME%\lib\spring-tx-4.3.9.RELEASE.jar;%APP_HOME%\lib\jcl-over-slf4j-1.7.25.jar;%APP_HOME%\lib\thymeleaf-2.1.5.RELEASE.jar;%APP_HOME%\lib\groovy-2.4.11.jar;%APP_HOME%\lib\logback-classic-1.1.11.jar;%APP_HOME%\lib\jul-to-slf4j-1.7.25.jar;%APP_HOME%\lib\log4j-over-slf4j-1.7.25.jar;%APP_HOME%\lib\tomcat-juli-8.5.15.jar;%APP_HOME%\lib\ognl-3.0.8.jar;%APP_HOME%\lib\unbescape-1.1.0.RELEASE.jar;%APP_HOME%\lib\logback-core-1.1.11.jar

@rem Execute app
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %APP_OPTS%  -classpath "%CLASSPATH%" com.proyectoFinal.OMSA.OmsaApplication %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable APP_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%APP_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
