@echo off
echo Iniciando contenedor MySQL...
docker compose -f compose-mysql.yml up -d
pause
