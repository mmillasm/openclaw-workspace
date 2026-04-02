#!/bin/bash
# update-status.sh
# Actualiza el estado de una agencia en el CSV
# Uso: ./update-status.sh <ID> <NUEVO_ESTADO> [NOTAS]

CSV_FILE="../agencias.csv"
ID="$1"
NUEVO_ESTADO="$2"
NOTAS="${3:-}"
FECHA="$(date +%Y-%m-%d)"

if [ -z "$ID" ] || [ -z "$NUEVO_ESTADO" ]; then
    echo "Uso: ./update-status.sh <ID> <NUEVO_ESTADO> [NOTAS]"
    echo ""
    echo "Estados válidos:"
    echo "  POR_CONTACTAR, EMAIL_ENVIADO, RESPONDIO, REUNION_AGENDADA,"
    echo "  PILOTO_PROPUESTO, PILOTO_ACEPTADO, CLIENTE, NO_INTERESADO"
    exit 1
fi

# Crear backup
cp "$CSV_FILE" "$CSV_FILE.backup.$(date +%s)"

# Actualizar
awk -F, -v OFS=, -v id="$ID" -v estado="$NUEVO_ESTADO" -v fecha="$FECHA" -v notas="$NOTAS" '
NR==1 {print; next}
$1==id {
    $11=estado
    $19=fecha
    if (notas != "") $17=notas
    print
    next
}
{print}
' "$CSV_FILE" > "$CSV_FILE.tmp" && mv "$CSV_FILE.tmp" "$CSV_FILE"

echo "✅ Agencia $ID actualizada a: $NUEVO_ESTADO"
echo "📅 Fecha: $FECHA"
[ -n "$NOTAS" ] && echo "📝 Notas: $NOTAS"
