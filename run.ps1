# Runs the Java Swing app (compile + run) with MySQL Connector/J 8.4.
# Usage:  powershell -ExecutionPolicy Bypass -File .\run.ps1

$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

$srcRoot = Join-Path $root 'src'
$binRoot = Join-Path $root 'bin'

# Build classpath from all JARs in lib/
$libDir = Join-Path $root 'lib'
$jars = Get-ChildItem -Path $libDir -Filter '*.jar' | ForEach-Object { $_.FullName }
$cpJars = ($jars -join ';')

if (-not (Test-Path $srcRoot)) { throw "Missing src folder: $srcRoot" }

if (-not (Test-Path $libDir)) { throw "Missing lib folder: $libDir" }
if (-not ($jars | Where-Object { $_ -like '*mysql-connector-j-8.4.0.jar' })) {
  throw "Missing JDBC jar: mysql-connector-j-8.4.0.jar in $libDir (expected Connector/J 8.4.0)" 
}
if (-not ($jars | Where-Object { $_ -like '*jackson-databind*.jar' })) {
  Write-Host "Warning: jackson-databind JAR not found in lib/. Please add required Jackson JARs." -ForegroundColor Yellow
}

if (-not (Test-Path $binRoot)) {
  New-Item -ItemType Directory -Path $binRoot | Out-Null
}

Write-Host "Compiling Java sources..." -ForegroundColor Cyan
$javaFiles = Get-ChildItem -Path $srcRoot -Recurse -Filter '*.java' | ForEach-Object { $_.FullName }
if (-not $javaFiles -or $javaFiles.Count -eq 0) { throw "No .java files found under $srcRoot" }


# Compile and show errors if any
Write-Host "Compiling all Java sources (showing errors if any)..." -ForegroundColor Yellow
$fullCp = "$cpJars;$srcRoot"
$javacOutput = & javac -encoding UTF-8 -cp $fullCp -d $binRoot @javaFiles 2>&1
if ($LASTEXITCODE -ne 0) {
  Write-Host "Javac compilation failed:" -ForegroundColor Red
  Write-Host $javacOutput -ForegroundColor Red
  exit 1
}
Write-Host $javacOutput



# Copy Concordia classes to ConcordiaWAR/WEB-INF/classes/Concordia
$concordiaBin = Join-Path $binRoot 'Concordia'
$webInfClasses = Join-Path $root 'ConcordiaWAR\WEB-INF\classes'
$dest = Join-Path $webInfClasses 'Concordia'
if (Test-Path $concordiaBin) {
    Write-Host "Copying Concordia classes to ConcordiaWAR/WEB-INF/classes/Concordia..." -ForegroundColor Cyan
    if (Test-Path $dest) {
        Remove-Item -Path $dest -Recurse -Force
    }
    Copy-Item -Path $concordiaBin -Destination $webInfClasses -Recurse -Force
} else {
    Write-Host "Concordia classes not found in bin/Concordia. Please check compilation." -ForegroundColor Red
}

# Repackage Concordia.war with updated classes
Write-Host "Repackaging Concordia.war with updated classes..." -ForegroundColor Cyan
if (Test-Path (Join-Path $root 'Concordia.war')) {
    Remove-Item -Path (Join-Path $root 'Concordia.war') -Force
}
Set-Location (Join-Path $root 'ConcordiaWAR')
jar -cf ../Concordia.war *
Set-Location $root

Write-Host "Starting Swing app (WareHouse.maindriver)..." -ForegroundColor Cyan
$cp = "$binRoot;$cpJars"
java -cp $cp WareHouse.maindriver

# Copy Concordia classes to ConcordiaWAR/WEB-INF/classes
$concordiaBin = Join-Path $binRoot 'Concordia'
$webInfClasses = Join-Path $root 'ConcordiaWAR\WEB-INF\classes'
if (Test-Path $concordiaBin) {
  Write-Host "Copying Concordia classes to ConcordiaWAR/WEB-INF/classes..." -ForegroundColor Cyan
  $dest = Join-Path $webInfClasses 'Concordia'
  if (-not (Test-Path $dest)) {
    New-Item -ItemType Directory -Path $dest -Force | Out-Null
  }
  Copy-Item -Path "$concordiaBin\*" -Destination $dest -Recurse -Force
} else {
  Write-Host "Concordia classes not found in bin/Concordia. Please check compilation." -ForegroundColor Red
}
