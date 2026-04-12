# LIFE CAL Application Runner
# This script runs the LIFE CAL JavaFX application using Maven

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  LIFE CAL System Launcher" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Maven is installed
$mavenInstalled = Get-Command mvn -ErrorAction SilentlyContinue

if ($null -eq $mavenInstalled) {
    Write-Host "❌ Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host ""
    Write-Host "📥 SOLUTION OPTIONS:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Option 1: Install Maven using Chocolatey" -ForegroundColor White
    Write-Host "  1. Install Chocolatey: https://chocolatey.org/install" -ForegroundColor Gray
    Write-Host "  2. Run: choco install maven -y" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Option 2: Download Maven manually" -ForegroundColor White
    Write-Host "  1. Download from: https://maven.apache.org/download.cgi" -ForegroundColor Gray
    Write-Host "  2. Extract to C:\apache-maven" -ForegroundColor Gray
    Write-Host "  3. Add to PATH: C:\apache-maven\bin" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Option 3: Use IntelliJ IDEA (Easiest)" -ForegroundColor White
    Write-Host "  1. Download: https://www.jetbrains.com/idea/download/" -ForegroundColor Gray
    Write-Host "  2. Open this project folder" -ForegroundColor Gray
    Write-Host "  3. Run LifeCalApp.java directly" -ForegroundColor Gray
    Write-Host ""
    
    # Try to find Maven in common locations
    $possibleMavenPaths = @(
        "C:\apache-maven\bin\mvn.cmd",
        "C:\Program Files\Apache\maven\bin\mvn.cmd",
        "C:\Program Files\Maven\bin\mvn.cmd",
        "$env:USERPROFILE\apache-maven\bin\mvn.cmd"
    )
    
    $foundMaven = $null
    foreach ($path in $possibleMavenPaths) {
        if (Test-Path $path) {
            $foundMaven = $path
            Write-Host "✅ Found Maven at: $foundMaven" -ForegroundColor Green
            break
        }
    }
    
    if ($null -ne $foundMaven) {
        Write-Host ""
        Write-Host "🚀 Running with found Maven installation..." -ForegroundColor Green
        & $foundMaven clean javafx:run
    } else {
        Write-Host "Press any key to exit..."
        $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        exit 1
    }
} else {
    Write-Host "✅ Maven found!" -ForegroundColor Green
    Write-Host "🚀 Starting LIFE CAL Application..." -ForegroundColor Green
    Write-Host ""
    
    # Run the application using Maven JavaFX plugin
    mvn clean javafx:run
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Application closed." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
