# Guía Completa: Prompts Profesionales para Claude Code

> Versión: 1.0 · Última actualización: 2026-04-05

---

## Índice

1. [Anatomía de un Prompt Efectivo](#1-anatomía-de-un-prompt-efectivo)
2. [Frameworks de Estructuración (CO-STAR)](#2-frameworks-de-estructuración-co-star)
3. [Patrón "Context Sandwich"](#3-patrón-context-sandwich)
4. [Técnicas Avanzadas de Prompt Engineering](#4-técnicas-avanzadas-de-prompt-engineering)
5. [El Archivo CLAUDE.md](#5-el-archivo-claudemd)
6. [Plantillas Reutilizables](#6-plantillas-reutilizables)
7. [Ejemplos de Prompts para Desarrollo Web](#7-ejemplos-de-prompts-para-desarrollo-web)
8. [Skills de OpenClaw para Mejorar Prompts](#8-skills-de-openclaw-para-mejorar-prompts)
9. [Errores Comunes y Cómo Evitarlos](#9-errores-comunes-y-cómo-evitarlos)
10. [Resumen Rápido (Cheat Sheet)](#10-resumen-rápido-cheat-sheet)

---

## 1. Anatomía de un Prompt Efectivo

Un buen prompt para Claude Code tiene **6 componentes clave**:

```
┌─────────────────────────────────────────────────────┐
│  1. ROL         → Quién eres como IA               │
│  2. CONTEXTO    → Qué existe ya (stack, archivos)   │
│  3. TAREA       → Qué necesitas hacer (claro)      │
│  4. RESTRICCIONES → Qué EVITAR y limitaciones       │
│  5. EJEMPLOS    → Few-shot para guiar formato      │
│  6. FORMATO     → Cómo quieres la respuesta         │
└─────────────────────────────────────────────────────┘
```

### Ejemplo Rápido

```
Actúa como desarrollador senior frontend especializado en React y Tailwind CSS.

CONTEXTO: Tenemos un proyecto Next.js 14 con App Router, TypeScript y Tailwind.
Ya existe un componente Button en /components/ui/Button.tsx que usa shadcn/ui.

 TAREA: Crea un componente Card de pricing con 3 planes, cada uno con nombre,
 precio, lista de features y un CTA.

RESTRICCIONES:
- Seguir exactamente el patrón de Button.tsx (estructura de archivo, props, estilos)
- Usar solo tokens de Tailwind disponibles en el proyecto
- El componente debe ser responsive (mobile-first)
- No usar CSS custom, solo Tailwind

FORMATO: Dame el código completo en un solo archivo .tsx
```

---

## 2. Frameworks de Estructuración (CO-STAR)

Creado por **GovTech Singapore** (ganador del primer concurso de Prompt Engineering de GPT-4 en Singapur).

| Letra | Componente | Descripción |
|-------|-----------|-------------|
| **C** | Context | Background, escenario, qué existe |
| **O** | Objective | Qué querés lograr — concreto y medible |
| **S** | Style | El estilo de respuesta o código (ej: "como senior dev", "functional React") |
| **T** | Tone | Tono emocional (formal, casual, entusiasta) |
| **A** | Audience | Para quién es (desarrolladores, usuarios finales, etc.) |
| **R** | Response | Formato de salida (código, markdown, JSON, etc.) |

### CO-STAR Aplicado a Desarrollo Web

```
CONTEXTO: Proyecto e-commerce en React + Vite + Tailwind. Carrito de compras
existente en useCart hook con estado en Zustand.

OBJETIVO: Crear un mini-widget de carrito flotante que muestre:
  - Cantidad de items
  - Precio total
  - Botón para ver carrito completo

ESTILO: Código limpio, funcional, TypeScript estricto, siguiendo patrones de
        componentes existentes del proyecto.

TONO: Profesional / production-ready (manejo de edge cases).

AUDIENCIA: Desenvolvedores que mantengan este proyecto.

RESPUESTA: Archivo .tsx completo con el componente y sus tipos.
```

### Variante: CO-STAR-A

Agrega un componente **Answer** que fuerza al modelo a dar una respuesta directa:

> *"Answer the request directly. Do not apologize, do not ask follow-up questions."*

---

## 3. Patrón "Context Sandwich"

Para código complejo, la estructura de 3 capas funciona mejor:

```
╔════════════════════════════════════════════════════╗
║              CONTEXTO (abajo)                      ║
║  Stack, archivos existentes, patrones, convenciones║
╠════════════════════════════════════════════════════╣
║              TAREA (centro)                       ║
║  Qué construir — específico, acotado, con requisitos║
╠════════════════════════════════════════════════════╣
║           RESTRICCIONES (arriba)                   ║
║  Qué evitar, compatibilidad, performance, SEO      ║
╚════════════════════════════════════════════════════╝
```

### Ejemplo Completo

```
CONTEXTO:
- Next.js 14 con App Router
- Base de datos: PostgreSQL + Drizzle ORM
- Auth: NextAuth v5
- Estilos: Tailwind CSS + shadcn/ui
- El archivo /api/products/route.ts ya tiene endpoints GET y POST

TAREA:
Crea la ruta API en /api/orders/route.ts con:
  - GET: lista de órdenes con paginación cursor-based
  - POST: crear orden nueva con validación Zod
  - Relations: incluir datos del usuario y los items de la orden

RESTRICCIONES:
- Usar Drizzle query builder, SIN raw SQL
- Seguir el mismo patrón de estructura que /api/products
- Incluir manejo de errores con mensajes claros
- Cursor-based pagination (NO offset)
- Validación de input con Zod
- Auth requerida en ambos endpoints
- Response times < 200ms
```

---

## 4. Técnicas Avanzadas de Prompt Engineering

### 4.1 Few-Shot Prompting (Ejemplos)

Los ejemplos son la forma más confiable de guiar el formato de salida.

```
No escribas un prompt con "por ejemplo, algo así". Dales el EJEMPLO EXACTO.

Para crear un buen few-shot:

1. Cantidad: 3-5 ejemplos para tareas complejas, 1-2 para simples
2. Calidad: Cada detalle del ejemplo DEBE coincidir con lo que querés
   (nomenclatura, estilo de código, formato)
3. Ubicación: Al inicio del prompt, nunca al final
4. Consistencia: No mezclar estilos distintos
```

**Ejemplo de few-shot para un componente:**

```tsx
// EJEMPLO - Este es el patrón a seguir:
import React from 'react';

interface ButtonProps {
  children: React.ReactNode;
  variant?: 'primary' | 'secondary';
  onClick?: () => void;
  disabled?: boolean;
}

export function Button({ children, variant = 'primary', onClick, disabled }: ButtonProps) {
  return (
    <button
      className={`px-4 py-2 rounded-lg font-medium transition-colors
        ${variant === 'primary' ? 'bg-blue-600 text-white hover:bg-blue-700' : 'bg-gray-200 text-gray-800 hover:bg-gray-300'}
        ${disabled ? 'opacity-50 cursor-not-allowed' : ''}`}
      onClick={onClick}
      disabled={disabled}
    >
      {children}
    </button>
  );
}
```

### 4.2 Chain of Thought (CoT) - Pensamiento en Pasos

Pedí que piense paso a paso antes de actuar:

```
Crea un dashboard de analytics. Antes de escribir código:

1. Define la estructura de datos y tipos TypeScript
2. Elige qué gráficos/visualizaciones usar (basado en los datos)
3. Diseña el layout responsive
4. Escribe el código componente por componente

Ahora implementalo.
```

### 4.3 Self-Correction / Revisión Adversarial

```
Implementá el componente Modal. Luego revisalo Vos Mismo buscando:
- Accesibilidad (focus trap, aria labels, escape key)
- Estados (loading, error, vacío)
- Responsive (scroll en mobile si contenido es largo)
- Performance (no renderizar contenido del modal hasta que abra)

Corrige los problemas que encuentres antes de finalizar.
```

### 4.4 XML Tags para Estructura

Para prompts largos o complejos, envolvé secciones en tags:

```xml
<project>
  <stack>Next.js 14, TypeScript, Tailwind, shadcn/ui</stack>
  <conventions>
    <file_pattern>feature/PageName.tsx</file_pattern>
    <component_pattern>functional components, named exports</component_pattern>
  </conventions>
</project>

<task>
  Crear landing page para startup SaaS B2B con:
  - Hero section con headline, subheadline y CTA primario
  - Features section (3 columnas)
  - Pricing section (3 planes)
  - CTA final
  - Footer simple
</task>

<constraints>
  - Solo un archivo .tsx (si es posible) o archivos separados
  - Mobile-first responsive
  - Colores de marca: primary=#6366F1, secondary=#1E1B4B
  - Tipografía: Inter (Google Fonts)
  - Velocidad de carga < 2s (lazy load imágenes)
  - SEO: meta tags, OG tags, semántica HTML
</constraints>
```

### 4.5 Prompt Chaining

Para tareas complejas, dividí en pasos secuenciales:

```
PASO 1: Diseña la arquitectura de componentes
  → Dame un mapa de qué componentes necesito crear y cómo se relacionan.

PASO 2: Implementá los componentes base
  → Creá [lista de componentes] siguiendo el mapa del paso 1.

PASO 3: Integrá los componentes
  → Armá la página principal usando los componentes del paso 2.

PASO 4: Agregá estilos y animaciones
  → Aplicá transiciones, hover states, animaciones de entrada.

PASO 5: Revisá accesibilidad
  → Verificá ARIA labels, focus, contraste, navegación por teclado.
```

---

## 5. El Archivo CLAUDE.md

El archivo `CLAUDE.md` es tu **mejor inversión** para trabajar con Claude Code.

### Qué es

Un archivo Markdown en la raíz del proyecto que Claude Code lee al inicio de cada sesión. Actúa como "onboarding guide" para la IA.

### Estructura Recomendada

```markdown
# Project Name

## Overview
Breve descripción del proyecto y su propósito.

## Tech Stack
- Frontend: React 18, Vite, TypeScript, Tailwind CSS
- Backend: Node.js, Express
- Base de datos: PostgreSQL, Prisma
- Deploy: Vercel

## Architecture
- `/src/components` — componentes UI reutilizables
- `/src/features` — lógica de features (carritos, auth, etc.)
- `/src/hooks` — hooks custom
- `/src/lib` — utilidades, API clients
- `/src/pages` — páginas de la app

## Coding Rules
- Componentes funcionales con named exports
- TypeScript strict mode — nunca usar `any`
- Props con interfaces, no inline types
- CSS: Tailwind utilities sobre custom CSS
- Imports: absolute paths con `@/` alias

## Design System
- Colores: tokens en `tailwind.config.ts` → brand-primary, brand-secondary
- Usar componentes de `shadcn/ui` como base
- Spacing: sistema de 4px (0, 4, 8, 16, 24, 32, 48, 64)

## Common Commands
- `npm run dev` — desarrollo local
- `npm run build` — build de producción
- `npm run lint` — linting con ESLint
- `npm test` — tests con Vitest

## Key Decisions
- Server Components para pages estáticas, Client Components para interactividad
- Estado global con Zustand (no Redux)
- API calls con React Query (TanStack Query)
```

### Beneficios Medibles

> Context engineering con CLAUDE.md puede reducir refactoring manual en un **60-80%**.

---

## 6. Plantillas Reutilizables

### 6.1 Plantilla Universal para Componentes

```
ACTÚA COMO: [rol/especialización]

CONTEXTO:
- Stack actual: [tecnologías]
- Archivos relacionados: [rutas/nombres]
- Patrones existentes: [código/estilo a seguir]

TAREA: Crear [nombre del componente] que [descripción de funcionalidad].

REQUISITOS:
- [requisito 1]
- [requisito 2]
- [requisito 3]

RESTRICCIONES:
- NO hacer [cosa a evitar]
- Usar [tecnología/patrón obligatorio]
- [otra limitación]

FORMATO: [archivo único / múltiples archivos / código inline]

EJEMPLO DE ESTILO:
[código de ejemplo si aplica]
```

### 6.2 Plantilla para Landing Pages

```
ACTÚA COMO: Diseñador y desarrollador frontend senior especializado en
             landing pages de alta conversión.

TAREA: Diseñar y codear una landing page para [producto/servicio].

INFO DEL PRODUCTO:
- Nombre: [nombre]
- Propuesta de valor: [qué resuelve, para quién]
- Target: [audiencia]
- Diferenciador: [por qué elegirnos]

SECCIONES REQUERIDAS:
- Hero: headline, subheadline, CTA principal, social proof rápido
- [definir otras secciones]

ESTILO VISUAL:
- Paleta: [colores hex]
- Tipografía: [fuentes]
- Vibe: [profesional/casual/minimalista/etc.]
- Inspiración: [sitios de referencia, opcional]

RESTRICCIONES:
- Mobile-first, responsive
- Solo HTML + CSS + JS vanilla (o [framework específico])
- Performance: Lighthouse score > 90
- Accesibilidad: WCAG 2.1 AA
- SEO: meta tags completos, OG tags, schema markup básico

OUTPUT: Código completo en [un archivo / estructura de proyecto].
Incluí说明 brevemente las decisiones de diseño tomadas.
```

### 6.3 Plantilla para API Routes

```
CONTEXTO:
- Framework: [Express / Fastify / Next.js API Routes / etc.]
- ORM: [Prisma / Drizzle / Sequelize / raw SQL]
- Auth: [NextAuth / Passport / JWT / etc.]
- Validador: [Zod / Joi / Yup]

TAREA: Crear endpoint(s) en [ruta de archivo].

ENDPOINTS:
- GET [ruta]: [qué hace, parámetros, paginación, filtros]
- POST [ruta]: [qué crea, body requerido, validaciones]

MANEJO DE ERRORES:
- 400: validación fallida
- 401: no autenticado
- 404: recurso no encontrado
- 500: error interno

RESPUESTA:
- Estructura JSON consistente
- incluir timestamps, IDs, datos relacionados

RESTRICCIONES:
- Usar [ORM/query builder específico]
- NO raw SQL
- Paginación: [cursor-based / offset-based]
- Rate limiting考虑一下
- Tests requeridos

FORMATO: Código completo + archivo de tests.
```

---

## 7. Ejemplos de Prompts para Desarrollo Web

### Ejemplo 1: Landing Page SaaS

```
Actúa como desarrollador frontend senior especializado en landing pages
de alta conversión.

Creá una landing page completa para "DevTask" — una herramienta de
gestión de tareas para equipos de desarrollo.

TARGET: Developers y Tech Leads en startups (B2B SaaS)
PROPUESTA: "Gestiona sprints, code reviews y bugs en un solo lugar — 
sin complejidades."

SECCIONES:
1. HERO
   - Headline: "Stop juggling tools. Ship faster."
   - Sub: "DevTask connects your code reviews, sprints, and bugs 
          in one board that developers actually love."
   - CTA: "Start Free Trial" (primary), "See Demo" (secondary)
   - Badge: "Trusted by 2,000+ engineering teams"

2. PROBLEM → Agitación del problema (brief, visual)

3. FEATURES (3 cards):
   - "Code Review First" — PRs, comments, approvals inline
   - "Sprint Automation" — Auto-assign, velocity tracking
   - "Instant Feedback" — Slack + GitHub notifications

4. METRICS (social proof):
   - "40% faster sprint completion"
   - "2.1M tasks completed"
   - "99.9% uptime"

5. PRICING (3 planes):
   - Starter: free, 5 members
   - Pro: $12/user/mo, unlimited members
   - Enterprise: custom, SSO + audit logs

6. CTA FINAL: "Join 2,000+ teams shipping faster"

STYLE:
- Color palette: primary=#4F46E5 (indigo), bg=#0F172A (dark)
- Font: Inter
- Vibe: dark, premium, developer-focused
- Animations: subtle fade-ins on scroll

CONSTRAINTS:
- Single HTML file with embedded CSS + JS
- Mobile-first responsive
- No frameworks, vanilla only
- Use CSS custom properties for theming
- Lazy load any images
- Lighthouse > 90

OUTPUT: Complete HTML + CSS + JS file. Use placeholder divs for images
instead of real image URLs.
```

### Ejemplo 2: Dashboard Component

```
Contexto: App React + Vite + TypeScript + Tailwind + shadcn/ui.
Proyecto existente con componentes en /components/ui/.
Usa Radix UI primitives bajo la superficie.

Tarea: Crear un Dashboard analytics con las siguientes métricas:
- Ventas totales (con trend %)
- Órdenes nuevas (últimos 7 días)
- Productos más vendidos (top 5, con thumbnail)
- Gráfico de ventas últimas 4 semanas (bar chart simple con CSS/SVG)

Requisitos:
- Layout: grid responsive (1 col mobile, 2 col tablet, 4 col desktop)
- Cada card: título, valor principal, trend (verde/rojo según dirección)
- Datos mockeados hardcodeados en un archivo data.ts separado
- El chart SVG debe ser responsive
- Incluir skeleton loading state

Estilo:
- Tarjetas con fondo sutil, bordes suaves, sombra ligera
- Números grandes (2xl-3xl), labels pequeños y grises
- Iconos con Lucide React

NO USAR: chart libraries (Recharts, Chart.js, etc.) — solo SVG/CSS.
Seguir el patrón de componentes existente (Tailwind + Radix bajo shadcn).
```

### Ejemplo 3: Full Page Checkout

```
Contexto: E-commerce app en React, estado global con Zustand,
carrito en useCart store.

Tarea: Crear página de checkout completa con 3 pasos:

STEP 1 - Shipping:
  - Formulario: nombre, email, dirección, ciudad, CP, país
  - Validación inline (nombre requerido, email formato, CP numérico)

STEP 2 - Payment:
  - Método: tarjeta de crédito (inputs: número, expiry, CVV)
  - Validación de número de tarjeta (Luhn algorithm básico)
  - Mostrar icono de tarjeta (Visa/Mastercard según prefijo)

STEP 3 - Review:
  - Resumen del pedido (items del carrito)
  - Costos desglosados (subtotal, shipping, tax, total)
  - Botón "Place Order" que simula éxito (loading state → success screen)

UI/UX:
- Progress indicator en la parte superior (1 → 2 → 3)
- Botones "Back" y "Continue" / "Place Order"
- Validación al intentar avanzar al siguiente paso
- Success screen después de completar (animación de checkmark)

Constraints:
- Un solo archivo .tsx
- Form state interno (no externalizar a Zustand)
- UX flow completo: no dejar pasos a medio terminar
- Responsive (stack en mobile, inline en desktop)

Output: Single React component file (page). Imports from 'react' and 'zustand'
for useCart. All in one file.
```

### Ejemplo 4: API Route Completa

```
Contexto: Next.js 14 App Router, PostgreSQL, Drizzle ORM, NextAuth v5,
validación con Zod, TypeScript strict mode.

Tarea: Crear API route para gestionar órdenes de un e-commerce.

Endpoints:
- GET /api/orders
  → Lista de órdenes del usuario autenticado
  → Cursor-based pagination (cursor = order ID, limit = 20)
  → Filtro por status: ?status=pending|processing|shipped|delivered|cancelled
  → Incluir: items de cada orden (con product details)

- POST /api/orders
  → Body: { items: [{productId, quantity, priceAtPurchase}], shippingAddress }
  → Validación Zod completa
  → Disminuir stock de productos
  → Retornar orden creada con todos los datos

Errores:
- 400: validación fallida (detalle de qué campo)
- 401: usuario no autenticado
- 404: producto no encontrado
- 409: stock insuficiente
- 500: error de base de datos

Constraints:
- NO raw SQL — usar Drizzle query builder exclusivamente
- Transactions para POST (orden + update stock)
- Rate limit: 100 requests/min por usuario
- Response < 200ms para GET (usar índices)
- Tests: archivo de tests con Vitest

Output:
1. /app/api/orders/route.ts
2. /app/api/orders/schema.ts (validación Zod)
3. /app/api/orders/__tests__/route.test.ts
```

---

## 8. Skills de OpenClaw para Mejorar Prompts

### Skill Disponible: `prompt-enhancer`

**Ubicación:** `~/.openclaw/workspace/skills/skills/prompt-enhancer/SKILL.md`

**Cómo usarlo:** Prefixá tu mensaje con `p:` o `prompt:`.

**Qué hace:**
1. Reescribe tu input rough en un prompt optimizado
2. Muestra el prompt mejorado antes de responder
3. Educa sobre qué hace bueno a un buen prompt

**Ejemplo:**
```
p: haceme una landing page para mi app
```

Se transforma en algo mucho más detallado y estructurado.

### Cómo Instalar Skills Adicionales

```bash
# Buscar skills en ClawHub
openclaw skills search "prompt"

# Instalar un skill
openclaw skills install <nombre-del-skill>

# Listar skills instalados
openclaw skills list

# Actualizar un skill
openclaw skills update <nombre-del-skill>
```

### Skills Relevantes para Prompts

| Skill | Descripción | Útil para |
|-------|-------------|-----------|
| `prompt-enhancer` | Reescribe inputs rough en prompts optimizados | Cualquier tarea |
| `coding-agent` | Delega tareas de código a Claude Code/Codex | Proyectos grandes |
| `brand-identity` | Genera identidad de marca | Landing pages, marketing |
| `seo-optimizer` | Optimiza para SEO | Páginas web públicas |
| `website-seo` | SEO on-page completo | Sites productivos |

---

## 9. Errores Comunes y Cómo Evitarlos

| Error | Por qué pasa | Solución |
|-------|-------------|----------|
| **"Hacé algo cool"** | Prompt demasiado vago | Ser específico: "un hover effect de 300ms con easing" |
| **Todo en un prompt** | Prompt mega-largo y complejo | Dividir en prompt chaining / pasos |
| **Sin contexto** | "Creá un botón" → resultado genérico | Agregar stack, archivos existentes, patrones |
| **Olvidar constraints** | Funciona pero viola requisitos | Listar explícitamente qué NO hacer |
| **Negativos vagos** | "No sea feo" | "Usar paleta monochromática, bordes suaves" |
| **Un solo ejemplo** | No hay margen de variación | 3-5 ejemplos diversos para tareas complejas |
| **Sin formato** | Código mezclado con explicaciones | Especificar: "Código en block ```tsx, luego explicación" |
| **Scope creep** | "Y mientras estás..." | Una tarea por prompt, por favor |

---

## 10. Resumen Rápido (Cheat Sheet)

### La Fórmula Mágica

```
ROL + CONTEXTO + TAREA + RESTRICCIONES + FORMATO = PROMPT EFECTIVO
```

### Checklist Antes de Enviar un Prompt

- [ ] ¿Definí el rol de la IA?
- [ ] ¿Di qué stack/archivos existen?
- [ ] ¿La tarea es específica y acotada?
- [ ] ¿Listé qué evitar (restricciones)?
- [ ] ¿Definí el formato de salida?
- [ ] ¿Agregué ejemplos para tareas complejas?
- [ ] ¿Es mobile-first si es UI?

### Atajos Mental

```
Si está vago → Especificá
Si es largo  → Dividí
Si es nuevo  → Dale ejemplos
Si es crítico → Pedí revisión adversarial
Si es proyecto → Creá CLAUDE.md
Si no sabés  → Usá prompt-enhancer (p:)
```

### Técnicas por Situación

| Situación | Técnica |
|-----------|---------|
| Código genérico | Agregar contexto + ejemplos (few-shot) |
| Task compleja | Prompt chaining (pasos secuenciales) |
| output inconsistente | Few-shot con 3-5 ejemplos |
| Errores frecuentes | Agregar constraints + self-correction |
| Proyecto nuevo | Crear CLAUDE.md primero |
| Input rough | Usar `p:` + prompt-enhancer |

---

## Recursos Adicionales

- [Documentación oficial de Prompt Engineering de Anthropic](https://docs.anthropic.com/en/build-with-claude/prompt-engineering)
- [Best Practices de Claude Code](https://code.claude.com/docs/en/best-practices)
- [CO-STAR Framework - GovTech Singapore](https://www.tech.gov.sg/technews/mastering-the-art-of-prompt-engineering-with-empower/)
- [ CLAUDE.md Best Practices - UXPlanet](https://uxplanet.org/claude-md-best-practices-1ef4f861ce7c)

---

*Documento creado para uso propio. Compartí, modificá, mejoralo.*
