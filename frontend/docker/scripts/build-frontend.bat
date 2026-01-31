@echo off
cd /d "%~dp0..\.."

echo ================================
echo   ClashZone - Build Frontend
echo ================================
echo.

echo Building...
npm run build

echo.
echo Build completo!
echo.
pause
