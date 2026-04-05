# 🏠 VYNXE — Master Dashboard

> Tecnología de automatización con IA accesible para pymes chilenas

**Última actualización:** 4 de abril de 2026, 01:40 UTC  
**Estado general:** Pre-validación — VYNXE Inmobiliarias  
**Próximo hito:** 5 entrevistas de validación + 2 pilotos (vertical Inmobiliarias)

---

## 📊 Health Check del Proyecto

| Área | Estado | Completado |
|------|--------|------------|
| 🔬 Investigación | ✅ Completo | 100% |
| 📄 Producto | ✅ Completo | 100% |
| 💼 Comercial | ✅ Completo | 100% |
| 📧 Outreach | ⚠️ Listo | 90% (falta enviar) |
| 🌐 Marketing Digital | ✅ Completo | 100% |
| 🤖 Automatización | ⚠️ Parcial | 70% |
| 🏢 Marca multi-vertical | ✅ Actualizado | 100% |

---

## 🏢 Arquitectura de Marcas

```
VYNXE (marca padre)
├── VYNXE Inmobiliarias  ← VERTICAL ACTIVA (en validación)
├── VYNXE Salud           ← Próxima vertical
├── VYNXE Retail          ← En planeación
└── VYNXE Services       ← En planeación
```

### Verticales — Visión General

| Vertical | Mercado Target | Dolor Principal | Estado |
|----------|---------------|------------------|--------|
| **VYNXE Inmobiliarias** | Corredores de propiedades (1-10) | WhatsApp consume noches y fines de semana | 🟢 En validación |
| **VYNXE Salud** | Clínicas, dentistas, psicólogos (1-5 profesionales) | Horas confirmando citas, 30% inasistencias | 🟡 Próxima |
| **VYNXE Retail** | Tiendas retail (1-10 empleados) | Pierden ventas por no responder a tiempo | 🟡 Planeación |
| **VYNXE Services** | Peluquerías, talleres, servicios (1-10 personas) | Agenda se empty por falta de confirmación | 🟡 Planeación |

**Stack técnico:** Igual para todas las verticales — WhatsApp Business API + n8n + GPT-4o mini + Google Calendar + Airtable.

**Lo que cambia por vertical:** Flujos de conversación, templates de preguntas, scoring de leads, landing pages, contenido Instagram, emails fríos.

**Lo que se reutiliza:** Infraestructura, marca, pricing (adaptado), template de propuestas, sistema de Outreach.

---

## 📁 Índice de Archivos por Categoría

### 1. 📚 Knowledge Base (Documentación Principal)

| Archivo | Descripción | Estado | Última modificación |
|---------|-------------|--------|---------------------|
| `vynxe-agent.md` | Knowledge base completo del proyecto | ✅ | 2026-04-02 |
| `vynxe-agent-quickref.md` | Quick reference card (cheat sheet) | ✅ | 2026-04-02 |

**Ubicación:** `/home/ubuntu/.openclaw/workspace/`

---

### 2. 🔬 Investigación de Mercado

| Archivo | Contenido | Estado |
|---------|-----------|--------|
| `market-research-chile.md` | Análisis completo del mercado chileno | ✅ |
| `propuesta-mvp-chatbot-corredores.md` | Propuesta comercial profesional | ✅ |
| `validation-kit.md` | Kit de validación con agencias | ✅ |

**Datos clave:**
- TAM: $15-30M USD (~7,000 corredores)
- SAM: $3-6M USD (~800 agencias)
- SOM 18 meses: $60-100K USD (~16 clientes)
- Competidores mapeados: 12 agencias
- Agencias target identificadas: 20

---

### 3. 🤖 Producto: MVP Chatbot

| Archivo | Descripción | Estado |
|---------|-------------|--------|
| `mvp-chatbot-corredores.md` | Flujo de conversación completo | ✅ |

**Características del producto:**
- ✅ Chatbot WhatsApp 24/7 con IA (GPT-4o mini)
- ✅ Detección de propiedad específica vs consulta general
- ✅ Sistema de lead scoring (FIT/INTENT/ENGAGEMENT)
- ✅ Calificación automática (HOT/WARM/COLD)
- ✅ Agendamiento inteligente con Google Calendar
- ✅ Integración con Airtable/CRM

**Stack técnico:**
- WhatsApp Business API
- n8n (orquestador)
- OpenAI GPT-4o mini
- Google Calendar API
- Airtable (CRM)
- Voiceflow (diseño flujos)

---

