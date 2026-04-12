@echo off
REM LifeCAL Launcher
REM Runs the application using Maven JavaFX plugin

title LIFE CAL System

cd /d "%~dp0"

echo ========================================
echo   LIFE CAL System - Starting...
echo ========================================
echo.

REM Check if in LifeCAL-Portable folder
if exist "..\pom.xml" (
    cd ..
)

REM Check if Maven is available
where mvn >nul 2>nul
if errorlevel 1 (
    echo ERROR: Maven not found in PATH
    echo.
    echo Please run using one of these methods:
    echo   1. Install Maven and add to PATH
    echo   2. Run from project root: mvn javafx:run
    echo.
    pause
    exit /b 1
)

REM Run application
echo Starting LifeCAL...
echo.
mvn javafx:run

if errorlevel 1 (
    echo.
    echo ========================================
    echo   Failed to start application
    echo ========================================
    pause
)
