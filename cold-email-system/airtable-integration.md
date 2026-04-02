# Integración Cold Email + Airtable

## 🎯 Objetivo
Conectar el sistema de cold emails con Airtable para gestión visual del pipeline y sincronización automática.

---

## 📋 Paso 1: Crear Base en Airtable

### 1.1 Crear nueva base
1. Ve a https://airtable.com
2. Clic en "Add a base" → "Create a new base"
3. Nombre: "Pipeline Agencias Inmobiliarias"

### 1.2 Configurar tabla "Agencias"

Crear tabla con estos campos:

| Campo | Tipo | Opciones/Configuración |
|-------|------|------------------------|
| ID | Single line text | (Primary field) |
| Nombre Agencia | Single line text | |
| Website | URL | |
| Email | Email | |
| Teléfono | Phone | |
| Nombre Contacto | Single line text | |
| Cargo | Single select | Gerente Comercial, Director, CEO, Otro |
| Zona | Single select | Las Condes, Vitacura, Providencia, Ñuñoa, Santiago, Otro |
| # Corredores | Number | Integer |
| Prioridad | Single select | ALTA, MEDIA, BAJA |
| Estado | Single select | POR_CONTACTAR, EMAIL_ENVIADO, RESPONDIO, REUNION_AGENDADA, PILOTO_PROPUESTO, PILOTO_ACEPTADO, CLIENTE, NO_INTERESADO, NO_CALIFICA |
| Email Enviado | Date | Include time: No |
| Respondió | Date | Include time: No |
| Reunión Agendada | Date | Include time: Yes |
| Piloto Aceptado | Date | Include time: No |
| Score Oportunidad | Number | Integer, 0-100 |
| Notas | Long text | |
| Secuencia Activa | Single select | Día 0, Día 3, Día 7, Día 14, Completada |
| Último Contacto | Date | Include time: No |
| Próximo Paso | Single line text | |
| Fecha Próximo Paso | Date | Include time: No |
| Asunto Email | Single line text | |
| Contenido Email | Long text | |

### 1.3 Crear vista "Kanban"

1. Clic en "Grid" (arriba izquierda) → "Create new view" → "Kanban"
2. Campo de agrupación: "Estado"
3. Colores por prioridad: ALTA (rojo), MEDIA (amarillo), BAJA (verde)

### 1.4 Crear vista "Pipeline"

1. "Create new view" → "Grid"
2. Filtros: Estado ≠ NO_INTERESADO, Estado ≠ NO_CALIFICA
3. Ordenar por: Prioridad (descendente), Fecha Próximo Paso (ascendente)

---

## 📋 Paso 2: Obtener API Key y Base ID

### 2.1 API Key Personal

1. Ve a https://airtable.com/create/tokens
2. Clic en "Create new token"
3. Nombre: "Cold Email System"
4. Scopes: 
   - ✅ data.records:read
   - ✅ data.records:write
   - ✅ schema.bases:read
5. Access: Add base → Selecciona tu base "Pipeline Agencias Inmobiliarias"
6. Copia el token (empieza con `pat...`)

### 2.2 Base ID

1. Abre tu base en Airtable
2. Mira la URL: `https://airtable.com/appXXXXXXXXXXXXXX/...`
3. El ID es la parte `appXXXXXXXXXXXXXX` (17 caracteres, empieza con `app`)

### 2.3 Table ID (opcional)

1. Ve a https://airtable.com/api
2. Selecciona tu base
3. Busca el `tableId` (empieza con `tbl`)

---

## 📋 Paso 3: Configurar Variables de Entorno

Agrega estas variables a tu `~/.bashrc` o `~/.zshrc`:

```bash
# Airtable Configuration
export AIRTABLE_API_KEY="patXXXXXXXXXXXXXX.XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
export AIRTABLE_BASE_ID="appXXXXXXXXXXXXXX"
export AIRTABLE_TABLE_NAME="Agencias"
```

Luego recarga:
```bash
source ~/.bashrc  # o ~/.zshrc
```

---

## 📋 Paso 4: Scripts de Integración

### 4.1 Script: CSV → Airtable (Importar agencias)

Crear archivo: `scripts/csv-to-airtable.py`

