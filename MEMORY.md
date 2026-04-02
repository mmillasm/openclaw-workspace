# MEMORY.md - Memoria de Largo Plazo

## Usuario
- Nombre: Ader M
- Usuario Telegram: @aderzeit (ID: 6637360763)
- Idioma: español

## Configuración activa
- Modelo: `opencode-go/minimax-m2.5`
- Workspace: `/home/ubuntu/.openclaw/workspace`
- Python: en actualización (usuario va a actualizar a 3.10+)
- Tailscale IP: `100.91.92.50`

## Proyecto principal: Mission Control
- Instalado en `~/mission-control/`
- URL: `https://xplain-vnic.tail918e51.ts.net/`
- Video de referencia: "OpenClaw is 100x better with this tool (Mission Control)" by Alex Finn
- Features: Calendar, Projects, Memories, Docs, Office (pixel art), Mission Statement, Reverse Prompting
- Arquitectura: Docker + Next.js 14 + Tailscale Serve

## Configuración de OpenClaw con Obsidian como memoria persistente
- Skill `obsidian-memory` en desarrollo

## Proyecto: Agencia de Automatizaciones con IA (En validación)

**Nicho seleccionado:** Corredores de propiedades en Chile  
**Propuesta:** "Automatizamos corredores de propiedades — tu asistente IA atiende leads 24/7, califica compradores y agenda visitas"

**Investigación completada:**
- ✅ Competencia mapeada y verificada (12 agencias chilenas activas)
- ✅ Market sizing: TAM $15-30M, SAM $3-6M, SOM $60-100K (18 meses)
- ✅ 20 agencias identificadas para entrevistas de validación
- ✅ Script de entrevistas de validación creado
- ✅ MVP de chatbot WhatsApp diseñado (2-3 semanas dev)
- ✅ Flujo de conversación corregido (detecta propiedad específica vs. consulta general)
- ✅ Propuesta comercial profesional completa
- ✅ Validation Kit con scripts, tracker y templates
- ✅ Sistema de cold email outreach completo
- ✅ Integración con Airtable para pipeline tracking

**Pricing propuesto:**
- Starter: $199K CLP/mes (1-2 corredores)
- Pro: $399K CLP/mes (3-8 corredores) — TARGET
- Agency: $799K CLP/mes (9+ corredores)

**Próximos pasos:**
1. Crear base en Airtable e importar agencias
2. Enviar primer lote de cold emails (5 agencias ALTA prioridad)
3. Completar 5 entrevistas de validación (Semana 1-2)
4. Validar disposición a pagar $399K/mes
5. Decisión Go/No-Go

**Stack técnico:**
- WhatsApp Business API
- n8n (orquestador)
- OpenAI GPT-4o mini
- Google Calendar API
- Airtable (CRM/pipeline)
- Voiceflow (diseño flujos)

**Archivos clave:**
- `propuesta-mvp-chatbot-corredores.md` - Propuesta comercial
- `mvp-chatbot-corredores.md` - Flujo de conversación
- `validation-kit.md` - Kit de validación
- `cold-email-workflow.md` - Sistema cold email
- `memory/2026-04-02.md` - Detalle completo de sesión

**Skills instaladas:** client-intake-bot-pro, voiceflow, whatsapp-business-api, n8n, lead-scorer, proposal-writer, markdown, send-email, follow-up-sequence-writer, sales-pipeline-tracker, airtable-automation, airtable-integration

## Archivos importantes
- `contexts/current-session.md` — tareas activas de configuración
- `memory/2026-03-31.md` — diario de sesiones

## Reglas y recordatorios
- Siempre listar las cosas que encuentre
- Guardar todo en archivos, no en notas mentales
- Respaldo automático de sesión antes de actualizaciones/reinicios
- Git commit automático después de respaldos