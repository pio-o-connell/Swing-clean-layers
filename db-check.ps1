[CmdletBinding()]
param(
  [string]$HostName = '127.0.0.1',
  [int]$Port = 3306,
  [string]$User = 'root',
  [string]$Password = 'ROOT',
  [string]$Database = 'warehouse',
  [string]$MysqlExe
)

$ErrorActionPreference = 'Stop'

function Resolve-MysqlExe {
  param([string]$Provided)

  if ($Provided) {
    if (Test-Path $Provided) { return (Resolve-Path $Provided).Path }
    throw "mysql.exe not found at: $Provided"
  }

  $candidates = @(
    'C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe',
    'C:\Program Files\MySQL\MySQL Server 8.3\bin\mysql.exe',
    'C:\Program Files\MySQL\MySQL Server 8.2\bin\mysql.exe',
    'C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe',
    'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe'
  )

  foreach ($p in $candidates) {
    if (Test-Path $p) { return $p }
  }

  $cmd = Get-Command mysql.exe -ErrorAction SilentlyContinue
  if ($cmd -and $cmd.Source) { return $cmd.Source }

  throw "Could not locate mysql.exe. Install MySQL Server tools or pass -MysqlExe 'C:\\Path\\to\\mysql.exe'."
}

$mysql = Resolve-MysqlExe -Provided $MysqlExe

Write-Host "mysql.exe: $mysql" -ForegroundColor Cyan

# Avoid embedding password in args; use env var temporarily.
$oldPwd = $env:MYSQL_PWD
$env:MYSQL_PWD = $Password

try {
  & $mysql --protocol=tcp -h $HostName -P $Port -u $User -e "SELECT VERSION() AS mysql_version, @@version_comment AS version_comment;" | Out-Host

  Write-Host "\nDatabase: $Database" -ForegroundColor Cyan
  & $mysql --protocol=tcp -h $HostName -P $Port -u $User -D $Database -e "SHOW TABLES;" | Out-Host

  # Get table names in a parsable format.
  $tables = & $mysql --protocol=tcp -h $HostName -P $Port -u $User -D $Database -N -B -e "SHOW TABLES;"

  if (-not $tables -or $tables.Count -eq 0) {
    Write-Host "No tables found in '$Database'." -ForegroundColor Yellow
    exit 0
  }

  Write-Host "\nRow counts" -ForegroundColor Cyan
  foreach ($t in $tables) {
    if ([string]::IsNullOrWhiteSpace($t)) { continue }
    $escapedTable = $t.Replace('`', '``')
    $sql = 'SELECT COUNT(*) FROM `' + $escapedTable + '`;'
    $count = & $mysql --protocol=tcp -h $HostName -P $Port -u $User -D $Database -N -B -e $sql
    "{0,-30} {1,10}" -f $t, $count
  }
}
finally {
  $env:MYSQL_PWD = $oldPwd
}
