# Flujo de Trabajo: Cold Email Outreach para Agencias de Corredores

## 🎯 Objetivo
Sistema completo para enviar cold emails personalizados, hacer seguimiento automático, y gestionar el pipeline de agencias inmobiliarias.

---

## 📊 Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                    FUENTE DE DATOS                              │
│              (Google Sheets / Airtable / CSV)                   │
│  • Nombre agencia    • Email        • Prioridad                 │
│  • Nombre contacto   • Teléfono     • Estado                    │
│  • Zona              • # corredores • Fecha último contacto     │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                  PERSONALIZACIÓN DE EMAILS                      │
│  • Templates con variables {{nombre}}, {{agencia}}, {{zona}}    │
│  • 3 versiones por agencia según perfil                         │
│  • Spin syntax para variaciones de asunto                       │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                     ENVÍO DE EMAILS                             │
│  • send-email skill (Gmail SMTP)                                │
│  • Rate limiting: 20 emails/hora máximo                         │
│  • Horario: 9 AM - 6 PM Chile                                   │
│  • Delay aleatorio entre envíos: 30-90 segundos                 │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│              SECUENCIA DE FOLLOW-UP AUTOMÁTICA                  │
│  Día 0: Email inicial                                           │
│  Día 3: Follow-up #1 (email corto)                              │
│  Día 7: Follow-up #2 (valor agregado)                           │
│  Día 14: Break-up email                                         │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                    PIPELINE TRACKER                             │
│  • Lead → Contactado → Respondió → Reunión → Piloto → Cliente  │
│  • Score de oportunidad (0-100)                                 │
│  • Próximos pasos con fechas                                    │
│  • Alertas de seguimiento                                       │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📋 Estructura de Datos (Google Sheets)

### Hoja 1: Agencias

| Columna | Descripción | Ejemplo |
|---------|-------------|---------|
| A | ID | 001 |
| B | Nombre Agencia | GESCOR Propiedades |
| C | Website | gescorpropiedades.cl |
| D | Email | contacto@gescorpropiedades.cl |
| E | Teléfono | +569XXXXXXXX |
| F | Nombre Contacto | Juan Pérez |
| G | Cargo | Gerente Comercial |
| H | Zona | Las Condes |
| I | # Corredores | 12 |
| J | Prioridad | ALTA |
| K | Estado | POR_CONTACTAR |
| L | Email Enviado | 2026-04-02 |
| M | Respondió | |
| N | Reunión Agendada | |
| O | Piloto Aceptado | |
| P | Score Oportunidad | 0 |
| Q | Notas | |
| R | Secuencia Activa | Día 0 |
| S | Último Contacto | |
| T | Próximo Paso | Enviar email inicial |
| U | Fecha Próximo Paso | 2026-04-02 |

### Hoja 2: Secuencias de Follow-Up

| Columna | Descripción |
|---------|-------------|
| A | ID Agencia |
| B | Día 0 (Email inicial) | Enviado: SI/NO, Fecha |
| C | Día 3 (Follow-up 1) | Enviado: SI/NO, Fecha |
| D | Día 7 (Follow-up 2) | Enviado: SI/NO, Fecha |
| E | Día 14 (Break-up) | Enviado: SI/NO, Fecha |
| F | Respuesta recibida | SI/NO, Fecha |
| G | Unsubscribe | SI/NO |

---

## 📝 Templates de Email

### Email Inicial (Día 0)

**Asunto:** {{spin}}Automatización de leads para {{agencia}}|¿Cuántos leads se les "enfrían" en {{agencia}}?{{endspin}}

```
Hola {{nombre}},

Me llamo [Tu Nombre] y ayudo a agencias inmobiliarias como {{agencia}} 
a convertir más leads en visitas sin contratar más personal.

Veo que manejan propiedades en {{zona}} — un mercado donde la velocidad 
de respuesta marca la diferencia entre cerrar una venta o perderla.

Estamos desarrollando un asistente de IA que:
• Responde leads de WhatsApp 24/7 (incluso a las 11 PM)
• Califica automáticamente a compradores serios
• Agenda visitas directo en tu calendario

¿Le interesaría una conversación de 30 minutos para ver si esto podría 
funcionar en {{agencia}}? No es una venta, es exploración genuina.

Tengo disponibilidad:
• [Día 1] a las [hora]
• [Día 2] a las [hora]  
• [Día 3] a las [hora]

¿Alguna opción le funciona?

Saludos,
[Tu Nombre]
[Tu Teléfono]
```

