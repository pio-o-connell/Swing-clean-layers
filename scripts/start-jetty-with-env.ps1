Param(
    [string]$DbUrl = 'jdbc:postgresql://127.0.0.1:5432/concordia',
    [string]$DbUser = 'postgres',
    [string]$DbPassword = 'password'
)

Set-Location -Path (Join-Path $PSScriptRoot '..')

$env:CONCORDIA_DB_URL = $DbUrl
$env:CONCORDIA_DB_USER = $DbUser
$env:CONCORDIA_DB_PASSWORD = $DbPassword

Write-Host "Starting Jetty with URL=$DbUrl user=$DbUser" -ForegroundColor Cyan

./start-jetty.ps1
