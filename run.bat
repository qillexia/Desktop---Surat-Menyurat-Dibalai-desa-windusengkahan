@echo off
if not exist bin mkdir bin
echo Compiling...
javac -d bin -cp "lib/*" -sourcepath src -encoding UTF-8 src/main/Main.java
if %errorlevel% neq 0 (
    echo Compilation Failed!
    pause
    exit /b
)

echo Copying resources...
xcopy /s /y "src\*.jrxml" "bin\" >nul
xcopy /s /y "src\*.png" "bin\" >nul 2>&1

echo Running...
java -cp "bin;lib/*" main.Main
