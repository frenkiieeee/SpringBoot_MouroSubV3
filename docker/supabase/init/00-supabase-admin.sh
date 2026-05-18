#!/bin/bash
# Crea y configura el rol 'supabase_admin' que necesita Supabase Studio.
# IMPORTANTE: este script SOLO se ejecuta en la primera inicializacion de la
# base de datos (cuando el volumen esta vacio). Es imprescindible para los
# despliegues nuevos: sin el, en un PC limpio Studio da error de supabase_admin.
# Se ejecuta antes que los .sql por el orden alfabetico (00 antes que 01).
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  DO \$\$
  BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'supabase_admin') THEN
      CREATE ROLE supabase_admin WITH LOGIN SUPERUSER CREATEROLE CREATEDB REPLICATION BYPASSRLS;
    END IF;
  END\$\$;

  -- Fijamos su contrasenya a la misma que POSTGRES_PASSWORD.
  ALTER ROLE supabase_admin WITH PASSWORD '$POSTGRES_PASSWORD';
EOSQL
