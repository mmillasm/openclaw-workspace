# OpenClaw + Claude Code: Guía Completa de Integración

> Investigación realizada: Abril 2026
> Fuentes: GitHub, Medium, documentación oficial, blogs especializados, Reddit

---

## 1. Cómo Funcionan Juntos

### Concepto General

**OpenClaw** y **Claude Code** son herramientas complementarias que abordan diferentes dimensiones de la automatización con IA:

| Herramienta | Rol Principal | Enfoque |
|-------------|---------------|---------|
| **OpenClaw** | Orquestador de workflows Autonomous AI agent platform | Automatización cross-platform, control de canales, memoria persistente, cron jobs |
| **Claude Code** | Asistente de coding especializado | Escritura de código, debugging, refactoring, comprensión profunda del codebase |

La combinación permite que **OpenClaw actúe como el "cerebro" orquestador** que delega tareas de coding a Claude Code como un sub-agente especializado.

### Arquitectura de Integración

```
┌─────────────────────────────────────────────────────────┐
│                     OpenClaw (Orquestador)               │
│  - Memorias (AGENTS.md, SOUL.md, MEMORY.md)             │
│  - Canales (Telegram, Discord, Email)                   │
│  - Cron Jobs & Heartbeats                               │
│  - Control de APIs y servicios externos                  │
└─────────────────────┬───────────────────────────────────┘
                      │ exec / PTY / Plugin
                      ▼
┌─────────────────────────────────────────────────────────┐
│               Claude Code (Ejecutor de Código)           │
│  - Writing & refactoring                                │
│  - Testing & debugging                                  │
│  - Comprensión de codebases completos                   │
└─────────────────────────────────────────────────────────┘
```

### Formas de Integración

#### Opción A: Plugin `@enderfga/openclaw-claude-code` (Más Robusta)

El plugin oficial que convierte Claude Code en un motor programable:

```bash
openclaw plugins install @enderfga/openclaw-claude-code --dangerously-force-unsafe-install
```

**Características:**
- 27 herramientas de API
- Sesiones persistentes (7 días de TTL en disco)
- Multi-engine (Claude, Codex, Gemini, Cursor)
- Multi-Agent Council (colaboración entre agentes)
- Ultraplan (planificación profunda con Opus)
- Ultrareview (equipo de bug-hunting)

#### Opción B: ClaudeClaw (Alternativa Ligera)

Integración directa de funciones de OpenClaw dentro de Claude Code:

```bash
claude plugin install moazbuilds/claudeclaw
```

**Características:**
- ~5 minutos de setup vs "nightmare" de OpenClaw
- Usa directamente tu suscripción de Claude Code
- Aislamiento folder-based
- 600k+ LOC menos que OpenClaw completo

#### Opción C: Claude Code Channels (Nativo de Anthropic)

Conecta Claude Code directamente a Telegram/Discord sin OpenClaw:

```bash
claude --channels plugin:telegram@claude-plugins-official
/claude --channels plugin:discord@claude-plugins-official
```

---

## 2. Casos de Uso Reales

### Caso 1: "Overnight Employee" (Empleado Nocturno)

**Escenario:** Un developer necesita que algo se complete mientras duerme.

```
Developer → OpenClaw (Telegram) → "Build me a REST API for user auth"
                                    │
                                    ▼
                          Claude Code (background)
                                    │
                                    ▼
                          OpenClaw entrega resultado por Telegram
```

**Resultado:** A la mañana siguiente, el developer tiene una API funcional.

### Caso 2: CI/CD Monitoring Automatizado

**Escenario:** Monitoreo de GitHub Actions con fixes automáticos.

```
OpenClaw (GitHub skill) detecta build fallido
         │
         ▼
Claude Code analiza logs del failure
         │
         ▼
OpenClaw postea análisis + fix sugerido en PR
```

### Caso 3: Multi-Agent Council para Code Review

**Escenario:** Tres agentes especializados revisan código en paralelo.

```javascript
const session = manager.councilStart('Build a REST API with auth', {
  agents: [
    { name: 'Architect', emoji: '🏗️', persona: 'System design', engine: 'claude', model: 'opus' },
    { name: 'Engineer', emoji: '⚙️', persona: 'Implementation', engine: 'codex', model: 'o4-mini' },
    { name: 'Reviewer', emoji: '🔍', persona: 'Code review', engine: 'claude', model: 'sonnet' },
  ],
  projectDir: '/tmp/api-project',
});
```

### Caso 4: Daily Development Digest

**Escenario:** Resumen automatizado de estado de proyectos.

```
OpenClaw escanea folder con stdout de CI runs
         │
         ▼
Identifica patrones de failures (LLM analysis)
         │
         ▼
Envia resumen estructurado a Slack/Telegram
```

### Caso 5: Building Micro-Apps desde Telegram

**Escenario:** Request de app via mensaje.

