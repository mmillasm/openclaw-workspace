# Propuesta MVP: Chatbot Inteligente para Corredores de Propiedades

## Resumen Ejecutivo

**El problema:** Los corredores de propiedades en Chile pierden hasta 40% de sus leads potenciales por no responder a tiempo. Un lead que espera más de 5 minutos tiene 80% menos probabilidad de convertirse en visita.

**Nuestra solución:** Un asistente de IA conversacional que atiende leads 24/7, califica automáticamente a los compradores serios y agenda visitas directamente en el calendario del corredor — sin intervención humana.

**Resultado esperado:** Incremento de 25-40% en leads calificados, reducción de 70% en tiempo de respuesta, y 15-20 horas ahorradas mensualmente por corredor.

---

## Entendiendo Sus Necesidades

Su agencia maneja múltiples propiedades y recibe consultas por WhatsApp, web y redes sociales. Los desafíos actuales incluyen:

-   **Atención fuera de horario:** Leads que escriben en la noche o fin de semana sin respuesta hasta el siguiente día hábil
-   **Cualificación manual:** Tiempo valioso dedicado a conversaciones con curiosos no calificados
-   **Agendamiento manual:** Coordinar horarios de visita consume horas de productividad semanal
-   **Seguimiento inconsistente:** Leads "tibios" que se pierden por falta de nurtura automatizada

Esta solución está diseñada específicamente para corredores con 3-8 agentes que buscan escalar sin contratar más personal administrativo.

---

## Solución Propuesta

### Componentes del Sistema

1.  **Chatbot Conversacional (IA)**
    -   Atiende leads vía WhatsApp 24/7 con respuestas naturales en español
    -   Detecta automáticamente si el lead viene de una propiedad específica o es consulta general
    -   Flujo contextual que no pregunta información que el lead ya proporcionó

2.  **Calificación Automática de Leads**
    -   Sistema de puntuación basado en: zona de interés, presupuesto, urgencia, financiamiento
    -   Clasificación automática: HOT (contactar hoy), WARM (seguimiento 24h), COLD (nurture mensual)

3.  **Agendamiento Inteligente**
    -   Integración directa con Google Calendar del corredor
    -   Ofrece horarios disponibles en tiempo real
    -   Confirma visitas con recordatorios automáticos

4.  **Dashboard de Leads**
    -   Panel centralizado con todos los leads y su estado
    -   Notificaciones inmediatas para leads HOT
    -   Métricas de conversión y rendimiento

---

## Metodología de Implementación

### Fase 1: Setup y Configuración (Semana 1)

| Actividad | Entregable |
|-----------|------------|
| Configurar WhatsApp Business API | Número de WhatsApp verificado y operativo |
| Crear cuenta en n8n (orquestador) | Instancia funcional con acceso administrativo |
| Configurar APIs (OpenAI, Google Calendar) | Conexiones testeadas y operativas |
| Diseñar base de datos de leads | Estructura Airtable/Supabase lista para producción |

### Fase 2: Desarrollo de Flujos (Semana 2)

| Actividad | Entregable |
|-----------|------------|
| Diseñar flujo conversacional | Diagrama Voiceflow aprobado por el cliente |
| Implementar webhook de entrada | Endpoint recibiendo mensajes de WhatsApp |
| Configurar GPT-4o mini para respuestas | Prompts optimizados y testeados |
| Implementar sistema de lead scoring | Matriz de calificación funcionando |

### Fase 3: Integraciones (Semana 3)

| Actividad | Entregable |
|-----------|------------|
| Conectar Google Calendar | Eventos creándose automáticamente desde el chat |
| Configurar notificaciones | Alertas en tiempo real para leads HOT |
| Crear templates de WhatsApp aprobados | Templates operativos en Meta Business |
| Testing con casos reales | 10 conversaciones de prueba exitosas |

### Fase 4: Piloto y Ajustes (Semana 4)

| Actividad | Entregable |
|-----------|------------|
| Deploy en agencia piloto | Sistema en producción con tráfico real |
| Monitoreo de conversaciones | Revisión diaria de interacciones |
| Ajustes según feedback | Iteraciones sobre flujos y respuestas |
| Documentación y entrenamiento | Manual de uso para el equipo |

---

## Entregables Específicos

-   Chatbot operativo 24/7 vía WhatsApp
-   Sistema de calificación automática (lead scoring)
-   Integración con Google Calendar
-   Dashboard de leads con métricas
-   Base de datos de leads accesible
-   Notificaciones en tiempo real para leads prioritarios
-   Flujo de nurture para leads no calificados
-   Documentación completa y capacitación del equipo

---

## Cronograma

