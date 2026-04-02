#!/usr/bin/env python3
"""
Actualizar estado de una agencia en Airtable
Uso: python3 update-airtable-status.py <ID_AGENCIA> <NUEVO_ESTADO> [NOTAS]
Ejemplo: python3 update-airtable-status.py 001 RESPONDIO "Interesado en reunión"
"""

import json
import os
import sys
import urllib.request
import urllib.error
import urllib.parse
from datetime import datetime

AIRTABLE_API_KEY = os.environ.get('AIRTABLE_API_KEY')
AIRTABLE_BASE_ID = os.environ.get('AIRTABLE_BASE_ID')
AIRTABLE_TABLE_NAME = os.environ.get('AIRTABLE_TABLE_NAME', 'Agencias')

if not AIRTABLE_API_KEY or not AIRTABLE_BASE_ID:
    print("❌ Error: Configura AIRTABLE_API_KEY y AIRTABLE_BASE_ID")
    sys.exit(1)

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
        agencia = result['fields'].get('Nombre Agencia', 'Desconocida')
        print(f"✅ Actualizado {agencia}: {fields}")
    return result

if __name__ == '__main__':
    if len(sys.argv) < 3:
        print("Uso: python3 update-airtable-status.py <ID_AGENCIA> <NUEVO_ESTADO> [NOTAS]")
        print("")
        print("Estados válidos:")
        print("  POR_CONTACTAR, EMAIL_ENVIADO, RESPONDIO, REUNION_AGENDADA,")
        print("  PILOTO_PROPUESTO, PILOTO_ACEPTADO, CLIENTE, NO_INTERESADO")
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
    today = datetime.now().strftime('%Y-%m-%d')
    now = datetime.now().strftime('%Y-%m-%dT%H:%M:%S')
    
    if new_status == 'EMAIL_ENVIADO':
        fields['Email Enviado'] = today
        fields['Secuencia Activa'] = 'Día 0'
    elif new_status == 'RESPONDIO':
        fields['Respondió'] = today
    elif new_status == 'REUNION_AGENDADA':
        fields['Reunión Agendada'] = now
    elif new_status == 'PILOTO_ACEPTADO':
        fields['Piloto Aceptado'] = today
    
    if notes:
        fields['Notas'] = notes
    
    # Actualizar
    update_record(record_id, fields)
