# MVP Chatbot Corredores de Propiedades - Flujo Corregido

## 🎯 Corrección Principal

**Problema identificado:** El bot ignoraba que el lead ya había visto una propiedad específica y hacía preguntas genéricas.

**Solución:** Detectar el contexto de entrada (propiedad específica vs. consulta general) y bifurcar el flujo inmediatamente.

---

## 📊 Flujo de Conversación Corregido

### Punto de Entrada A: Lead desde Propiedad Específica

**Contexto:** Usuario vio un departamento/casa específica en el sitio web y hace clic en "Contactar por WhatsApp"

```
[LEAD]: "Hola, vi una propiedad en su web"

[BOT]: "¡Hola! 👋 Veo que te interesó una propiedad. 
        
        ¿Me puedes decir cuál? Puedes:
        • Pegar el título del aviso
        • Decirme la dirección aproximada
        • O simplemente describirla (ej: 'depto 2D 2B en Las Condes')
        
        Así te doy toda la info y coordinamos una visita."

[LEAD]: "Es el departamento en Apoquindo 4500, Las Condes"

[BOT]: "¡Perfecto! 🏢 *Departamento en Apoquindo 4500, Las Condes*
        
        • 2 dormitorios, 2 baños
        • 75 m² útiles
        • $8.500 UF
        • Disponible para visitas: lun-vie 10:00-18:00
        
        Antes de agendar, me gustaría confirmar unos datos:
        
        ¿Este departamento es para compra o arriendo?"

[LEAD]: "Compra"

[BOT]: "Excelente. ¿Este sería para habitarlo o como inversión?"

[LEAD]: "Para habitar"

[BOT]: "Entendido. ¿Tienes financiamiento aprobado o estás en proceso?"

        [SI tiene/aprobado] → "¡Perfecto! Eso agiliza todo."
        [SI está en proceso] → "Sin problema, muchos clientes visitan mientras aprueban."
        [SI aún no] → "Te recomiendo hablar con nuestro ejecutivo de crédito, puede acelerar el proceso."

[BOT]: "¿Cuándo te vendría bien visitarlo? Tenemos estos horarios disponibles esta semana:
        
        • Mañana (jueves) a las 11:00
        • Mañana (jueves) a las 16:00  
        • Viernes a las 10:00
        • Viernes a las 15:00
        
        ¿Alguno te funciona?"

[LEAD]: "Viernes a las 10:00"

[BOT]: "✅ *¡VISITA AGENDADA!*
        
        📍 Departamento Apoquindo 4500, Las Condes
        📅 Viernes [fecha] a las 10:00 hrs
        👤 Te atenderá: [Nombre Corredor]
        📞 Tel: [+56 9 XXXX XXXX]
        
        *Importante:* 
        • Llevar cédula de identidad
        • Duración aprox: 30 minutos
        • Si necesitas cancelar, avísanos con 2 horas de anticipación
        
        ¿Necesitas que te envíe la ubicación exacta por GPS?"
```

---

### Punto de Entrada B: Consulta General (sin propiedad específica)

**Contexto:** Usuario escribe sin referirse a una propiedad en particular

```
[LEAD]: "Hola, busco departamento"

[BOT]: "¡Hola! 👋 Soy el asistente de [Nombre Agencia]. 
        Te ayudo a encontrar la propiedad ideal y agendar visitas.
        
        Para recomendarte lo mejor, necesito conocer tu búsqueda:
        
        ¿Estás buscando para *comprar* o *arriendo*?"

[LEAD]: "Comprar"

[BOT]: "Perfecto. ¿En qué zona te gustaría? 
        
        Puedes decirme:
        • Comuna específica (ej: Las Condes, Providencia, Ñuñoa)
        • Zona amplia (ej: "sector oriente", "cerca del metro")
        • O varias opciones (ej: "Las Condes o Vitacura")"

[LEAD]: "Las Condes o Vitacura"

[BOT]: "¿Cuántos dormitorios necesitas?
        • 1 dormitorio
        • 2 dormitorios
        • 3+ dormitorios
        • Estudio/loft"

[LEAD]: "2 dormitorios"

[BOT]: "¿Cuál es tu rango de presupuesto aproximado?
        
        • Hasta 5.000 UF
        • 5.000 - 8.000 UF
        • 8.000 - 12.000 UF
        • 12.000 - 15.000 UF
        • Más de 15.000 UF"

[LEAD]: "8.000 - 12.000 UF"

[BOT]: "¿Qué tan pronto necesitas concretar?
        
        • *Esta semana* (ya estoy viendo opciones)
        • *Este mes* (tengo urgencia moderada)
        • *En 2-3 meses* (estoy comparando)
        • *Aún no sé* (solo informándome)"

[LEAD]: "Este mes"

[BOT]: "*¡Excelente!* Tenemos [X] propiedades que se ajustan a tu búsqueda:
        
        📍 Las Condes / Vitacura
        🛏️ 2 dormitorios
        💰 8.000 - 12.000 UF
        
        Voy a pasarte las 3 mejores opciones disponibles para visita este mes:
        
        *[Aquí el bot envía cards con 3 propiedades + fotos + precios]*
        
        ¿Alguna te interesa para visitar?"
```

