# Investigación de Estrategias de Precios para VYNXE

> **Fecha de investigación:** Abril 2025  
> **Enfoque:** Agente virtual inmobiliario con IA para corredores de propiedades en Chile  
> **Pricing actual:** Starter $199K/mes · Pro $399K/mes · Agency $799K/mes

---

## 1. Análisis del Mercado Competitivo

### 1.1 Contexto del mercado chileno de IA inmobiliaria

El mercado SaaS en Latinoamérica crece a un **CAGR del 25%** (2024–2030), superando a Europa y Asia. Chile concentra 618 empresas SaaS activas. La adopción de IA en corretaje inmobiliario chileno está en expansión, con herramientas que automatizan hasta el **40% de las tareas** del corredor.

El posicionamiento de VYNXE como "asistente virtual inmobiliario que atiende 24/7, separa curiosos de compradores reales y agenda visitas automáticamente" lo diferencia por su **enfoque vertical especializado** (no es un chatbot genérico) y por resolver un dolor concreto: el desperdicio de tiempo del corredor en leads no calificados.

### 1.2 Comparativa de precios con competidores

Todos los precios están normalizados a **USD/mes** para comparación directa (tipo de cambio aproximado: 1 USD ≈ 950 CLP en abril 2025).

| Competidor / Herramienta | Plan más bajo | Plan medio | Plan alto | Especialización Inmobiliaria |
|---|---|---|---|---|
| **VYNXE (actual)** | **$199K CLP ≈ $209 USD** | **$399K CLP ≈ $420 USD** | **$799K CLP ≈ $841 USD** | **Sí — nicho corredores** |
| **MightyChat.ai** | $0 (30 msgs/mes) | $30 USD (2K msgs, 1 agente) | $399 USD (40K msgs, 10 agentes) | Sí (genérico configurable) |
| **Asyntai** | $0 (100 msgs) | $39 USD (2.5K msgs) | $449 USD (plan Pro) | Parcial — ventas inmobiliarias |
| **Cliengo** | $16.750 CLP ≈ $18 USD (básico) | $44.667 CLP ≈ $47 USD (profesional) | $83.750 CLP ≈ $88 USD (full) | Parcial — "Ignacio" en beta |
| **RentOso** | No disclosed | Suscripción modular por módulo | No disclosed | Sí — "brigada de osos" IA |
| **Lidz.ai** | No disclosed | No disclosed | No disclosed | Sí — prospección inmobiliaria |
| **Softimizados.cl** | ~$60 USD estimado | ~$120 USD estimado | ~$300+ USD | Chatbots + IA genéricos |
| **Chatbot Chile** | ~$10 USD estimado | ~$50 USD estimado | ~$150+ USD | Genérico |
| **Sierra Interactive (Global)** | $299 USD/mes | $449 USD/mes | $599 USD/mes | Sí, pero CRM completo |

**Observaciones clave:**

- **VYNXE es competitivo en precio.** Su plan Starter ($209 USD) está por debajo del punto de entrada de herramientas globales especializadas ($299–$399 USD).
- **MightyChat.ai tiene precios más bajos** ($30 USD/mes) pero no es específicamente inmobiliario ni incluye agendamiento automático completo.
- **Cliengo** es el competidor más establecido en Chile con precios locales (~$18–$88 USD/mes), pero su agente inmobiliario "Ignacio" aún está en beta.
- **RentOso** es el competidor más similar en propuesta (copiloto IA especializado en corredores), pero no publica precios — likely premium.
- La franja de **$200–$500 USD/mes** es el sweet spot para corredores de propiedades medianos en Chile.

### 1.3 Benchmark: Pricing de chatbots IA para ventas en LatAm

- **Planes básicos WhatsApp/IA:** $10–$60 USD/mes (genéricos, volumen bajo)
- **Planes profesionales:** $100–$300 USD/mes (con integraciones, CRM, múltiples canales)
- **Planes empresariales:** $400–$1,000+ USD/mes (equipos, API, custom)