### 4. 💼 Comercial & Pricing

**Pricing por Vertical:**

| Plan | VYNXE Inmobiliarias | VYNXE Salud | VYNXE Retail | VYNXE Services |
|------|---------------------|-------------|--------------|----------------|
| **Starter** | $199K/mes (1-2) | $199K/mes (1-2) | $149K/mes (1-3) | $149K/mes (1-2) |
| **Pro** ⭐ | $399K/mes (3-8) | $399K/mes (3-5) | $299K/mes (4-10) | $299K/mes (3-7) |
| **Agency** | $799K/mes (9+) | $799K/mes (6+) | $599K/mes (10+) | $599K/mes (8+) |
| Setup | $300-600K | $300-600K | $200-400K | $200-400K |

*Pricing validado para Inmobiliarias. Las demás verticales son estimaciones basadas en mercado.*

**Documentos:**
- Propuesta comercial: `propuesta-mvp-chatbot-corredores.md`

---

### 5. 📧 Outreach & Cold Email System

| Archivo | Descripción | Estado |
|---------|-------------|--------|
| `cold-email-workflow.md` | Documentación del sistema completo | ✅ |
| `cold-email-system/agencias.csv` | Base de 10 agencias priorizadas | ✅ |
| `cold-email-system/templates/email-inicial.txt` | Template email inicial | ✅ |
| `cold-email-system/templates/followup-1.txt` | Template follow-up día 3 | ✅ |
| `cold-email-system/templates/followup-2.txt` | Template follow-up día 7 | ✅ |
| `cold-email-system/templates/breakup.txt` | Template break-up día 14 | ✅ |
| `cold-email-system/scripts/cold-email-sender.sh` | Script envío emails | ✅ |
| `cold-email-system/scripts/follow-up-checker.sh` | Script check follow-ups | ✅ |
| `cold-email-system/airtable/csv-to-airtable.py` | Importar CSV a Airtable | ✅ |
| `cold-email-system/airtable/update-airtable-status.py` | Actualizar estado | ✅ |
| `cold-email-system/airtable/sync-airtable-csv.py` | Sincronización bidireccional | ✅ |

**Agencias en pipeline:**

| Prioridad | Cantidad | Agencias |
|-----------|----------|----------|
| 🔴 ALTA | 4 | GESCOR, Boettcher, URBANI, Houm |
| 🟡 MEDIA | 4 | Altea, Vercetti, Yapur, KiteProp |
| 🟢 BAJA | 2 | HousePricing, Propital |

**Secuencia de contacto:**
- Día 0: Email inicial
- Día 3: Follow-up corto
- Día 7: Valor agregado (caso de éxito)
- Día 14: Break-up email

**Próximo paso:** Crear base Airtable → Importar agencias → Enviar primer lote (5 ALTA)

---

### 6. 🌐 Marketing Digital

#### 6.1 Landing Page (vynxe.cl)

| Archivo | Descripción | Estado |
|---------|-------------|--------|
| `vynxe-landing-inmobiliaria.html` | Landing page completa (~41KB) | ✅ |
| `vynxe-sitemap.xml` | Sitemap para Google Search Console | ✅ |
| `vynxe-robots.txt` | Robots.txt con referencia | ✅ |
| `vynxe-landing-README.md` | Documentación de la landing | ✅ |

**SEO implementado:**
- Title: "VYNXE — Chatbots IA para Corredores de Propiedades | Chile" (58 chars) ✅
- Meta description: 155 chars ✅
- Open Graph tags ✅
- Twitter Cards ✅
- Schema.org: Organization + LocalBusiness + FAQPage ✅
- Keywords: chatbot inmobiliaria chile, automatización corredores ✅

**Estructura de la landing:**
1. Hero — "Tu Asistente Virtual Inmobiliario Que Nunca Duerme"
2. Stats — 24/7, 10x leads, 0 perdidos, 3h ahorradas
3. Problema — WhatsApp explota, respondes tarde, citas se pisan
4. Solución — 6 features específicas inmobiliarias
5. Cómo funciona — 4 pasos sin técnico
6. Pricing — Starter $199K / Pro $399K / Agency $799K
7. Testimonial — Caso Pedro Gómez
8. FAQ — 5 preguntas con Schema markup
9. CTA — Demo gratuita

**Pendiente para deploy:**
- ⏳ Subir archivos al servidor de vynxe.cl
- ⏳ Crear imágenes: og-image.jpg (1200x630), logo.png, favicon.svg
- ⏳ Configurar Google Search Console
- ⏳ Actualizar email y teléfono en Schema

