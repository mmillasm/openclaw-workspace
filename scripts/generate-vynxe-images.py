#!/usr/bin/env python3
"""
VYNXE — Generador de Mockups para Instagram
Genera imágenes de WhatsApp, Calendario y Dashboard
"""

from PIL import Image, ImageDraw, ImageFont
import os

# Colores VYNXE
BG_DARK = (15, 15, 26)        # #0F0F1A
SURFACE = (26, 26, 46)         # #1A1A2E
BLUE = (79, 142, 255)          # #4F8EFF
PURPLE = (139, 92, 246)        # #8B5CF6
WHITE = (255, 255, 255)
GRAY = (160, 160, 176)         # #A0A0B0
GREEN = (37, 211, 102)         # Verde WhatsApp recibido
GREEN_LIGHT = (0, 200, 83)     # Verde claro
GRAY_SENT = (45, 45, 45)       # Mensaje enviado
GRAY_TEXT = (200, 200, 200)

# Dimensiones
PHONE_WIDTH = 540
PHONE_HEIGHT = 960

def hex_to_rgb(hex_color):
    hex_color = hex_color.lstrip('#')
    return tuple(int(hex_color[i:i+2], 16) for i in (0, 2, 4))

def load_font(size, bold=False):
    """Carga fuente, fallback a Arial si no existe"""
    try:
        if bold:
            return ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", size)
        return ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", size)
    except:
        return ImageFont.load_default()

def create_gradient(width, height, color1, color2, direction='horizontal'):
    """Crea imagen con gradiente"""
    img = Image.new('RGB', (width, height))
    draw = ImageDraw.Draw(img)
    
    for i in range(height):
        ratio = i / height
        r = int(color1[0] + (color2[0] - color1[0]) * ratio)
        g = int(color1[1] + (color2[1] - color1[1]) * ratio)
        b = int(color1[2] + (color2[2] - color1[2]) * ratio)
        draw.line([(0, i), (width, i)], fill=(r, g, b))
    
    return img

def draw_rounded_rect(draw, xy, radius, fill):
    """Dibuja rectángulo redondeado"""
    x1, y1, x2, y2 = xy
    
    # Rectángulo principal
    draw.rectangle([x1 + radius, y1, x2 - radius, y2], fill=fill)
    draw.rectangle([x1, y1 + radius, x2, y2 - radius], fill=fill)
    
    # Esquinas
    draw.ellipse([x1, y1, x1 + radius*2, y1 + radius*2], fill=fill)
    draw.ellipse([x2 - radius*2, y1, x2, y1 + radius*2], fill=fill)
    draw.ellipse([x1, y2 - radius*2, x1 + radius*2, y2], fill=fill)
    draw.ellipse([x2 - radius*2, y2 - radius*2, x2, y2], fill=fill)

def create_whatsapp_mockup(filename, messages, business_name="VYNXE Inmobiliaria"):
    """Crea mockup de chat de WhatsApp"""
    
    # Imagen completa con gradiente
    img = create_gradient(PHONE_WIDTH, PHONE_HEIGHT, BG_DARK, SURFACE)
    draw = ImageDraw.Draw(img)
    
    # Header verde
    header_height = 80
    draw.rectangle([0, 0, PHONE_WIDTH, header_height], fill=(18, 140, 125))  # Verde WhatsApp
    
    # Avatar (círculo)
    avatar_x, avatar_y = 50, 40
    draw.ellipse([30, 20, 70, 60], fill=PURPLE)
    
    # Nombre del negocio
    font_small = load_font(14)
    font_medium = load_font(16, bold=True)
    font_large = load_font(24, bold=True)
    
    draw.text((85, 20), business_name, font=font_medium, fill=WHITE)
    draw.text((85, 40), "en línea", font=font_small, fill=(180, 255, 180))
    
    # Menú (tres puntos)
    for i, y in enumerate([28, 38, 48]):
        draw.ellipse([490, y, 500, y+6], fill=WHITE)
    
    # Área de mensajes
    y_pos = 100
    for msg in messages:
        is_received = msg['sent_by'] == 'contact'
        bubble_color = GREEN if is_received else GRAY_SENT
        text_color = (255, 255, 255) if not is_received else WHITE
        
        # Posición
        if is_received:
            x_bubble = 20
        else:
            x_bubble = PHONE_WIDTH - 20 - msg['width']
        
        # Dibujar burbuja
        draw_rounded_rect(draw, [x_bubble, y_pos, x_bubble + msg['width'], y_pos + msg['height'] + 20], 15, bubble_color)
        
        # Texto (wrap manual)
        words = msg['text'].split()
        lines = []
        current_line = ""
        max_width = msg['width'] - 30
        
        for word in words:
            test_line = current_line + " " + word if current_line else word
            # Approximate width check
            if len(test_line) * 8 < max_width:
                current_line = test_line
            else:
                if current_line:
                    lines.append(current_line)
                current_line = word
        if current_line:
            lines.append(current_line)
        
        for i, line in enumerate(lines):
            draw.text((x_bubble + 15, y_pos + 8 + i*18), line, font=font_small, fill=text_color)
        
        y_pos += msg['height'] + 35
        
        # Hora
        hour_x = x_bubble + msg['width'] - 50 if not is_received else x_bubble + 10
        draw.text((hour_x, y_pos - 25), "21:45" if is_received else "21:46", font=load_font(10), fill=GRAY)
    
    # Input area
    input_y = PHONE_HEIGHT - 70
    draw.rectangle([10, input_y, PHONE_WIDTH - 10, input_y + 50], fill=SURFACE)
    draw.ellipse([20, input_y + 15, 45, input_y + 40], fill=GRAY)  # Emoji
    draw.ellipse([PHONE_WIDTH - 65, input_y + 12, PHONE_WIDTH - 30, input_y + 42], fill=GREEN)  # Send
    
    # Fake icon
    draw.polygon([(PHONE_WIDTH - 55, input_y + 20), (PHONE_WIDTH - 55, input_y + 35), (PHONE_WIDTH - 40, input_y + 27)], fill=WHITE)
    
    img.save(filename)
    print(f"✅ Creado: {filename}")

