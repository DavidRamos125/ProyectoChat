@echo off
echo Iniciando contenedor PostgreSQL...
docker compose -f compose-postgres.yml up -d
pause
