# Claude Code Templates - Exportado para Vinxe/OpenClaw

Repositorio clonado desde: https://github.com/davila7/claude-code-templates

## 📁 Estructura

```
claude-code-templates-export/
├── agents/           # Agentes especializados para Claude Code
├── skills/           # Skills reutilizables (prompts estructurados)
└── README.md         # Este archivo
```

## 🤖 Agentes Disponibles

### 1. `sales-automator.md`
Especialista en automatización de ventas y outreach.
- Secuencias de cold emails con personalización
- Campañas de follow-up
- Templates de propuestas y cotizaciones
- Case studies y social proof
- Scripts de ventas y manejo de objeciones

**Uso:** Cuando necesites crear campañas de cold email, secuencias de follow-up o materiales de ventas.

### 2. `content-marketer.md`
Estratega senior de contenido marketing.
- Estrategias de contenido SEO-optimizado
- Content planning y editorial calendar
- Campañas multi-canal
- Análisis de audiencia y ROI de contenido

**Uso:** Para desarrollar estrategias de contenido, crear campañas de marketing, optimizar SEO.

## 🛠️ Skills Disponibles

### 1. `lead-research-assistant.md`
Asistente de investigación de leads calificados.
- Identifica empresas que coinciden con tu ICP
- Prioriza leads con scoring 1-10
- Estrategias de contacto personalizadas
- Enriquecimiento de datos de decision-makers

**Uso:** Cuando necesites encontrar y cualificar leads para Vinxe u otros proyectos.

### 2. `email-sequence.md`
Diseñador experto de secuencias de email.
- Welcome sequences
- Lead nurture sequences
- Re-engagement sequences
- Onboarding sequences
- Sales sequences

**Uso:** Para crear cualquier tipo de secuencia de emails automatizada.

## 🚀 Cómo Usar

### Opción 1: Con Claude Code (via coding-agent skill)
Cuando necesites usar Claude Code para tareas complejas de coding:

```bash
# Usar el skill coding-agent que ya tienes instalado
# Los agents y skills exportados pueden usarse como system prompts
```

### Opción 2: Adaptar prompts para OpenClaw
Los archivos `.md` contienen prompts estructurados que puedo usar directamente como contexto cuando me pidas tareas relacionadas.

**Ejemplo de uso:**
- "Crea una secuencia de cold emails para Vinxe usando el sales-automator"
- "Investiga leads de agencias inmobiliarias en Chile con el lead-research-assistant"
- "Diseña una estrategia de contenido para Instagram usando content-marketer"

## 📊 Componentes en el Repositorio Original

El repo completo incluye muchos más componentes:

| Categoría | Componentes |
|-----------|-------------|
| **Agents** | 100+ especialistas (development, marketing, security, etc.) |
| **Skills** | 200+ capacidades modulares |
| **Commands** | Comandos slash personalizados |
| **MCPs** | Integraciones con servicios externos |
| **Analytics** | Dashboard de monitoreo en tiempo real |

## 🔧 Herramientas CLI Incluidas

```bash
# En claude-code-templates/cli-tool/
npx claude-code-templates@latest --analytics        # Dashboard de analytics
npx claude-code-templates@latest --health-check     # Check de salud
npx claude-code-templates@latest --skill [name]     # Instalar skill específico
npx claude-code-templates@latest --agent [name]     # Instalar agent específico
```

## 💡 Aplicaciones para Vinxe

1. **Cold Email Outreach**: Usar `sales-automator` + `email-sequence` para perfeccionar las campañas
2. **Lead Research**: Usar `lead-research-assistant` para encontrar más agencias inmobiliarias
3. **Content Marketing**: Usar `content-marketer` para la estrategia de Instagram y blog
4. **Sales Enablement**: Crear scripts de ventas y manejo de objeciones

## 📝 Notas

- Los prompts están optimizados para Claude Code pero funcionan con cualquier modelo
- Puedo adaptar estos prompts según necesites
- El repositorio original tiene licencia MIT

---

**Última actualización:** 2026-04-02