#### 6.2 Estrategia de Contenido 360°

| Archivo | Descripción | Estado | Presupuesto |
|---------|-------------|--------|-------------|
| `vynxe-content-strategy.md` | Estrategia completa v2.0 (fuentes 2026) | ✅ | $650-950 |
| `vynxe-content-strategy-bootstrap.md` | Versión Bootstrap - $50/mes | ✅ | $50/mes |
| `vynxe-content-strategy-ads-only.md` | **$50 PUROS PARA ADS** | ✅ | **$50 ads** ⭐ |
| `vynxe-content-strategy-revision-fuentes.md` | Análisis de fuentes 2026 | ✅ | - |
| `vynxe-instagram-contenido.md` | Contenido Semana 1 específico | ✅ | - |
| `lead-magnets/` | Carpeta con 4 recursos gratuitos | ✅ | - |

**🎯 ESTRATEGIA ACTIVA: $50 USD PUROS PARA ADS**

Los $50 mensuales son exclusivamente para publicidad pagada. Las herramientas (Canva, hosting) se usan en versión gratuita.

**Filosofía: "Sniper, no shotgun"**
- Con $50/mes (~$1.67/día) no se pueden tener ads 24/7
- Se usan en micro-campañas estratégicas de alto impacto
- Una campaña intensa de 5 días vale más que ads dispersos

**Estrategia Mensual Rotativa:**
- **Mes 1:** "One Big Shot" - $50 en 5 días ($10/día) en 1 Reel ultra-targeteado
- **Mes 2:** "Dos Campañas" - $25 awareness + $25 conversión con retargeting
- **Mes 3:** "LinkedIn InMails" - 10-15 InMails B2B directo

**Metas realistas 90 días ($50 ads + orgánico + outreach):**
- 8-12 leads generados
- 150-250 seguidores Instagram
- 100-200 visitas/mes
- 2-3 pilotos potenciales

**Distribución $50/mes:**
- Todo para ads: $50 (sin herramientas)
- Canva: Gratis | Hosting: Usar existente | Email: Gratis

**Lead Magnets creados (todos sin jerga técnica):**

| # | Lead Magnet | Archivo | Formato | Propósito |
|---|-------------|---------|---------|-----------|
| 1 | Checklist: "¿Tu Agencia Está Dejando Pasar Clientes?" | `vynxe-checklist-dejando-pasar-clientes.md` | PDF (pendiente) | Auto-evaluación 15 preguntas |
| 2 | Calculadora: "¿Cuánto Dinero Dejas de Ganar?" | `vynxe-calculadora-dinero-perdido.md` | Sheets/PDF (pendiente) | Descubre pérdidas mensuales |
| 3 | Guía: "Cómo Atender Clientes Rápido..." | `vynxe-guia-atender-clientes-rapido.md` | PDF (pendiente) | Guía completa 20+ páginas |
| 4 | Templates: "10 Mensajes Listos para WhatsApp" | `vynxe-10-mensajes-whatsapp.md` | PDF/Doc (pendiente) | Copiar y pegar |

**Lenguaje usado (cero jerga técnica):**
- ❌ "Lead" → ✅ "cliente potencial"
- ❌ "Automatización" → ✅ "sistema de respuestas"
- ❌ "Chatbot" → ✅ "asistente virtual"
- ❌ "Cualificación" → ✅ "separar a los que quieren comprar"

**Resumen estrategia:**
- **Fase 1 (Semanas 1-4):** Fundamentos — Instagram + Blog SEO
- **Fase 2 (Semanas 5-8):** Escalamiento — Duplicar frecuencia + Email marketing
- **Fase 3 (Semanas 9-12):** Autoridad — Thought leadership + Webinars

**Pilares de contenido:**
1. Educación (40%) — Cómo funciona la IA para inmobiliarias
2. Problema-Solución (30%) — "¿Te pasa que...?"
3. Social Proof (20%) — Casos y testimonios
4. Comunidad (10%) — Cultura y detrás de cámaras

**Buyer Personas definidos:**
- **Pedro:** Corredor independiente (1-3 corredores), 35-50 años
- **María:** Gerente de agencia mediana (5-15 corredores)

**Canales prioritarios:**
- 🔴 Instagram (diario) — Awareness + Engagement
- 🔴 Blog SEO (2x semana) — Tráfico orgánico
- 🟡 LinkedIn (2x semana) — B2B + Authority
- 🟡 Email newsletter (semanal) — Nurturing

