# Guía Práctica: Derivación a Humano en Asistente Virtual de WhatsApp para Inmobiliarias

> Basada en mejores prácticas de la industria. Contexto: asistente virtual inmobiliario en WhatsApp Business. Idioma: español neutro.

---

## 1. CUÁNDO DERIVAR A UN HUMANO

### Señales claras de derivación obligatoria

- **El cliente lo pide explícitamente.** Frases como "hablar con alguien", "hablar con un agente", "quiero atención personal" → derivar de inmediato, sin preguntas.
- **Consulta sobre temas sensibles.** Negociación de precio, disputas, quejas, temas legales, datos financieros, contratos.
- **Alta intención de compra.** Cuando el cliente dice "quiero agendar una visita", "estoy listo para comprar", "necesito hablar de financiamiento" → derivar al agente de ventas.
- **Frustración o enojo detectado.** Repeticiones de la misma pregunta, mensajes en mayúsculas, palabras negativas ("frustrado", "pésimo", "inaceptable").
- **El bot no entiende después de 2-3 intentos.** Cuando el bot malinterpreta o no puede responder la misma consulta reformulada.
- **Consultas fuera del alcance del bot.** Información no disponible en la base de conocimientos: datos de propiedades no listadas, info legal específica, temas fiscales.
- **Clientes VIP o casos de alto valor.** Inversiones grandes, clientes referidos, situaciones que justifican atención personalizada.
- **Problemas técnicos complejos.** Troubleshooting que requiere acceso a sistemas o configuraciones fuera del alcance del bot.

### Señales de alta intención (prioridad de derivación)

| Señal | Acción |
|---|---|
| "Quiero agendar una visita" | Derivar inmediatamente a ventas |
| "Necesito hablar con alguien HOY" | Derivar con prioridad alta |
| "Estoy listo para comprar" | Derivar y notificar al agente |
| "Cuánto es la mensalidad / financiamiento" | Derivar si el bot no tiene esa info |
| Mismo intento fallido 2 veces | Derivar antes de un tercer intento |

---

## 2. QUÉ DECIR CUANDO NO SE SABE LA RESPUESTA

### Principios clave

1. **Nunca inventar ni adivinar.** Es peor dar información incorrecta que admitir que no se sabe.
2. **Ser transparente.** Indicar que se es un asistente automatizado.
3. **Mantener el tono cálido pero honesto.**
4. **Siempre ofrecer un siguiente paso claro.**

### Plantillas de respuesta recomendadas

#### Cuando la pregunta no está en la base de conocimientos:

```
Entiendo tu pregunta. 😊
Esa información no la tengo disponible en este momento,
pero un asesor puede responderte con todos los detalles.

¿Prefieres que te derive con uno?
```

#### Cuando el bot no entiende el mensaje:

```
Disculpa, no logré entender tu mensaje.
¿Podrías reformularlo o darme más contexto?

Mientras tanto, te dejo estas opciones:
1️⃣ Preguntarme algo específico sobre una propiedad
2️⃣ Derivar a un asesor que pueda ayudarte mejor
```

#### Cuando se detecta frustración:

```
Lamento que estés teniendo esa experiencia. 😔
Entiendo que necesitas una respuesta clara.
Voy a conectarte con un asesor que puede ayudarte de forma personalizada.
```

#### Cuando se ofrece la derivación:

```
Voy a transferir tu consulta a nuestro equipo.
Un asesor se pondrá en contacto contigo en las próximas [X] horas
o puedes escribirnos directamente al [número de teléfono].

¿Está bien si continuamos por ahí?
```

#### Como mensaje de cierre cuando se deriva:

```
Perfecto, estás siendo derivado a uno de nuestros asesores.
Para que te ayude más rápido, te comparte un resumen de tu consulta:

"[Resumen de lo que el cliente preguntó]"

Te contactaremos pronto. 🙏
```

### Frases que NO usar

