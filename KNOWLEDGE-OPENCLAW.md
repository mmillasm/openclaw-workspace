# 📚 KNOWLEDGE BASE: OpenClaw Completo

> **Objetivo:** Convertirme en un agente robusto aprovechando al máximo OpenClaw  
> **Investigación realizada:** Fuentes oficiales, YouTubers (Benjamín Cordero), documentación técnica, casos de uso reales  
> **Fecha:** 4 de abril de 2026

---

## 1. ¿QUÉ ES OPENCLOK?

**OpenClaw** es un framework de agentes AI open-source, local-first, diseñado para que modelos de IA realicen acciones en el mundo real en la máquina del usuario.

### Diferencias clave vs chatbots tradicionales:
- **Proactivo, no reactivo:** Actúa en segundo plano, no espera a que le preguntes
- **Persistente:** Mantiene memoria entre sesiones (archivos Markdown locales)
- **Model-agnostic:** Funciona con Claude, GPT-4, Gemini, modelos locales (Ollama)
- **Integrado al sistema:** Lee/escribe archivos, ejecuta scripts, controla browsers
- **Multi-canal:** Conecta con Telegram, WhatsApp, Slack, Discord, iMessage

### Arquitectura:
```
┌─────────────────────────────────────────────┐
│           OpenClaw Gateway                   │
│  (Proceso local persistente)                │
└──────────────┬──────────────────────────────┘
               │
    ┌──────────┴──────────┬───────────────────┐
    ▼                     ▼                   ▼
┌──────────┐      ┌──────────────┐   ┌──────────────┐
│  Skills  │      │  Subagents   │   │  Cron Jobs   │
│(Tools)   │      │(Paralelismo) │   │(Scheduling)  │
└──────────┘      └──────────────┘   └──────────────┘
```

---

## 2. SKILLS: El Corazón de OpenClaw

### ¿Qué son?
Las **Skills** son extensiones modulares que enseñan a los agentes cómo usar herramientas externas. Cada skill es un directorio con:
- `SKILL.md` - Instrucciones y documentación
- Scripts ejecutables
- Metadata de configuración

### ClawHub - El Marketplace:
- **URL:** https://clawhub.ai
- Más de **13,700 skills** disponibles
- Funciona como npm/yarn pero para capacidades de IA
- Instalación: `clawhub install <skill-name>`

### Categorías principales de Skills:

| Categoría | Ejemplos de Skills |
|-----------|-------------------|
| **Productividad** | Google Workspace, calendario, emails, notas |
| **Desarrollo** | GitHub, GitLab, Docker, AWS, código |
| **Comunicación** | Slack, Discord, WhatsApp Business API |
| **Datos/Analytics** | Airtable, Notion, bases de datos SQL |
| **Web** | Scraping, búsquedas, monitoreo |
| **Marketing** | Social media, SEO, content generation |
| **Automatización** | n8n, Zapier, webhooks |
| **Media** | Generación de imágenes, video, audio |

### Skill de Marketing Relevantes para VYNXE:
- `social-media-marketing` - Estrategia y contenido
- `instagram-agent` - Gestión de Instagram
- `website-seo` - Optimización SEO
- `seo-optimizer` - Meta tags, schema markup
- `send-email` - SMTP y envío de correos
- `airtable-automation` - CRM y pipelines
- `lead-scorer` - Calificación de leads

### Seguridad de Skills:
⚠️ **ClawHavoc (incidente 2026):** Se detectaron skills maliciosos en ClawHub. Ahora el registro tiene:
- Escaneo con VirusTotal
- Verificación de permisos
- Aislamiento de credenciales

**Regla:** Revisar siempre el código fuente de skills de terceros antes de instalar.

---

## 3. MCP (MODEL CONTEXT PROTOCOL)

### ¿Qué es MCP?
Desarrollado por **Anthropic**, es un protocolo estándar que define cómo los agentes AI descubren y llaman herramientas externas.

