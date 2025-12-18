@echo off
REM JGame Client Launcher for Windows
REM Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

setlocal enabledelayedexpansion

cd /d "%~dp0"

REM Configuration
if "%JGAME_SERVER%"=="" set JGAME_SERVER=http://localhost:8080
if "%JAVA_OPTS%"=="" set JAVA_OPTS=-Xmx256m

echo.
echo ========================================
echo        JGame Client v1.0
echo ========================================
echo.

REM Check Java
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Java not found. Please install JDK 21+.
    pause
    exit /b 1
)

for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do set JAVA_VER=%%g
echo Java version: %JAVA_VER%
echo Server: %JGAME_SERVER%
echo.

REM Build if needed
if not exist "jgame-client-java\target\classes" (
    echo Building project...
    call mvn package -DskipTests -q
)

REM Run JavaFX client
echo Launching JavaFX client...
echo.

cd jgame-client-java
call mvn javafx:run -Dserver.url=%JGAME_SERVER%

pause
