# Jetty Start Script for Windows PowerShell
# Change to the script's directory, then up to project root
Set-Location $PSScriptRoot
if (-Not (Test-Path "$PWD\pom.xml") -and (Test-Path "..\pom.xml")) {
	Set-Location ..
}

# Set environment variables
$projectJdk = Join-Path $PWD "environments/jdk-25"
$msJdk = "C:/Program Files/Microsoft/jdk-17.0.17.10-hotspot"
$projectJava = Join-Path $projectJdk "bin/java.exe"
$msJava = Join-Path $msJdk "bin/java.exe"
if (Test-Path $projectJava) {
	$env:JAVA_HOME = $projectJdk
} elseif (Test-Path $msJava) {
	$env:JAVA_HOME = $msJdk
} else {
	throw "No suitable JDK found (checked $projectJdk and $msJdk)."
}
$env:PATH = "$env:JAVA_HOME\bin;${env:PATH}"
$env:JETTY_BASE = "$PWD\jetty-base"

# Start Jetty (Windows friendly: use jetty.cmd, fall back to java -jar)
$jettyCmd = Join-Path $PWD "jetty-home-12.1.5/bin/jetty.cmd"
Write-Host "Using JETTY_BASE=$env:JETTY_BASE"
if (Test-Path $jettyCmd) {
	& $jettyCmd start
} else {
	# Fallback startup honoring the configured jetty base
	& (Join-Path $env:JAVA_HOME "bin/java.exe") "-Djetty.base=$($env:JETTY_BASE)" -jar "$PWD\jetty-home-12.1.5\start.jar"
}