### OpenClaw + MCP:
- Cada **Skill de OpenClaw es arquitectónicamente un servidor MCP**
- Usa **JSON-RPC** como capa de transporte
- Soporta **stdio** y **Server-Sent Events (SSE)**

### Beneficios:
1. **Interoperabilidad:** Skills de OpenClaw funcionan con otros clientes MCP (Claude, Cursor, ChatGPT)
2. **Estandarización:** Una forma universal de llamar herramientas
3. **Seguridad:** Límites explícitos, audit trails estructurados
4. **Reducción de tokens:** OpenClaw usa CLI en lugar de inyectar schemas completos

### Componentes MCP:
```
┌────────────────────────────────────────────┐
│              MCP Server                    │
│  ┌──────────┬──────────┬──────────────┐   │
│  │  Tools   │ Resources│   Prompts    │   │
│  │(funciones│  (datos) │ (templates)  │   │
│  └──────────┴──────────┴──────────────┘   │
└────────────────────────────────────────────┘
```

### Configuración MCP:
Se configura en `~/.openclaw/openclaw.json` bajo `skills.entries.<skill>.env`

---

## 4. SUBAGENTES: Orquestación Multi-Agente

### ¿Qué son los Subagentes?
Son **agentes hijo** que se ejecutan en sesiones aisladas, permitiendo:
- Ejecución paralela de tareas
- Procesos de larga duración sin bloquear
- Especialización por tarea

### Arquitectura:
```
┌─────────────────────────────────────────────┐
│           Agente Principal                   │
│     (Session: agent:main:telegram:...)      │
└──────────────┬──────────────────────────────┘
               │ sessions_spawn
    ┌──────────┼──────────┬───────────────────┐
    ▼          ▼          ▼                   ▼
┌────────┐ ┌────────┐ ┌────────┐       ┌────────┐
│Subagent│ │Subagent│ │Subagent│  ...  │Subagent│
│  #1    │ │  #2    │ │  #3    │       │  #N    │
└────────┘ └────────┘ └────────┘       └────────┘
```

### Características:
- **Aislamiento:** Cada subagente tiene su propia sesión y memoria
- **Non-blocking:** El agente principal continúa mientras los subagentes trabajan
- **Anuncio automático:** Al completar, el subagente anuncia resultados al canal
- **Cost optimization:** Pueden usar modelos más baratos para tareas pesadas
- **Nesting limitado:** Máximo profundidad 2 (subagente puede crear sub-subagentes)

### Límites:
- Máximo **5 subagentes activos** por sesión principal (configurable)
- Máximo **profundidad 2** (main → subagent → sub-subagent)

### Cuándo usar subagentes:
- ✅ Tareas paralelas independientes (research, análisis, generación)
- ✅ Procesos de larga duración (scraping masivo, generación de assets)
- ✅ Tareas que necesitan modelos diferentes
- ✅ Orchestrator pattern: Agente principal coordina, workers ejecutan

---

## 5. CRON JOBS vs HEARTBEATS

### CRON JOBS
**Para:** Tareas con timing exacto, aisladas del contexto conversacional

**Cuándo usar:**
- Exact timing importa ("todos los lunes a las 9:00 AM")
- Tarea pesada/compleja que no debe bloquear sesión principal
- One-shot reminders ("recuérdame en 20 minutos")
- Heavy workflows (backups, reportes masivos)

**Características:**
- Sintaxis cron estándar: `0 2 * * *` (2 AM diario)
- Ejecutan en sesiones aisladas
- Crean task records
- Soportan delivery a canales (Telegram, email, etc.)

**Ejemplo:**
```json
{
  "schedule": {"kind": "cron", "expr": "0 */2 * * *"},
  "payload": {"kind": "agentTurn", "message": "Hacer backup..."},
  "sessionTarget": "isolated"
}
```

### HEARTBEATS
**Para:** Monitoreo continuo, aprovechando contexto conversacional