def create_calendar_mockup(filename):
    """Crea mockup de Google Calendar"""
    
    img = create_gradient(PHONE_WIDTH, PHONE_HEIGHT, BG_DARK, SURFACE)
    draw = ImageDraw.Draw(img)
    
    font_title = load_font(20, bold=True)
    font_medium = load_font(14)
    font_small = load_font(12)
    
    # Header
    draw.rectangle([0, 0, PHONE_WIDTH, 70], fill=SURFACE)
    draw.text((20, 25), "📅 Abril 2026", font=font_title, fill=WHITE)
    draw.ellipse([480, 25, 510, 55], fill=GRAY)  # Menú
    
    # Días de la semana
    days = ["Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"]
    x_start = 20
    for i, day in enumerate(days):
        x = x_start + i * 75
        draw.text((x, 85), day, font=font_small, fill=GRAY)
    
    # Eventos del calendario
    events = [
        {"day": 0, "hour": 10, "title": "📍 Visita Alonso de Córdoba", "color": BLUE},
        {"day": 0, "hour": 14, "title": "📍 Visita Vitacura Loft", "color": PURPLE},
        {"day": 1, "hour": 9, "title": "📞 Llamada cliente", "color": (34, 197, 94)},
        {"day": 1, "hour": 15, "title": "📍 Visita Ñuñoa", "color": BLUE},
        {"day": 2, "hour": 10, "title": "📝 Firma contrato", "color": PURPLE},
        {"day": 3, "hour": 11, "title": "📍 Visita La Reina", "color": BLUE},
    ]
    
    y_base = 120
    hour_height = 60
    
    for event in events:
        x = x_start + event["day"] * 75 + 5
        y = y_base + (event["hour"] - 8) * hour_height
        
        # Event box
        draw_rounded_rect(draw, [x, y, x + 70, y + 50], 8, event["color"])
        draw.text((x + 5, y + 5), event["title"][2:], font=font_small, fill=WHITE)
        draw.text((x + 5, y + 28), f"{event['hour']}:00", font=load_font(10), fill=(200, 200, 255))
    
    # Stats al final
    stats_y = 620
    draw.text((20, stats_y), "📊 Esta semana:", font=font_medium, fill=WHITE)
    draw.text((20, stats_y + 25), "6 visitas agendadas", font=font_small, fill=GRAY)
    draw.text((20, stats_y + 45), "3 llamadas de seguimiento", font=font_small, fill=GRAY)
    
    # Indicador de éxito
    draw_rounded_rect(draw, [20, stats_y + 75, 200, stats_y + 115], 10, (34, 197, 94))
    draw.text((40, stats_y + 88), "✅ Calendario al día", font=font_medium, fill=WHITE)
    
    img.save(filename)
    print(f"✅ Creado: {filename}")

