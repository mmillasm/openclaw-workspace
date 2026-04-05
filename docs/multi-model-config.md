# Cómo Configurar Múltiples Modelos de IA en OpenClaw

## Conceptos Clave

### Formato de Modelos
```
provider/model
```
Ejemplos:
- `openai/gpt-5.4`
- `anthropic/claude-opus-4-6`
- `opencode-go/minimax-m2.5`
- `google/gemini-3-flash-preview`
- `moonshot/kimi-k2.5`

---

## Paso 1: Configurar Múltiples Proveedores

Editá `~/.openclaw/openclaw.json`:

```json5
{
  "agents": {
    "defaults": {
      "model": {
        "primary": "opencode-go/minimax-m2.5",  // Modelo por defecto
        "fallbacks": [
          "anthropic/claude-opus-4-6",
          "openai/gpt-5.4"
        ]
      }
    }
  }
}
```

### Ejemplo: Primario + Fallback + Local

```json5
{
  "agents": {
    "defaults": {
      "model": {
        "primary": "opencode-go/minimax-m2.5",
        "fallbacks": [
          "anthropic/claude-opus-4-6",
          "ollama/llama3.3"
        ]
      }
    }
  }
}
```

---

## Paso 2: Variables de Entorno para Auth

### Proveedores Built-in (no requieren config extra)

| Proveedor | Variable | Ejemplo |
|-----------|----------|---------|
| OpenAI | `OPENAI_API_KEY` | `sk-...` |
| Anthropic | `ANTHROPIC_API_KEY` | `sk-ant-...` |
| Google | `GEMINI_API_KEY` | `AIza...` |
| OpenCode | `OPENCODE_API_KEY` | `ok...` |
| OpenCode Go | `OPENCODE_GO_API_KEY` | `okgo...` |
| MiniMax | `MINIMAX_API_KEY` | — |
| Moonshot | `MOONSHOT_API_KEY` | `sk-...` |

### Rotación de Keys (para rate limits)

```bash
# Múltiples keys para un proveedor
export OPENAI_API_KEYS="key1,key2,key3"
export ANTHROPIC_API_KEYS="key1,key2"

# Override en vivo (máxima prioridad)
export OPENCLAW_LIVE_OPENAI_KEY="key-especial"
```

---

## Paso 3: Modelos Personalizados (OpenAI-compatible)

### Ejemplo: Ollama local

```json5
{
  "models": {
    "mode": "merge",
    "providers": {
      "ollama": {
        "baseUrl": "http://127.0.0.1:11434/v1",
        "apiKey": "ollama-local",
        "api": "openai-completions",
        "models": [
          {
            "id": "llama3.3",
            "name": "Llama 3.3",
            "contextWindow": 200000,
            "maxTokens": 8192
          }
        ]
      }
    }
  },
  "agents": {
    "defaults": {
      "model": {
        "primary": "ollama/llama3.3"
      }
    }
  }
}
```

### Ejemplo: LM Studio

```json5
{
  "models": {
    "providers": {
      "lmstudio": {
        "baseUrl": "http://localhost:1234/v1",
        "apiKey": "LMSTUDIO_KEY",
        "api": "openai-completions",
        "models": [
          {
            "id": "mi-modelo-local",
            "name": "Mi Modelo Local",
            "cost": { "input": 0, "output": 0 }
          }
        ]
      }
    }
  }
}
```

### Ejemplo: vLLM

```json5
{
  "models": {
    "providers": {
      "vllm": {
        "baseUrl": "http://127.0.0.1:8000/v1",
        "apiKey": "vllm-local",
        "api": "openai-completions",
        "models": [
          {
            "id": "tu-modelo-id",
            "name": "vLLM Model"
          }
        ]
      }
    }
  }
}
```

---

## Paso 4: Proveedores con Configuración Especial

### MiniMax

```json5
{
  "models": {
    "mode": "merge",
    "providers": {
      "minimax": {
        "baseUrl": "https://api.minimax.chat/v1",
        "apiKey": "${MINIMAX_API_KEY}",
        "api": "openai-completions",
        "models": [
          { "id": "MiniMax-Text-01", "name": "MiniMax Text" }
        ]
      }
    }
  }
}
```

### Moonshot (Kimi)

```json5
{
  "models": {
    "providers": {
      "moonshot": {
        "baseUrl": "https://api.moonshot.ai/v1",
        "apiKey": "${MOONSHOT_API_KEY}",
        "api": "openai-completions",
        "models": [
          { "id": "kimi-k2.5", "name": "Kimi K2.5" }
        ]
      }
    }
  }
}
```

---

## Paso 5: Failover Automático

OpenClaw maneja fallos en 2 etapas:

1. **Rotación de auth profiles** dentro del mismo proveedor
2. **Fallback a siguiente modelo** si el proveedor falla completamente

### Configurar Fallbacks Explícitos

```json5
{
  "agents": {
    "defaults": {
      "model": {
        "primary": "opencode-go/minimax-m2.5",
        "fallbacks": [
          "anthropic/claude-opus-4-6",
          "openai/gpt-5.4",
          "google/gemini-3-flash-preview"
        ]
      }
    }
  }
}
```

### Cooldowns (para rate limits)

```json5
{
  "auth": {
    "cooldowns": {
      "billingBackoffHours": 5,
      "billingMaxHours": 24,
      "overloadedProfileRotations": 1,
      "overloadedBackoffMs": 0,
      "rateLimitedProfileRotations": 2
    }
  }
}
```

---

## Comandos CLI Útiles

```bash
# Ver modelos disponibles
openclaw models list

# Configurar modelo por defecto
openclaw models set opencode-go/minimax-m2.5

# Autenticarse con un proveedor
openclaw onboard --auth-choice openai-api-key
openclaw onboard --auth-choice anthropic
openclaw onboard --auth-choice gemini-api-key

# Ver estado de auth
openclaw models auth list
```

---

## Ejemplo Completo: Stack Multi-Provider

```json5
{
  "env": {
    "OPENAI_API_KEY": "sk-...",
    "ANTHROPIC_API_KEY": "sk-ant-...",
    "MINIMAX_API_KEY": "tu-key"
  },
  "models": {
    "mode": "merge",
    "providers": {
      "ollama": {
        "baseUrl": "http://127.0.0.1:11434/v1",
        "apiKey": "local",
        "api": "openai-completions",
        "models": [
          { "id": "llama3.3", "name": "Llama 3.3" }
        ]
      }
    }
  },
  "agents": {
    "defaults": {
      "model": {
        "primary": "opencode-go/minimax-m2.5",
        "fallbacks": [
          "anthropic/claude-opus-4-6",
          "openai/gpt-5.4",
          "ollama/llama3.3"
        ]
      }
    }
  },
  "auth": {
    "cooldowns": {
      "overloadedBackoffMs": 0
    }
  }
}
```

---

## Tips

1. **Primario** = tu mejor opción (rápido/barato)
2. **Fallbacks** = opciones de backup (más caros o lentos)
3. **Ollama/vLLM** = para cuando no tengas internet
4. **Rotación de keys** = para evitar rate limits
5. **Cooldowns** = configurá según tu uso

---

## Troubleshooting

### "Model not found"
- Verificá que el provider esté configurado
- Ejecutá `openclaw models list` para ver disponibles

### "Auth failed"
- Verificá que la API key sea correcta
- Ejecutá `openclaw models auth list` para ver estado

### "Rate limit"
- Agregá más API keys con rotación
- Esperá el cooldown (exponential backoff)