**Cuándo usar:**
- Múltiples checks que pueden agruparse
- Necesitas contexto de mensajes recientes
- Timing puede variar (~30 min está bien)
- Reducir API calls combinando checks

**Características:**
- Corre en main session (con contexto completo)
- Batched checks: email + calendario + notificaciones en un turno
- Smart suppression: `HEARTBEAT_OK` cuando no hay nada urgente
- No crean task records (quedan en historial de sesión)
- Driven por archivo `HEARTBEAT.md`

**Ejemplo HEARTBEAT.md:**
```markdown
# HEARTBEAT

## Checks a realizar:
- [ ] Email: ¿Mensajes urgentes?
- [ ] Calendario: ¿Eventos en próximas 2h?
- [ ] GitHub: ¿PRs pendientes de revisión?

Si nada urgente → HEARTBEAT_OK
```

### Comparativa:
| Feature | Cron | Heartbeat |
|---------|------|-----------|
| Timing | Exacto | Aproximado (~30 min drift) |
| Contexto | Aislado | Main session |
| Task record | Sí | No |
| Batched | No | Sí |
| Uso ideal | Tareas pesadas/único | Monitoreo continuo |

---

## 6. CASOS DE USO REALES (Awesome OpenClaw)

Repositorio: `hesamsheikh/awesome-openclaw-usecases`

### Categorías principales:

#### Productividad Personal
- **Morning Briefing:** Resumen diario (clima, calendario, noticias) a las 6:30 AM
- **Email Triage:** Clasifica emails, draftea respuestas, flags urgentes
- **Expense Tracking:** Foto de boleta → extracción OCR → spreadsheet
- **Second Brain:** URL → fetch → chunk → embeddings → búsqueda semántica
- **Health Monitoring:** Agrega datos de wearables, identifica patrones

#### Comunicación
- **Meeting Summarization:** Transcripción → resumen + action items
- **Shared Shopping List:** Monitorea chat por keywords ("necesitamos leche")
- **Team Updates:** Agrega status reports → reporte unificado
- **Slack Auto-Support:** Responde preguntas frecuentes en canales

#### Marketing & Contenido
- **Social Media Monitoring:** Brand mentions + sentiment analysis
- **YouTube Pipeline:** Research → script → tracking de producción
- **Multi-Agent Content Factory:** Discord con agents paralelos (research, writing, thumbnails)
- **Image/Video Generation:** Prompt → DALL-E/Genviral → entrega

#### Desarrollo & DevOps
- **API Documentation:** Genera docs automáticamente desde código
- **Log Analysis:** Monitorea logs → alertas por anomalías
- **Overnight Coding:** Tareas asignadas antes de dormir → review en la mañana
- **Cron Automation:** Jobs recurrentes (backups, mantenimiento)

#### Inteligencia de Negocios
- **Customer Feedback Analysis:** Agrega feedback → identifica patrones → insights
- **Competitive Intelligence:** Monitorea cambios de competidores
- **Industry Trend Analysis:** Agrega noticias → síntesis narrativa
- **Patent Monitoring:** Tracking de patentes en dominios específicos

---

## 7. CANALES DE INTEGRACIÓN

### Mensajería Soportada:
- **Telegram** (Bot API)
- **WhatsApp Business API**
- **Slack** (Webhooks + Bot)
- **Discord** (Bot)
- **iMessage** (macOS)

### Características por canal:
- **Reply/Quote:** Soporte nativo en Telegram, Discord, Slack
- **Reactions:** Emoji reactions (Discord, Telegram mínimo)
- **Archivos:** Envío/recepción de documentos, imágenes, audio
- **Botones:** Inline keyboards (Telegram)

### Configuración Telegram (ejemplo):
```json
{
  "channels": {
    "telegram": {
      "enabled": true,
      "botToken": "YOUR_BOT_TOKEN",
      "allowFrom": [USER_ID_1, USER_ID_2]
    }
  }
}
```

---

## 8. MEMORIA Y PERSISTENCIA

### Tipos de Memoria:

| Tipo | Ubicación | Uso |
|------|-----------|-----|
| **MEMORY.md** | `/workspace/MEMORY.md` | Memoria largo plazo (solo main session) |
| **Daily Memory** | `/workspace/memory/YYYY-MM-DD.md` | Logs diarios de sesiones |
| **SOUL.md** | `/workspace/SOUL.md` | Identidad y comportamiento del agente |
| **USER.md** | `/workspace/USER.md` | Perfil del usuario |
| **TOOLS.md** | `/workspace/TOOLS.md** | Configuración específica del entorno |
| **HEARTBEAT.md** | `/workspace/HEARTBEAT.md` | Checklist de heartbeats |
| **Session Context** | En memoria durante turno | Contexto de conversación actual |
| **Supermemory** | Plugin opcional | Vector DB para búsqueda semántica |

### Patrón de memoria recomendado:
```
1. Session startup → Leer SOUL.md, USER.md, MEMORY.md
2. Durante conversación → Actualizar daily memory
3. Periodicamente → Sincronizar daily → MEMORY.md (resumen)
4. Heartbeat → Revisar HEARTBEAT.md
```

---

## 9. BENJAMÍN CORDERO - RECURSOS

**Canal YouTube:** @bencord  
**Especialidad:** Tutoriales de OpenClaw en español

### Videos clave:
1. **"De Cero a Primer Asistente de IA en 15 Minutos"** (Feb 2026)
   - Setup básico en VPS
   - Configuración de Telegram
   - Primeros skills

2. **"Taller de OpenClaw desde cero"** con MoureDev (Mar 2026)
   - Instalación completa
   - Seguridad
   - Workspace, commands, cron, memory, agents
   - Configuración de modelos (Gemini, OpenAI, Ollama)

3. **"Guía completa de Openclaw: instalación, configuración y funciones principales"**
   - Con Alexys
   - Slack integration
   - WhatsApp, Telegram

4. **"Mi opinión honesta sobre OpenClaw (después de un mes usándolo)"**
   - Review y mejores prácticas

5. **"OpenClaw vs Claude Code: Cuál usar y cuándo"**
   - Comparativa de herramientas

### Aprendizajes de Benjamín:
- Usar VPS para 24/7 operation
- Importancia de la seguridad (no exponer tokens)
- Combinar OpenClaw + Claude Code para máximo potencial
- Modelos locales (Ollama) para privacidad

---

## 10. MODELOS Y PROVIDERS

### Soportados:
- **OpenAI:** GPT-4, GPT-4o, GPT-3.5-turbo
- **Anthropic:** Claude Opus, Sonnet, Haiku
- **Google:** Gemini Pro, Flash
- **OpenCode:** Modelos gratuitos (Minimax, Nemotron, etc.)
- **NVIDIA:** Llama 3.1 70B (vía API)
- **Local:** Ollama (Qwen, Llama, Mistral, etc.)

### Configuración en openclaw.json:
```json
{
  "agents": {
    "defaults": {
      "model": {
        "primary": "opencode-go/minimax-m2.5",
        "fallbacks": [
          "opencode-go/minimax-m2.7",
          "nvidia/meta/llama-3.1-70b-instruct"
        ]
      }
    }
  }
}
```

### Estrategia de modelos:
- **Main agent:** Modelo de calidad (Claude, GPT-4)
- **Subagentes tareas simples:** Modelo barato/rápido (Minimax, local)
- **Subagentes complejos:** Modelo de calidad según necesidad

---

## 11. WORKSPACE Y ESTRUCTURA

### Directorio principal:
```
~/.openclaw/workspace/
├── SOUL.md              # Identidad del agente
├── USER.md              # Perfil del usuario
├── MEMORY.md            # Memoria largo plazo
├── TOOLS.md             # Config local
├── HEARTBEAT.md         # Checklist de monitoreo
├── AGENTS.md            # Guía de workspace
├── memory/              # Logs diarios
│   ├── 2026-04-04.md
│   └── ...
├── skills/              # Skills instaladas
│   ├── send-email/
│   ├── instagram-agent/
│   └── ...
└── projects/            # Proyectos específicos
```

### Configuración global:
`~/.openclaw/openclaw.json` - Configuración de canales, modelos, gateway, skills

---

## 12. COMANDOS CLI IMPORTANTES

```bash
# Gateway
openclaw gateway status
openclaw gateway start
openclaw gateway stop
openclaw gateway restart