**Metas 90 días:**
- 50 leads generados
- 500 seguidores Instagram
- 500 visitas/mes orgánicas
- 200 email subscribers
- >5% engagement rate

#### 6.3 Instagram (@vynxe.cl)

**Perfil optimizado:**
- Username: @vynxe.cl
- Nombre: VYNXE | IA para Inmobiliarias
- Bio: 🤖 Chatbots IA para Corredores de Propiedades / 📍 Chile / Especialistas inmobiliarios

**5 Reels creados:**
1. **Mistake Hook** — "Deja de hacer esto ☠️" (45-60s)
2. **Secret Hook** — "3 herramientas que las agencias no quieren que sepas" (30-45s)
3. **Story+Result** — Caso Pedro: 12→120 leads/día (60-75s)
4. **Tutorial** — "Cómo crear un chatbot en 3 pasos" (60-90s)
5. **Mito vs Realidad** — Desmintiendo prejuicios (30-45s)

**Calendario Semana 1:**
- 3 Reels (Lun, Mié, Jue)
- 2 Carousels (Mar, Sáb)
- 7 Stories diarios

**Métricas objetivo:**
- Followers: 50-100
- Views por Reel: 500+
- DMs: 5+

**Pendiente:**
- ⏳ Crear cuenta de Instagram
- ⏳ Configurar perfil (foto, bio, link)
- ⏳ Crear/editar videos
- ⏳ Publicar contenido

---

### 7. 🛠️ Skills Instaladas

#### Producto & Automatización
- ✅ `client-intake-bot-pro`
- ✅ `voiceflow`
- ✅ `whatsapp-business-api`
- ✅ `n8n`
- ✅ `lead-scorer`

#### Marketing & Ventas
- ✅ `instagram-agent`
- ✅ `social-media-marketing`
- ✅ `website-seo`
- ✅ `seo-optimizer`
- ✅ `proposal-writer`
- ✅ `follow-up-sequence-writer`
- ✅ `sales-pipeline-tracker`

#### Utilidades
- ✅ `markdown`
- ✅ `send-email`
- ✅ `airtable-automation`
- ✅ `airtable-integration`

**Total skills:** 16

---

## 🎯 Próximos Pasos (Priorizados)

### Esta semana (Semana 1)

| # | Tarea | Prioridad | Tiempo estimado | Estado |
|---|-------|-----------|-----------------|--------|
| 1 | Crear cuenta Airtable + importar agencias | 🔴 Alta | 20 min | ⏳ Pendiente |
| 2 | Enviar primer lote cold emails (5 ALTA) | 🔴 Alta | 30 min | ⏳ Pendiente |
| 3 | Crear cuenta Instagram + configurar perfil | 🟡 Media | 15 min | ⏳ Pendiente |
| 4 | Subir landing page a vynxe.cl | 🟡 Media | 30 min | ⏳ Pendiente |
| 5 | Crear imágenes para landing (og-image, logo) | 🟡 Media | 1 hora | ⏳ Pendiente |
| 6 | Configurar Google Search Console | 🟢 Baja | 15 min | ⏳ Pendiente |

### Semana 2-3

| # | Tarea | Prioridad |
|---|-------|-----------|
| 7 | Completar 5 entrevistas de validación | 🔴 Alta |
| 8 | Agendar reuniones con interesados | 🔴 Alta |
| 9 | Publicar contenido Instagram semanal | 🟡 Media |
| 10 | Enviar segundo lote cold emails | 🟡 Media |

### Decisión Go/No-Go (Fin Semana 3)

**GO si:**
- ✅ ≥3 entrevistados muestran interés genuino
- ✅ ≥2 aceptan piloto a $399K

**NO-GO si:**
- ❌ <2 entrevistados califican HOT
- ❌ Nadie acepta pagar >$200K/mes

---

## 📊 Pipeline de Agencias (Visual)

