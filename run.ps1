# Runs the Java Swing app (compile + run) with MySQL Connector/J 8.4.
# Usage:  powershell -ExecutionPolicy Bypass -File .\run.ps1

$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

$srcRoot = Join-Path $root 'src'
$binRoot = Join-Path $root 'bin'
$jar     = Join-Path $root 'lib\mysql-connector-j-8.4.0.jar'

if (-not (Test-Path $srcRoot)) { throw "Missing src folder: $srcRoot" }
if (-not (Test-Path $jar))     { throw "Missing JDBC jar: $jar (expected Connector/J 8.4.0)" }

if (-not (Test-Path $binRoot)) {
  New-Item -ItemType Directory -Path $binRoot | Out-Null
}

Write-Host "Compiling Java sources..." -ForegroundColor Cyan
$javaFiles = Get-ChildItem -Path $srcRoot -Recurse -Filter '*.java' | ForEach-Object { $_.FullName }
if (-not $javaFiles -or $javaFiles.Count -eq 0) { throw "No .java files found under $srcRoot" }

javac -encoding UTF-8 -cp "$jar;$srcRoot" -d $binRoot @javaFiles

Write-Host "Starting Swing app (WareHouse.maindriver)..." -ForegroundColor Cyan
$cp = "$binRoot;$jar"
java -cp $cp WareHouse.maindriver
