@echo off
echo === Budowanie i uruchamianie Klienta ===
echo.
call mvnw.cmd clean package -Pclient -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo BLAD: Kompilacja nie powiodla sie
    pause
    exit /b 1
)
echo.
echo === Uruchamianie ===
java --enable-native-access=ALL-UNNAMED -jar target\client.jar
pause
