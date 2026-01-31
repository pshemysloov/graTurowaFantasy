@echo off
echo === Budowanie i uruchamianie Serwera ===
echo.
call mvnw.cmd clean package -Pserver -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo BLAD: Kompilacja nie powiodla sie
    pause
    exit /b 1
)
echo.
echo === Uruchamianie ===
java --enable-native-access=ALL-UNNAMED -jar target\server.jar
pause
