@echo off
echo Iniciando contenedor Oracle XE...
docker compose -f compose-oracle.yml up -d
pause
