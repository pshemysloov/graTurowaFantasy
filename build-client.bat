@echo off
echo === Budowanie Klienta ===
call mvnw.cmd clean package -Pclient -DskipTests
echo.
echo Gotowe: target\client.jar
pause