**VYNXE se posiciona en el rango profesional-alto ($420 USD Pro), lo cual es apropiado para corredores que ya entienden el ROI de la automatización.** Sin embargo, el plan Starter ($209 USD) está diseñado para ser el point de entrada, lo cual es estratégico.

---

## 2. Modelos de Pricing: Análisis Comparativo

### 2.1 Modelos utilizados en el mercado

| Modelo | Descripción | Ventajas | Desventajas | ¿Sirve para VYNXE? |
|---|---|---|---|---|
| **Por mensaje/conversación** | $X por cada interacción | Predictible para el cliente | Penaliza uso alto; complejo de escalar | ⚠️ Complejo — mejor como add-on |
| **Por usuario/agente** | $X por agente虚拟 o usuario | Simple, predecible | No incentiva uso intensivo | ✅ Bueno — VYNXE ya lo usa parcialmente |
| **Por tier fijo (Good-Better-Best)** | 3 planes con features diferenciadas | Ancla el valor, facilita decisión | Puede confundir si hay muchos features | ✅ Excelente — modelo actual de VYNXE |
| **Freemium** | Plan gratuito limitado + pago por más | Adquisición masiva, bajo friction | Conversión baja (2–5%), mucho soporte gratis | ⚠️ Riesgo — muchos leads no calificados |
| **Free trial** | Acceso completo limitado (7–30 días) | Alta conversión (15–50%), filtra leads | Requiere buen onboarding | ✅ Recomendado — ver sección 4 |
| **Usage-based / hybrid** | Fee fijo + $X por uso adicional | Justo para ambos lados | Complejidad, difícil de predecir para el cliente | ⚠️ Posible para Agency tier |
| **Value-based** | Precio según el valor entregado ($/lead calificado, % de cierre) | Maximiza revenue, alinea incentivos | Difícil de medir y vender | ⚠️ Interesante como add-on premium |
| **Perpetual/license** | Pago único + mantenimiento | Revenue inmediato | No recurrente — contra natura para SaaS | ❌ No recomendado |

### 2.2 Recomendación para VYNXE

**Modelo recomendado: Tiered Pricing (Good-Better-Best) + Free Trial + Annual Discount**

Este es el modelo actual de VYNXE y es correcto. Los ajustes sugeridos están en la sección 3.

**Estructura actual vs. sugerida:**

| Atributo | Actual (VYNXE) | Sugerido |
|---|---|---|
| Modelo base | Tiered fijo (3 tiers) | Mantener — es correcto |
| Trial | No mencionado | **14 días free trial** sin tarjeta |
| Facturación | ¿Mensual + anual? | Anual con **20% descuento** |
| Modelo usage | No | Add-on por conversaciones extra en Starter |
| Onboarding | No mencionado | **Onboarding guiado** incluido en Pro+ |

---

## 3. Estrategias de Penetration Pricing

### 3.1 Análisis de posición actual

VYNXE ya usa **penetration pricing** parcialmente:
- Sus precios ($209–$841 USD/mes) están **por debajo** de competidores globales ($299–$1,000+ USD/mes)
- Esto es agresivo y adecuado para un mercado en adopción temprana

### 3.2 Estrategias recomendadas

#### A) Precio de lanzamiento (Launch Pricing) — Corto plazo (0–6 meses)

**Objetivo:** Adquirir los primeros 20–50 clientes de referencia.

- **Oferta "Founder":** Starter a **$149K CLP/mes** ($157 USD) en vez de $199K — limitado a los primeros 20 clientes con contrato anual prepagado.
- **Equivalente Pro "Founder":** $299K/mes ($315 USD) en vez de $399K.
- **¿Por qué funciona?** Los early adopters de Chile valoran el status de "cliente fundador" y son evangelizadores. Esto genera casos de éxito que justifican precio completo después.
- **Regla:** Solo para clientes que firmen **contrato anual** (asegura LTV mínimo de $1,800 USD por cliente).

#### B) Precio de segmento (Geographic/Segment Penetration) — Mediano plazo

