@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------

@IF "%__MVN_ENV_DEBUG%"=="" @ECHO OFF

@SETLOCAL

SET ERROR_CODE=0

@REM Set local scope for the variables with windows NT shell
IF "%OS%"=="Windows_NT" @SETLOCAL

@REM ==== START VALIDATION ====
IF NOT "%JAVA_HOME%" == "" GOTO OkJHome

ECHO.
ECHO Error: JAVA_HOME is not defined correctly.
ECHO We cannot execute %0 without JAVA_HOME or java on PATH.
ECHO.
SET ERROR_CODE=1
GOTO end

:OkJHome
IF EXIST "%JAVA_HOME%\bin\java.exe" GOTO init

ECHO.
ECHO Error: JAVA_HOME is set to an invalid directory.
ECHO %JAVA_HOME%
ECHO Please set the JAVA_HOME variable in your environment to match the
ECHO location of your Java installation.
ECHO.
SET ERROR_CODE=1
GOTO end

:init
SET "DIRNAME=%~dp0"
IF "%DIRNAME%" == "" SET "DIRNAME=."
SET "APP_BASE_NAME=%~n0"
SET "APP_HOME=%DIRNAME%"

@REM Execute mvn if installed or fallback
WHERE mvn >nul 2>nul
IF %ERRORLEVEL% EQU 0 (
    mvn %*
    IF ERRORLEVEL 1 SET ERROR_CODE=1
    GOTO end
)

ECHO Executing Maven Wrapper...
SET "WRAPPER_JAR=%APP_HOME%\.mvn\wrapper\maven-wrapper.jar"
"%JAVA_HOME%\bin\java.exe" -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%APP_HOME%." org.apache.maven.wrapper.MavenWrapperMain %*
IF ERRORLEVEL 1 SET ERROR_CODE=1

:end
@ENDLOCAL & EXIT /B %ERROR_CODE%
