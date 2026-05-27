#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$SCRIPT_DIR/.env"
ENV_EXAMPLE="$SCRIPT_DIR/.env.example"

# Generate random 32-char alphanumeric string for passwords/secrets
generate_secret() {
    openssl rand -base64 32 | tr -d '/+' | cut -c1-32
}

# Detect primary LAN IP (excluding docker0, lo, etc.)
detect_ip() {
    local ip=""
    # Try to get the IP of the default route interface
    ip=$(ip route get 1.1.1.1 2>/dev/null | grep -oP 'src \K\S+' | head -1)
    if [ -z "$ip" ]; then
        # Fallback: first non-loopback IP
        ip=$(hostname -I | awk '{print $1}')
    fi
    if [ -z "$ip" ]; then
        ip="localhost"
    fi
    echo "$ip"
}

# Generate .env from .env.example if it doesn't exist
if [ ! -f "$ENV_FILE" ]; then
    echo "First run detected. Generating .env from template..."

    if [ ! -f "$ENV_EXAMPLE" ]; then
        echo "ERROR: .env.example not found at $ENV_EXAMPLE"
        exit 1
    fi

    cp "$ENV_EXAMPLE" "$ENV_FILE"

    # Auto-detect IP
    DETECTED_IP=$(detect_ip)
    echo "Detected LAN IP: $DETECTED_IP"

    # Replace placeholders with generated values
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
