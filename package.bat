@echo off
REM JGame Distribution Packaging Script
REM Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

setlocal enabledelayedexpansion

cd /d "%~dp0"

set VERSION=1.0-SNAPSHOT
set BIN_DIR=bin
set DIST_DIR=dist

echo.
echo ========================================
echo    JGame Distribution Builder
echo ========================================
echo.

REM Clean and create directories
if exist "%BIN_DIR%" rmdir /s /q "%BIN_DIR%"
if exist "%DIST_DIR%" rmdir /s /q "%DIST_DIR%"
mkdir "%BIN_DIR%"
mkdir "%DIST_DIR%"

REM Build project
echo Building project...
call mvn clean package -DskipTests -q

if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

REM Copy JARs to bin
echo Copying JARs to bin/...
copy jgame-core\target\jgame-core-%VERSION%.jar "%BIN_DIR%\" >nul
copy jgame-persistence\target\jgame-persistence-%VERSION%.jar "%BIN_DIR%\" >nul
copy jgame-server\target\jgame-server-%VERSION%.jar "%BIN_DIR%\" >nul
copy jgame-client-java\target\jgame-client-java-%VERSION%.jar "%BIN_DIR%\" >nul

REM Copy game plugins
mkdir "%BIN_DIR%\plugins"
for /d %%d in (jgame-games\jgame-game-*) do (
    if exist "%%d\target\*.jar" copy "%%d\target\*.jar" "%BIN_DIR%\plugins\" >nul
)

REM Copy launcher scripts
copy run-server.bat "%BIN_DIR%\" >nul
copy run-client.bat "%BIN_DIR%\" >nul
copy run-server.sh "%BIN_DIR%\" >nul
copy run-client.sh "%BIN_DIR%\" >nul

REM Copy web client
echo Copying web client...
xcopy /s /e /q jgame-client-web\src "%BIN_DIR%\web\" >nul

REM Create distribution ZIP
echo Creating distribution archive...
powershell -Command "Compress-Archive -Path '%BIN_DIR%\*' -DestinationPath '%DIST_DIR%\jgame-%VERSION%.zip' -Force"

echo.
echo ========================================
echo    Distribution created!
echo ========================================
echo.
echo Output: %DIST_DIR%\jgame-%VERSION%.zip
echo Binaries: %BIN_DIR%\
echo.

pause
