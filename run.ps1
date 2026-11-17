Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "  Starting Skill Swap Platform..." -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan

# Check if Java is installed
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "[ERROR] Java is not installed or not in your PATH." -ForegroundColor Red
    Write-Host "Please download and install Java 17 (LTS) or higher from:" -ForegroundColor Yellow
    Write-Host "https://adoptium.net/temurin/releases/?version=17" -ForegroundColor White
    Read-Host "Press Enter to exit..."
    exit 1
}

Write-Host "Java is detected successfully:" -ForegroundColor Green
java -version

# Open browser in parallel after 4 seconds
Start-Job -ScriptBlock {
    Start-Sleep -Seconds 4
    Start-Process "http://localhost:8080"
} | Out-Null

# Run app
if (Test-Path ".\mvnw.cmd") {
    Write-Host "`nStarting Spring Boot via Maven Wrapper..." -ForegroundColor Cyan
    cmd.exe /c "mvnw.cmd spring-boot:run"
} else {
    Write-Host "`nStarting Spring Boot via Maven..." -ForegroundColor Cyan
    mvn spring-boot:run
}
