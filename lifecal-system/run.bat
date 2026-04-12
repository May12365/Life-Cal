@echo off
REM LIFE CAL Application Runner
REM This script runs the LIFE CAL JavaFX application using Maven

echo ========================================
echo   LIFE CAL System Launcher
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo [OK] Maven found!
    echo [*] Starting LIFE CAL Application...
    echo.
    mvn clean javafx:run
) else (
    echo [ERROR] Maven is not installed or not in PATH
    echo.
    echo SOLUTION OPTIONS:
    echo.
    echo Option 1: Install Maven manually
    echo   1. Download from: https://maven.apache.org/download.cgi
    echo   2. Extract to C:\apache-maven
    echo   3. Add to PATH: C:\apache-maven\bin
    echo.
    echo Option 2: Use IntelliJ IDEA (Easiest)
    echo   1. Download: https://www.jetbrains.com/idea/download/
    echo   2. Open this project folder
    echo   3. Run LifeCalApp.java directly
    echo.
    
    REM Try to find Maven in common locations
    if exist "C:\apache-maven\bin\mvn.cmd" (
        echo [OK] Found Maven at C:\apache-maven\bin\mvn.cmd
        echo [*] Running with found Maven installation...
        echo.
        call "C:\apache-maven\bin\mvn.cmd" clean javafx:run
        goto :end
    )
    
    if exist "C:\Program Files\Apache\maven\bin\mvn.cmd" (
        echo [OK] Found Maven at C:\Program Files\Apache\maven\bin\mvn.cmd
        echo [*] Running with found Maven installation...
        echo.
        call "C:\Program Files\Apache\maven\bin\mvn.cmd" clean javafx:run
        goto :end
    )
    
    echo [ERROR] Could not find Maven installation
    pause
    exit /b 1
)

:end
echo.
echo ========================================
echo Application closed.
echo ========================================
pause