```
User (Telegram) → "Build me a landing page for my product"
                        │
                        ▼
              OpenClaw interpreta el request
                        │
                        ▼
              Claude Code genera el código
                        │
                        ▼
              OpenClaw hace deploy
                        │
                        ▼
              Entrega link al usuario
```

---

## 3. Configuración/Setup Necesario

### Setup Completo (OpenClaw + Plugin)

#### Paso 1: Instalar Claude Code CLI

```bash
npm install -g @anthropic-ai/claude-code
claude --version  # Verificar instalación
```

#### Paso 2: Instalar OpenClaw

```bash
curl -fsSL https://openclaw.ai/install.sh | bash
```

#### Paso 3: Instalar el Plugin de Integración

```bash
openclaw plugins install @enderfga/openclaw-claude-code --dangerously-force-unsafe-install
```

#### Paso 4: Configurar API de Claude

**Opción A: Directa (Anthropic)**
```bash
openclaw config set providers.anthropic.apiKey "sk-ant-..."
openclaw config set providers.anthropic.api "anthropic-messages"
openclaw config set providers.anthropic.baseUrl "https://api.anthropic.com/v1"
```

**Opción B: Via EvoLink.AI (Recomendado para Claude 4.5 Opus)**
```bash
# Obtener key en evolink.ai
openclaw config set providers.evolink.apiKey "tu-key"
```

#### Paso 5: Configurar Canales (Telegram ejemplo)

```bash
# Crear bot via @BotFather en Telegram
openclaw config set channels.telegram.botToken "TU_BOT_TOKEN"
openclaw config set channels.telegram.enabled true
openclaw config set channels.telegram.dmPolicy "pairing"

openclaw gateway restart
```

#### Paso 6: Pairing de Telegram

```
1. Enviar cualquier mensaje a tu bot en Telegram
2. Ver código: openclaw pairing list telegram
3. Aprobar: openclaw pairing approve telegram <CODIGO>
```

### Setup Alternativo: ClaudeClaw

```bash
# Instalación en un paso
claude plugin marketplace add moazbuilds/claudeclaw
claude plugin install claudeclaw

# Dentro de Claude Code:
/claudeclaw:start
# Setup wizard te guía por modelo, heartbeat, Telegram, Discord, security
```

### Configuración para Claude Code Channels (Sin OpenClaw)

```bash
# Telegram
/claude --channels plugin:telegram@claude-plugins-official
/telegram:configure TU_BOT_TOKEN
/telegram:access-policy allowlist

# Discord
/claude --channels plugin:discord@claude-plugins-official
/discord:configure TU_BOT_TOKEN
/discord:access-policy allowlist
```

---

## 4. Ventajas de Usar Ambos

### Ventajas de la Combinación

| Ventaja | Descripción |
|---------|-------------|
| **Breadth + Depth** | OpenClaw da alcance cross-platform; Claude Code da profundidad en coding |
| **Automatización Completa** | Desde scheduling hasta deployment, sin intervención humana |
| **Memoria Persistente** | OpenClaw mantiene contexto entre sesiones; Claude Code tiene contexto de codebase |
| **Costos Optimizados** | Usa Sonnet para daily tasks, Opus para coding pesado |
| **Multi-Agent** | Council de agentes especializados trabajando en paralelo |
| **Productividad 24/7** | "AI employee" que trabaja mientras duermes |
| **Flexibilidad de Modelos** | Routing automático entre Claude, Codex, Gemini según tarea |

### Comparativa: Solo OpenClaw vs Combinación

| Aspecto | Solo OpenClaw | OpenClaw + Claude Code |
|---------|---------------|------------------------|
| Coding tasks | Funcional pero básico | Profesional, con debugging profundo |
| Contexto de codebase | Limitado | Completo |
| Multi-engine | No | Sí (Claude, Codex, Gemini, Cursor) |
| Council de agents | No | Sí |
| Ultraplan | No | Sí (30 min de investigación) |
| Ultrareview | No | Sí (5-20 agents en paralelo) |
| Setup complexity | Media-Alta | Media |

---

## 5. Ejemplos Concretos de Workflows

### Workflow 1: Refactoring Automatizado Nocturno

```yaml
name: Nocturnal Code Refactor
trigger: Cron (0 2 * * *)  # 2 AM daily

steps:
  - openclaw:check_codebase_health
    action: scan_for_technical_debt
    
  - openclaw:send_to_claude_code
    task: |
      Refactor the authentication module.
      Current issues identified:
      - Duplicated validation logic
      - No rate limiting
      - Password hashing uses MD5 (!!)
      Keep all public APIs intact. Run tests after each change.
    model: opus
    
  - openclaw:commit_and_notify
    on_completion: send_summary_to_telegram
```

### Workflow 2: Code Review Paralelo

```javascript
// Usando el plugin @enderfga/openclaw-claude-code
const { SessionManager } = require('@enderfga/openclaw-claude-code');

const manager = new SessionManager();
await manager.startSession({ name: 'review-task', cwd: '/project' });

// Enviar PR para review
const result = await manager.sendMessage('review-task', `
Review PR #142:
- Check for security vulnerabilities
- Verify tests coverage
- Look for performance issues
- Comment on each finding
`);

// Auto-review con Ultrareview
const review = manager.ultrareviewStart('/project', {
  agentCount: 10,
  maxDurationMinutes: 15,
});
```

