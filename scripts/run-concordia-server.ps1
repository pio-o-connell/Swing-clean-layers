Param(
    [string]$DbUrl = 'jdbc:postgresql://127.0.0.1:5432/concordia',
    [string]$DbUser = 'postgres',
    [string]$DbPassword = 'password',
    [int]$Port = 8080
)

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot '..')
Set-Location -Path $repoRoot

function Stop-PortListener {
    param([int]$ListenPort)
    $connections = Get-NetTCPConnection -LocalPort $ListenPort -State Listen -ErrorAction SilentlyContinue
    if (-not $connections) {
        return
    }

    $pids = $connections | Select-Object -ExpandProperty OwningProcess -Unique
    foreach ($pid in $pids) {
        try {
            $proc = Get-Process -Id $pid -ErrorAction Stop
            Write-Host "Stopping process $($proc.ProcessName) (PID $pid) on port $ListenPort" -ForegroundColor Yellow
            Stop-Process -Id $pid -Force
        } catch {
            Write-Warning "Could not stop PID $pid: $($_.Exception.Message)"
        }
    }
}

Stop-PortListener -ListenPort $Port

$env:CONCORDIA_DB_URL = $DbUrl
$env:CONCORDIA_DB_USER = $DbUser
$env:CONCORDIA_DB_PASSWORD = $DbPassword

Write-Host "Starting Jetty with URL=$DbUrl user=$DbUser" -ForegroundColor Cyan

& "$repoRoot/start-jetty.ps1"
if ($LASTEXITCODE -ne 0) {
    throw "start-jetty.ps1 exited with code $LASTEXITCODE"
}