```python
#!/usr/bin/env python3
"""
Importar agencias desde CSV a Airtable
Uso: python3 csv-to-airtable.py ../agencias.csv
"""

import csv
import json
import os
import sys
import urllib.request
import urllib.error

# Configuración
AIRTABLE_API_KEY = os.environ.get('AIRTABLE_API_KEY')
AIRTABLE_BASE_ID = os.environ.get('AIRTABLE_BASE_ID')
AIRTABLE_TABLE_NAME = os.environ.get('AIRTABLE_TABLE_NAME', 'Agencias')

if not AIRTABLE_API_KEY or not AIRTABLE_BASE_ID:
    print("❌ Error: Configura AIRTABLE_API_KEY y AIRTABLE_BASE_ID")
    sys.exit(1)

BASE_URL = f"https://api.airtable.com/v0/{AIRTABLE_BASE_ID}/{AIRTABLE_TABLE_NAME}"

def airtable_request(method, url, data=None):
    """Hacer request a Airtable API"""
    headers = {
        'Authorization': f'Bearer {AIRTABLE_API_KEY}',
        'Content-Type': 'application/json'
    }
    
    req = urllib.request.Request(url, method=method)
    for key, value in headers.items():
        req.add_header(key, value)
    
    if data:
        req.data = json.dumps(data).encode('utf-8')
    
    try:
        with urllib.request.urlopen(req) as response:
            return json.loads(response.read().decode('utf-8'))
    except urllib.error.HTTPError as e:
        print(f"❌ Error HTTP {e.code}: {e.read().decode()}")
        return None

def create_record(fields):
    """Crear un registro en Airtable"""
    data = {"fields": fields}
    result = airtable_request('POST', BASE_URL, data)
    if result:
        print(f"✅ Creado: {fields.get('Nombre Agencia', 'Sin nombre')}")
    return result

def csv_to_airtable(csv_file):
    """Importar CSV a Airtable"""
    records_created = 0
    records_failed = 0
    
    with open(csv_file, 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        
        for row in reader:
            # Mapear campos CSV a Airtable
            fields = {
                'ID': row['ID'],
                'Nombre Agencia': row['Nombre_Agencia'],
                'Website': row['Website'],
                'Email': row['Email'],
                'Teléfono': row['Telefono'],
                'Nombre Contacto': row['Nombre_Contacto'],
                'Cargo': row['Cargo'],
                'Zona': row['Zona'],
                '# Corredores': int(row['Num_Corredores']) if row['Num_Corredores'] else 0,
                'Prioridad': row['Prioridad'],
                'Estado': row['Estado'],
                'Score Oportunidad': int(row['Score_Oportunidad']) if row['Score_Oportunidad'] else 0,
                'Notas': row['Notas'],
                'Secuencia Activa': row['Secuencia_Activa'],
                'Próximo Paso': row['Proximo_Paso'],
            }
            
            # Fechas (solo si tienen valor)
            if row['Email_Enviado']:
                fields['Email Enviado'] = row['Email_Enviado']
            if row['Fecha_Proximo_Paso']:
                fields['Fecha Próximo Paso'] = row['Fecha_Proximo_Paso']
            
            if create_record(fields):
                records_created += 1
            else:
                records_failed += 1
            
            # Rate limiting: max 5 req/segundo
            import time
            time.sleep(0.2)
    
    print(f"\n📊 Resumen:")
    print(f"✅ Creados: {records_created}")
    print(f"❌ Fallidos: {records_failed}")

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("Uso: python3 csv-to-airtable.py <archivo.csv>")
        sys.exit(1)
    
    csv_to_airtable(sys.argv[1])
```

Hacer ejecutable:
```bash
chmod +x scripts/csv-to-airtable.py
```

### 4.2 Script: Actualizar estado en Airtable

Crear archivo: `scripts/update-airtable-status.py`

