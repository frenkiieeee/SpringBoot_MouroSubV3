#!/bin/bash
set -e

# Este script se encarga de generar el archivo .env a partir de .env.example en la primera ejecucion
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$SCRIPT_DIR/.env"
ENV_EXAMPLE="$SCRIPT_DIR/.env.example"

# Genera una cadena aleatoria de 32 caracteres para contraseñas y secretos
generate_secret() {
    openssl rand -base64 32 | tr -d '/+' | cut -c1-32
}

# Detecta la IP local principal (excluye docker0, lo, etc)
detect_ip() {
    local ip=""
    # Intentar obtener la IP de la interfaz de la ruta por defecto
    ip=$(ip route get 1.1.1.1 2>/dev/null | grep -oP 'src \K\S+' | head -1)
    if [ -z "$ip" ]; then
        # Alternativa: primera IP no-loopback
        ip=$(hostname -I | awk '{print $1}')
    fi
    if [ -z "$ip" ]; then
        ip="localhost"
    fi
    echo "$ip"
}

# Crea .env a partir de .env.example si no existe
if [ ! -f "$ENV_FILE" ]; then
    echo "First run detected. Generating .env from template..."

    if [ ! -f "$ENV_EXAMPLE" ]; then
        echo "ERROR: .env.example not found at $ENV_EXAMPLE"
        exit 1
    fi

    cp "$ENV_EXAMPLE" "$ENV_FILE"

    # Detectar IP automaticamente
    DETECTED_IP=$(detect_ip)
    echo "Detected LAN IP: $DETECTED_IP"

    # Sustituye marcadores por valores generados
    sed -i "s/YOUR_LAN_IP/$DETECTED_IP/g" "$ENV_FILE"
    sed -i "s/JWT_SECRET_PLACEHOLDER/$(generate_secret)/g" "$ENV_FILE"
    sed -i "s/POSTGRES_PASSWORD_PLACEHOLDER/$(generate_secret)/g" "$ENV_FILE"
    sed -i "s/ANON_KEY_PLACEHOLDER/$(openssl rand -base64 48 | tr -d '/+' | cut -c1-60)/g" "$ENV_FILE"
    sed -i "s/SERVICE_ROLE_KEY_PLACEHOLDER/$(openssl rand -base64 48 | tr -d '/+' | cut -c1-60)/g" "$ENV_FILE"

    echo ".env generated successfully with auto-detected IP ($DETECTED_IP)."
    echo "Review .env before running docker compose up."
else
    echo ".env already exists. To regenerate, delete it first."
fi