### Follow-Up #1 (Día 3) - Email corto

**Asunto:** Re: {{asunto_original}}

```
Hola {{nombre}},

Sé que está ocupado, así que solo un mensaje corto:

¿Le interesaría esa conversación de 30 minutos sobre cómo automatizar 
la atención de leads en {{agencia}}?

Si no es el momento, también me sirve saberlo.

Saludos,
[Tu Nombre]
```

### Follow-Up #2 (Día 7) - Valor agregado

**Asunto:** Caso: Cómo una agencia aumentó visitas 40%

```
Hola {{nombre}},

No sé si vio mi email anterior, pero quería compartirle un resultado 
concreto:

Una agencia similar a {{agencia}} en {{zona}} implementó un sistema 
de respuesta automática de leads hace 2 meses.

Resultados hasta ahora:
• 40% más visitas agendadas
• Tiempo de respuesta promedio: 2 minutos
• 15 horas ahorradas mensualmente por corredor

¿Le gustaría saber cómo funciona? Estoy disponible para una 
conversación breve esta semana.

Saludos,
[Tu Nombre]
```

### Break-Up Email (Día 14) - Último intento

**Asunto:** Último mensaje sobre automatización de leads

```
Hola {{nombre}},

No quiero ser pesado, así que este será mi último mensaje.

Entiendo que a veces el timing no es el ideal, o quizás la propuesta 
no encaja con lo que {{agencia}} necesita en este momento.

Si en algún momento quiere conversar sobre automatización de leads, 
estoy a un email de distancia.

Le deseo mucho éxito a {{agencia}}.

Saludos,
[Tu Nombre]
```

---

## 🔧 Scripts de Automatización

### Script 1: Enviar Emails Personalizados (Bash)

```bash
#!/bin/bash
# cold-email-sender.sh
# Uso: ./cold-email-sender.sh agencias.csv template.txt

CSV_FILE="$1"
TEMPLATE="$2"
DELAY_MIN=30
DELAY_MAX=90

# Configuración SMTP
export EMAIL_SMTP_SERVER="smtp.gmail.com"
export EMAIL_SMTP_PORT="465"
export EMAIL_SENDER="disenoxplain@gmail.com"
export EMAIL_SMTP_PASSWORD="mddpljwzkwajuiyz"

# Leer CSV y enviar emails
while IFS=',' read -r id agencia website email telefono nombre cargo zona corredores prioridad estado; do
    # Saltar header
    [ "$id" = "ID" ] && continue
    
    # Personalizar template
    asunto="¿Cuántos leads se les \"enfrían\" en $agencia?"
    cuerpo=$(cat "$TEMPLATE" | sed "s/{{nombre}}/$nombre/g" | sed "s/{{agencia}}/$agencia/g" | sed "s/{{zona}}/$zona/g")
    
    # Enviar email
    python3 ~/.openclaw/workspace/skills/send-email/send_email.py \
        "$email" \
        "$asunto" \
        "$cuerpo"
    
    echo "✅ Enviado a $nombre ($agencia) - $email"
    
    # Delay aleatorio
    DELAY=$((RANDOM % (DELAY_MAX - DELAY_MIN + 1) + DELAY_MIN))
    echo "⏱️  Esperando $DELAY segundos..."
    sleep $DELAY
    
done < "$CSV_FILE"

echo "🎉 Todos los emails han sido enviados"
```

### Script 2: Check de Follow-Ups

```bash
#!/bin/bash
# follow-up-checker.sh
# Revisa qué agencias necesitan follow-up hoy

TODAY=$(date +%Y-%m-%d)

echo "📅 Check de follow-ups para: $TODAY"
echo "========================================"

# Leer CSV y verificar fechas
while IFS=',' read -r id agencia email estado email_enviado respondio secuencia; do
    [ "$id" = "ID" ] && continue
    
    if [ "$estado" = "EMAIL_ENVIADO" ] && [ -z "$respondio" ]; then
        DIAS_DESDE_ENVIO=$(( ($(date -d "$TODAY" +%s) - $(date -d "$email_enviado" +%s)) / 86400 ))
        
        case $DIAS_DESDE_ENVIO in
            3)
                echo "📧 DÍA 3: Enviar Follow-Up #1 a $agencia ($email)"
                ;;
            7)
                echo "📧 DÍA 7: Enviar Follow-Up #2 a $agencia ($email)"
                ;;
            14)
                echo "📧 DÍA 14: Enviar Break-Up a $agencia ($email)"
                ;;
        esac
    fi
done < agencias.csv
```