def create_dashboard_mockup(filename):
    """Crea mockup de dashboard del asistente"""
    
    img = create_gradient(PHONE_WIDTH, PHONE_HEIGHT, BG_DARK, SURFACE)
    draw = ImageDraw.Draw(img)
    
    font_title = load_font(18, bold=True)
    font_medium = load_font(14)
    font_small = load_font(12)
    font_large = load_font(32, bold=True)
    
    # Header
    draw.rectangle([0, 0, PHONE_WIDTH, 70], fill=SURFACE)
    draw.text((20, 20), "🤖 VYNXE Asistente", font=font_title, fill=WHITE)
    draw.ellipse([480, 20, 510, 50], fill=BLUE)  # Status online
    
    # Stats cards
    stats = [
        {"label": "Mensajes", "value": "847", "icon": "💬", "color": BLUE},
        {"label": "Respuestas auto", "value": "623", "icon": "⚡", "color": PURPLE},
        {"label": "Tasa respuesta", "value": "98.7%", "icon": "✅", "color": (34, 197, 94)},
        {"label": "Conversión", "value": "34%", "icon": "📈", "color": (251, 191, 36)},
    ]
    
    y = 90
    for i, stat in enumerate(stats):
        x = 20 if i % 2 == 0 else 280
        if i >= 2:
            y = 230
        
        # Card background
        draw_rounded_rect(draw, [x, y, x + 240, y + 120], 15, SURFACE)
        
        # Icon
        draw.text((x + 15, y + 15), stat["icon"], font=load_font(24), fill=WHITE)
        
        # Value
        draw.text((x + 15, y + 50), stat["value"], font=font_large, fill=stat["color"])
        
        # Label
        draw.text((x + 15, y + 90), stat["label"], font=font_small, fill=GRAY)
    
    # Actividad reciente
    y = 370
    draw.text((20, y), "📋 Actividad reciente", font=font_medium, fill=WHITE)
    
    # Lista de actividad
    activities = [
        ("14:32", "Nuevo lead: Juan Pérez", True),
        ("14:28", "Visita agendada: Alonso de Córdoba", True),
        ("14:15", "Pregunta respondida: Precio arriendo", True),
        ("13:58", "Recordatorio enviado", True),
    ]
    
    y += 35
    for time, text, success in activities:
        # Time
        draw.text((20, y), time, font=font_small, fill=GRAY)
        
        # Text
        draw.text((80, y), text, font=font_small, fill=WHITE)
        
        # Checkmark
        draw.text((PHONE_WIDTH - 40, y), "✅", font=font_small, fill=(34, 197, 94))
        
        y += 35
    
    # Botón de acción
    draw_rounded_rect(draw, [20, y + 20, PHONE_WIDTH - 20, y + 70], 15, BLUE)
    draw.text((150, y + 32), "Ver dashboard completo →", font=font_medium, fill=WHITE)
    
    img.save(filename)
    print(f"✅ Creado: {filename}")

def create_comparison_ads(filename):
    """Crea imagen de comparación Antes/Después"""
    
    img = Image.new('RGB', (1080, 1080), BG_DARK)
    draw = ImageDraw.Draw(img)
    
    # Gradiente sutil
    for i in range(1080):
        ratio = i / 1080
        r = int(BG_DARK[0] + (SURFACE[0] - BG_DARK[0]) * ratio * 0.3)
        g = int(BG_DARK[1] + (SURFACE[1] - BG_DARK[1]) * ratio * 0.3)
        b = int(BG_DARK[2] + (SURFACE[2] - BG_DARK[2]) * ratio * 0.3)
        draw.line([(0, i), (1080, i)], fill=(r, g, b))
    
    font_title = load_font(48, bold=True)
    font_medium = load_font(24)
    font_large = load_font(72, bold=True)
    
    # Título
    draw.text((540, 80), "ANTES VS DESPUÉS", font=font_title, fill=WHITE, anchor="mm")
    
    # Lado izquierdo (ANTES)
    draw.rectangle([50, 200, 520, 1000], fill=(40, 20, 20))  # Tono rojo
    
    draw.text((285, 250), "😰", font=load_font(80), fill=WHITE, anchor="mm")
    draw.text((285, 370), "ANTES", font=font_title, fill=(255, 100, 100), anchor="mm")
    
    items_antes = [
        "WhatsApp hasta las 12am",
        "Clientes perdidos",
        "Visitas desorganizadas",
        "Estrés constante"
    ]
    y = 470
    for item in items_antes:
        draw.text((285, y), "❌ " + item, font=font_medium, fill=(255, 150, 150), anchor="mm")
        y += 60
    
    # Lado derecho (DESPUÉS)
    draw.rectangle([560, 200, 1030, 1000], fill=(20, 40, 20))  # Tono verde
    
    draw.text((795, 250), "😌", font=load_font(80), fill=WHITE, anchor="mm")
    draw.text((795, 370), "DESPUÉS", font=font_title, fill=(100, 255, 150), anchor="mm")
    
    items_despues = [
        "Respuestas instantáneas",
        "Solo clientes serios",
        "Visitas agendadas",
        "15 min por la mañana"
    ]
    y = 470
    for item in items_despues:
        draw.text((795, y), "✅ " + item, font=font_medium, fill=(150, 255, 180), anchor="mm")
        y += 60
    
    # Línea central
    draw.line([(540, 200), (540, 1000)], fill=WHITE, width=3)
    
    # Precio al final
    draw.text((540, 1020), "💰 Desde $199K/mes", font=font_medium, fill=BLUE, anchor="mm")
    
    img.save(filename)
    print(f"✅ Creado: {filename}")