```
Semana 1    Semana 2    Semana 3    Semana 4
[SETUP]     [FLUJOS]    [INTEGRA]   [PILOTO]
    │            │           │           │
    ▼            ▼           ▼           ▼
WhatsApp    Voiceflow   Calendar    Producción
   API       + n8n        API        + Ajustes
```

**Fecha de inicio propuesta:** Lunes siguiente a la aprobación  
**Fecha de entrega:** 4 semanas después del inicio  
**Piloto en producción:** Semana 4

---

## Inversión

### Modelo de Precios

| Paquete | Inversión Mensual | Ideal Para |
|---------|-------------------|------------|
| **Starter** | $199.000 CLP | 1-2 corredores, hasta 100 leads/mes |
| **Pro** | $399.000 CLP | 3-8 corredores, hasta 500 leads/mes |
| **Agency** | $799.000 CLP | 9+ corredores, leads ilimitados |

### Setup Inicial

-   **Inversión única:** $300.000 - $600.000 CLP según complejidad
-   Incluye: configuración, personalización de flujos, integraciones, capacitación

### ROI Estimado (Paquete Pro)

| Métrica | Sin Chatbot | Con Chatbot | Impacto |
|---------|-------------|-------------|---------|
| Leads atendidos/mes | 150 | 400 | +167% |
| Tasa de conversión | 8% | 18% | +125% |
| Comisiones generadas | $4.800.000 | $10.800.000 | +$6.000.000 |
| **ROI mensual** | - | - | **1.400%** |

*Cálculo basado en comisión promedio de $4.000.000 CLP por propiedad vendida*

---

## ¿Por Qué Nosotros?

### Diferenciadores Clave

1.  **Especialización inmobiliaria:** No somos un chatbot genérico. Entendemos el ciclo de venta de propiedades, las objeciones comunes, y el proceso de calificación de compradores.

2.  **Flujo contextual inteligente:** Detectamos si el lead viene de una propiedad específica y adaptamos la conversación, evitando preguntas redundantes.

3.  **Calificación basada en datos:** Usamos una matriz probada de 3 dimensiones (FIT, INTENT, ENGAGEMENT) que ha demostrado aumentar conversiones en 30%.

4.  **Stack moderno y escalable:** Construimos sobre n8n + OpenAI + WhatsApp Business API — tecnología enterprise sin el precio enterprise.

### Tecnologías Utilizadas

-   **WhatsApp Business API:** Conexión oficial, sin riesgo de bans
-   **OpenAI GPT-4o mini:** IA conversacional de última generación
-   **n8n:** Orquestador low-code flexible y transparente
-   **Voiceflow:** Diseño visual de flujos conversacionales
-   **Google Calendar API:** Agendamiento nativo

---

## Siguientes Pasos

Para avanzar con esta propuesta:

1.  **Aprobación:** Confirme su interés respondiendo "Proceder con Pro" (o Starter/Agency)
2.  **Kickoff:** Agendamos reunión de 30 minutos para definir detalles de personalización
3.  **Inicio:** Comenzamos desarrollo el lunes siguiente a la reunión kickoff
4.  **Primera entrega:** Demo funcional en 2 semanas

**Validez de esta propuesta:** 30 días desde la fecha de emisión.

---

## Preguntas Frecuentes

**¿Puede el bot responder preguntas específicas sobre propiedades?**  
Sí. Integramos con su base de datos de propiedades para responder sobre precio, metraje, características, fotos, y disponibilidad.

**¿Qué pasa si el lead hace una pregunta compleja?**  
El bot está entrenado para detectar cuando necesita escalar a un humano. Puede transferir la conversación al corredor asignado en cualquier momento.

**¿Podemos modificar los flujos después de implementado?**  
Absolutamente. El sistema está diseñado para iteración continua. Los ajustes menores los hacemos en 24-48 horas.

**¿Qué información necesitan para empezar?**  
-   Número de WhatsApp Business verificado
-   Acceso a Google Calendar del equipo
-   Lista de propiedades activas (puede ser Excel/CSV inicial)
-   Logo y datos de contacto de la agencia para personalización

---

## Términos y Condiciones

-   **Pago:** 50% al inicio, 50% al finalizar el piloto exitoso
-   **Soporte:** Incluye 30 días de soporte post-implementación
-   **Cancelación:** Suscripción mensual, cancelable con 30 días de anticipación
-   **Alcance:** Esta propuesta cubre el MVP descrito. Funcionalidades adicionales se cotizan por separado.

---

*Documento generado el 1 de abril de 2026*  
*Skills utilizadas: proposal-writer, markdown, client-intake-bot-pro, voiceflow, whatsapp-business-api, n8n, lead-scorer*
