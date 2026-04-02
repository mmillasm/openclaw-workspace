# Validation Kit - Agencia de Automatizaciones IA para Corredores

## 📋 Índice

1. [Script de Entrevista de Validación](#1-script-de-entrevista-de-validación)
2. [Tracker de Agencias](#2-tracker-de-agencias)
3. [Email de Contacto Inicial](#3-email-de-contacto-inicial)
4. [Template de Notas Post-Entrevista](#4-template-de-notas-post-entrevista)
5. [Scorecard de Validación](#5-scorecard-de-validación)

---

## 1. Script de Entrevista de Validación

### Objetivo
Validar que:
- El problema existe y es doloroso
- Están dispuestos a pagar ~$400K CLP/mes
- Tienen presupuesto para automatización
- Hay urgencia real por resolverlo

### Duración: 30 minutos

---

### FASE 1: CALIFICACIÓN (5 minutos)

**Pregunta 1: Contexto del negocio**
> "Cuéntame un poco sobre su agencia. ¿Cuántos corredores son y qué tipo de propiedades manejan principalmente?"

*Qué escuchar:*
- Tamaño del equipo (ideal: 3-8 corredores)
- Tipo de propiedades (ideal: departamentos/casas premium)
- Ubicación (ideal: Santiago oriente)

**Pregunta 2: Volumen de leads**
> "¿Cuántos leads reciben aproximadamente por semana, y por qué canales? (web, portales, referidos, redes sociales)"

*Qué escuchar:*
- Volumen consistente (ideal: 20+ leads/semana)
- Múltiples canales
- Algún canal digital activo

**Pregunta 3: Proceso actual**
> "Hablemos de su proceso actual. ¿Cómo llega un lead, quién lo atiende primero, y cómo se convierte en visita?"

*Qué escuchar:*
- Reconocen el bottleneck
- Hay un punto donde los leads se pierden
- Alguien menciona "no alcanzamos a responder a todos"

---

### FASE 2: PROBLEMA (10 minutos)

**Pregunta 4: El dolor real**
> "¿Qué es lo más frustrante de cómo manejan los leads hoy? ¿Alguna vez perdieron una venta por responder tarde?"

*Qué escuchar:*
- Historia específica de pérdida
- Frustración genuina
- "El lead se enfrió", "el cliente ya había visto con otro"

**Pregunta 5: Costo del problema**
> "Si tuvieran que ponerle un número, ¿cuántas comisiones creen que pierden al mes por no atender leads a tiempo?"

*Qué escuchar:*
- Número concreto (ideal: $2M+ CLP mensuales)
- Calculan en comisiones, no en "oportunidades"
- Hay awareness del costo real

**Pregunta 6: Lo que ya intentaron**
> "¿Han probado alguna solución antes? Plantillas de respuesta, asistente virtual, algún software? ¿Qué funcionó y qué no?"

*Qué escuchar:*
- Intentaron algo (buena señal: reconocen el problema)
- Por qué falló (evitar esos errores)
- No han encontrado solución completa (oportunidad)

---

### FASE 3: SOLUCIÓN (10 minutos)

**Presentación breve del concepto:**
> "Estamos desarrollando un asistente de IA que atiende leads por WhatsApp 24/7, califica automáticamente a los compradores serios y agenda visitas directamente en el calendario del corredor — todo sin intervención humana.

> Un lead que escribe a las 11 PM recibe respuesta inmediata, responde 4 preguntas de calificación, y si es serio, el corredor tiene una cita agendada para mañana a las 10 AM."

**Pregunta 7: Reacción inicial**
> "¿Qué le parece ese flujo? ¿Le resolvería el problema que mencionó antes?"

*Qué escuchar:*
- "Eso es exactamente lo que necesito" (señal fuerte)
- Preguntas sobre cómo funciona (interés)
- Escepticismo (manejar objeciones)

**Pregunta 8: Preocupaciones**
> "¿Qué le preocupa o le haría dudar de implementar algo así?"

*Qué escuchar:*
- Pérdida del "toque personal" (objeción común)
- Complejidad técnica
- Costo
- Nota: registrar para incluir en FAQ del producto

---

### FASE 4: PRECIO (5 minutos)

**Pregunta 9: Disposición a pagar**
> "Tenemos tres opciones: un paquete Starter a $199.000 mensual para 1-2 corredores, uno Pro a $399.000 para 3-8 corredores que incluye todo lo que hablamos, y uno Agency para equipos más grandes.

> Pensando en su agencia, ¿cuál sería la opción que más sentido tendría? ¿Y honestamente, ¿ese rango de precios es viable para su presupuesto?"

*Qué escuchar:*
- Señalan directamente al Pro ($399K) → validación fuerte
- Dicen "me interesa el Pro pero $399K es alto" → negociar/hacer descuento early-bird
- Piden Starter → quizás no es el segmento ideal
- Dicen "necesito pensarlo" → no hay urgency, seguir educando

**Pregunta 10: Compromiso**
> "Si tuviéramos un piloto funcional en 3 semanas, ¿estaría dispuesto a probarlo con tráfico real de leads por 30 días?"

*Qué escuchar:*
- "Sí, con gusto" → lead HOT
- "Depende de..." → necesitan más info
- "No por ahora" → COLD, no perder tiempo

---

## 2. Tracker de Agencias

### Cómo usar este tracker

1. Importa este CSV a Airtable, Google Sheets o Excel
2. Actualiza el estado después de cada interacción
3. Usa las notas para registrar insights de cada llamada
4. Prioriza las que tengan mayor Score de Oportunidad

### Estructura del CSV

```csv
Nombre_Agencia,Website,Email,Telefono,Ubicacion,Corredores_Est,Prioridad,Estado,Score_Op,Fecha_Contacto,Medio_Contacto,Respuesta,Notas_Interes,Objeciones,Pricing_Mencionado,Compromiso,Proximo_Paso,Fecha_Siguiente
GESCOR Propiedades,gescorpropiedades.cl,contacto@gescorpropiedades.cl,+56 9 XXXX XXXX,Santiago,12,ALTA,POR_CONTACTAR,,,,,,,,,,
Boettcher Propiedades,boettcher.cl,info@boettcher.cl,+56 9 XXXX XXXX,Las Condes,8,ALTA,POR_CONTACTAR,,,,,,,,,,
Altea Propiedades,alteapropiedades.cl,contacto@alteapropiedades.cl,+56 9 XXXX XXXX,Providencia,6,MEDIA,POR_CONTACTAR,,,,,,,,,,
Vercetti Propiedades,vercettipropiedades.cl,info@vercetti.cl,+56 9 XXXX XXXX,Vitacura,5,MEDIA,POR_CONTACTAR,,,,,,,,,,
URBANI,urbani.cl,hola@urbani.cl,+56 9 XXXX XXXX,Santiago,15,ALTA,POR_CONTACTAR,,,,,,,,,,
Propiedades Yapur,propiedadesyapur.cl,contacto@yapur.cl,+56 9 XXXX XXXX,Las Condes,4,MEDIA,POR_CONTACTAR,,,,,,,,,,
Propital,propital.com,hello@propital.com,+56 9 XXXX XXXX,Santiago,3,BAJA,POR_CONTACTAR,,,,,,,,,,
Houm,houm.com,cl@houm.com,+56 9 XXXX XXXX,Santiago,20+,ALTA,POR_CONTACTAR,,,,,,,,,,
HousePricing,housepricing.cl,contacto@housepricing.cl,+56 9 XXXX XXXX,Ñuñoa,3,BAJA,POR_CONTACTAR,,,,,,,,,,
KiteProp,kiteprop.com,contacto@kiteprop.com,+56 9 XXXX XXXX,Santiago,5,MEDIA,POR_CONTACTAR,,,,,,,,,,
```

### Estados posibles

| Estado | Descripción |
|--------|-------------|
| POR_CONTACTAR | Aún no se ha enviado email o llamado |
| EMAIL_ENVIADO | Email de contacto inicial enviado |
| SIN_RESPUESTA | Email enviado, sin respuesta en 5 días |
| REUNION_AGENDADA | Tienen una llamada/reunión programada |
| REUNION_COMPLETADA | Entrevista realizada, esperando decisión |
| PILOTO_ACEPTADO | Aceptaron el piloto de 30 días |
| NO_INTERESADOS | Rechazaron explícitamente |
| NO_CALIFICA | No cumplen criterios (ej: muy pequeños) |

### Score de Oportunidad (0-100)

Calculado automáticamente según:
- **+30** Tamaño ideal (3-8 corredores)
- **+25** Zona premium (Las Condes, Vitacura, Providencia)
- **+20** Mencionaron problema de leads explícitamente
- **+15** Dijeron que $399K "está bien" o "me interesa"
- **+10** Aceptaron reunión en menos de 1 semana

**Interpretación:**
- **80-100:** Prioridad máxima, piloto casi seguro
- **60-79:** Buena oportunidad, necesitan un push
- **40-59:** Interesantes pero necesitan más educación
- **<40:** Probablemente no sea el momento

---

## 3. Email de Contacto Inicial

### Versión A: Directa y al grano (Recomendada)

**Asunto:** Automatización de leads para [Nombre Agencia] — 30 min para conversar?

```
Hola [Nombre],

Me llamo [Tu Nombre] y ayudo a agencias inmobiliarias a convertir más leads en visitas sin contratar más personal.

Veo que [Nombre Agencia] maneja propiedades en [Zona] — un mercado donde la velocidad de respuesta marca la diferencia entre cerrar una venta o perderla.

Estamos desarrollando un asistente de IA que:
• Responde leads de WhatsApp 24/7 (incluso a las 11 PM)
• Califica automáticamente a compradores serios
• Agenda visitas directo en tu calendario

¿Le interesaría una conversación de 30 minutos para ver si esto podría funcionar en su agencia? No es una venta, es exploración genuina.

Tengo disponibilidad:
• [Día 1] a las [hora]
• [Día 2] a las [hora]
• [Día 3] a las [hora]

¿Alguna opción le funciona?

Saludos,
[Tu Nombre]
[Tu Teléfono]
```

### Versión B: Basada en problema (Más suave)

**Asunto:** ¿Cuántos leads se les "enfrían" cada mes?

```
Hola [Nombre],

Trabajo con corredores de propiedades en Santiago y me he dado cuenta de algo:

El 80% de los leads que llegan por la noche o fin de semana no reciben respuesta hasta el siguiente día hábil. Y para ese momento, ya contactaron a otra agencia.

¿Esa es una realidad que también enfrentan en [Nombre Agencia]?

Estamos desarrollando una solución específica para esto, y me gustaría conversar 30 minutos con agencias que entiendan el problema bien — sin compromiso de compra, solo feedback honesto.

¿Tendrías disponibilidad esta semana o la siguiente?

Saludos,
[Tu Nombre]
```

### Versión C: Con social proof (Después de tener 1-2 pilotos)

**Asunto:** Cómo [Agencia X] aumentó sus visitas en 40%

```
Hola [Nombre],

Hace un mes [Agencia X] en [Zona] implementó un asistente de IA para sus leads de WhatsApp.

Resultados hasta ahora:
• 40% más visitas agendadas
• Tiempo de respuesta promedio: 2 minutos (antes era 8+ horas)
• 15 horas ahorradas mensualmente por corredor

Estamos buscando 2-3 agencias más para un piloto similar. ¿Le interesaría una breve conversación para ver si encajaría con [Nombre Agencia]?

Quedo atento,
[Tu Nombre]
```

---

## 4. Template de Notas Post-Entrevista

### Información básica

```markdown
# Entrevista - [Nombre Agencia]

**Fecha:** [YYYY-MM-DD]  
**Entrevistado:** [Nombre y cargo]  
**Medio:** [Llamada/Zoom/Presencial]  
**Duración:** [XX minutos]

---

## Perfil del entrevistado

- **Tamaño de la agencia:** [X corredores]
- **Zona principal:** [Zona]
- **Tipo de propiedades:** [Departamentos/Casas/Mixto]
- **Volumen de leads:** [X por semana]
- **Canales principales:** [Web/Portales/Referidos/Redes]

---

## Insights clave

### Dolor principal identificado
[Describir en 1-2 oraciones el problema más fuerte que mencionaron]

### Costo del problema
[Monto estimado que pierden mensualmente por no atender leads]

### Soluciones previas intentadas
[Qué probaron antes y por qué falló]

### Reacción al concepto
- [ ] "Eso es exactamente lo que necesitamos"
- [ ] "Interesante, cuéntame más"
- [ ] "No estoy seguro si aplica a nosotros"
- [ ] "No nos interesa por ahora"

### Objeciones principales
1. [Objeción 1]
2. [Objeción 2]

---

## Validación de precios

**Paquete mencionado:** [Starter/Pro/Agency]

**Reacción al precio:**
- [ ] "Está perfecto" / "Me interesa el Pro"
- [ ] "Es alto, pero negociable"
- [ ] "Necesito pensarlo"
- [ ] "No tenemos presupuesto para eso"

**Monto que podrían pagar:** [Si dijeron algo diferente]

---

## Compromiso obtenido

- [ ] Aceptaron piloto de 30 días
- [ ] Quieren demo primero
- [ ] Necesitan consultar con [persona]
- [ ] Pedieron material por email
- [ ] No hay interés

**Fecha de seguimiento:** [YYYY-MM-DD]

**Próximo paso:** [Acción específica]

---

## Score de validación

| Criterio | Puntuación | Notas |
|----------|------------|-------|
| Problema es doloroso | /10 | |
| Tienen presupuesto | /10 | |
| Hay urgency | /10 | |
| Decisor presente | /10 | |
| Reacción positiva a solución | /10 | |
| Aceptable el pricing | /10 | |
| **TOTAL** | **/60** | |

**Veredicto:**
- [ ] **HOT** (45-60): Piloto prioritario
- [ ] **WARM** (30-44): Seguimiento activo
- [ ] **COLD** (<30): No invertir más tiempo

---

## Acciones inmediatas

- [ ] Enviar material adicional
- [ ] Agendar demo/piloto
- [ ] Conectar con [persona]
- [ ] Esperar su respuesta
- [ ] Archivar

**Responsable:** [Tu nombre]  
**Fecha límite:** [YYYY-MM-DD]
```

---

## 5. Scorecard de Validación

### Meta de entrevistas

| Métrica | Meta | Actual | % |
|---------|------|--------|---|
| Emails enviados | 20 | 0 | 0% |
| Respuestas recibidas | 10 | 0 | 0% |
| Reuniones agendadas | 5 | 0 | 0% |
| Entrevistas completadas | 5 | 0 | 0% |
| Pilotos confirmados | 2 | 0 | 0% |

### Checklist de validación

**¿El problema existe?**
- [ ] 3/5 entrevistados mencionaron perder leads por respuesta tardía
- [ ] 2/5 compartieron historia específica de pérdida de comisión

**¿Hay disposición a pagar?**
- [ ] 3/5 dijeron que $399K es "aceptable" o "me interesa"
- [ ] 1/5 aceptó pagar $399K sin negociar

**¿Hay urgency?**
- [ ] 2/5 quieren solución en menos de 1 mes
- [ ] 1/5 aceptó piloto inmediato

**¿Es el segmento correcto?**
- [ ] 4/5 tienen entre 3-8 corredores
- [ ] 3/5 están en zonas premium (Las Condes/Vitacura/Providencia)

### Decisión Go/No-Go

**GO si:**
- ≥3 entrevistados muestran interés genuino
- ≥2 aceptan piloto a $399K
- El problema se repite en ≥60% de entrevistas

**NO-GO si:**
- <2 entrevistados califican como HOT
- ≥3 objeciones en común que no podemos resolver
- Nadie acepta pagar >$200K/mes

**PIVOT si:**
- El problema existe pero no a $399K → probar $199K
- Les interesa pero no el chatbot → validar otra solución
- El segmento está mal → probar agencias más grandes/pequeñas

---

## 📎 Recursos adicionales

### Herramientas recomendadas

| Uso | Herramienta | Gratuito? |
|-----|-------------|-----------|
| Tracker | Airtable | Sí (hasta 1200 registros) |
| Email frío | Instantly.ai | No ($37/mes) |
| Alternativa email | Gmail + Mail Merge con Sheets | Sí |
| Calendario | Calendly | Sí (básico) |
| Notas | Notion / Obsidian | Sí |

### Tiempos sugeridos

| Actividad | Tiempo estimado |
|-----------|-----------------|
| Preparar lista de 20 agencias | 2 horas |
| Personalizar y enviar 20 emails | 3 horas |
| Seguimiento de no-respuestas | 1 hora/día |
| Realizar 5 entrevistas (30 min c/u) | 2.5 horas |
| Documentar notas post-entrevista | 1.5 horas |
| Análisis y decisión Go/No-Go | 1 hora |
| **TOTAL** | **~11 horas** |

---

*Validation Kit creado el 2 de abril de 2026*  
*Para: Agencia de Automatizaciones IA - Nicho: Corredores de Propiedades*
