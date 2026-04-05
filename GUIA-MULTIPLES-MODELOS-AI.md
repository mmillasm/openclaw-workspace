# 🤖 GUÍA: Configurar Múltiples Proveedores de Modelos AI

> **Objetivo:** Configurar OpenClaw con múltiples proveedores de modelos AI para tener fallbacks automáticos y nunca quedarse sin capacidad de respuesta.
> **Fecha:** 5 de abril de 2026

---

## 📊 CONFIGURACIÓN ACTUAL DE ADER

Tu configuración actual en `openclaw.json`:

```json
{
  "agents": {
    "defaults": {
      "model": {
        "primary": "opencode-go/minimax-m2.5",
        "fallbacks": [
          "opencode-go/minimax-m2.7",
          "opencode-go/nemotron-3-super-free",
          "opencode-go/big-pickle",
          "opencode-go/mimo-v2-pro-free",
          "nvidia/meta/llama-3.1-70b-instruct"
        ]
      }
    }
  }
}
```

**Proveedores ya configurados:**
- ✅ OpenCode (MiniMax, Nemotron, Big Pickle, Mimo) — **GRATIS**
- ✅ NVIDIA (Llama 3.1 70B) — API key configurada

---

## 🏗️ ARQUITECTURA: Cómo Funciona el Failover

OpenClaw maneja fallos en **2 etapas**:

```
1️⃣ AUTH PROFILE ROTATION (dentro del mismo proveedor)
   └─ Si tienes múltiples API keys del mismo provider, las rota
   
2️⃣ MODEL FALLBACK (cambio a otro modelo/provider)
   └─ Si todos los profiles del provider fallan, pasa al siguiente
```

### Tipos de Errores y Comportamiento:

| Error | Comportamiento |
|-------|----------------|
| **Rate limit** | Cooldown corto (1-5 min), intenta siguiente profile |
| **Auth failure** | Cooldown, rotación a siguiente profile |
| **Timeout** | Retry con backoff, luego failover |
| **Billing error** | Cooldown largo (5h-24h), marca profile como disabled |

### Cooldowns por Defecto:

```
1ra falla → 1 minuto
2da falla → 5 minutos  
3ra falla → 25 minutos
4ta falla → 1 hora (capped)
```

---

## 📝 ESTRUCTURA DE CONFIGURACIÓN

### Formato de Modelos:
```
provider/model
Ejemplos:
- opencode-go/minimax-m2.5
- anthropic/claude-sonnet-4-6
- openai/gpt-4o
- google/gemini-1.5-pro
- nvidia/meta/llama-3.1-70b-instruct
```

### Jerarquía de Configuración:

```json5
{
  "models": {
    "mode": "merge",  // merge = combina configs
    "providers": {
      "provider-name": {
        "baseUrl": "https://api.provider.com",
        "apiKey": "YOUR_API_KEY",
        "api": "openai-completions | anthropic | etc",
        "models": [
          {
            "id": "model-id",
            "name": "Human Readable Name",
            "cost": { "input": 0, "output": 0 },
            "contextWindow": 128000,
            "maxTokens": 4096
          }
        ]
      }
    }
  },
  
  "agents": {
    "defaults": {
      "model": {
        "primary": "provider/model",      // Modelo principal
        "fallbacks": [
          "provider/model",                // Intento 2
          "provider/model",                // Intento 3
          "provider/model"                 // Intento 4...
        ]
      },
      "imageModel": {                     // Modelos para imágenes
        "primary": "provider/model",
        "fallbacks": ["provider/model"]
      }
    }
  }
}
```

---

## 🔧 PROVEEDORES SOPORTADOS

### Gratuitos/Semigratuitos:
| Provider | Modelos | Costo | Calidad |
|----------|---------|-------|---------|
| **OpenCode** | MiniMax, Nemotron, Big Pickle, Mimo | **GRATIS** | Buena |
| **Ollama** | Qwen, Llama, Mistral (locales) | **GRATIS** | Variable |
| **Groq** | Llama, Mixtral | **Gratis (rate limited)** | Alta |
| **HuggingFace** | Inference Endpoints | Pay-per-use | Variable |

### Pago por Uso:
| Provider | Modelos Populares | Costo Aproximado |
|----------|-------------------|------------------|
| **Anthropic** | Claude Opus, Sonnet, Haiku | $3-15/1M tokens |
| **OpenAI** | GPT-4o, GPT-4o mini | $2.5-15/1M tokens |
| **Google** | Gemini 1.5 Pro, Flash | $0.125-3.5/1M tokens |
| **NVIDIA** | Llama 3.1 70B | ~$0.9/1M tokens |
| **OpenRouter** | Modelos múltiples | Variable por modelo |

### Recomendación para Ader (VYNXE):
```
Primary: opencode-go/minimax-m2.5 (GRATIS, rápido)
Fallback 1: opencode-go/minimax-m2.7 (GRATIS, mejor calidad)
Fallback 2: anthropic/claude-sonnet-4-6 (PAGO, máxima calidad)
Fallback 3: google/gemini-1.5-flash (PAGO, económico)
```

---

## ⚡ COMO AGREGAR UN NUEVO PROVEEDOR

### Paso 1: Obtener API Key del proveedor

### Paso 2: Ejecutar onboard o agregar manualmente

```bash
# Opción A: Onboard interactivo
openclaw onboard

# Opción B: Editar openclaw.json manualmente
```

### Paso 3: Configurar en `models.providers`

