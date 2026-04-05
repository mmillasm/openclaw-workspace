# Guía Completa: Claude Code + Antigravity + Nano Banana 2 para Crear Websites Profesionales

> **Investigación completada:** 5 de abril de 2026
> **Autor:** Subagente de investigación automatizado
> **Fuentes:** Anthropic, Google, GitHub, blogs técnicos, casos de estudio

---

## Tabla de Contenidos

1. [Qué es Claude Code](#1-qué-es-claude-code)
2. [Qué es Antigravity](#2-qué-es-antigravity)
3. [Qué es Nano Banana 2](#3-qué-es-nano-banana-2)
4. [Cómo se usan juntos](#4-cómo-se-usan-juntos-para-crear-websites-profesionales)
5. [Ejemplos de sitios creados](#5-ejemplos-de-sitios-creados-con-esta-combinación)
6. [Flujo de trabajo paso a paso](#6-flujo-de-trabajo-paso-a-paso)
7. [Requisitos y configuración](#7-requisitos-y-configuración-necesaria)

---

## 1. Qué es Claude Code

### Definición

**Claude Code** es la herramienta de codificación agéntica de Anthropic, diseñada para asistir en tareas de desarrollo de software interactuando con tu codebase a través de comandos en lenguaje natural. No es un simple autocompletado: es un agente de IA que puede planificar, ejecutar y verificar tareas complejas de forma autónoma.

### Características Principales

| Característica | Descripción |
|----------------|-------------|
| **Operación agéntica** | Puede leer codebases completas, planificar enfoques multi-archivo, ejecutar cambios, correr tests e iterar ante fallos |
| **Terminal-first** | Opera directamente en tu terminal, trabajando con el directorio actual como contexto |
| **Modelo Context Protocol (MCP)** | Protocolo abierto que permite conectar Claude con fuentes externas: Google Drive, Jira, Slack, bases de datos, etc. |
| **Entiende codebases** | Puede escanear y comprender proyectos enteros, hacer ediciones coordinadas y gestionar Git |
| **Seguridad explícita** | Requiere permiso explícito antes de modificar archivos o ejecutar comandos |
| **Contexto amplio** | Hasta 1M tokens de contexto en beta para usuarios de API |

### Modelos Disponibles

- **Claude Opus 4.6** — Para tareas complejas de razonamiento
- **Claude Sonnet 4.6** — Balance entre velocidad y capacidad
- Acceso incluido en suscripciones Pro ($20/mes) y Max ($100/mes)

### Lo que NO es Claude Code

- ❌ No es un IDE visual (no tiene interfaz gráfica propia)
- ❌ No genera imágenes nativamente (requiere integraciones)
- ❌ No es un chat genérico — está especializado en código

### Recursos Oficiales

- Documentación: https://code.claude.com/docs/en/overview
- Producto: https://www.anthropic.com/product/claude-code

---

## 2. Qué es Antigravity

### Definición

**Antigravity** es el IDE de IA de Google (lanzado en preview público el 18 de noviembre de 2025), construido sobre una bifurcación de VS Code. Su diferenciador principal: es **agente-first**, no solo un asistente de código. Permite orquestar múltiples agentes de IA que trabajan en paralelo de forma autónoma.

### Características Principales

| Característica | Descripción |
|----------------|-------------|
| **Arquitectura multi-agente** | Puedes lanzar múltiples agentes especializados simultáneamente: código, testing, seguridad, optimización |
| **Browser subagent** | Agente integrado con Gemini 2.5 Computer Use para verificación visual de UI |
| **Mission Control** | Interfaz Kanban-style para gestionar y monitorizar agentes en background |
| **Artifactos** | Los agentes generan planes de implementación, task lists, diffs de código, walkthroughs y screenshots |
| **Skills (SKILL.md)** | Sistema de habilidades extensibles mediante archivos Markdown con YAML frontmatter |
| **Review Policies** | Control configurable: auto-proceed, approval on milestones, o review en cada paso |
| **Integración Gemini 3.1** | Modelo bundle incluido en la experiencia |

### Comparación: Antigravity vs Claude Code

| Aspecto | Antigravity | Claude Code |
|---------|------------|-------------|
| **Interfaz** | IDE visual completo (VS Code fork) | Terminal/CLI nativo |
| **Enfoque** | Orquestación visual multi-agente | Ejecución granular desde terminal |
| **Verificación visual** | Browser agent integrado | Requiere setup manual (Puppeteer) |
| **Curva de aprendizaje** | Más suave, visual | Más técnica, para devs de terminal |
| **Costo** | Punto de entrada gratuito (2026) | Incluido en suscripción Pro/Max |

### Sistema de Skills (SKILL.md)

El formato `SKILL.md` permite extender capacidades con archivos Markdown + YAML:

```yaml
---
name: mi-skill
description: Cuando necesito generar landing pages profesionales
---

# Contenido de la skill
Instrucciones, scripts, templates...
```

**Scopes de Skills:**
- **Workspace:** `.agent/skills/` — específicos del proyecto, versionables
- **Global:** `~/.gemini/antigravity/skills/` — disponibles en todos los proyectos

### Recursos Oficiales

- Producto: https://antigravity.google/product
- Blog: https://antigravity.google/blog/introducing-google-antigravity
- Codelabs: https://codelabs.developers.google.com/getting-started-google-antigravity

---

## 3. Qué es Nano Banana 2

### Definición

**Nano Banana 2** (internamente conocido como **Gemini 3.1 Flash Image**) es el modelo de generación y edición de imágenes de Google, especializado en renders de UI/UX, mockups profesionales y diseño de interfaces de alta fidelidad. El nombre "Nano Banana" es un nombre clave interno (similar a cómo Google usa nombres de frutas para sus modelos en desarrollo).

### Características Principales

| Característica | Descripción |
|----------------|-------------|
| **Generación de alta resolución** | Soporta输出 hasta 4K |
| **Text rendering preciso** | Renderiza texto multilenguaje con precisión — problema común en otros modelos |
| **Multi-image context** | Puede usar hasta 14 imágenes de referencia simultáneamente |
| **Edición conversacional** | Generar → editar → iterar en conversación natural |
| **Consistencia de personajes** | Ideal para branding visual coherente |
| **SynthID watermarks** | Marcas de agua invisibles para transparencia de IA |
| **Text-to-live** | Transforma prompts en animaciones de 4 segundos |

### Capacidades para Diseño Web

Nano Banana 2 destaca en:

1. **UI Mockups profesionales** — Genera interfaces completas con layout coherente
2. **Landing pages conceptuales** — Visuales de alta calidad para presentar ideas
3. **Logos y branding** — Generación de identidades visuales consistentes
4. **Iconografía y gráficos** — Assets para interfaces
5. **Iteración visual rápida** — Generar → revisar → ajustar en minutos

### Limitaciones Importantes

- ⚠️ **No genera código directamente** — genera imágenes, no HTML/CSS/React
- ⚠️ **Requiere integración** — necesita Claude Code o Antigravity para traducir las imágenes a código funcional
- ⚠️ **Dependencia de API** — Necesita API key de Google AI / Gemini

### Recursos Oficiales

- Blog: https://blog.google/innovation-and-ai/technology/developers-tools/build-with-nano-banana-2/
- Documentación: https://ai.google.dev/gemini-api/docs/image-generation

---

## 4. Cómo se usan juntos para crear websites profesionales

### El Stack Completo

```
┌─────────────────────────────────────────────────────────────┐
│                    WORKFLOW INTEGRADO                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   Nano Banana 2          Antigravity           Claude Code  │
│   ┌───────────┐          ┌───────────┐        ┌───────────┐│
│   │ Genera    │─────────▶ │ Orquesta   │──────▶ │ Implementa││
│   │ imágenes  │          │ múltiples  │        │ código    ││
│   │ UI/UX     │          │ agentes    │        │ funcional ││
│   └───────────┘          └───────────┘        └───────────┘│
│         │                     │                     │       │
│         ▼                     ▼                     ▼       │
│   Mockups              Plans + Reviews         Deploy       │
│   visuales             Verificación            producción  │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Estrategia de Integración

**Paso 1 — Visión (Nano Banana 2)**
- Genera mockups visuales de alta calidad
- Crea múltiples conceptos de diseño rápidamente
- Itera en colores, layouts, tipografías visualmente
- Genera assets: logos, iconos, hero images

**Paso 2 — Estructura (Antigravity)**
- Usa agentes de contenido para definir modelos de datos
- Agentes de código para scaffold del proyecto
- Browser agent para verificar cada pantalla
- Review policies para control de calidad

**Paso 3 — Implementación (Claude Code)**
- Traduce mockups a código real (HTML, CSS, React, etc.)
- Implementa features específicas con precisión
- Correcciones iterativas y optimizaciones
- Deploy a producción

### Integración vía Model Context Protocol (MCP)

Claude Code puede conectarse a Nano Banana 2 mediante MCP:

```bash
# Configuración típica en CLAUDE.md
# Usar gemini-code o plugin de generación de imágenes
# con API key de Google AI Studio
```

Alternativas para integración:
1. **Agente intermediate:** Nano Banana 2 → prompt estructurado → Claude Code
2. **MCP servers:** Configurar conexión directa entre herramientas
3. **Workflow manual:** Generar imagen → describir en texto → pasar a Claude Code

### Qué puede hacer cada herramienta mejor

| Tarea | Mejor Herramienta | Por qué |
|-------|-------------------|---------|
| Diseño visual inicial | Nano Banana 2 | Generación de imágenes de altísima calidad |
| Mockups de UI | Nano Banana 2 | Renderizado preciso de interfaces |
| Arquitectura de proyecto | Antigravity | Visión holística con múltiples agentes |
| Generación de contenido | Antigravity | Agentes de contenido especializados |
| Código frontend preciso | Claude Code | Ejecución granular, control total |
| Refactoring complejo | Claude Code | Terminal-first, manejo preciso de archivos |
| Verificación visual | Antigravity | Browser agent integrado |
| Deployment | Claude Code | Integración nativa con Git y CLI |

---

## 5. Ejemplos de sitios creados con esta combinación

### Nota sobre evidencia disponible

La combinación específica "Claude Code + Antigravity + Nano Banana 2" es relativamente nueva (finales 2025/inicio 2026), por lo que los casos de estudio documentados públicamente son limitados. Sin embargo, hay abundante evidencia de flujos similares:

### Patrones de Uso Documentados

**1. Landing pages de startups**
- Flujo: Diseño visual con IA → Implementación con Claude Code → Deploy
- Tiempo promedio reportado: 2-4 horas (vs 1-2 semanas tradicionalmente)
- stack técnico típico: Next.js + Tailwind CSS + Vercel

**2. Dashboards y aplicaciones SaaS**
- Uso de Gemini/Nano Banana para mockups de data visualization
- Claude Code para implementación de componentes interactivos
- Antigravity para arquitectura multi-página

**3. E-commerce storefronts**
- Generación de lifestyle imagery con Nano Banana 2
- Implementación de catálogos con Claude Code
- Orquestación de flujos complejos con Antigravity

### Repositorios y Recursos Relacionados

```
# Templates y starters para este workflow
github.com/kingbootoshi/nano-banana-2-skill
github.com/ishandutta2007/open-antigravity
github.com/sabahattinkalkan/antigravity-fullstack-hq
github.com/galonge/antigravity-demo-app
```

### Comunidad y Referencias

- **Reddit r/ClaudeCode** — Discusiones activas sobre workflows de websites
- **YouTube** — Múltiples tutoriales de "vibe coding" con Claude Code (buscar "Claude Code website tutorial")
- **Medium** — Artículos sobre "Complete Claude Code Workflow" y optimizaciones
- **Build to Launch Substack** — Deep dives en integraciones Claude + Gemini

---

## 6. Flujo de trabajo paso a paso

### Flujo Recomendado: De concepto a producción

```
FASE 1: CONCEPTUALIZACIÓN (1-2 horas)
│
├── 1.1 Definir el proyecto
│   └── Propósito, audiencia, objetivos de negocio
│
├── 1.2 Investigación competitiva
│   └── Analizar sitios similares, identificar qué funciona
│
└── 1.3 Wireframes mentally
    └── Estructura de páginas, features principales

FASE 2: DISEÑO VISUAL (2-4 horas)
│
├── 2.1 Generar concepts con Nano Banana 2
│   ├── Homepage hero section
│   ├── Color palette concepts
│   ├── Layout variations
│   └── Mobile-first concepts
│
├── 2.2 Seleccionar mejor concepto
│   └── Iterar en detalles: tipografía, espaciado, iconografía
│
└── 2.3 Generar assets
    ├── Logo (si aplica)
    ├── Hero image / lifestyle photography
    └── Iconos y gráficos de soporte

FASE 3: SETUP DEL PROYECTO (1 hora)
│
├── 3.1 Elegir tech stack
│   ├── Next.js + Tailwind (recomendado para la mayoría)
│   ├── Astro (para sitios más estáticos)
│   └── Vite + React (más control)
│
├── 3.2 Inicializar con Claude Code
│   └── npx create-claude-code-app o init manual
│
└── 3.3 Configurar CLAUDE.md
    └── Documentar: stack, convenciones, constraints

FASE 4: IMPLEMENTACIÓN (4-12 horas)
│
├── 4.1 Construir estructura base
│   └── Layout principal, navegación, pages
│
├── 4.2 Implementar página por página
│   ├── Homepage → sections → CTA
│   ├── Inner pages (según complejidad)
│   └── Mobile responsiveness
│
├── 4.3 Iteración visual con screenshots
│   ├── Claude puede usar Puppeteer para capturar
│   └── Comparar con mockup de referencia
│
└── 4.4 Optimizaciones
    ├── Performance (Core Web Vitals)
    ├── Accesibilidad (a11y audit)
    └── SEO (meta tags, schema markup)

FASE 5: VERIFICACIÓN Y DEPLOY (1-2 horas)
│
├── 5.1 Testing cross-browser
│   └── Chrome, Firefox, Safari, mobile
│
├── 5.2 Review de contenido
│   └── Copias, imágenes, links
│
└── 5.3 Deploy
    └── Vercel, Cloudflare Pages, Netlify, o custom
```

### Comandos Clave de Claude Code

```bash
# Inicializar proyecto
claude-code init

# Iniciar modo interactivo
claude-code

# Comandos útiles dentro de Claude Code
/improve       # Mejorar código seleccionado
/test          # Generar y ejecutar tests
/docs          # Generar documentación
/deploy        # Desplegar (con configuración previa)

# Con git
/commit "mensaje"   # Commit con mensaje
/pr             # Crear pull request
/branch nombre   # Crear branch
```

### Workflow Antigravity Multi-Agente

```markdown
# En Antigravity, usar Mission Control:

1. **Agente Content** → Genera copy y estructura de contenido
2. **Agente Code** → Implementa scaffolding y componentes
3. **Agente Browser** → Verifica cada implementación
4. **Agente Security** → Escanea vulnerabilidades
5. **Agente Optimization** → Performance audit

# Los 5 pueden correr en paralelo.
```

---

## 7. Requisitos y configuración necesaria

### Requisitos de Software

#### Obligatorios

| Herramienta | Requisito | Instalación |
|------------|-----------|-------------|
| **Claude Code** | Node.js 20+ | `npm install -g @anthropic/claude-code` |
| **Git** | Última versión | Preinstalado en la mayoría de sistemas |
| **Node.js** | v20 o superior | nodejs.org |

#### Opcionales pero recomendados

| Herramienta | Para qué |
|------------|----------|
| **VS Code** | Editor para Antigravity (o CLI directo) |
| **Puppeteer** | Screenshots automatizados en Claude Code |
| **pnpm o yarn** | Package manager alternativo |
| **Docker** | Entornos isolados |
| **GitHub CLI** | Gestión de repos desde terminal |

### Cuentas y API Keys Necesarias

#### Obligatorias

1. **Anthropic API** (para Claude Code)
   - Suscripción Pro ($20/mes) o Max ($100/mes)
   - API key desde: console.anthropic.com

2. **Google AI** (para Nano Banana 2)
   - API key desde: aistudio.google.com
   - billing enabled en Google Cloud Console

#### Opcionales según necesidades

| Servicio | Uso |
|----------|-----|
| **Vercel** | Deploy automático |
| **Cloudflare** | DNS + Pages + Workers |
| **Supabase** | Backend/database |
| **Stripe** | Pagos |
| **Resend** | Email transactional |
| **GitHub** | Repos + Actions CI/CD |

### Configuración del Proyecto (CLAUDE.md)

Archivo fundamental para que Claude Code trabaje efectivamente:

```markdown
# CLAUDE.md — Contexto del proyecto

## Proyecto
Landing page para [tu proyecto], enfocada en conversión.

## Tech Stack
- Next.js 14 (App Router)
- TypeScript
- Tailwind CSS
- Deploy en Vercel

## Convenciones
- Components en /components/ui/ y /components/sections/
- Usar clsx para conditional classes
- Mobile-first responsive design
- Accesibilidad: aria-labels, focus states

## Comandos
- `pnpm dev` — desarrollo local
- `pnpm build` — build producción
- `pnpm test` — tests con Vitest

## Constraints
- Máximo 3 colores principales
- No usar JavaScript vanilla, solo React
- Todas las imágenes deben tener alt text
```

### Configuración de Antigravity

```markdown
# .agent/skills/web-design-essentials/SKILL.md

---
name: web-design-essentials
description: Cuando necesito diseñar y construir websites profesionales con alta calidad visual y UX
---

# Skill: Web Design Essentials

## Principios de Diseño
1. Mobile-first responsive
2. Typography: escala modular (1.25 ratio)
3. Spacing: 4px base grid
4. Color: máximo 3 primarios + neutros

## Tech Stack Preferido
- Next.js + Tailwind CSS
- shadcn/ui para componentes base
- Lucide React para iconografía

## Workflow
1. Generar mockup mental
2. Crear estructura de archivos
3. Implementar mobile first
4. Escalar a desktop
5. Verificar con browser agent
```

### Variables de Entorno Típicas (.env.example)

```bash
# Anthropic
ANTHROPIC_API_KEY=sk-ant-...

# Google AI (para Gemini/Nano Banana 2)
GOOGLE_API_KEY=AIza...

# Database (ejemplo Supabase)
NEXT_PUBLIC_SUPABASE_URL=https://xxx.supabase.co
NEXT_PUBLIC_SUPABASE_ANON_KEY=eyJ...
SUPABASE_SERVICE_ROLE_KEY=eyJ...

# Analytics
NEXT_PUBLIC_GA_ID=G-XXXXXXXXXX

# Email (ejemplo Resend)
RESEND_API_KEY=re_...
```

### Configuración de Git Hooks (opcional pero recomendado)

```bash
# .husky/pre-commit
pnpm lint
pnpm type-check
pnpm test
```

---

## Anexos

### Glosario de Términos

| Término | Definición |
|---------|-----------|
| **Agentic** | Capacidad de IA para planificar y ejecutar tareas de forma autónoma |
| **MCP** | Model Context Protocol — estándar para conectar IAs con herramientas externas |
| **Artifactos** | Outputs generados por agentes: planes, diffs, walkthroughs |
| **SKILL.md** | Formato para definir habilidades extensibles de agentes de IA |
| **Vibe coding** | Paradigma de desarrollo donde descreibes en lenguaje natural y la IA ejecuta |
| **Core Web Vitals** | Métricas de Google para performance web (LCP, FID, CLS) |

### Recursos Adicionales

**Documentación oficial:**
- Claude Code: https://code.claude.com/docs
- Antigravity: https://antigravity.google/blog
- Gemini API: https://ai.google.dev/docs

**Communities:**
- r/ClaudeCode en Reddit
- Discord de Anthropic
- GitHub Discussions de antigravity-*

**Tutoriales sugeridos (YouTube):**
- Buscar: "Claude Code complete workflow"
- Buscar: "Google Antigravity IDE tutorial"
- Buscar: "Claude Code website tutorial 2025"

### Próximos Pasos Recomendados

1. ✅ Configurar cuenta de Anthropic (Pro mínimo)
2. ✅ Configurar API key de Google AI Studio
3. ✅ Instalar Claude Code: `npm install -g @anthropic/claude-code`
4. ✅ Crear primer proyecto de prueba
5. ✅ Experimentar con Nano Banana 2 para generar un mockup
6. ✅ Implementar el mockup con Claude Code
7. ✅ Deployar a Vercel o Cloudflare Pages

---

*Documento generado automáticamente. Última actualización: Abril 2026. La información puede variar según actualizaciones de las herramientas. Verificar siempre la documentación oficial más reciente.*