**Objetivo:** Dominar el segmento de corredores individuales en Chile.

- **Lanzar un plan "Solo":** $99K CLP/mes ($104 USD) — 1 agente, 500 conversaciones/mes, 1 canal (WhatsApp), agendamiento básico.
  - Esto captura el corredor que hoy paga $0 (usa WhatsApp manual) y está dispuesto a pagar hasta ~$100 USD.
  - Conversión esperada: 3–5% de leads que no convierten a Starter.
- **Premium para agencies locales:** Agency a **$599K/mes** ($631 USD) con 3 corredores incluidos + API access — apunta a inmobiliarias boutique que hoy pagan $800K+ en herramientas desarticuladas.

#### C) Bundling estratégico — Largo plazo

- **Bundle "Trimestre Inmobiliario":** 3 meses de Pro por el precio de 2.5 ($399K × 2.5 = $998K en vez de $1.197K) — elimina la fricción del compromiso anual.
- **Bundle "Onboarding Pack":** Pro + configuración inicial + 3 plantillas de propiedades por $499K/mes (vs. $399K + costo de setup). Reduce el "esfuerzo de empezar" que bloquea a muchos corredores.

### 3.3 Riesgo del penetration pricing y cómo mitigarlo

| Riesgo | Mitigación |
|---|---|
| Percepción de baja calidad por precio bajo | Comunicar el valor: "automatización premium al precio de un assistant básico" |
| Clientes esperan precio bajo para siempre | Subir precios solo a **nuevos clientes** — existing mantienen rate lock |
| Cannibalización de tiers | El plan "Solo" no compite con Pro — es un funnel de entrada |
| Margen terlalu tipis | Compensar con contrato anual y upsell a 6–12 meses |

---

## 4. Promociones y Descuentos Sugeridos

### 4.1 Descuentos por compromiso (recomendados — alto impacto)

| Descuento | Condición | Impacto en MRR | Beneficio |
|---|---|---|---|
| **20% off anual** | Pago anticipado de 12 meses | Reduces MRR pero aseguras cash flow y reducen churn | Reducción de churn de ~30–50% según benchmarks SaaS |
| **15% off semestral** | Pago anticipado de 6 meses | Menos descuento, más flexible | Útil para clientes que no quieren anuales |
| **10% off trimestral** | Pago anticipado de 3 meses | Mínimo riesgo, máximo appeal | Bueno como promoción estándar |

### 4.2 Promociones de adquisición (para leads nuevos)

| Promoción | Detalle | Ideal para |
|---|---|---|
| **"14 días gratis, sin tarjeta"** | Trial completo de Pro sin credit card | Captura leads que no quieren compromiso upfront. Conversión esperada: 15–25% |
| **"Migrate & Save"** | 2 meses gratis para clientes que migran desde Cliengo/MightyChat | Canibalización positiva de competidores |
| **"Primer mes a $1"** | $1 peso el primer mes + mes 2–12 a precio normal | Elimina fricción de entrada. Requiere contrato anual |
| **"Embajador VYNXE"** | 1 mes gratis por cada cliente referido que contrate | Modelo de referral: CAC bajo, churn bajo |

### 4.3 Promociones estacionales (Chile)

|时机| Promoción | Detalle |
|---|---|---|
| **Marzo (temporada de rentrée)** | "En Marcha con VYNXE" — 15% off para contratos anuales firmados en marzo | Coincide con el reinicio de actividades inmobiliarias |
| **Septiembre (Feria Inmobiliaria)** | 2×1 en el primer año de Agency | Atrae agencies a probar el tier alto |
| **Noviembre–Diciembre** | "Cierra el año con más leads" — Starter gratis hasta enero si contratas antes del 15 de diciembre | Aprovecha el push de cierre anual |
| **Black Friday Chile (última semana de noviembre)** | Descuento del 25% en planes anuales | Aprovecha la cultura de ofertas de fin de año |

### 4.4 Descuentos por volumen (para Agencies con múltiples usuarios)

