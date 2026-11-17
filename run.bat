@echo off
title Skill Swap - Peer-to-Peer Skill Exchange
echo ========================================================
echo   Starting Skill Swap Platform...
echo ========================================================

:: Check for Java
where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java is not detected on your system PATH.
    echo Please install Java 17 or higher from:
    echo https://adoptium.net/temurin/releases/?version=17
    echo.
    pause
    exit /b 1
)

echo Java detected!
java -version
echo.

:: Launch Spring Boot app using Maven Wrapper or Maven
if exist mvnw.cmd (
    echo Launching with Maven Wrapper...
    start http://localhost:8080
    call mvnw.cmd spring-boot:run
) else (
    echo Launching with installed Maven...
    start http://localhost:8080
    mvn spring-boot:run
)

pause