```python
#!/usr/bin/env python3
"""
Actualizar estado de una agencia en Airtable
Uso: python3 update-airtable-status.py <ID_AGENCIA> <NUEVO_ESTADO> [NOTAS]
"""

import json
import os
import sys
import urllib.request
import urllib.error
from datetime import datetime

AIRTABLE_API_KEY = os.environ.get('AIRTABLE_API_KEY')
AIRTABLE_BASE_ID = os.environ.get('AIRTABLE_BASE_ID')
AIRTABLE_TABLE_NAME = os.environ.get('AIRTABLE_TABLE_NAME', 'Agencias')

BASE_URL = f"https://api.airtable.com/v0/{AIRTABLE_BASE_ID}/{AIRTABLE_TABLE_NAME}"

def airtable_request(method, url, data=None):
    headers = {
        'Authorization': f'Bearer {AIRTABLE_API_KEY}',
        'Content-Type': 'application/json'
    }
    
    req = urllib.request.Request(url, method=method)
    for key, value in headers.items():
        req.add_header(key, value)
    
    if data:
        req.data = json.dumps(data).encode('utf-8')
    
    try:
        with urllib.request.urlopen(req) as response:
            return json.loads(response.read().decode('utf-8'))
    except urllib.error.HTTPError as e:
        print(f"❌ Error HTTP {e.code}: {e.read().decode()}")
        return None

def find_record_by_id(agency_id):
    """Buscar registro por ID de agencia"""
    formula = f"{{ID}}='{agency_id}'"
    url = f"{BASE_URL}?filterByFormula={urllib.parse.quote(formula)}"
    
    result = airtable_request('GET', url)
    if result and result.get('records'):
        return result['records'][0]['id']  # Airtable record ID
    return None

def update_record(record_id, fields):
    """Actualizar registro"""
    url = f"{BASE_URL}/{record_id}"
    data = {"fields": fields}
    
    result = airtable_request('PATCH', url, data)
    if result:
        print(f"✅ Actualizado: {fields}")
    return result

if __name__ == '__main__':
    if len(sys.argv) < 3:
        print("Uso: python3 update-airtable-status.py <ID_AGENCIA> <NUEVO_ESTADO> [NOTAS]")
        sys.exit(1)
    
    agency_id = sys.argv[1]
    new_status = sys.argv[2]
    notes = sys.argv[3] if len(sys.argv) > 3 else ""
    
    # Buscar registro
    record_id = find_record_by_id(agency_id)
    if not record_id:
        print(f"❌ No se encontró agencia con ID: {agency_id}")
        sys.exit(1)
    
    # Preparar campos a actualizar
    fields = {
        'Estado': new_status,
        'Último Contacto': datetime.now().strftime('%Y-%m-%d'),
    }
    
    # Actualizar fechas según estado
    if new_status == 'EMAIL_ENVIADO':
        fields['Email Enviado'] = datetime.now().strftime('%Y-%m-%d')
        fields['Secuencia Activa'] = 'Día 0'
    elif new_status == 'RESPONDIO':
        fields['Respondió'] = datetime.now().strftime('%Y-%m-%d')
    elif new_status == 'REUNION_AGENDADA':
        fields['Reunión Agendada'] = datetime.now().strftime('%Y-%m-%dT%H:%M:%S')
    elif new_status == 'PILOTO_ACEPTADO':
        fields['Piloto Aceptado'] = datetime.now().strftime('%Y-%m-%d')
    
    if notes:
        fields['Notas'] = notes
    
    # Actualizar
    update_record(record_id, fields)
```

### 4.3 Script: Sincronización Bidireccional

Crear archivo: `scripts/sync-airtable-csv.py`

```python
#!/usr/bin/env python3
"""
Sincronizar datos entre Airtable y CSV
Uso: python3 sync-airtable-csv.py --to-csv (exportar Airtable a CSV)
       python3 sync-airtable-csv.py --to-airtable (importar CSV a Airtable)
"""

import argparse
import csv
import json
import os
import urllib.request

AIRTABLE_API_KEY = os.environ.get('AIRTABLE_API_KEY')
AIRTABLE_BASE_ID = os.environ.get('AIRTABLE_BASE_ID')
AIRTABLE_TABLE_NAME = os.environ.get('AIRTABLE_TABLE_NAME', 'Agencias')
CSV_FILE = '../agencias.csv'

BASE_URL = f"https://api.airtable.com/v0/{AIRTABLE_BASE_ID}/{AIRTABLE_TABLE_NAME}"

def get_all_records():
    """Obtener todos los registros de Airtable"""
    records = []
    offset = None
    
    while True:
        url = BASE_URL
        if offset:
            url += f"?offset={offset}"
        
        req = urllib.request.Request(url)
        req.add_header('Authorization', f'Bearer {AIRTABLE_API_KEY}')
        
        with urllib.request.urlopen(req) as response:
            data = json.loads(response.read().decode())
            records.extend(data.get('records', []))
            
            offset = data.get('offset')
            if not offset:
                break
    
    return records

def export_to_csv():
    """Exportar Airtable a CSV"""
    records = get_all_records()
    
    fieldnames = [
        'ID', 'Nombre_Agencia', 'Website', 'Email', 'Telefono',
        'Nombre_Contacto', 'Cargo', 'Zona', 'Num_Corredores',
        'Prioridad', 'Estado', 'Email_Enviado', 'Respondio',
        'Reunion_Agendada', 'Piloto_Aceptado', 'Score_Oportunidad',
        'Notas', 'Secuencia_Activa', 'Ultimo_Contacto',
        'Proximo_Paso', 'Fecha_Proximo_Paso'
    ]
    
    with open(CSV_FILE, 'w', newline='', encoding='utf-8') as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        
        for record in records:
            fields = record['fields']
            writer.writerow({
                'ID': fields.get('ID', ''),
                'Nombre_Agencia': fields.get('Nombre Agencia', ''),
                'Website': fields.get('Website', ''),
                'Email': fields.get('Email', ''),
                'Telefono': fields.get('Teléfono', ''),
                'Nombre_Contacto': fields.get('Nombre Contacto', ''),
                'Cargo': fields.get('Cargo', ''),
                'Zona': fields.get('Zona', ''),
                'Num_Corredores': fields.get('# Corredores', ''),
                'Prioridad': fields.get('Prioridad', ''),
                'Estado': fields.get('Estado', ''),
                'Email_Enviado': fields.get('Email Enviado', ''),
                'Respondio': fields.get('Respondió', ''),
                'Reunion_Agendada': fields.get('Reunión Agendada', ''),
                'Piloto_Aceptado': fields.get('Piloto Aceptado', ''),
                'Score_Oportunidad': fields.get('Score Oportunidad', ''),
                'Notas': fields.get('Notas', ''),
                'Secuencia_Activa': fields.get('Secuencia Activa', ''),
                'Ultimo_Contacto': fields.get('Último Contacto', ''),
                'Proximo_Paso': fields.get('Próximo Paso', ''),
                'Fecha_Proximo_Paso': fields.get('Fecha Próximo Paso', ''),
            })
    
    print(f"✅ Exportados {len(records)} registros a {CSV_FILE}")

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Sincronizar Airtable y CSV')
    parser.add_argument('--to-csv', action='store_true', help='Exportar Airtable a CSV')
    parser.add_argument('--to-airtable', action='store_true', help='Importar CSV a Airtable')
    
    args = parser.parse_args()
    
    if args.to_csv:
        export_to_csv()
    elif args.to_airtable:
        print("Usa csv-to-airtable.py para importar")
    else:
        parser.print_help()
```