| Usuarios | Descuento sugerido |
|---|---|
| 1 corredor | 0% (precio lista) |
| 2–3 corredores | 5% off |
| 4–5 corredores | 10% off |
| 6+ corredores | 15% off + onboarding dedicado |

### 4.5 No-discounts (reglas claras para proteger el pricing)

- **Nunca hacer descuentos por "negociación"** — esto destruye el valor percibido. Si un prospecto pide descuento, ofrecer: más features (trial extendido, onboarding adicional) en vez de reducir precio.
- **No hacer descuentos en Agency tier** — es el tier de prestige; el descuento lo devalúa.
- **Los descuentos son por contrato, no por mes** — siempre atados a prepago, nunca sobre la mensualidad.

---

## 5. Pricing Psicológico y Optimización de Tiers

### 5.1 Análisis del modelo actual de VYNXE

```
Starter:  $199K/mes   (?)
Pro:      $399K/mes   (?)
Agency:   $799K/mes
```

**Problemas potenciales:**
1. El salto de Starter a Pro es **2x** ($199K → $399K). Puede sentirse muy grande.
2. Agency ($799K) está justo bajo el umbral psicológico de $1M — correcto.
3. Si Starter = $199K y no hay un tier más bajo visible, se pierde el corredor con menor disposición a pagar ($50–$100K).
4. Si los 3 tiers no tienen features claramente diferenciadas, el "ancla" no funciona.

### 5.2 Optimización: Modelo Good-Better-Best refinado

**Tiers recomendados:**

| Plan | Precio mensual | Precio anual (–20%) | Target | Diferenciador clave |
|---|---|---|---|---|
| **Solo** *(nuevo)* | $99K CLP/mes | $79K CLP/mes | Corredor individual, primer contacto con IA | 1 agente, WhatsApp, respuestas básicas, hasta 300 conversaciones/mes |
| **Starter** | $199K CLP/mes | $159K CLP/mes | Corredor activo que quiere separar curiosos de buyers | 1–2 agentes, WhatsApp + web, agendamiento, hasta 1,000 conversaciones/mes |
| **Pro** | $399K CLP/mes | $319K CLP/mes | Corredor que necesita automatización completa | Agentes ilimitados, múltiples canales, CRM básico, hasta 5,000 conversaciones/mes, integraciones |
| **Agency** | $799K CLP/mes | $639K CLP/mes | Inmobiliaria boutique, equipo de 2–5 corredores | Todo de Pro + múltiples usuarios, API access, reportes avanzados, soporte prioritario |

**¿Por qué 4 tiers?**
- El tier "Solo" funciona como **decoy**: hace que Starter ($199K) se sienta como una "buena compra" en vez de la opción más cara.
- El tier "Agency" se justifica al compararlo con los $799K — es 4× Starter pero ofrece valor desproporcionado.
- El salto Starter → Pro es "solo" $200K extra — se siente alcanzable comparado con Agency.

### 5.3 Anclaje (Anchoring) — Técnica clave

**Estrategia de presentación:**

1. **Mostrar Agency primero** ($799K) — "El paquete completo para inmobiliarias que van en serio"
2. **Pro en segundo lugar** ($399K) — "El favorito de los corredores top — todo lo que necesitas, nada que no uses"
3. **Starter tercero** ($199K) — "Para empezar a automatizar sin complicaciones"
4. **Solo cuarto** ($99K) — "Tu primer paso hacia la automatización"

Esto utiliza el principio de **anclaje**: Agency hace que Pro se vea razonable, y Starter se vea como "ganga".

### 5.4 Nombres de tiers

Evitar nombres genéricos. Sugerencias alineadas con el branding VYNXE:

- Solo → **"VYNXE Esencial"**
- Starter → **"VYNXE Pro上手"** (o "VYNXE Starter")
- Pro → **"VYNXE Elite"**
- Agency → **"VYNXE Team"**

---

## 6. Métricas Clave para Monitorear Pricing

