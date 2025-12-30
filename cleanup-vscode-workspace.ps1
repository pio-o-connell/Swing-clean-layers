# PowerShell script to clean VS Code Java workspace
Remove-Item -Recurse -Force .metadata,.settings,.classpath,.project
Write-Host "Workspace cleanup complete. Please close and reopen VS Code, then re-import your Maven project if needed."