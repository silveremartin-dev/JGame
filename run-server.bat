@echo off
REM JGame Server Launcher for Windows
REM Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

setlocal enabledelayedexpansion

cd /d "%~dp0"

REM Configuration
if "%JGAME_PORT%"=="" set JGAME_PORT=8080
if "%JAVA_OPTS%"=="" set JAVA_OPTS=-Xmx512m -Xms256m

echo.
echo ========================================
echo        JGame Server v1.0
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
echo Server port: %JGAME_PORT%
echo.

REM Build if needed
if not exist "jgame-server\target\classes" (
    echo Building project...
    call mvn package -DskipTests -q
)

REM Run server
echo Starting server...
echo.

cd jgame-server
call mvn exec:java -Dexec.mainClass="org.jgame.server.JGameServer" -Dserver.port=%JGAME_PORT%

pause