| Métrica | Target | Acción si está fuera de rango |
|---|---|---|
| **Conversion rate trial → paid** | 20–30% | Mejorar onboarding o alargar trial a 21 días |
| **Churn rate mensual** | < 3% | Investigar razones — probablemente pricing o falta de valor percibido |
| **MRR (Monthly Recurring Revenue)** | Crecimiento 15–20% MoM en fase early | Si bajo, revisar pricing o go-to-market |
| **ARPU (Average Revenue Per User)** | Monitorear por tier | Si Starter tiene 70%+ de clientes, subir precios o crear un tier más atractivo |
| **Sales cycle length** | < 14 días | Si > 30 días, simplificar propuesta de valor o crear un tier de entrada más accesible |
| **Discount rate** | < 20% de deals con descuento | Si > 30%, hay un problema de pricing o de sales process |

---

## 7. Roadmap de Implementación Sugerido

### Fase 1: Inmediato (mes 1–2)
- [ ] Lanzar **trial gratuito de 14 días** (sin tarjeta de crédito) en el plan Pro
- [ ] Agregar **descuento anual del 20%** visible en pricing page
- [ ] Crear **landing de migración** ("Viene de Cliengo/MightyChat? Te ayudamos a migrar + 2 meses gratis")

### Fase 2: Corto plazo (mes 3–4)
- [ ] Evaluar lanzar **tier "Solo"** a $99K/mes como funnel de entrada
- [ ] Implementar **"Embajador VYNXE"**: 1 mes gratis por referral
- [ ] A/B test: pricing con y sin el tier intermedio

### Fase 3: Mediano plazo (mes 5–8)
- [ ] Si ARPU de Starter > 60% de revenue, **subir precios un 10–15%** y comunicar como "actualización de features"
- [ ] Introducir **add-ons usage-based**: $5K CLP por cada 500 conversaciones adicionales en Starter/Solo
- [ ] Crear **promociones estacionales** para marzo y septiembre

### Fase 4: Largo plazo (mes 9–12)
- [ ] Evaluar **pricing basado en valor** para Agency: "$X por cada visita agendada que el asistente agende" como alternativa a fee fijo
- [ ] Considerar **pricing por resultado**: descuento en fee mensual a cambio de % sobre leads calificados (modelo performance-based)

---

## 8. Conclusiones y Recomendaciones Prioritarias

### Lo que VYNXE está haciendo bien:
✅ **Ya tiene penetration pricing** — precios por debajo del mercado global  
✅ **Modelo tiered (Good-Better-Best)** es el correcto para SaaS B2B  
✅ **El nicho especializado** (corredores inmobiliarios) justifica pricing premium vs. chatbots genéricos  

### Ajustes prioritarios:
1. **Trial gratuito de 14 días** — es la táctica de mayor ROI inmediato (conversión 15–25%)  
2. **Descuento anual del 20%** — mejora cash flow y reduce churn drásticamente  
3. **Evaluar tier "Solo"** — captura el segmento que hoy no puede pagar $199K y se queda en WhatsApp manual  
4. **Anclar mostrando Agency primero** — usa el tier más alto como palanca psicológica  
5. **Programa de referral** — tu mejor canal de adquisición es un corredor satisfecho hablando con sus colegas  

### Cuidado con:
- ❌ No competir solo en precio — el diferenciador de VYNXE es el **agendamiento automático** y la **especialización inmobiliaria**, no ser el más barato
- ❌ No ofrecer descuentos por negociación — esto destruye el funnel
- ❌ No lanzar freemium — el volumen de soporte gratis destruiría la operación early-stage

---

*Investigación compilada por agente de investigación VYNXE · Abril 2025*  
*Fuentes: MightyChat.ai, Cliengo, Asyntai, RentOso, Sierra Interactive, Paddle, ChartMogul, Baremetrics, PayProGlobal, comparasoftware.cl, superpyme.cl, cnnchile.com, tracxn.com, econosur.org, tuu.cl, chilestarts.com, chartmogul.com, poweredbysearch.com, sixteenventures.com, userpilot.com*