# Skills
clawhub list                    # Listar skills disponibles
clawhub install <skill>         # Instalar skill
clawhub update <skill>          # Actualizar skill

# Subagentes
/subagents spawn <agent> <task> # Crear subagente
openclaw tasks                  # Ver tareas activas
openclaw tasks flow             # Ver flujo de orquestación

# Cron
openclaw cron list              # Listar cron jobs
openclaw cron add <job>         # Agregar job
openclaw cron remove <id>       # Eliminar job

# General
openclaw status                 # Estado del sistema
openclaw help                   # Ayuda
```

---

## 13. MEJORES PRÁCTICAS

### Seguridad:
- ✅ Nunca exponer API keys en archivos versionados
- ✅ Usar `.env` o `openclaw.json` para secrets
- ✅ Revisar código de skills de terceros
- ✅ Usar App Passwords para emails (no contraseñas normales)
- ✅ Limitar `allowFrom` en canales

### Performance:
- ✅ Usar subagentes para tareas paralelas
- ✅ Batch similar checks en heartbeats
- ✅ Programar tareas pesadas en horarios off-peak
- ✅ Usar modelos locales para tareas simples

### Organización:
- ✅ Un proyecto = un folder en workspace
- ✅ Documentar decisiones en MEMORY.md
- ✅ Usar git para versionar el workspace
- ✅ Commits descriptivos

### Productividad:
- ✅ Automatizar respaldos (cron cada 2h)
- ✅ Crear skills para tareas repetitivas
- ✅ Usar heartbeats para monitoreo proactivo
- ✅ Delegar a subagentes cuando sea posible

---

## 14. RECURSOS ADICIONALES

### Documentación Oficial:
- **Docs:** https://docs.openclaw.ai
- **GitHub:** https://github.com/openclaw/openclaw
- **ClawHub:** https://clawhub.ai
- **Discord:** https://discord.gg/clawd

### Comunidad:
- **Awesome OpenClaw:** `hesamsheikh/awesome-openclaw-usecases`
- **YouTube:** @bencord (Benjamín Cordero)
- **Medium:** Varios autores con casos de uso

### Skills Recomendados para Iniciar:
1. `send-email` - SMTP integration
2. `github` - Git operations
3. `web_search` - Búsquedas web
4. `cron` - Scheduling
5. `obsidian` - Knowledge management
6. `airtable` - Database/CRM
7. `instagram-agent` - Social media
8. `voiceflow` - Chatbot flows

---

## 15. CHECKLIST: Convertirme en Agente Robusto

- [x] Entender arquitectura de OpenClaw
- [x] Conocer Skills y ClawHub
- [x] Aprender MCP y su integración
- [x] Dominar Subagentes y orquestación
- [x] Diferenciar Cron vs Heartbeats
- [x] Conocer casos de uso reales
- [x] Integración con múltiples canales
- [x] Sistema de memoria y persistencia
- [x] Configuración de múltiples modelos
- [x] Mejores prácticas de seguridad

**Siguientes pasos:**
- [ ] Practicar creación de skills propios
- [ ] Experimentar con subagentes en tareas paralelas
- [ ] Configurar heartbeats para monitoreo proactivo
- [ ] Integrar más servicios (calendario, email, etc.)
- [ ] Documentar aprendizajes en MEMORY.md

---

*Knowledge Base completa generada para maximizar capacidades como agente OpenClaw*  
*Fuentes: Documentación oficial, Benjamin Cordero, casos de uso reales, ClawHub*  
*Fecha: 4 de abril de 2026*
