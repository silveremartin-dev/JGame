@echo off
REM JGame Build and Copy JARs to /bin
REM Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

setlocal enabledelayedexpansion

cd /d "%~dp0"

set VERSION=1.0-SNAPSHOT

echo.
echo ========================================
echo    JGame Build to /bin
echo ========================================
echo.

REM Build project
echo Building project...
call mvn clean package -DskipTests -q

if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

REM Create bin directory structure
if not exist "bin" mkdir bin
if not exist "bin\lib" mkdir bin\lib
if not exist "bin\plugins" mkdir bin\plugins

echo.
echo Copying JARs to bin/...

REM Copy main JARs
copy /Y "jgame-core\target\jgame-core-%VERSION%.jar" "bin\" >nul 2>&1
copy /Y "jgame-persistence\target\jgame-persistence-%VERSION%.jar" "bin\" >nul 2>&1
copy /Y "jgame-server\target\jgame-server-%VERSION%.jar" "bin\" >nul 2>&1
copy /Y "jgame-client-java\target\jgame-client-java-%VERSION%.jar" "bin\" >nul 2>&1

REM Copy dependencies to lib
echo Copying dependencies...
call mvn dependency:copy-dependencies -DoutputDirectory=bin\lib -pl jgame-server -q 2>nul

REM Copy game plugins
echo Copying game plugins...
for %%G in (chess checkers goose solitaire) do (
    if exist "jgame-games\jgame-game-%%G\target\jgame-game-%%G-%VERSION%.jar" (
        copy /Y "jgame-games\jgame-game-%%G\target\jgame-game-%%G-%VERSION%.jar" "bin\plugins\" >nul 2>&1
        echo   - jgame-game-%%G.jar
    )
)

REM Copy launcher scripts
copy /Y "run-server.bat" "bin\" >nul 2>&1
copy /Y "run-client.bat" "bin\" >nul 2>&1
copy /Y "run-server.sh" "bin\" >nul 2>&1
copy /Y "run-client.sh" "bin\" >nul 2>&1

REM Copy web client
if not exist "bin\web" mkdir bin\web
xcopy /s /e /q /y "jgame-client-web\src\*" "bin\web\" >nul 2>&1

echo.
echo ========================================
echo    Build Complete!
echo ========================================
echo.
echo Output structure:
echo   bin\
echo     jgame-core-%VERSION%.jar
echo     jgame-persistence-%VERSION%.jar
echo     jgame-server-%VERSION%.jar
echo     jgame-client-java-%VERSION%.jar
echo     lib\              (dependencies)
echo     plugins\          (game JARs)
echo     web\              (web client)
echo     run-server.bat
echo     run-client.bat
echo.

dir /b bin\*.jar 2>nul
echo.
echo Plugins:
dir /b bin\plugins\*.jar 2>nul

pause
