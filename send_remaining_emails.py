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
    return result.returncode == 0, result.stdout + result.stderr

emails = [
    ("info@boettcher.cl", "¿Cuántos leads se les 'enfrían' en Boettcher Propiedades?", """Hola María,

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
Ader"""),
    
    ("hola@urbani.cl", "¿Cuántos leads se les 'enfrían' en URBANI?", """Hola Pedro,

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
Ader"""),
    
    ("cl@houm.com", "¿Cuántos leads se les 'enfrían' en Houm?", """Hola Diego,

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
Ader"""),
    
    ("contacto@alteapropiedades.cl", "¿Cuántos leads se les 'enfrían' en Altea Propiedades?", """Hola Carlos,

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
Ader""")
]

for i, (to, subject, body) in enumerate(emails):
    print(f"Enviando {i+1}/4 a {to}...")
    success, result = send_email(to, subject, body)
    if success:
        print(f"✅ Enviado: {to}")
    else:
        print(f"❌ Error: {result}")
    
    if i < len(emails) - 1:
        delay = 45
        print(f"⏱️  Esperando {delay}s...")
        time.sleep(delay)

print("\n🎉 Emails restantes enviados")
