@echo off
setlocal

REM Student Grade Tracker - GUI Launcher (Windows)
REM Requirements: Java JDK installed (javac and java available in PATH)

cd /d "%~dp0.."

if not exist out mkdir out

echo Compiling...
javac -cp "libs\jfreechart-1.5.4.jar" -d out src\*.java Main.java
if errorlevel 1 (
  echo.
  echo Compile failed. Please install JDK and ensure "javac" works in terminal.
  exit /b 1
)

echo.
echo Running GUI...
java -cp "out;libs\jfreechart-1.5.4.jar" Main

endlocal
