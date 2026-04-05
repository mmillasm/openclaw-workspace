# 🎯 TEMPLATE: Anatomía de un Prompt para Claude Code

> **Fuente:** Template oficial de Anthropic para Claude Code  
> **Uso:** Estructura completa para prompts profesionales que generen resultados de alta calidad

---

## 📋 Estructura Completa del Prompt

### 1. TASK (Tarea) 🔴
```
I want to [TASK] so that [SUCCESS CRITERIA].

First, read these files completely before responding:
```

**Ejemplo:**
```
I want to create a landing page for a real estate AI chatbot so that 
corredores de propiedades in Chile understand the value proposition 
and request a demo.

First, read these files completely before responding:
```

---

### 2. CONTEXT FILES (Archivos de Contexto) 🔵
```
[filename.md] — [what it contains]
[filename.md] — [what it contains]
[filename.md] — [what it contains]
```

**Ejemplo:**
```
vynxe-brand-identity.md — Brand guidelines, colors, typography, voice
vynxe-prompt-maestro.md — Complete marketing context for VYNXE
vynxe-design-tokens.css — CSS variables for styling
```

---

### 3. REFERENCE (Referencia) 🟢
```
Here is a reference to what I want to achieve:

[Upload reference file as markdown, or paste it here]

Here's what makes this reference work:

[Paste your reverse-engineered blueprint - the patterns, tone, 
structure, and rules you extracted from the reference. Format 
each one as a rule starting with "Always" or "Never."]
```

**Ejemplo:**
```
Here is a reference to what I want to achieve:

[Paste landing page de stripe.com]

Here's what makes this reference work:

- Always use clear, benefit-driven headlines
- Never use jargon or technical terms without explanation
- Always have a single primary CTA above the fold
- Never clutter the hero section with multiple messages
```

---

### 4. SUCCESS BRIEF (Brief de Éxito) 🟡
```
Here's what I need for my version:

SUCCESS BRIEF
Type of output + length:
[Contract, memo, report, proposal, landing page, post?]

Recipient's reaction:
[What should they think/feel/do after reading?]

Does NOT sound like:
[What to avoid - generic AI, too casual, formal, jargon-heavy?]

Success means:
[They sign? They approve? They reply? They take action?]
```

**Ejemplo:**
```
SUCCESS BRIEF
Type of output + length:
Landing page, 5-7 sections, mobile-first

Recipient's reaction:
"This is exactly what my agency needs" → Click "Agendar Demo"

Does NOT sound like:
- Generic AI text
- Corporate buzzwords ("synergy", "leverage")
- Overpromising ("10x your sales overnight")

Success means:
They understand the problem, see the solution, and request a demo call
```

---

### 5. RULES (Reglas) 🩷
```
My context file contains my standards, constraints, landmines, 
and audience. Read it fully before starting. If you're about to break 
one of my rules, stop and tell me.
```

**Ejemplo:**
```
My vynxe-brand-identity.md contains my standards, constraints, 
landmines, and audience. Read it fully before starting. If you're 
about to break one of my rules, stop and tell me.
```

---

### 6. CONVERSATION (Conversación) 🟣
```
DO NOT start executing yet. Instead, ask me clarifying questions 
(use 'AskUserQuestion' tool) so we can refine the approach 
together step by step.
```

---

### 7. PLAN (Plan) 🟠
```
Before you write anything, list the 3 rules from my context file that 
matter most for this task.

Then give me your execution plan (5 steps maximum).
Only begin work once we've aligned.
```

---

### 8. ALIGNMENT (Alineación) 🔵
```
[Wait for user approval before proceeding]
```

---

## ✅ EJEMPLO COMPLETO: Landing Page VYNXE

```markdown
I want to create a landing page for VYNXE Inmobiliarias so that 
corredores de propiedades in Chile understand how an AI assistant 
can help them respond to clients 24/7 and request a demo.

First, read these files completely before responding:

vynxe-brand-identity.md — Brand guidelines, colors (#6366F1, #8B5CF6), 
                         typography (Inter), voice (Direct, Useful, Human)
vynxe-prompt-maestro.md — Complete marketing context
vynxe-design-tokens.css — CSS variables and component styles

Here is a reference to what I want to achieve:

[Paste stripe.com landing page structure]

Here's what makes this reference work:

- Always start with a provocative question that resonates with the pain point
- Always use clear, scannable sections with single focus
- Never use more than 2 CTAs per section
- Always show pricing transparently
- Never use stock photos of people smiling at camera
- Always use real screenshots or authentic product images

Here's what I need for my version:

SUCCESS BRIEF
Type of output + length:
Single HTML landing page, 5-7 sections, responsive, SEO-optimized

Recipient's reaction:
"I waste too much time on WhatsApp" → "This could solve it" → 
"I want to see how it works" → Click "Agendar Demo"

Does NOT sound like:
- Generic AI marketing text
- "Revolutionary AI technology" (too buzzwordy)
- "Chatbot" (use "asistente virtual")
- "Leads" (use "clientes potenciales")

Success means:
They understand the WhatsApp problem, see VYNXE as the solution, 
and request a 20-minute demo call

My vynxe-brand-identity.md contains my standards, constraints, 
landmines, and audience. Read it fully before starting. If you're 
about to break one of my rules, stop and tell me.

DO NOT start executing yet. Instead, ask me clarifying questions 
so we can refine the approach together step by step.

Before you write anything, list the 3 rules from my context file that 
matter most for this task.

Then give me your execution plan (5 steps maximum).
Only begin work once we've aligned.
```

---

## 💡 CLAVES DE ESTE TEMPLATE

### Por qué funciona:

1. **Task claro:** Define qué hacer y por qué (success criteria)
2. **Contexto completo:** No asume conocimiento, provee archivos
3. **Referencia visual:** Muestra qué se quiere lograr (no solo describirlo)
4. **Reverse engineering:** Extrae patrones de la referencia
5. **Success brief detallado:** Define qué éxito significa
6. **Reglas explícitas:** Contexto de constraints y "landmines"
7. **Conversación antes de acción:** Alinha antes de ejecutar
8. **Plan con límites:** Máximo 5 pasos, alineación requerida

### Errores comunes que evita:

- ❌ Empezar a escribir sin entender el contexto
- ❌ Asumir qué quiere el usuario
- ❌ No tener criterios claros de éxito
- ❌ Ignorar reglas de marca/voz
- ❌ Entregar sin validar alineación

---

## 🔄 FLUJO DE TRABAJO CON ESTE TEMPLATE

```
1. USER crea prompt con esta estructura
           ↓
2. CLAUDE lee archivos de contexto
           ↓
3. CLAUDE pregunta dudas (AskUserQuestion)
           ↓
4. CLAUDE presenta: 3 reglas clave + plan de 5 pasos
           ↓
5. USER aprueba o ajusta
           ↓
6. CLAUDE ejecuta
           ↓
7. USER revisa y da feedback
```

---

*Template: The Anatomy of a Claude Prompt*  
*Añadido a conocimiento: 5 de abril de 2026*
