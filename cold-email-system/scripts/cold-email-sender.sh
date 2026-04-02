#!/bin/bash
# cold-email-sender.sh
# Sistema de envío de cold emails personalizados
# Uso: ./cold-email-sender.sh [ID_ESPECIFICO]

set -e

# Configuración
CSV_FILE="../agencias.csv"
TEMPLATE_DIR="../templates"
DELAY_MIN=30
DELAY_MAX=90
LOG_FILE="../results/envios-$(date +%Y-%m-%d).log"

# Configuración SMTP (ya configurada en openclaw.json)
export EMAIL_SMTP_SERVER="smtp.gmail.com"
export EMAIL_SMTP_PORT="465"
export EMAIL_SENDER="disenoxplain@gmail.com"
export EMAIL_SMTP_PASSWORD="mddpljwzkwajuiyz"

# Función para logging
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$LOG_FILE"
}

# Función para personalizar template
personalizar_email() {
    local template="$1"
    local nombre="$2"
    local agencia="$3"
    local zona="$4"
    
    cat "$template" | \
        sed "s/{{nombre}}/$nombre/g" | \
        sed "s/{{agencia}}/$agencia/g" | \
        sed "s/{{zona}}/$zona/g"
}

# Función para enviar email
enviar_email() {
    local email="$1"
    local asunto="$2"
    local cuerpo="$3"
    
    python3 ~/.openclaw/workspace/skills/send-email/send_email.py \
        "$email" \
        "$asunto" \
        "$cuerpo" 2>&1
}

# Función para actualizar CSV
actualizar_estado() {
    local id="$1"
    local nuevo_estado="$2"
    local fecha="$(date +%Y-%m-%d)"
    
    # Crear backup
    cp "$CSV_FILE" "$CSV_FILE.backup"
    
    # Actualizar línea específica
    awk -F, -v OFS=, -v id="$id" -v estado="$nuevo_estado" -v fecha="$fecha" '
    NR==1 {print; next}
    $1==id {$11=estado; $12=fecha; print}
    NR!=1 && $1!=id {print}
    ' "$CSV_FILE.backup" > "$CSV_FILE"
    
    rm "$CSV_FILE.backup"
}

# Header
log "========================================="
log "INICIANDO CAMPAÑA DE COLD EMAILS"
log "Fecha: $(date)"
log "========================================="

# Contadores
ENVIADOS=0
FALLIDOS=0

# Procesar CSV
while IFS=',' read -r id agencia website email telefono nombre cargo zona corredores prioridad estado email_enviado resto; do
    # Saltar header
    [ "$id" = "ID" ] && continue
    
    # Si se especificó un ID, solo procesar ese
    if [ -n "$1" ] && [ "$id" != "$1" ]; then
        continue
    fi
    
    # Solo enviar a los que están POR_CONTACTAR
    if [ "$estado" != "POR_CONTACTAR" ]; then
        log "⏭️  Saltando $agencia (estado: $estado)"
        continue
    fi
    
    log "📧 Procesando: $nombre - $agencia ($email)"
    
    # Personalizar asunto y cuerpo
    asunto="¿Cuántos leads se les \"enfrían\" en $agencia?"
    cuerpo=$(personalizar_email "$TEMPLATE_DIR/email-inicial.txt" "$nombre" "$agencia" "$zona")
    
    # Enviar email
    if enviar_email "$email" "$asunto" "$cuerpo"; then
        log "✅ Email enviado exitosamente a $agencia"
        actualizar_estado "$id" "EMAIL_ENVIADO"
        ENVIADOS=$((ENVIADOS + 1))
        
        # Delay aleatorio entre envíos
        DELAY=$((RANDOM % (DELAY_MAX - DELAY_MIN + 1) + DELAY_MIN))
        log "⏱️  Esperando $DELAY segundos antes del siguiente..."
        sleep $DELAY
    else
        log "❌ Error enviando a $agencia ($email)"
        FALLIDOS=$((FALLIDOS + 1))
    fi
    
done < "$CSV_FILE"

# Resumen
log "========================================="
log "RESUMEN DE ENVÍO"
log "========================================="
log "✅ Enviados exitosamente: $ENVIADOS"
log "❌ Fallidos: $FALLIDOS"
log "📁 Log guardado en: $LOG_FILE"
log "========================================="