### Workflow 3: Telegram → Coding Task → Deploy

```
┌──────────────────────────────────────────────────────────────┐
│  USER (Telegram)                                             │
│  "Deploy the new landing page to staging"                   │
└────────────────────┬─────────────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────────────┐
│  OpenClaw (Recibe mensaje, interpreta contexto)              │
│  - Lee CLAUDE.md del proyecto                                │
│  - Identifica que es un deploy a staging                     │
└────────────────────┬─────────────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────────────┐
│  Claude Code (Ejecuta el coding)                             │
│  - Genera/modifica landing page                              │
│  - Ejecuta tests                                             │
│  - Hace build                                                │
│  - Deploya a staging server                                  │
└────────────────────┬─────────────────────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────────────────────┐
│  OpenClaw (Reporta resultado)                                │
│  "✅ Landing page deployada a staging                        │
│   URL: https://staging.ejemplo.com                           │
│   Tests: 24/24 passed"                                      │
└──────────────────────────────────────────────────────────────┘
```

### Workflow 4: Bug Hunt Masivo

```javascript
// 20 agents buscan bugs en paralelo
const hunt = manager.ultrareviewStart('/my-big-project', {
  agentCount: 20,
  maxDurationMinutes: 30,
  focusAreas: ['security', 'performance', 'logic', 'types', 'memory-leaks']
});

// Poll status
setInterval(async () => {
  const status = manager.ultrareviewStatus(hunt.id);
  console.log(`Found ${status.bugsFound} bugs so far...`);
}, 60000);
```

### Workflow 5: Daily Standup Automatizado

```yaml
cron: "0 9 * * 1-5"  # 9 AM Monday-Friday

actions:
  - openclaw:check_git_activity
    outputs:
      - commits_since_yesterday
      - prs_merged
      - files_changed
      
  - openclaw:send_to_claude_code
    model: sonnet
    task: |
      Based on this activity:
      {{commits_since_yesterday}}
      
      Generate a concise standup update:
      1. What did you do yesterday?
      2. What are you doing today?
      3. Any blockers?
      
  - openclaw:post_to_slack
    channel: "#team-standups"
```

---

## 6. Recursos y Links

### GitHub Repositorios Clave

| Repo | Descripción | Link |
|------|-------------|------|
| `enderfga/openclaw-claude-code` | Plugin principal de integración | https://github.com/Enderfga/openclaw-claude-code |
| `moazbuilds/claudeclaw` | OpenClaw integrado en Claude Code | https://github.com/moazbuilds/claudeclaw |
| `coollabsio/openclaw` | Docker images oficiales de OpenClaw | https://github.com/coollabsio/openclaw |
| `win4r/openclaw-workspace` | Skill para mantener archivos OpenClaw | https://github.com/win4r/openclaw-workspace |

### Documentación Recomendada

- **Getting Started con el plugin:** `skills/references/getting-started.md` en el repo de enderfga
- **Tools Reference:** 27 herramientas documentadas
- **Council Protocol:** Colaboración multi-agente
- **Claude Code Channels:** `/help channels` dentro de Claude Code

### Skills de OpenClaw Relacionados

| Skill | Uso |
|-------|-----|
| `coding-agent` | Delegar tareas de coding a Claude/Codex |
| `clawflow` | Orquestación de tareas complejas |
| `github` | Monitoreo de repos y CI/CD |

---

## 7. Notas Finales y Recomendaciones

### Cuándo Usar Qué

| Situación | Recomendación |
|-----------|---------------|
| Automatización general, inbox triage, scheduling | **OpenClaw** |
| Coding profundo, refactoring, debugging | **Claude Code** |
| Necesitas ambas cosas | **OpenClaw + Plugin** |
| Quick setup, simple caso de uso | **ClaudeClaw** |
| Solo messaging channels | **Claude Code Channels** |

### Tips de Optimización

1. **Model Routing:** Usa Sonnet para tareas rápidas, Opus/4.5 para coding pesado
2. **Sesiones Persistentes:** El plugin mantiene 7 días de contexto
3. **Safety First:** Siempre usa `--dangerously-force-unsafe-install` con precaución
4. **Cost Tracking:** El plugin tiene accounting de tokens en tiempo real

### Riesgos y Consideraciones

- **Seguridad:** Ambos sistemas tienen acceso elevado. No expones tokens públicamente.
- **API Costs:** Sin límite, los costos pueden escalar. Monitoriza usage.
- **Contexto:** Sesiones muy largas pueden perder coherencia. Usa compact cuando sea necesario.

---

*Investigación compilada el 5 de Abril de 2026. El ecosystem evoluciona rápidamente; verificar versiones actuales antes de implementar.*