---

## 🏷️ Sistema de Calificación de Leads

### Dimensiones de Puntuación

| Dimensión | Peso | Descripción |
|-----------|------|-------------|
| **FIT** | 40% | ¿Coincide con el perfil del comprador ideal? |
| **INTENT** | 35% | ¿Qué tan listo está para comprar? |
| **ENGAGEMENT** | 25% | ¿Qué tan activo/responsivo es? |

### Matriz de Puntuación

**FIT (0-40 puntos)**

| Criterio | Excelente (10) | Bueno (6) | Regular (3) | Bajo (1) |
|----------|---------------|-----------|-------------|----------|
| Zona | Oriente Santiago | Otras comunas premium | Santiago centro | Otras |
| Presupuesto | >8.000 UF | 5.000-8.000 UF | 3.000-5.000 UF | <3.000 UF |
| Financiamiento | Aprobado | En proceso | Pre-aprobado | Sin definir |
| Urgencia | Esta semana | Este mes | 2-3 meses | Indefinido |

**INTENT (0-35 puntos)**

| Criterio | Puntos |
|----------|--------|
| Vio propiedad específica en web | 10 |
| Solicitó visita presencial | 10 |
| Compra (vs arriendo) | 5 |
| Para habitar (vs inversión) | 5 |
| Preguntó por proceso de compra | 5 |

**ENGAGEMENT (0-25 puntos)**

| Criterio | Puntos |
|----------|--------|
| Responde en <5 min | 10 |
| Proporciona teléfono/email | 5 |
| Comparte más datos de los pedidos | 5 |
| Múltiples interacciones | 5 |

### Clasificación de Leads

| Score | Grado | Acción |
|-------|-------|--------|
| 80-100 | A (HOT) | Notificación inmediata al corredor + llamada en 2 horas |
| 60-79 | B (WARM) | Email al corredor + seguimiento en 24 horas |
| 40-59 | C (TIBIO) | Agregar a lista de seguimiento + recontacto en 7 días |
| 20-39 | D (COLD) | Nurture sequence mensual |
| 0-19 | F | Auto-respuesta amable, no priorizar |

---

## 🛠️ Stack Técnico Recomendado

| Componente | Herramienta | Costo/mes | Skills Relacionadas |
|------------|-------------|-----------|---------------------|
| WhatsApp Business API | Meta + Wassenger/Twilio | $30-$50 USD | `whatsapp-business-api` |
| Orquestador | n8n (self-hosted) | $0 | `n8n` |
| IA Conversacional | OpenAI GPT-4o mini | $20-$30 USD | - |
| Diseño de Flujos | Voiceflow | Gratis-$50 USD | `voiceflow` |
| Calendario | Google Calendar API | Gratis | - |
| Base de Datos | Airtable / Supabase | Gratis-$20 USD | - |
| CRM | Pipedrive / HubSpot | $15-$50 USD | - |

**Total estimado:** ~$100-$200 USD/mes por agencia

---

