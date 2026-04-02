#!/bin/bash
# follow-up-checker.sh
# Revisa qué agencias necesitan follow-up hoy

CSV_FILE="../agencias.csv"
TEMPLATE_DIR="../templates"
TODAY=$(date +%Y-%m-%d)

export EMAIL_SMTP_SERVER="smtp.gmail.com"
export EMAIL_SMTP_PORT="465"
export EMAIL_SENDER="disenoxplain@gmail.com"
export EMAIL_SMTP_PASSWORD="mddpljwzkwajuiyz"

echo "📅 CHECK DE FOLLOW-UPS PARA: $TODAY"
echo "========================================"

while IFS=',' read -r id agencia website email telefono nombre cargo zona corredores prioridad estado email_enviado respondio reunion_agendada piloto_aceptado score notas secuencia ultimo_contacto proximo_paso fecha_proximo; do
    [ "$id" = "ID" ] && continue
    
    # Solo revisar las que tienen email enviado pero no han respondido
    if [ "$estado" = "EMAIL_ENVIADO" ] && [ -z "$respondio" ]; then
        
        # Calcular días desde envío
        if [ -n "$email_enviado" ]; then
            dias=$(( ($(date -d "$TODAY" +%s) - $(date -d "$email_enviado" +%s)) / 86400 ))
            
            case $dias in
                3)
                    echo "📧 DÍA 3: $agencia ($email) - Enviar Follow-Up #1"
                    echo "   Acción: ./cold-email-sender.sh --followup1 $id"
                    ;;
                7)
                    echo "📧 DÍA 7: $agencia ($email) - Enviar Follow-Up #2"
                    echo "   Acción: ./cold-email-sender.sh --followup2 $id"
                    ;;
                14)
                    echo "📧 DÍA 14: $agencia ($email) - Enviar Break-Up"
                    echo "   Acción: ./cold-email-sender.sh --breakup $id"
                    ;;
                15)
                    echo "🚫 DÍA 15+: $agencia - Mover a NO_INTERESADO"
                    ;;
            esac
        fi
    fi
done < "$CSV_FILE"

echo "========================================"
echo "✅ Check completado"
