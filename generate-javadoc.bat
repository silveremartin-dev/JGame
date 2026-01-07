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

REM Generate aggregated Javadoc
echo Generating Javadoc...
call mvn javadoc:aggregate -q -DreportOutputDirectory=javadoc -DdestDir=. -Ddoctitle="JGame Platform API" -Dwindowtitle="JGame API"

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo    Javadoc generated successfully!
    echo ========================================
    echo.
    
    REM Check if generated in javadoc folder directly
    if exist "javadoc\index.html" (
        echo Output: javadoc\index.html
    ) else (
        REM Check known locations in target
        if exist "target\reports\apidocs\index.html" (
            echo Output found in target\reports\apidocs
            echo Copying to javadoc folder...
            xcopy /E /Y /Q "target\reports\apidocs\*" "javadoc\"
            echo Output: javadoc\index.html
        ) else (
            if exist "target\site\apidocs\index.html" (
                echo Output found in target\site\apidocs
                echo Copying to javadoc folder...
                xcopy /E /Y /Q "target\site\apidocs\*" "javadoc\"
                echo Output: javadoc\index.html
            ) else (
                echo Warning: Could not locate index.html. Please check target folder manually.
            )
        )
    )
    
    echo.
    echo Done!
) else (
    echo Javadoc generation failed.
    pause
    exit /b 1
)

pause
