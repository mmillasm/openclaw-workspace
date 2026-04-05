# Guía de Prompting Profesional para Claude Code

> Cómo escribir prompts que generan resultados excelentes — páginas web, apps, contenido — sin iteraciones excesivas.

---

## 1. Fundamentos: Cómo Piensa Claude Code

Claude Code funciona mejor cuando se le dan instrucciones literales y específicas. Los modelos modernos (4.x) siguen instrucciones al pie de la letra — no infieren intent ni van "más allá" a menos que se lo pidas explícitamente. Esto significa que **mientras más preciso seas, mejor será el resultado**.

**Regla de oro:** Si tienes que elegir entre una instrucción corta y una larga, elige la larga. La especificidad > brevedad.

---

## 2. La Anatomía de un Prompt Profesional

Un prompt profesional completo tiene 6 componentes. No siempre necesitas los 6, pero saberlos te permite construir el prompt exacto para cada situación.

```
ROL          → Quién eres / qué rol debe adoptar
CONTEXTO     → Situación, fondo, información de fondo
TAREA        → Qué exactamete quieres que haga
CONSTRAINTS  → Restricciones, límites, qué NO hacer
OUTPUT       → Formato esperado, estructura del resultado
EVIDENCE     → Ejemplos, referencias, inputs concretos
```

### Ejemplo rápido con la fórmula CO-STAR:

```
CONTEXTO    → "Somos una startup B2B SaaS de project management..."
ROL         → "Actúa como un copywriter de respuesta directa..."
TAREA       → "Escribe 5 variaciones del headline hero..."
A (Acción)  → "Cada headline debe incluir el beneficio principal..."
S (Estructura) → "Máximo 10 palabras por headline..."
T (Target)  → "...para CTOs y Engineering Managers..."
R (Resultado) → "Devuélveme en formato JSON con headline y razón de cada uno"
```

---

## 3. Técnicas de Prompting Específicas para Claude Code

### 3.1 Few-Shot Prompting (El Más Poderoso)

Incluir 3-5 ejemplos de entrada→salida es la técnica más fiable para obtener el formato, tono y estructura exactos. Usa etiquetas XML:

```
<examples>
  <example>
    <input>Quiero perder peso rápido</input>
    <output>Headline: "Pierde 5kg en 30 días — sin dieta extrema"</output>
  </example>
  <example>
    <input>Aprendo mejor con un tutor personal</input>
    <output>Headline: "Tu tutor personal de idiomas, disponible 24/7"</output>
  </example>
</examples>
```

**¿Por qué funciona?** Los ejemplos superan a las explicaciones. Si quieres formato JSON, pon un ejemplo JSON. Si quieres tono casual, pon un ejemplo casual.

### 3.2 Contexto Antes de la Pregunta

Para tareas complejas con documentos largos o inputs extensos:

```
PONER PRIMERO → Contexto largo, datos, documentos, código
AL FINAL      → La instrucción específica
```

Este cambio puede mejorar los resultados hasta un **30%** en tareas multi-documento.

### 3.3 Extended Thinking para Problemas Complejos

Para tareas de debugging, arquitectura o decisiones complejas:

```
"Antes de escribir código, piensa paso a paso sobre [problema]. 
Explica tu razonamiento, luego implementa."
```

Esto fuerza a Claude a razonar en voz alta antes de generar, reduciendo errores.

### 3.4 Constraints Negativos (Tan Importantes como los Positivos)

```
✅ DOs         → "Usa Tailwind CSS, incluye tests, mobile-first"
❌ DON'Ts      → "NO uses Bootstrap, NO generes código sin tests, 
                  NO hagas cambios en archivos que no estén en la lista"
```

Incluir explícitamente qué NO hacer elimina iteraciones por sorpresas.

### 3.5 Chain of Density (Para Contenido)

Cuando necesitas contenido denso y completo:

```
"Pide lo mismo 3 veces en un solo prompt: primero versión ligera, 
luego versión expandida, finalmente versión densa. Devuelve las 3."
```

### 3.6 Self-Critique Iterativo

Para resultados de alta calidad sin ir y venir:

```
"1. Genera una primera versión
2. Identifica los 3 puntos más débiles
3. Reescribe enfocado en esos puntos
4. Devuelve versión final + lista de mejoras hechas"
```

---

## 4. El Archivo CLAUDE.md — Tu Palanca de Alto Impacto

El archivo `CLAUDE.md` (o `AGENTS.md`) en la raíz de tu proyecto es el punto de palanca más grande para influencer el comportamiento de Claude Code.

### Qué Include (Idealmente < 300 líneas)