- ❌ "No puedo ayudarte con eso."
- ❌ "Esa información no existe."
- ❌ "No entiendo tu pregunta." (sin ofrecer alternativa)
- ❌ "Consulte nuestra página web." (abandona al cliente)
- ❌ Inventar datos de propiedades, precios o disponibilidad.

---

## 3. CÓMO ENTRENAR EL ASISTENTE PARA MEJORAR

### Paso 1 — Registrar todas las derivaciones

Cada vez que el bot deriva a un humano, guardar:

- La pregunta original del cliente
- El motivo de la derivación (categoría)
- Si el cliente quedó satisfecho o no con la transferencia

**Herramienta mínima:** una hoja de cálculo con columnas: fecha, pregunta, categoría de derivación, resolución.

### Paso 2 — Análisis semanal

Dedicar 30-60 minutos por semana a revisar las derivaciones acumuladas.

**Preguntas guía:**
- ¿Qué preguntas se repiten frecuentemente?
- ¿Qué porcentaje se pudo haber evitado con mejor entrenamiento?
- ¿Hay patrones estacionales? (ej.: preguntas sobre créditos hipotecarios en ciertos meses)

### Paso 3 — Agregar nuevas respuestas

Del análisis anterior:

1. Tomar las 5 preguntas más frecuentes que causaron derivación.
2. Redactar una respuesta clara y concisa (máximo 3-4 oraciones).
3. Agregarla a la base de conocimientos / flujos del bot.
4. Probar la respuesta antes de activarla.

### Paso 4 — Mejorar la detección de intención

Revisar si las preguntas no reconocidas tenían patrones:

- ¿El cliente usó sinonimos o regionalismos?
- ¿La pregunta era sobre un tema nuevo no contemplado?
- ¿Era una variación de una pregunta ya entrenada?

**Acción:** agregar sinónimos, reformular intents existentes, crear nuevos topics.

### Paso 5 — Feedback loop con el equipo de ventas

El agente humano que recibe las derivaciones sabe quéinfo necesita. Crear un canal (chat, documento compartido) donde los agentes reporten:

- Qué info les faltó albot
- Preguntas frecuentes que llegaron por derivación
- Casos donde el cliente llegó confundido por info incorrecta del bot

### Paso 6 — Métricas a seguir

| Métrica | Objetivo |
|---|---|
| Tasa de derivación | Bajar mes a mes |
| Derivaciones por pregunta | Identificar las más problemáticas |
| Tiempo promedio de derivación | Menos de 30 segundos |
| Satisfacción post-derivación | Mínimo 4/5 |
| Consultas resueltas por el bot | Subir mes a mes |

### Paso 7 — Actualización periódica del inventario

En inmobiliarias, la información cambia constantemente (precios, disponibilidad, condiciones). Asegurar que la base de conocimientos del bot seactualice:

- Cuando entra un nuevo proyecto o propiedad
- Cuando cambia un precio o condición
- Cuando se agota un inventario

**Regla práctica:** si cambia en el catálogo, cambia en el bot. En el mismo día.

---

## CHECKLIST RÁPIDO

- [ ] Definir al menos 5 categorías de derivación obligatoria
- [ ] Tener plantillas de transición aprobadas por el equipo
- [ ] Crear un registro de derivaciones (hoja de cálculo o CRM)
- [ ] Agendar revisión semanal de derivaciones
- [ ] Establecer un canal de comunicación con el equipo de ventas para feedback
- [ ] Definir el tiempo máximo de esperaacceptable antes de la derivación
- [ ] Crear un script de transferencia que resuma la consulta para el agente
- [ ] Revisar y actualizar la base de conocimientos al menos 1 vez al mes

---

## FUENTES

- d7networks.com — WhatsApp chatbot lead management in real estate
- kommunicate.io — WhatsApp escalation human handoff
- quidget.ai — When should chatbots escalate to human agents
- cobbai.com — Chatbot escalation best practices
- gupshup.ai — Real estate chatbot guide
- botpress.com — Chatbot best practices (español)
- propflo.ai — WhatsApp marketing use cases in real estate
- alhena.ai — AI human escalation chatbot handoff