---

## 📋 Paso 5: Workflow de Uso Diario

### Escenario 1: Enviar emails y actualizar Airtable

```bash
# 1. Enviar emails desde CSV
./cold-email-sender.sh

# 2. Actualizar Airtable con los envíos
python3 airtable/update-airtable-status.py 001 EMAIL_ENVIADO
python3 airtable/update-airtable-status.py 002 EMAIL_ENVIADO
# ... o crear un script que lea el log y actualice automáticamente
```

### Escenario 2: Recibir respuesta y actualizar

```bash
# Cuando un corredor responde al email
python3 airtable/update-airtable-status.py 001 RESPONDIO "Interesado en reunión el jueves"
```

### Escenario 3: Agendar reunión

```bash
python3 airtable/update-airtable-status.py 001 REUNION_AGENDADA "Reunión Zoom confirmada"
```

### Escenario 4: Ver pipeline en Airtable

1. Abre tu base en Airtable
2. Ve a la vista "Kanban"
3. Arrastra tarjetas entre columnas según avancen
4. Usa la vista "Pipeline" para ver prioridades

---

## 📋 Paso 6: Automatización con n8n (Opcional Avanzado)

Si quieres automatizar completamente, puedes usar n8n para:

### Trigger 1: Webhook de respuesta de email
- Detectar cuando alguien responde a tu Gmail
- Buscar agencia en Airtable por email
- Actualizar estado a "RESPONDIO"

### Trigger 2: Follow-ups automáticos
- Revisar Airtable diariamente
- Encontrar agencias con EMAIL_ENVIADO y fecha +3 días
- Enviar follow-up automático
- Actualizar Secuencia Activa

### Trigger 3: Alertas de leads HOT
- Cuando Score Oportunidad > 80
- Enviar notificación a tu email/Telegram
- Crear tarea de seguimiento

---

## 📊 Estructura de Costos

| Componente | Costo | Notas |
|------------|-------|-------|
| Airtable Free | $0 | Hasta 1,200 registros por base |
| Airtable Plus | $12/mes | Ilimitados registros, 5GB adjuntos |
| API Calls | Limitado | 5 requests/segundo en free |

---

## ✅ Checklist de Setup

- [ ] Crear base en Airtable con tabla "Agencias"
- [ ] Configurar todos los campos según especificaciones
- [ ] Crear vistas Kanban y Pipeline
- [ ] Generar Personal Access Token en Airtable
- [ ] Configurar variables de entorno (.bashrc)
- [ ] Probar conexión: `python3 csv-to-airtable.py agencias.csv`
- [ ] Verificar que los registros aparecen en Airtable
- [ ] Crear backup: Exportar Airtable a CSV semanalmente

---

## 🚨 Solución de Problemas

### Error 401: Unauthorized
- Verificar que el token sea correcto
- Confirmar que el token tiene acceso a la base

### Error 422: INVALID_VALUE
- Verificar que los valores de single select coincidan exactamente
- Revisar que los números no tengan texto

### Rate Limiting (429)
- Airtable permite ~5 requests/segundo
- Los scripts ya incluyen `time.sleep(0.2)` entre requests

### Campos no aparecen
- Verificar que los nombres de campo coincidan exactamente (case sensitive)
- Usar `AIRTABLE_GET_BASE_SCHEMA` para ver el schema real

---

*Integración creada el 2 de abril de 2026*
*Skills: airtable-automation, airtable-integration*