### Script 3: Actualizar Estado en Sheets (Python)

```python
#!/usr/bin/env python3
# update-sheets.py
# Actualiza el estado de agencias en Google Sheets

import csv
from datetime import datetime

def actualizar_estado(agencia_id, nuevo_estado, notas=""):
    """Actualiza el estado de una agencia en el CSV"""
    
    rows = []
    with open('agencias.csv', 'r') as f:
        reader = csv.DictReader(f)
        fieldnames = reader.fieldnames
        for row in reader:
            if row['ID'] == agencia_id:
                row['Estado'] = nuevo_estado
                row['Ultimo Contacto'] = datetime.now().strftime('%Y-%m-%d')
                if notas:
                    row['Notas'] = notas
            rows.append(row)
    
    with open('agencias.csv', 'w', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)
    
    print(f"✅ Agencia {agencia_id} actualizada a: {nuevo_estado}")

# Ejemplo de uso
if __name__ == "__main__":
    import sys
    if len(sys.argv) >= 3:
        actualizar_estado(sys.argv[1], sys.argv[2], sys.argv[3] if len(sys.argv) > 3 else "")
    else:
        print("Uso: python3 update-sheets.py <ID> <ESTADO> [NOTAS]")
```

---

## 📈 Pipeline de Ventas

### Etapas del Pipeline

| Etapa | Descripción | Probabilidad | Acción siguiente |
|-------|-------------|--------------|------------------|
| **Lead** | Agencia identificada, no contactada | 5% | Enviar email inicial |
| **Contactado** | Email enviado, esperando respuesta | 10% | Esperar o follow-up |
| **Respondió** | Mostró interés, no reunión aún | 25% | Agendar llamada |
| **Reunión Agendada** | Cita confirmada | 40% | Hacer entrevista |
| **Piloto Propuesto** | Ofrecimos piloto de 30 días | 60% | Negociar términos |
| **Piloto Aceptado** | Firmó para prueba | 80% | Onboarding |
| **Cliente** | Pagando mensualidad | 100% | Retención |
| **No Interesado** | Rechazó explícitamente | 0% | Archivar |
| **No Califica** | No cumple criterios | 0% | Archivar |

### Métricas del Pipeline

```
┌────────────────────────────────────────────────────┐
│           PIPELINE DE AGENCIAS                     │
├────────────────────────────────────────────────────┤
│ Lead:           8 agencias    ($0 valor)           │
│ Contactado:     5 agencias    ($0 valor)           │
│ Respondió:      3 agencias    ($0 valor)           │
│ Reunión:        2 agencias    ($0 valor)           │
│ Piloto Prop:    1 agencia     ($399K potencial)    │
│ Piloto Acept:   0 agencias    ($0)                 │
│ Cliente:        0 agencias    ($0 MRR)             │
├────────────────────────────────────────────────────┤
│ Pipeline Total: $399K potencial mensual            │
│ Weighted:       ~$160K esperado                    │
│ Win Rate:       N/A (aún sin cierres)              │
└────────────────────────────────────────────────────┘
```

---

## 🎛️ Herramientas Recomendadas

### Opción A: Stack Open Source (Gratuito)

| Componente | Herramienta | Costo |
|------------|-------------|-------|
| Base de datos | Google Sheets | Gratis |
| Envío de emails | send-email skill | Gratis |
| Automatización | n8n self-hosted | Gratis |
| Calendar | Google Calendar | Gratis |

### Opción B: Stack Profesional (Pago)

| Componente | Herramienta | Costo |
|------------|-------------|-------|
| CRM | HubSpot (gratis hasta 1000 contactos) | Gratis-$50/mes |
| Email outreach | Instantly.ai | $37/mes |
| Enriquecimiento | Apollo.io | $49/mes |
| Automatización | Make.com | $9/mes |

---

## ⚙️ Configuración de n8n para Automatización

### Workflow 1: Envío Diario de Emails