def create_price_card(filename):
    """Crea imagen de precio para ads"""
    
    img = Image.new('RGB', (1080, 1080), BG_DARK)
    draw = ImageDraw.Draw(img)
    
    # Gradiente
    for i in range(1080):
        ratio = i / 1080
        r = int(BLUE[0] + (PURPLE[0] - BLUE[0]) * ratio)
        g = int(BLUE[1] + (PURPLE[1] - BLUE[1]) * ratio)
        b = int(BLUE[2] + (PURPLE[2] - BLUE[2]) * ratio)
        if i > 400 and i < 680:
            draw.line([(0, i), (1080, i)], fill=(r, g, b))
    
    font_large = load_font(80, bold=True)
    font_medium = load_font(28)
    font_small = load_font(20)
    
    # Precio principal
    draw.text((540, 300), "$199K", font=font_large, fill=WHITE, anchor="mm")
    draw.text((540, 400), "por mes", font=font_medium, fill=GRAY, anchor="mm")
    
    # Características
    features = [
        "✓ Asistente WhatsApp 24/7",
        "✓ Agenda automático",
        "✓ Separa leads reales",
        "✓ Configuración en 1 semana",
        "✓ Soporte incluido"
    ]
    
    y = 500
    for feature in features:
        draw.text((540, y), feature, font=font_small, fill=WHITE, anchor="mm")
        y += 50
    
    # CTA
    draw_rounded_rect(draw, [290, 800, 790, 900], 20, BLUE)
    draw.text((540, 850), "💬 Escríbeme para más info", font=font_medium, fill=WHITE, anchor="mm")
    
    img.save(filename)
    print(f"✅ Creado: {filename}")

# Crear directorio de salida
output_dir = "/home/ubuntu/.openclaw/workspace/vynxe-social-assets"
os.makedirs(output_dir, exist_ok=True)

print("🎨 Generando mockups de VYNXE...")
print("=" * 50)

# 1. WhatsApp mocks
create_whatsapp_mockup(
    f"{output_dir}/whatsapp-chat-1.png",
    messages=[
        {"sent_by": "contact", "text": "Hola! Vi la propiedad en elportal. Está disponible?", "width": 320, "height": 50},
        {"sent_by": "vynxe", "text": "¡Hola! 👋 Sí, la propiedad en Alonso de Córdoba está disponible. ¿Te gustaría agendar una visita?", "width": 400, "height": 60},
        {"sent_by": "contact", "text": "Sí! Puedo mañana a las 3pm?", "width": 250, "height": 50},
        {"sent_by": "vynxe", "text": "¡Perfecto! Te confirmo: Mañana miércoles a las 3pm en Alonso de Córdoba 5600. Te envío recordatorio 1 hora antes.", "width": 450, "height": 70},
        {"sent_by": "contact", "text": "Excelente, gracias!", "width": 200, "height": 45},
    ]
)

create_whatsapp_mockup(
    f"{output_dir}/whatsapp-chat-2.png",
    messages=[
        {"sent_by": "contact", "text": "Cuánto cuesta el arriendo?", "width": 250, "height": 45},
        {"sent_by": "vynxe", "text": "$850.000 mensuales + 2 meses de garantía", "width": 350, "height": 50},
        {"sent_by": "contact", "text": "Tiene estacionamiento?", "width": 240, "height": 45},
        {"sent_by": "vynxe", "text": "Sí, incluye 1 estacionamiento cubierto", "width": 330, "height": 50},
        {"sent_by": "contact", "text": "Puedo ir a ver mañana?", "width": 220, "height": 45},
        {"sent_by": "vynxe", "text": "¡Claro! Tenemos disponible mañana a las 10am, 2pm o 4pm. ¿Cuál prefieres?", "width": 420, "height": 65},
    ]
)

# 2. Calendar mock
create_calendar_mockup(f"{output_dir}/calendar-week.png")

# 3. Dashboard mock
create_dashboard_mockup(f"{output_dir}/dashboard-stats.png")

# 4. Ads comparison
create_comparison_ads(f"{output_dir}/ad-comparison.png")

# 5. Price card
create_price_card(f"{output_dir}/ad-price.png")

print("=" * 50)
print(f"✅ Todos los assets en: {output_dir}/")
print("\nArchivos generados:")
for f in sorted(os.listdir(output_dir)):
    print(f"  📷 {f}")
