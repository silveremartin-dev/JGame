@echo off
REM JGame Javadoc Generation Script
REM Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

setlocal enabledelayedexpansion

cd /d "%~dp0"

echo.
echo ========================================
echo    JGame Javadoc Generator
echo ========================================
echo.

REM Create output directory
if not exist "javadoc" mkdir javadoc

REM Generate Javadoc
echo Generating Javadoc...
call mvn javadoc:aggregate -Ddoctitle="JGame Platform API" -Dwindowtitle="JGame API" -DoutputDirectory=javadoc

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo    Javadoc generated!
    echo ========================================
    echo.
    echo Output: javadoc\index.html
    
    REM List output
    dir javadoc
) else (
    echo Javadoc generation failed.
    exit /b 1
)
