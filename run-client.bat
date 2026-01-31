@echo off
echo === Uruchamianie Klienta ===
echo.
if not exist target\client.jar (
    echo BLAD: Nie znaleziono target\client.jar
    echo Najpierw uruchom build-client.bat
    pause
    exit /b 1
)
java --enable-native-access=ALL-UNNAMED -jar target\client.jar
pause
