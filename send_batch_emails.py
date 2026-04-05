#!/usr/bin/env python3
import subprocess
import time
import random

emails = [
    {
        "to": "contacto@gescorpropiedades.cl",
        "subject": "¿Cuántos leads se les 'enfrían' en GESCOR Propiedades?",
        "body": """Hola Juan,

Me llamo Ader y ayudo a agencias inmobiliarias como GESCOR a convertir más leads en visitas sin contratar más personal.

Veo que manejan propiedades en Las Condes — un mercado donde la velocidad de respuesta marca la diferencia entre cerrar una venta o perderla.

Estamos desarrollando un asistente de IA que:
• Responde leads de WhatsApp 24/7 (incluso a las 11 PM)
• Califica automáticamente a compradores serios
• Agenda visitas directo en tu calendario

¿Le interesaría una conversación de 30 minutos para ver si esto podría funcionar en GESCOR? No es una venta, es exploración genuina.

Tengo disponibilidad:
• Lunes a las 10 AM
• Miércoles a las 3 PM
• Viernes a las 11 AM

¿Alguna opción le funciona?

Saludos,
Ader
+56 9 1234 5678"""
    },
    {
        "to": "info@boettcher.cl",
        "subject": "¿Cuántos leads se les 'enfrían' en Boettcher Propiedades?",
        "body": """Hola María,

Me llamo Ader y ayudo a agencias inmobiliarias como Boettcher a convertir más leads en visitas sin contratar más personal.

Veo que manejan propiedades en Las Condes — un mercado donde la velocidad de respuesta marca la diferencia entre cerrar una venta o perderla.

Estamos desarrollando un asistente de IA que:
• Responde leads de WhatsApp 24/7 (incluso a las 11 PM)
• Califica automáticamente a compradores serios
• Agenda visitas directo en tu calendario

¿Le interesaría una conversación de 30 minutos para ver si esto podría funcionar en Boettcher? No es una venta, es exploración genuina.

Tengo disponibilidad:
• Lunes a las 10 AM
• Miércoles a las 3 PM
• Viernes a las 11 AM

¿Alguna opción le funciona?

Saludos,
Ader
+56 9 1234 5678"""
    },
    {
        "to": "hola@urbani.cl",
        "subject": "¿Cuántos leads se les 'enfrían' en URBANI?",
        "body": """Hola Pedro,

Me llamo Ader y ayudo a agencias inmobiliarias como URBANI a convertir más leads en visitas sin contratar más personal.

Veo que manejan propiedades en Santiago — un mercado donde la velocidad de respuesta marca la diferencia entre cerrar una venta o perderla.

Estamos desarrollando un asistente de IA que:
• Responde leads de WhatsApp 24/7 (incluso a las 11 PM)
• Califica automáticamente a compradores serios
• Agenda visitas directo en tu calendario

¿Le interesaría una conversación de 30 minutos para ver si esto podría funcionar en URBANI? No es una venta, es exploración genuina.

Tengo disponibilidad:
• Lunes a las 10 AM
• Miércoles a las 3 PM
• Viernes a las 11 AM

¿Alguna opción le funciona?

Saludos,
Ader
+56 9 1234 5678"""
    },
    {
        "to": "cl@houm.com",
        "subject": "¿Cuántos leads se les 'enfrían' en Houm?",
        "body": """Hola Diego,

Me llamo Ader y ayudo a agencias inmobiliarias como Houm a convertir más leads en visitas sin contratar más personal.

Veo que manejan propiedades en Santiago — un mercado donde la velocidad de respuesta marca la diferencia entre cerrar una venta o perderla.

Estamos desarrollando un asistente de IA que:
• Responde leads de WhatsApp 24/7 (incluso a las 11 PM)
• Califica automáticamente a compradores serios
• Agenda visitas directo en tu calendario

¿Le interesaría una conversación de 30 minutos para ver si esto podría funcionar en Houm? No es una venta, es exploración genuina.

Tengo disponibilidad:
• Lunes a las 10 AM
• Miércoles a las 3 PM
• Viernes a las 11 AM

¿Alguna opción le funciona?

Saludos,
Ader
+56 9 1234 5678"""
    },
    {
        "to": "contacto@alteapropiedades.cl",
        "subject": "¿Cuántos leads se les 'enfrían' en Altea Propiedades?",
        "body": """Hola Carlos,

Me llamo Ader y ayudo a agencias inmobiliarias como Altea a convertir más leads en visitas sin contratar más personal.

Veo que manejan propiedades en Providencia — un mercado donde la velocidad de respuesta marca la diferencia entre cerrar una venta o perderla.

Estamos desarrollando un asistente de IA que:
• Responde leads de WhatsApp 24/7 (incluso a las 11 PM)
• Califica automáticamente a compradores serios
• Agenda visitas directo en tu calendario

¿Le interesaría una conversación de 30 minutos para ver si esto podría funcionar en Altea? No es una venta, es exploración genuina.

Tengo disponibilidad:
• Lunes a las 10 AM
• Miércoles a las 3 PM
• Viernes a las 11 AM

¿Alguna opción le funciona?

Saludos,
Ader
+56 9 1234 5678"""
    }
]

script = "/home/ubuntu/.openclaw/workspace/skills/send-email/send_email.py"

for i, email in enumerate(emails):
    print(f"Enviando email {i+1}/5 a {email['to']}...")
    result = subprocess.run(
        ["python3", script, email["to"], email["subject"], email["body"]],
        capture_output=True,
        text=True
    )
    if result.returncode == 0:
        print(f"✅ Enviado a {email['to']}")
    else:
        print(f"❌ Error enviando a {email['to']}: {result.stderr}")
    
    # Delay aleatorio entre envíos (30-90 segundos)
    if i < len(emails) - 1:
        delay = random.randint(30, 90)
        print(f"⏱️  Esperando {delay} segundos...")
        time.sleep(delay)

print("\n🎉 Lote de emails enviado")
