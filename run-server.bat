@echo off
echo === Uruchamianie Serwera ===
echo.
if not exist target\server.jar (
    echo BLAD: Nie znaleziono target\server.jar
    echo Najpierw uruchom build-server.bat
    pause
    exit /b 1
)
java --enable-native-access=ALL-UNNAMED -jar target\server.jar
pause
