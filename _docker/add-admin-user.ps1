$bin = Join-Path $([Environment]::GetEnvironmentVariable("JBOSS_HOME", "User")) "bin"

& $bin\add-user.ps1 -u admin -p admin123!