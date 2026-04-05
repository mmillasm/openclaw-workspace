# Follow-Up Día 3 - Cold Emails

**Fecha de envío:** 2026-04-05 (3 días después del email inicial)

**Regla:** Solo enviar a quienes NO respondieron al email inicial.

---

## Email 1: GESCOR Propiedades

**Para:** Juan Pérez - contacto@gescorpropiedades.cl

**Asunto:** Re: ¿Cuántos leads se les "enfrían" en GESCOR Propiedades?

```
Hola Juan,

Sé que está ocupado, así que solo un mensaje corto:

¿Le interesaría esa conversación de 30 minutos sobre cómo automatizar la atención de leads en GESCOR?

Si no es el momento, también me sirve saberlo.

Saludos,
Ader
```

---

## Email 2: Boettcher Propiedades

**Para:** María González - info@boettcher.cl

**Asunto:** Re: ¿Cuántos leads se les "enfrían" en Boettcher Propiedades?

```
Hola María,

Sé que está ocupada, así que solo un mensaje corto:

¿Le interesaría esa conversación de 30 minutos sobre cómo automatizar la atención de leads en Boettcher?

Si no es el momento, también me sirve saberlo.

Saludos,
Ader
```

---

## Email 3: URBANI

**Para:** Pedro Soto - hola@urbani.cl

**Asunto:** Re: ¿Cuántos leads se les "enfrían" en URBANI?

```
Hola Pedro,

Sé que está ocupado, así que solo un mensaje corto:

¿Le interesaría esa conversación de 30 minutos sobre cómo automatizar la atención de leads en URBANI?

Si no es el momento, también me sirve saberlo.

Saludos,
Ader
```

---

## Email 4: Houm

**Para:** Diego Silva - cl@houm.com

**Asunto:** Re: ¿Cuántos leads se les "enfrían" en Houm?

```
Hola Diego,

Sé que está ocupado, así que solo un mensaje corto:

¿Le interesaría esa conversación de 30 minutos sobre cómo automatizar la atención de leads en Houm?

Si no es el momento, también me sirve saberlo.

Saludos,
Ader
```

---

## Email 5: Altea Propiedades

**Para:** Carlos Rodríguez - contacto@alteapropiedades.cl

**Asunto:** Re: ¿Cuántos leads se les "enfrían" en Altea Propiedades?

```
Hola Carlos,

Sé que está ocupado, así que solo un mensaje corto:

¿Le interesaría esa conversación de 30 minutos sobre cómo automatizar la atención de leads en Altea?

Si no es el momento, también me sirve saberlo.

Saludos,
Ader
```

---

## Script para enviar (followup-day3.sh)

```python
#!/usr/bin/env python3
import os
import subprocess
import time

def send_email(to, subject, body):
    env = os.environ.copy()
    env.update({
        'EMAIL_SMTP_SERVER': 'smtp.gmail.com',
        'EMAIL_SMTP_PORT': '587',
        'EMAIL_SENDER': 'disenoxplain@gmail.com',
        'EMAIL_SMTP_PASSWORD': 'idxtprzkrxajdhqw',
        'EMAIL_USE_TLS': 'true'
    })
    
    result = subprocess.run(
        ['python3', '/home/ubuntu/.openclaw/workspace/skills/send-email/send_email.py', to, subject, body],
        env=env,
        capture_output=True,
        text=True
    )
    return result.returncode == 0

emails = [
    ("contacto@gescorpropiedades.cl", "Re: ¿Cuántos leads se les \"enfrían\" en GESCOR Propiedades?", "Hola Juan,\n\nSé que está ocupado, así que solo un mensaje corto:\n\n¿Le interesaría esa conversación de 30 minutos sobre cómo automatizar la atención de leads en GESCOR?\n\nSi no es el momento, también me sirve saberlo.\n\nSaludos,\nAder"),
    ("info@boettcher.cl", "Re: ¿Cuántos leads se les \"enfrían\" en Boettcher Propiedades?", "Hola María,\n\nSé que está ocupada, así que solo un mensaje corto:\n\n¿Le interesaría esa conversación de 30 minutos sobre cómo automatizar la atención de leads en Boettcher?\n\nSi no es el momento, también me sirve saberlo.\n\nSaludos,\nAder"),
    ("hola@urbani.cl", "Re: ¿Cuántos leads se les \"enfrían\" en URBANI?", "Hola Pedro,\n\nSé que está ocupado, así que solo un mensaje corto:\n\n¿Le interesaría esa conversación de 30 minutos sobre cómo automatizar la atención de leads en URBANI?\n\nSi no es el momento, también me sirve saberlo.\n\nSaludos,\nAder"),
    ("cl@houm.com", "Re: ¿Cuántos leads se les \"enfrían\" en Houm?", "Hola Diego,\n\nSé que está ocupado, así que solo un mensaje corto:\n\n¿Le interesaría esa conversación de 30 minutos sobre cómo automatizar la atención de leads en Houm?\n\nSi no es el momento, también me sirve saberlo.\n\nSaludos,\nAder"),
    ("contacto@alteapropiedades.cl", "Re: ¿Cuántos leads se les \"enfrían\" en Altea Propiedades?", "Hola Carlos,\n\nSé que está ocupado, así que solo un mensaje corto:\n\n¿Le interesaría esa conversación de 30 minutos sobre cómo automatizar la atención de leads en Altea?\n\nSi no es el momento, también me sirve saberlo.\n\nSaludos,\nAder"),
]

for to, subject, body in emails:
    if send_email(to, subject, body):
        print(f"✅ Enviado a {to}")
    else:
        print(f"❌ Error: {to}")
    time.sleep(45)
```

---

## ⚠️ IMPORTANTE - Verificar antes de enviar

Antes de ejecutar el script el **2026-04-05**, verificar:

1. **¿Alguien respondió?** Si sí → NO enviar follow-up a esa persona
2. **¿Se agendó reunión?** Si sí → Marcar como "REUNION_AGENDADA" en Airtable/CSV
3. **¿Pidieron no ser contactados?** Si sí → Eliminar de la lista

**Para ver quién respondió:**
```
grep -r "Respondio" cold-email-system/agencias.csv
```

**Ejecutar script:**
```bash
python3 cold-email-system/followup-day3.sh
```
