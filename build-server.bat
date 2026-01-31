@echo off
echo === Budowanie Serwera ===
call mvnw.cmd clean package -Pserver -DskipTests
echo.
echo Gotowe: target\server.jar
pause
