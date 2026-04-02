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

## Proyecto: Vinxe — Agencia de Automatización IA para Inmobiliarias (En validación)

**Nombre:** Vinxe  
**Nicho:** Corredores de propiedades en Chile  
**Propuesta:** "Tu asistente virtual inmobiliario que atiende clientes 24/7, separa a los curiosos de los que quieren comprar, y agenda visitas automáticamente"

**Presencia digital:**
- Web: https://vynxe.cl (landing lista, pendiente deploy)
- Instagram: @vinxe.cl (contenido creado, pendiente crear cuenta)
- Email: hola@vynxe.cl

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
- ✅ Dashboard Airtable creado (3 tablas: Agencias, Tareas, Dashboard)
- ✅ 10 agencias importadas al pipeline

**Marketing digital:**
- ✅ Landing page SEO-optimizada (`vinxe-landing-inmobiliaria.html`)
- ✅ Contenido Instagram Semana 1 (5 Reels + 2 Carousels + 7 Stories)
- ✅ Versión simple sin tecnicismos (`vinxe-instagram-simple.md`)
- ✅ Perfil optimizado: @vinxe.cl

**Dashboard en vivo:**
- URL: https://airtable.com/apptwVpujukA0sS1U
- Tabla Agencias: 10 registros
- Tabla Tareas: 8 tareas priorizadas
- Tabla Dashboard: Health check del proyecto

**Pricing propuesto:**
- Starter: $199K CLP/mes (1-2 corredores)
- Pro: $399K CLP/mes (3-8 corredores) — TARGET
- Agency: $799K CLP/mes (9+ corredores)

**Próximos pasos:**
1. ✅ Crear base en Airtable e importar agencias (COMPLETADO)
2. ⏳ Enviar primer lote cold emails (5 agencias ALTA: GESCOR, Boettcher, URBANI, Houm, Altea)
3. ⏳ Crear cuenta Instagram @vinxe.cl y publicar contenido Semana 1
4. ⏳ Subir landing page a vynxe.cl + crear imágenes (og-image, logo)
5. ⏳ Completar 5 entrevistas de validación (Semana 1-2)
6. ⏳ Validar disposición a pagar $399K/mes
7. ⏳ Decisión Go/No-Go (meta: 2 pilotos cerrados)

**Stack técnico:**
- WhatsApp Business API
- n8n (orquestador)
- OpenAI GPT-4o mini
- Google Calendar API
- Airtable (CRM/pipeline)
- Voiceflow (diseño flujos)

**Archivos clave (Índice maestro: `VINXE-DASHBOARD.md`):**

**Producto:**
- `propuesta-mvp-chatbot-corredores.md` - Propuesta comercial
- `mvp-chatbot-corredores.md` - Flujo de conversación
- `validation-kit.md` - Kit de validación

**Outreach:**
- `cold-email-workflow.md` - Sistema cold email
- `cold-email-system/agencias.csv` - 10 agencias priorizadas

**Marketing:**
- `vinxe-landing-inmobiliaria.html` - Landing page completa (SEO optimizada)
- `vinxe-sitemap.xml` + `vinxe-robots.txt` - SEO técnico
- `vinxe-instagram-simple.md` - Contenido Instagram Semana 1 (lenguaje simple)
- `vinxe-instagram-contenido.md` - Contenido versión técnica

**Documentación:**
- `VINXE-DASHBOARD.md` - Índice maestro de todo el proyecto
- `vinxe-agent.md` - Knowledge base completo
- `vinxe-agent-quickref.md` - Quick reference card
- `memory/2026-04-02.md` - Detalle completo de sesión

**Sistema de Diseño:**
- `vinxe-brand-identity.md` - Identidad de marca (wordmark puro)
- `vinxe-design-tokens.css` - Tokens CSS (colores, tipografía, espaciado)
- `vinxe-tailwind.config.js` - Configuración Tailwind completa
- `vinxe-figma-structure.md` - Estructura de páginas y componentes Figma
- `vinxe-voice-guide.md` - Guía de voz por canal (emails, Instagram, web)
- `vinxe-image-prompts-branded.md` - Prompts de imágenes para la marca

**Skills instaladas (16 total):**
- Producto: client-intake-bot-pro, voiceflow, whatsapp-business-api, n8n, lead-scorer
- Marketing: instagram-agent, social-media-marketing, website-seo, seo-optimizer
- Ventas: proposal-writer, follow-up-sequence-writer, sales-pipeline-tracker
- Utilidades: markdown, send-email, airtable-automation, airtable-integration

## Archivos importantes
- `contexts/current-session.md` — tareas activas de configuración
- `memory/2026-03-31.md` — diario de sesiones

## Reglas y recordatorios
- Siempre listar las cosas que encuentre
- Guardar todo en archivos, no en notas mentales
- Respaldo automático de sesión antes de actualizaciones/reinicios
- Git commit automático después de respaldos