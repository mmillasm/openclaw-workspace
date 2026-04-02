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
import time

# Configuración
AIRTABLE_API_KEY = os.environ.get('AIRTABLE_API_KEY')
AIRTABLE_BASE_ID = os.environ.get('AIRTABLE_BASE_ID')
AIRTABLE_TABLE_NAME = os.environ.get('AIRTABLE_TABLE_NAME', 'Agencias')

if not AIRTABLE_API_KEY or not AIRTABLE_BASE_ID:
    print("❌ Error: Configura las variables de entorno:")
    print("   export AIRTABLE_API_KEY='patXXXX...'")
    print("   export AIRTABLE_BASE_ID='appXXXX...'")
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
            fields = {}
            
            if row.get('ID'): fields['ID'] = row['ID']
            if row.get('Nombre_Agencia'): fields['Nombre Agencia'] = row['Nombre_Agencia']
            if row.get('Website'): fields['Website'] = row['Website']
            if row.get('Email'): fields['Email'] = row['Email']
            if row.get('Telefono'): fields['Teléfono'] = row['Telefono']
            if row.get('Nombre_Contacto'): fields['Nombre Contacto'] = row['Nombre_Contacto']
            if row.get('Cargo'): fields['Cargo'] = row['Cargo']
            if row.get('Zona'): fields['Zona'] = row['Zona']
            if row.get('Num_Corredores'): 
                try:
                    fields['# Corredores'] = int(row['Num_Corredores'])
                except:
                    pass
            if row.get('Prioridad'): fields['Prioridad'] = row['Prioridad']
            if row.get('Estado'): fields['Estado'] = row['Estado']
            if row.get('Score_Oportunidad'):
                try:
                    fields['Score Oportunidad'] = int(row['Score_Oportunidad'])
                except:
                    pass
            if row.get('Notas'): fields['Notas'] = row['Notas']
            if row.get('Secuencia_Activa'): fields['Secuencia Activa'] = row['Secuencia_Activa']
            if row.get('Proximo_Paso'): fields['Próximo Paso'] = row['Proximo_Paso']
            if row.get('Email_Enviado'): fields['Email Enviado'] = row['Email_Enviado']
            if row.get('Fecha_Proximo_Paso'): fields['Fecha Próximo Paso'] = row['Fecha_Proximo_Paso']
            
            if create_record(fields):
                records_created += 1
            else:
                records_failed += 1
            
            # Rate limiting: max 5 req/segundo
            time.sleep(0.2)
    
    print(f"\n📊 Resumen:")
    print(f"✅ Creados: {records_created}")
    print(f"❌ Fallidos: {records_failed}")

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("Uso: python3 csv-to-airtable.py <archivo.csv>")
        sys.exit(1)
    
    csv_to_airtable(sys.argv[1])
