@echo off
REM JGame Build Script for Windows
REM Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

setlocal enabledelayedexpansion

cd /d "%~dp0"

echo.
echo ========================================
echo        JGame Build Script
echo ========================================
echo.

REM Check prerequisites
echo Checking prerequisites...

where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [X] Java not found
    pause
    exit /b 1
)
echo [OK] Java found

where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [X] Maven not found
    pause
    exit /b 1
)
echo [OK] Maven found

echo.
echo Building JGame Platform...
echo.

REM Clean and build
call mvn clean install -DskipTests

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo        BUILD SUCCESSFUL!
    echo ========================================
    echo.
    echo To start the server:  run-server.bat
    echo To start the client:  run-client.bat
) else (
    echo.
    echo Build failed!
)

pause