## 📋 Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                         ENTRY POINTS                            │
├─────────────────────────────────────────────────────────────────┤
│  WhatsApp Web    │    Website Widget    │    Facebook/IG DM     │
└────────┬─────────┴──────────┬───────────┴──────────┬────────────┘
         │                    │                      │
         └────────────────────┼──────────────────────┘
                              ▼
              ┌───────────────────────────────┐
              │   WhatsApp Business API       │
              │   (Meta Cloud API)            │
              └───────────────┬───────────────┘
                              │
                              ▼
              ┌───────────────────────────────┐
              │   Webhook Handler (n8n)       │
              │   • Detectar tipo de entrada  │
              │   • Enriquecer datos          │
              │   • Enviar a procesador IA    │
              └───────────────┬───────────────┘
                              │
              ┌───────────────┴───────────────┐
              │                               │
              ▼                               ▼
    ┌─────────────────────┐      ┌─────────────────────┐
    │  Lead desde         │      │  Consulta General   │
    │  Propiedad          │      │                     │
    │  Específica         │      │                     │
    └──────────┬──────────┘      └──────────┬──────────┘
               │                            │
               ▼                            ▼
    ┌─────────────────────┐      ┌─────────────────────┐
    │  Flujo Contextual   │      │  Flujo de           │
    │  • Mostrar info     │      │  Cualificación      │
    │    propiedad        │      │  • Preguntas        │
    │  • Validar interés  │      │    progresivas      │
    │  • Agendar visita   │      │  • Match con BD     │
    └──────────┬──────────┘      └──────────┬──────────┘
               │                            │
               └────────────┬───────────────┘
                            ▼
              ┌───────────────────────────────┐
              │   GPT-4o mini (OpenAI)        │
              │   • Generar respuestas        │
              │   • Manejar objeciones        │
              │   • Detectar intención        │
              └───────────────┬───────────────┘
                              │
                              ▼
              ┌───────────────────────────────┐
              │   Lead Scorer (cálculo)       │
              │   • Calcular puntaje          │
              │   • Clasificar A/B/C/D/F      │
              │   • Enviar alertas            │
              └───────────────┬───────────────┘
                              │
              ┌───────────────┴───────────────┐
              │                               │
              ▼                               ▼
    ┌─────────────────────┐      ┌─────────────────────┐
    │  Lead HOT (A/B)     │      │  Lead TIBIO/COLD    │
    │  • Notificar        │      │  • Guardar en BD    │
    │    corredor         │      │  • Agregar a        │
    │  • Crear evento     │      │    nurture          │
    │    calendar         │      │  • Recontacto auto  │
    └─────────────────────┘      └─────────────────────┘
```

---

## ✅ Checklist de Implementación

### Semana 1: Setup
- [ ] Configurar WhatsApp Business API (Meta Developer)
- [ ] Crear cuenta n8n (self-hosted o cloud)
- [ ] Configurar API keys (OpenAI, Google Calendar)
- [ ] Crear base de datos (Airtable/Supabase)

### Semana 2: Flujos
- [ ] Diseñar flujo en Voiceflow (o n8n directo)
- [ ] Implementar webhook de entrada
- [ ] Conectar GPT-4o mini para respuestas
- [ ] Implementar sistema de lead scoring

### Semana 3: Integraciones
- [ ] Conectar Google Calendar (agendamiento)
- [ ] Configurar notificaciones al corredor
- [ ] Crear templates de WhatsApp aprobados
- [ ] Testing con casos reales

### Semana 4: Piloto
- [ ] Deploy en agencia piloto
- [ ] Monitorear conversaciones
- [ ] Ajustar flujos según feedback
- [ ] Medir métricas (conversiones, tiempos)

---

## 📊 Métricas Clave a Trackear

| Métrica | Meta | Cómo medir |
|---------|------|------------|
| Tasa de respuesta del bot | >90% | n8n executions |
| Leads calificados/mes | 20+ | Airtable/BD |
| Visitas agendadas automáticamente | 30% de leads | Calendar events |
| Tiempo promedio primera respuesta | <2 min | n8n logs |
| Conversión lead → visita | 25% | Tracking manual |
| Satisfacción del corredor | >4/5 | Encuesta semanal |

---

## 🎓 Skills Instaladas y Cómo Usarlas

### 1. client-intake-bot-pro
**Para qué:** Base del flujo conversacional y calificación
**Uso:** Adaptar las preguntas del intake al contexto inmobiliario

### 2. voiceflow
**Para qué:** Diseño visual de flujos de conversación
**Uso:** Prototipar y documentar los flujos A y B antes de implementar en n8n

### 3. whatsapp-business-api
**Para qué:** Conexión oficial con WhatsApp
**Uso:** Enviar/recibir mensajes, templates, gestionar webhooks

### 4. n8n
**Para qué:** Orquestación de todo el sistema
**Uso:** Webhook handler, integración con APIs, lógica de negocio

### 5. lead-scorer
**Para qué:** Calificación automática de leads
**Uso:** Implementar matriz de puntuación y clasificación A/B/C/D/F

---

*Documento creado el 2026-04-01*
*Skills utilizadas: client-intake-bot-pro, voiceflow, whatsapp-business-api, n8n, lead-scorer*