```json
{
  "models": {
    "providers": {
      "anthropic": {
        "apiKey": "sk-ant-...",
        "api": "anthropic",
        "models": [
          {
            "id": "claude-sonnet-4-6",
            "name": "Claude Sonnet 4",
            "contextWindow": 200000,
            "maxTokens": 8192,
            "cost": {
              "input": 3,
              "output": 15
            }
          }
        ]
      }
    }
  }
}
```

### Paso 4: Agregar a fallbacks

```json
{
  "agents": {
    "defaults": {
      "model": {
        "primary": "opencode-go/minimax-m2.5",
        "fallbacks": [
          "opencode-go/minimax-m2.7",
          "anthropic/claude-sonnet-4-6",
          "google/gemini-1.5-flash"
        ]
      }
    }
  }
}
```

---

## 🔐 GESTIÓN DE MÚLTIPLES API KEYS

### Un solo provider con múltiples keys:

```json
{
  "auth": {
    "profiles": {
      "openai:key1": {
        "provider": "openai",
        "mode": "api_key"
      },
      "openai:key2": {
        "provider": "openai", 
        "mode": "api_key"
      }
    },
    "order": {
      "openai": ["openai:key1", "openai:key2"]
    }
  }
}
```

### Variables de entorno para API Keys:

```bash
# Formatos aceptados
OPENAI_API_KEY=sk-...
OPENAI_API_KEY_1=sk-...
OPENAI_API_KEY_2=sk-...

ANTHROPIC_API_KEY=sk-ant-...
GOOGLE_API_KEY=AIza...
```

### Orden de rotación:

1. **Explicit config:** `auth.order[provider]`
2. **Configured profiles:** `auth.profiles`
3. **Stored profiles:** en `auth-profiles.json`
4. **Round-robin:** OAuth antes que API keys, luego por lastUsed

---

## 🎯 ESTRATEGIA RECOMENDADA PARA VYNXE

### Configuración Óptima (Costo/Beneficio):

```json
{
  "agents": {
    "defaults": {
      "model": {
        "primary": "opencode-go/minimax-m2.5",
        "fallbacks": [
          "opencode-go/minimax-m2.7",
          "opencode-go/nemotron-3-super-free",
          "anthropic/claude-sonnet-4-6",
          "google/gemini-1.5-flash"
        ]
      }
    },
    "subagents": {
      "model": {
        "primary": "opencode-go/minimax-m2.5",
        "fallbacks": [
          "opencode-go/minimax-m2.7",
          "google/gemini-1.5-flash"
        ]
      }
    }
  }
}
```

### Estrategia por Tipo de Tarea:

| Tarea | Modelo Recomendado | Proveedor |
|-------|-------------------|-----------|
| **Respuestas rápidas** | minimax-m2.5 | OpenCode (gratis) |
| **Copy importante** | claude-sonnet-4-6 | Anthropic (pago) |
| **Código complejo** | gpt-4o | OpenAI (pago) |
| **Research/análisis** | claude-opus-4-6 | Anthropic (pago) |
| **Tareas pesadas subagentes** | nemotron-3-super-free | OpenCode (gratis) |

### Override por sesión:

```
/model anthropic/claude-sonnet-4-6
```

Esto fuerza el modelo para la sesión actual.

---

## 🔄 COMANDOS ÚTILES

```bash
# Listar modelos disponibles
openclaw models list

# Cambiar modelo por defecto
openclaw models set provider/model

# Ver estado del gateway
openclaw gateway status

# Onboard (configurar nuevos providers)
openclaw onboard

# Ver modelos activos
openclaw status
```

---

## ⚠️ ERRORES COMUNES Y SOLUCIONES

| Error | Causa | Solución |
|-------|-------|----------|
| `Invalid API key` | Key incorrecta o expirada | Verificar API key en dashboard del provider |
| `Rate limit exceeded` | Muchas requests | Esperar cooldown o agregar fallback |
| `Model not found` | ID de modelo incorrecto | Usar `openclaw models list` |
| `Context length exceeded` | Prompt muy largo | Reducir contexto o usar modelo con mayor context window |
| `Billing failed` | Sin crédito | Recargar credits del provider |

---

## 📊 STORAGE DE AUTH PROFILES

Los profiles se guardan en:
```
~/.openclaw/agents/<agentId>/agent/auth-profiles.json
```

Estructura:
```json
{
  "profiles": {
    "provider:profileId": {
      "provider": "openai",
      "type": "api_key",
      "key": "encrypted_key"
    }
  },
  "usageStats": {
    "provider:profileId": {
      "lastUsed": 1736160000000,
      "cooldownUntil": 1736160600000,
      "errorCount": 2
    }
  }
}
```

---

## ✅ CHECKLIST PARA CONFIGURAR MÚLTIPLES PROVIDERS

- [ ] Identificar modelos a usar (gratis + pago)
- [ ] Obtener API keys de cada provider
- [ ] Configurar cada provider en `models.providers`
- [ ] Agregar modelos a `agents.defaults.model.fallbacks`
- [ ] Probar failover manualmente (`/model provider/model`)
- [ ] Verificar que cooldowns funcionan (forzar un error)
- [ ] Documentar estrategia en este archivo

---

## 🚀 PRÓXIMOS PASOS RECOMENDADOS

1. **Agregar Anthropic** (Claude) para tareas de alta calidad
2. **Configurar Groq** como alternativa gratuita de alta velocidad
3. **Probar failover** cambiando primary a un modelo que no existe
4. **Monitorear costos** durante 1 semana antes de añadir providers pagos

---

*Guía creada: 5 de abril de 2026*  
*Fuente: Documentación oficial OpenClaw + investigación propia*