```
1. PROJECT OVERVIEW     → Qué es, para quién, propósito
2. TECH STACK           → Stack completo: lenguaje, framework, DB, etc.
3. ARCHITECTURE         → Estructura de carpetas, patrones de diseño
4. CODING CONVENTIONS   → Naming, estilos, imports, manejo de errores
5. UI RULES             → Sistema de diseño, patrones de componentes
6. WORKFLOW             → Cómo trabajar: git flow, testing, build commands
7. COMMANDS             → Comandos exactos: dev server, build, test, deploy
8. TESTING STANDARDS    → Qué tests, cobertura mínima, cómo correrlos
9. FILE PLACEMENT       → Dónde van los nuevos archivos
10. RED LINES           → Cosas que NO hacer
```

### Progressive Disclosure con @imports

```
@import docs/testing.md
@import docs/deployment.md
@import docs/api-conventions.md
```

Mantiene el CLAUDE.md principal limpio (~50-100 líneas) y detalla en archivos separados.

### Consejo Clave

> Un CLAUDE.md con 50 líneas enfocadas supera a uno con 500 líneas de todo. **Menos es más.**

---

## 5. Estructura de Prompts por Tipo de Tarea

### Landing Page / Web
```
ROL: copywriter especialista en conversión
CONTEXTO: producto, audiencia, propuesta de valor única
TAREA: escribir [sección específica o página completa]
CONSTRAINTS: palabras clave SEO, tono, límite de caracteres
OUTPUT: headline + subheadline + bullets + CTA
EVIDENCE: ejemplos de páginas que funcionan en tu industria
```

### Email Marketing
```
ROL: email marketer con experiencia en [industria]
CONTEXTO: objetivo de campaña, audiencia, etapa del funnel
TAREA: escribir secuencia de [N] emails
CONSTRAINTS: largo, tono, urgencia, CTA específico
OUTPUT: subject lines + preview text + body + CTA
FRAMEWORK: usar AIDA, PAS o BAB según el objetivo
```

### App / Código
```
ROL: [full-stack / frontend / backend] developer
CONTEXTO: stack, arquitectura existente, propósito de la feature
TAREA: implementar [feature específica]
CONSTRAINTS: tech stack, tests requeridos, mobile-first, 
             NO usar [lista], archivos只能在[X]目录
OUTPUT: código + tests + documentación inline
VERIFICATION: cómo verificar que funciona
```

### Contenido Blog / SEO
```
ROL: content strategist + SEO specialist
CONTEXTO: topic, keyword objetivo, audiencia
TAREA: escribir artículo completo optimizado para SEO
CONSTRAINTS: palabras clave, headers H2/H3, largo (X palabras)
OUTPUT: título SEO, meta description, introducción, 
        cuerpo con headers, conclusión, FAQ schema
EVIDENCE: 3 artículos top-ranking para referencia de estilo
```

---

## 6. Errores Comunes que Cuestan Iteraciones

| ❌ Error | ✅ Solución |
|---|---|
| "Haz algo bonito" | "Diséña con paleta #FF6B35 + #1A1A2E, tipografía Inter, cards con shadow-sm" |
| "Incluye lo importante" | "Incluye: pricing, FAQ, testimonios. Excluye: animations complejas" |
| "El tono profesional" | "Tono: formal pero accesible, frases cortas, sin jerga técnica" |
| "类似 a Stripe" | "El flujo de checkout similar a Stripe: 3 pasos, progress bar, form validation inline" |
| "Que funcione bien" | "Mobile-first, Lighthouse score >90, Core Web Vitals pass, sin console errors" |

---

## 7. Prompts de Meta-Prompting

Cuando no sabes cómo estructurar el prompt:

```
"Analiza esta solicitud y reescríbela como un prompt 
profesional completo con: rol, contexto, tarea, constraints, 
output format, y 2 ejemplos. 

Solicitud: [tu idea aquí]"
```

Esto te da el template sobre el cual iterar antes de enviar a Claude Code.

---

## 8. ClawHub Skills Relevantes Encontrados

Después de buscar en ClawHub, estos skills son relevantes para prompting:

- **`prompt-enhancer`** — Reescribe inputs rough en prompts optimizados (prefijo "p:")
- **`prompt-architect`** — Arquitectura de prompts avanzada
- **`openclaw-claude-code`** — Claude Code Agent wrapper skill
- **`apex-stack-claude-code`** — APEX STACK para Claude Code autonomous agent
- **`coding-agent`** — Framework para delegar tareas de código a Claude Code

**Recomendación:** Instalar `prompt-enhancer` y `openclaw-claude-code`:

```bash
clawhub install prompt-enhancer
clawhub install openclaw-claude-code
```

---

*Última actualización: 2026-04-05*
*Basado en: Anthropic best practices, Claude Code docs, Arize, Humanlayer, y análisis de patrones probados en producción.*