```
┌─────────────────────────────────────────────────────────────┐
│                    PIPELINE VINXE                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  🔴 POR_CONTACTAR        [████████░░] 10 agencias           │
│     ├─ ALTA: GESCOR, Boettcher, URBANI, Houm               │
│     ├─ MEDIA: Altea, Vercetti, Yapur, KiteProp             │
│     └─ BAJA: HousePricing, Propital                        │
│                                                             │
│  🟡 EMAIL_ENVIADO        [░░░░░░░░░░] 0 agencias            │
│                                                             │
│  🟠 RESPONDIO            [░░░░░░░░░░] 0 agencias            │
│                                                             │
│  🔵 REUNION_AGENDADA     [░░░░░░░░░░] 0 agencias            │
│                                                             │
│  🟣 PILOTO_PROPUESTO     [░░░░░░░░░░] 0 agencias            │
│                                                             │
│  🟢 PILOTO_ACEPTADO      [░░░░░░░░░░] 0 agencias            │
│                                                             │
│  ⭐ CLIENTE              [░░░░░░░░░░] 0 agencias ($0 MRR)    │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  Meta: 2 pilotos en 3 semanas                               │
│  Revenue potencial: $798K CLP/mes                           │
└─────────────────────────────────────────────────────────────┘
```

---

## 📞 Contactos y Recursos

### Presencia Digital
- **Web:** https://vynxe.cl
- **Instagram:** @vynxe.cl (pendiente crear)
- **Email:** hola@vynxe.cl
- **Email operativo:** disenoxplain@gmail.com

### Herramientas Configuradas
- **Email SMTP:** Gmail (disenoxplain@gmail.com)
- **Airtable:** Pendiente crear base
- **Git:** Respaldo automático configurado

### Git Commits Recientes
- `cde4cd7` — Update VYNXE Agent: landing + Instagram + skills nuevas
- `99ad967` — Agencia IA + Cold Email System + Airtable integration
- `74b2544` — Add VYNXE Agent knowledge base

---

## 💡 Insights Clave (No olvidar)

1. **Corrección importante:** El flujo debe detectar SIEMPRE si el lead viene de propiedad específica vs consulta general
2. **Diferenciador:** Somos especialistas inmobiliarios, no agencia genérica de IA
3. **Preferencias de Ader:** Actuar directamente, sistemas completos, documentación clara
4. **Pricing validado:** $399K es el sweet spot (Pro plan)
5. **Stack aprobado:** n8n + GPT-4o + WhatsApp + Airtable

---

## 🚨 Reglas Anti-Spam (Recordatorio)

- Rate limiting: Máximo 20 emails/hora, 50/día
- Personalización: Mínimo 3 variables por email
- Horario: Solo 9 AM - 6 PM hora Chile
- Unsubscribe: Incluir en follow-ups
- No re-enviar a "no interesado"

---

## 📚 Archivos de Marca (Actualizados Abril 2026)

| Archivo | Descripción | Última modificación |
|---------|-------------|---------------------|
| `vynxe-brand-identity.md` | Guía de identidad completa — 4 verticales | 2026-04-04 |
| `vynxe-voice-guide.md` | Guía de voz por canal y vertical | 2026-04-04 |
| `VINXE-DASHBOARD.md` | Este archivo — índice maestro | 2026-04-04 |

### Navegador de Verticales

| Vertical | Landing | Email Frío | Instagram | Flujo Bot |
|----------|---------|------------|-----------|-----------|
| Inmobiliarias | ✅ Listo | ✅ Listo | ✅ Listo | ✅ Listo |
| Salud | ⏳ Pendiente | ✅ Listo | ✅ Listo | ✅ Listo |
| Retail | ⏳ Pendiente | ✅ Listo | ✅ Listo | ✅ Listo |
| Services | ⏳ Pendiente | ✅ Listo | ✅ Listo | ✅ Listo |

| Vertical | Landing | Email Frío | Instagram | Flujo Bot |
|----------|---------|------------|-----------|-----------|
| Inmobiliarias | ✅ Listo | ✅ Listo | ✅ Listo | ✅ Listo |
| Salud | ⏳ Pendiente | ✅ Listo | ✅ Listo | ✅ Listo |
| Retail | ⏳ Pendiente | ✅ Listo | ✅ Listo | ✅ Listo |
| Services | ⏳ Pendiente | ✅ Listo | ✅ Listo | ✅ Listo |

---

## 📝 Notas de la Sesión Actual

**Fecha:** 2 de abril de 2026  
**Logros:**
- ✅ Landing page inmobiliaria completa con SEO
- ✅ Contenido Instagram Semana 1 (5 Reels)
- ✅ 4 skills nuevas instaladas (website-seo, instagram-agent, etc.)
- ✅ Knowledge base actualizado con toda la info
- ✅ Git commit con todos los cambios

**Próxima sesión:**
- Pendiente: Airtable + primer envío de emails

---

*Dashboard generado automáticamente por CyberClaw*  
*Para actualizar: Editar VINXE-DASHBOARD.md*