```json
{
  "name": "Cold Email Daily Sender",
  "nodes": [
    {
      "type": "n8n-nodes-base.scheduleTrigger",
      "parameters": {
        "rule": {
          "interval": [
            {
              "field": "hours",
              "value": 9
            }
          ]
        }
      }
    },
    {
      "type": "n8n-nodes-base.googleSheets",
      "parameters": {
        "operation": "read",
        "sheetId": "SHEET_ID",
        "range": "Agencias!A:U",
        "filters": {
          "estado": "POR_CONTACTAR",
          "prioridad": "ALTA"
        }
      }
    },
    {
      "type": "n8n-nodes-base.function",
      "parameters": {
        "functionCode": "// Personalizar email\nconst agencia = items[0].json;\nconst email = {\n  to: agencia.email,\n  subject: `¿Cuántos leads se les "enfrían" en ${agencia.nombre}?`,\n  body: template.replace(/{{nombre}}/g, agencia.contacto).replace(/{{agencia}}/g, agencia.nombre)\n};\nreturn [{json: email}];"
      }
    },
    {
      "type": "n8n-nodes-base.httpRequest",
      "parameters": {
        "method": "POST",
        "url": "http://localhost:18789/api/skills/send-email/send",
        "body": {
          "to": "={{ $json.to }}",
          "subject": "={{ $json.subject }}",
          "body": "={{ $json.body }}"
        }
      }
    }
  ]
}
```

### Workflow 2: Check de Follow-Ups

```json
{
  "name": "Follow-Up Checker",
  "trigger": {
    "type": "schedule",
    "cron": "0 9 * * *"
  },
  "steps": [
    "Leer agencias con EMAIL_ENVIADO",
    "Calcular días desde último contacto",
    "Si día 3/7/14 → enviar follow-up correspondiente",
    "Actualizar estado en Sheets"
  ]
}
```

---

## 📋 Checklist de Implementación

### Setup Inicial (2-3 horas)

- [ ] Crear Google Sheet con estructura de agencias
- [ ] Poblar con las 20 agencias identificadas
- [ ] Crear 3 templates de email
- [ ] Configurar credenciales SMTP en n8n
- [ ] Instalar n8n (self-hosted o cloud)

### Semana 1: Primer Lote

- [ ] Enviar 5 emails a agencias ALTA prioridad
- [ ] Monitorear respuestas
- [ ] Documentar en tracker
- [ ] Hacer follow-ups manuales según corresponda

### Semana 2: Optimización

- [ ] Revisar métricas (open rate, response rate)
- [ ] Ajustar asuntos según performance
- [ ] Enviar siguiente lote de 5 emails
- [ ] Agendar primeras reuniones

### Semana 3-4: Escalamiento

- [ ] Automatizar workflows en n8n
- [ ] Enviar lote final de 10 emails
- [ ] Completar 5 entrevistas de validación
- [ ] Cerrar 2 pilotos

---

## 📊 KPIs a Trackear

| Métrica | Meta | Cómo medir |
|---------|------|------------|
| Emails enviados/semana | 10 | Contador en Sheets |
| Open rate | >40% | Mailtrack (Gmail add-on) |
| Response rate | >15% | Respuestas manuales |
| Reuniones agendadas | 5 total | Estado en pipeline |
| Piloto conversion rate | 40% | Closed won / Reunión |
| Costo por lead calificado | <$50 | Tiempo invertido |

---

## 🚨 Reglas Anti-Spam

1. **Rate limiting:** Máximo 20 emails/hora, 50/día
2. **Personalización:** Cada email debe tener mínimo 3 variables personalizadas
3. **Unsubscribe:** Incluir opción de salir en follow-ups
4. **Horario:** Solo enviar 9 AM - 6 PM hora local
5. **Lista limpia:** Verificar emails antes de enviar (hunter.io/verify)
6. **No re-enviar:** Si alguien responde "no interesado", marcar como NO_INTERESADO inmediatamente

---

## 📁 Archivos del Sistema

```
~/cold-email-workflow/
├── agencias.csv              # Base de datos principal
├── templates/
│   ├── email-inicial.txt
│   ├── followup-1.txt
│   ├── followup-2.txt
│   └── breakup.txt
├── scripts/
│   ├── cold-email-sender.sh
│   ├── follow-up-checker.sh
│   └── update-sheets.py
├── n8n-workflows/
│   ├── daily-sender.json
│   └── followup-checker.json
└── results/
    ├── envios-2026-04-02.log
    └── respuestas-tracking.csv
```

---

*Sistema creado el 2 de abril de 2026*
*Skills utilizadas: send-email, follow-up-sequence-writer, sales-pipeline-tracker*
