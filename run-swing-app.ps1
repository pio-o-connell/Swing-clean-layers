# Compile all Java source files for the Swing app and run the main application
# Run this script from your project root (where this script is located)

# Compile all Java files in swing-app/src/main/java and output to swing-app/bin
if (!(Test-Path "swing-app/bin")) {
    New-Item -ItemType Directory -Path "swing-app/bin" | Out-Null
}
javac -d swing-app/bin -cp lib/postgresql-42.7.3.jar (Get-ChildItem -Recurse -Filter *.java -Path swing-app/src/main/java | ForEach-Object { $_.FullName })

# Run the Swing application
java -cp "swing-app/bin;lib/postgresql-42.7.3.jar" Concordia.infrastructure.ApplicationBootstrap
