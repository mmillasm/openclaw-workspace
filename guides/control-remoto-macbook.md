# Guía Técnica: Control Remoto de MacBook para OpenClaw

> **Objetivo:** Permitir que OpenClaw (corriendo en un servidor/VPS) ejecute tareas en un MacBook remoto, incluyendo automatización CLI, AppleScript y acceso gráfico.

**Arquitectura general:**
```
[OpenClaw Gateway (VPS/Linux)] 
       |
       |  ← SSH o WebSocket seguro
       ↓
[MacBook de Ader] → ejecuta comandos, AppleScript, automation
```

---

## 1. Opciones de Control Remoto en macOS

### 1.1 SSH (Remote Login) — ⭐ Recomendado para OpenClaw

**Qué es:** Acceso por línea de comandos, encriptado, nativo en macOS.

**Cómo habilitarlo:**
1. Apple Menu → **System Settings** → **General** → **Sharing**
2. Activar **Remote Login**
3. Configurar acceso: "Only these users" (más seguro) o "All users"
4. Anotar el comando SSH que aparece: `ssh username@host`

**Puertos:** TCP 22  
**Seguridad por defecto:** Encriptación SSH robusta (AES, ChaCha20)

**Comando típico:**
```bash
ssh username@192.168.1.x        # LAN
ssh username@macbook.ts.net     # vía Tailscale
```

**Claves SSH (recomendado):**
```bash
# En el CLIENTE (VPS con OpenClaw):
ssh-keygen -t ed25519 -C "openclaw-gateway"
ssh-copy-id username@macbook.ts.net

# Ya no necesita contraseña después de esto
```

---

### 1.2 Screen Sharing / VNC — Para control gráfico

**Qué es:** Escritorio remoto visual. Muy útil para tareas GUI.

**Cómo habilitarlo:**
1. Apple Menu → **System Settings** → **General** → **Sharing**
2. Activar **Screen Sharing**
3. Opcional: "VNC viewers may control screen with password" → establecer contraseña
4. Permitir usuarios específicos

**Puertos:** TCP 5900 (VNC)  
**⚠️ Seguridad:** VNC **NO está encriptado por defecto**. Siempre usar túnel SSH.

**Túnel SSH para VNC seguro:**
```bash
# Desde el VPS con OpenClaw:
ssh -L 5900:localhost:5900 username@macbook.ts.net

# Luego conectar VNC viewer a localhost:5900
```

**Desde otro Mac:**
- Ir a `Screen Sharing.app` → buscar el Mac en red → conectar

**Alternativas VNC con mejor rendimiento:**
- **RealVNC Connect** (gratis para uso personal)
- **Chicken of the VNC** (cliente Mac clásico)
- **NoMachine** (mucho más rápido que VNC puro, multiplataforma)

---

### 1.3 Apple Remote Desktop (ARD) — Para gestión profesional

**Qué es:** Solución empresarial de Apple para gestionar flotas de Macs.

**Precio:** ~$80 USD (App Store)

**Características:**
- Distribución de software
- Reporting de hardware/software (200+ atributos)
- Control gráfico en tiempo real
- Modo cortina (el usuario no ve lo que haces)
- Transferencia de archivos drag & drop
- Encriptación AES-128 + RSA-2048

**Uso típico:** Organizaciones con muchos Macs. Para un solo MacBook overkill.

**Compatibilidad:** Puede controlar VNC en Windows/Linux también.

---

### 1.4 Comparativa Rápida

| Característica | SSH | Screen Sharing/VNC | ARD |
|---|---|---|---|
| **Tipo** | Línea de comandos | Gráfico | Gráfico + gestión |
| **Encriptado** | ✅ Sí (nativo) | ❌ No (necesita SSH tunnel) | ✅ AES-128 |
| **macOS nativo** | ✅ Terminal | ✅ Screen Sharing | 💰 De pago |
| **Automatización** | ✅ Scripts/shell | ❌ Limitada | ✅ Automator |
| **Transferencia archivos** | ✅ SCP/SFTP | ⚠️ Limitada | ✅ Drag & drop |
| **Costo** | Gratis | Gratis | ~$80 |
| **Mejor para** | **OpenClaw + CLI** | GUI ocasional | Flotas de Macs |

**→ Para OpenClaw: SSH es la opción principal. Screen Sharing como backup visual.**

---

## 2. Cómo OpenClaw Ejecuta Comandos en Mac Remoto

OpenClaw tiene **dos arquitecturas principales** para control remoto:

### 2.1 Arquitectura: OpenClaw en VPS controla MacBook

```
┌─────────────────┐         SSH          ┌──────────────────┐
│  OpenClaw       │ ──────────────────→  │  MacBook de Ader │
│  Gateway (VPS)  │   ejecuta comandos    │  (nodo pasivo)   │
│  + Claude Code  │   vía exec SSH       │                  │
└─────────────────┘                       └──────────────────┘
```

**Cómo funciona:**
1. OpenClaw Gateway corre en el VPS
2. Usa `exec` tool con configuración SSH:
   ```bash
   # El exec tool puede configurarse para SSH:
   openclaw config set exec.ssh.enabled true
   openclaw config set exec.ssh.target "user@macbook.ts.net"
   ```
3. Los comandos se ejecutan vía SSH en el MacBook
4. Los resultados vuelven al gateway

**Limitación actual:** Esta arquitectura requiere que OpenClaw tenga un plugin/exec backend que soporte SSH target. Revisar la configuración de `exec` en el gateway.

---

### 2.2 Arquitectura: MacBook como Node Host de OpenClaw

```
┌─────────────────┐         WebSocket     ┌──────────────────┐
│  OpenClaw       │ ←──────────────────   │  OpenClaw Mac    │
│  Gateway (VPS)  │   MacBook ofrece      │  app (node)      │
│                 │   screen, notify,     │                  │
│  Agente         │   automation          │  + AppleScript   │
│  (Claude Code)  │                      │  + Shortcuts     │
└─────────────────┘                      └──────────────────┘
       ↑
   Ader usa
   Telegram/etc
```

**El MacBook ofrece a OpenClaw:**
- Notificaciones nativas
- Screen recording
- Automation (AppleScript, Shortcuts)
- Microphone
- Accessibility (control de UI)
- System commands

**Cómo configurar:**
1. Instalar app OpenClaw en el MacBook
2. Conectar la app al Gateway del VPS (WebSocket sobre Tailscale o SSH tunnel)
3. Aprobar el device: `openclaw devices approve --latest`
4. El gateway puede invocar tools en el nodo MacBook

**Documentación oficial:** `docs.openclaw.ai/platforms/mac/remote`

---

### 2.3 Tailscale: El Pegamento de Seguridad

Tailscale crea una red privada virtual (VPN) mesh entre dispositivos, simplificando enormemente la conexión SSH.

**Por qué Tailscale:**
- No necesita abrir puertos en el router
- Cifrado WireGuard automático
- DNS privado: `macbook-de-ader.ts.net`
- Conexión directa incluso detrás de NAT

**Instalación en el MacBook:**
```bash
# Opción 1: Descargar de https://tailscale.com/download/mac
# Opción 2: Homebrew
brew install tailscale

# Login y conectar
tailscale up --accept-routes
```

**Instalación en el VPS (Linux):**
```bash
# Instalar Tailscale
curl -fsSL https://tailscale.com/install.sh | sh

# Unirse a la red
tailscale up --accept-routes
```

**SSH vía Tailscale (sin passwords ni claves):**
```bash
# Habilitar SSH en Tailscale (solo autenticación Tailscale)
tailscale configure --accept-routes

# Ahora SSH directo por la red Tailscale:
ssh admin@macbook-de-ader.ts.net
```

**Habilitar Tailscale SSH en la admin console:**
1. Ir a [admin.ts.net](https://login.tailscale.com/admin/dns)
2. Access Controls → agregar:
```json
"ssh": [
  {
    "accept": true,
    "src": ["group:everyone"],
    "dst": ["tag:macbook"],
    "users": ["admin"]
  }
]
```

**⚠️ Para Tailscale SSH server en macOS:** Necesitas instalar `tailscaled` (CLI open source), **no** la app GUI del Mac App Store. La app GUI sandboxeada no puede actuar como servidor SSH.

---

## 3. Integración con Claude Code

Claude Code puede ejecutarse **directamente en el MacBook remoto** vía SSH:

### 3.1 SSH directo a MacBook y correr Claude Code ahí

```bash
# Desde el VPS con OpenClaw:
ssh admin@macbook-de-ader.ts.net

# Una vez dentro:
claude

# Ahora tienes Claude Code corriendo en el MacBook con acceso a:
# - Archivos del MacBook
# - Homebrew, npm, git
# - AppleScript vía CLI
```

### 3.2 Claude Code Desktop con SSH Environment

Claude Code Desktop permite elegir un "SSH environment" como destino:

1. Settings → Environment → **SSH**
2. Configurar host: `admin@macbook-de-ader.ts.net`
3. Claude Code corre **en el MacBook** pero se controla desde la UI del VPS

### 3.3 Integración VS Code Remote SSH

Si ya usas VS Code Remote SSH:
```
Claude Code detecta automáticamente conexiones SSH de VS Code
→ puedes continuar sesiones desde Claude Code en el mismo contexto
```

### 3.4 Flujo OpenClaw → Claude Code → MacBook

```
Ader (Telegram)
    ↓ mensaje
OpenClaw Gateway (VPS)
    ↓ detecta tarea compleja
    → Spawn subagent Claude Code vía SSH
    → SSH a admin@macbook.ts.net
    → claude --print --permission-mode bypassPermissions
    → Claude Code ejecuta tarea en MacBook
    → Resultado vuelve por SSH
    → OpenClaw resume y responde a Ader
```

**Nota de seguridad:** `--permission-mode bypassPermissions` permite ejecución sin prompts. Usar solo en contextos controlados (no para código no verificado).

---

## 4. Seguridad y Autenticación

### 4.1 Principios Fundamentales

| Riesgo | Mitigación |
|---|---|
| Acceso no autorizado | SSH keys (no passwords) + MFA en Apple ID |
| Exposición de puertos | Solo binds loopback + Tailscale/SSH tunnel |
| Movimiento lateral | User isolation, permissions mínimas |
| Session hijacking | Tokens de corta duración, TLS |
| Sniffing de VNC | **Siempre** túnel SSH para VNC |

### 4.2 Hardening SSH en MacBook

**Archivo:** `~/.ssh/sshd_config` (sudo para editar)

```bash
# Solo autenticación por clave
PasswordAuthentication no
PubkeyAuthentication yes

# Deshabilitar root login
PermitRootLogin no

# Cambiar puerto (opcional, security through obscurity)
Port 22  #也可以改成其他端口如 2222

# Limitar usuarios
AllowUsers admin

# Deshabilitar protocolos inseguros
Protocol 2

# Log de intentos fallidos
LogLevel VERBOSE
```

**Reiniciar SSH:**
```bash
sudo launchctl kickstart -k system/com.openssh.sshd
```

### 4.3 Autenticación OpenClaw Gateway

El gateway soporta múltiples modos de auth:

```json
{
  "gateway": {
    "auth": {
      "mode": "token",           // token, password, o trusted-proxy
      "token": "tu-token-secreto",
      "allowTailscale": true     // si usas Tailscale, identidad Tailscale = trusted
    }
  }
}
```

**Para Tailscale:**
```json
{
  "gateway": {
    "auth": {
      "mode": "trusted-proxy",   // Tailscale es el proxy de confianza
      "allowTailscale": true
    }
  }
}
```

### 4.4 Permisos TCC en macOS

El MacBook necesita approve estos permisos para OpenClaw:

- **Screen Recording** → para capturar pantalla
- **Accessibility** → para controlar la UI
- **Automation** → para AppleScript y Shortcuts
- **Notifications** → para notificaciones nativas
- **Microphone** → para voz
- **Speech Recognition** → para Voice Wake

**Cómo aprobar:** La primera vez que OpenClaw intenta usar cada capability, macOS muestra un diálogo de permiso. Alternativamente, usar el onboarding flow.

### 4.5 Mac Firewall

```bash
# Ver estado del firewall
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --getglobalstate

# Abrir puertos necesarios (SSH ya viene incluido por defecto)
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --add /usr/sbin/sshd
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --unblockapp /Applications/Screen\ Sharing.app
```

---

## 5. Automatización de Tareas en macOS

### 5.1 AppleScript (El más potente)

**Ejecutar desde SSH:**
```bash
osascript -e 'tell application "Safari" to open location "https://example.com"'

# Script multi-línea:
osascript << 'EOF'
tell application "Finder"
    set desktop picture to POSIX file "/Library/Desktop Pictures/Morning.jpg"
end tell
EOF

# Control de apps:
osascript -e 'tell application "Mail" to get unread count of inbox'
osascript -e 'tell application "Finder" to make new Finder window'
osascript -e 'tell application "System Events" to keystroke "hello world"'
```

**Ejecutar archivo .scpt:**
```bash
osascript /path/to/script.scpt
```

**SSH + AppleScript ejemplo completo:**
```bash
ssh admin@macbook.ts.net 'osascript -e '\''tell app "Terminal" to do script "echo hola"'\'
```

**Capabilities de AppleScript:**
- Control de casi cualquier app macOS
- Automatización de UI (System Events)
- Acceso al sistema de archivos
- Gestión de ventanas, menús
- Acceso a datos (contacts, calendar, mail)
- ⚠️ No funciona bien con apps Catalyst (apps iOS en Mac)

---

### 5.2 Shortcuts (Apple, GUI, fácil)

**Desde CLI:**
```bash
# Listar shortcuts disponibles
shortcuts list

# Ejecutar un shortcut por nombre
shortcuts run "Mi Automation"

# Con parámetros
shortcuts run "Buscar en archivos" --input-text "proyecto X"
```

**Crear Shortcuts (desde la app Shortcuts):**
1. Abrir app Shortcuts
2. New Shortcut
3. Añadir acciones: Run Shell Script, Ask for Input, etc.
4. Guardar

**Shortcuts disponibles vía CLI pueden ser invocados por OpenClaw vía SSH:**
```bash
ssh admin@macbook.ts.net "shortcuts run 'Screenshot y guardar'"
```

**Ventajas sobre AppleScript:**
- Más fácil de crear (GUI drag & drop)
- Mejor acceso a features modernos (Focus mode, etc.)
- Funciona en apps Catalyst
- Más accesible para no-programadores

---

### 5.3 Comandos CLI Esenciales en macOS

**Sistema:**
```bash
# No dejar dormir
caffeinate -d -t 3600 &  # 1 hora despierto

# Captura de pantalla
screencapture -x ~/screenshot.png

# Información del sistema
sw_vers           # versión macOS
system_profiler   # hardware completo

# Gestionar apps
open -a Safari    # abrir app
killall Safari    # forzar cerrar
```

**Archivos:**
```bash
ls -la ~/Projects
cp -R origen destino
mdfind "kMDItemDisplayName == '*proyecto*'"  # Spotlight desde CLI
```

**Red:**
```bash
networksetup -setairportpower en0 on    # Wi-Fi on/off
curl ifconfig.me                         # IP pública
ping -c 3 google.com                    # test conectividad
```

**Modificar preferences ocultas (⚠️谨慎):**
```bash
# Desactivar ask for password al despertar
defaults write com.apple.securityaskrelogin -bool false

# Mostrar archivos ocultos en Finder
defaults write com.apple.finder AppleShowAllFiles -bool true
killall Finder

# Cambiar dock size
defaults write com.apple.dock tilesize -int 48
killall Dock
```

**Homebrew (si instalado):**
```bash
brew install node python git
brew update && brew upgrade
brew services restart postgresql
```

---

### 5.4 Combinación Poderosa: SSH + AppleScript + Shortcuts

```bash
# Desde el VPS, enviar tarea compleja al MacBook:
ssh admin@macbook.ts.net << 'ENDSSH'
  # 1. Ejecutar shortcut de automatización
  shortcuts run "Preparar entorno de desarrollo"
  
  # 2. AppleScript para config específica
  osascript -e 'tell app "Finder" to set desktop picture to POSIX file "/Users/admin/wallpaper.jpg"'
  
  # 3. Abrir apps necesarias
  open -a "Visual Studio Code"
  open -a "Safari"
  
  # 4. Screenshot del resultado
  screencapture -x ~/resultado.png
  
  echo "Tarea completada"
ENDSSH
```

---

## 6. ¿Puede OpenClaw Controlar el MacBook de Ader?

### ✅ SÍ, es totalmente posible. Aquí el plan completo:

### Escenario A: OpenClaw en VPS + MacBook como nodo pasivo

**Arquitectura:**
```
OpenClaw (VPS/Linux) [Gateway + Agente]
       ↕ SSH o Tailscale SSH
MacBook de Ader [recibe y ejecuta comandos]
```

**Pasos de configuración:**

1. **MacBook: Habilitar SSH**
   ```
   System Settings → General → Sharing → Remote Login ON
   ```

2. **MacBook: Instalar Tailscale**
   ```
   brew install tailscale
   tailscale up
   ```

3. **MacBook: Instalar OpenClaw CLI (opcional, para node mode)**
   ```
   brew install opencore-node  # revisar comando exacto
   # o desde npm:
   npm install -g openclaw
   ```

4. **VPS: Configurar exec SSH target**
   ```bash
   # En la config de OpenClaw Gateway:
   # Configurar el exec provider para SSH al MacBook
   ```

5. **Probar:**
   ```bash
   # Desde VPS:
   ssh admin@macbook-de-ader.ts.net "hostname && echo 'SSH funciona!'"
   ssh admin@macbook-de-ader.ts.net "osascript -e 'say \"Hola Ader\"'"
   ```

### Escenario B: MacBook como OpenClaw Node Host

**Arquitectura:**
```
OpenClaw (VPS/Linux) [Gateway principal]
       ↕ WebSocket (Tailscale o SSH tunnel)
MacBook de Ader [Node: screen, AppleScript, notifications]
```

**Pasos:**

1. **MacBook: Instalar app OpenClaw**
   - Descargar de openclaw.ai o Mac App Store
   - Instalar en /Applications

2. **MacBook: Conectar al Gateway remoto**
   ```
   App → Settings → General → OpenClaw runs: "Remote over SSH"
   - Transport: SSH tunnel
   - SSH target: user@vps-ip
   - Test remote → Success
   ```

3. **VPS: Approve device pairing**
   ```bash
   openclaw devices list
   openclaw devices approve --latest
   ```

4. **Resultado:**
   - OpenClaw Gateway puede invocar `node.invoke` en el MacBook
   - Permite: screen capture, notify, AppleScript, Shortcuts
   - Notificaciones nativas macOS aparecen en el MacBook

### Escenario C: Tailscale SSH (Más simple)

Este es el setup más robusto para Ader:

```
1. MacBook: brew install tailscale && tailscale up
2. VPS:    brew install tailscale && tailscale up
3. Ambos ahora están en la misma red privada
4. VPS puede hacer: ssh admin@macbook.ts.net
5. OpenClaw exec tool usa ese SSH para ejecutar comandos
```

**No necesita abrir puertos. No necesita configurar VPN. Tailscale lo maneja todo.**

---

## 7. Limitaciones y Mejores Prácticas

### 7.1 Limitaciones Conocidas

| Limitación | Impacto | Solución |
|---|---|---|
| MacBook duerme | Comandos fallan | `caffeinate` o energy settings |
| No hay display (headless) | Apps GUI no funcionan | Usar Xvfb o VNC con virtual display |
| AppleScript limitado en apps Catalyst | Algunas apps no scriptables | Usar UI Automation o Shortcuts |
| Tailscale necesita internet | Sin conexión no funciona | Offline backup: SSH directo con IP |
| Screen Sharing lento | Lag en acceso gráfico | NoMachine o SSH + CLI |
| Permisos TCC bloquen automation | Apps negadas | Aprobar manualmente en System Settings |
| SIP restricciones | No se puede modificar /System | Working around SIP no recomendado |

### 7.2 Mejores Prácticas

**Seguridad:**
```bash
# 1. Siempre SSH keys, nunca passwords
ssh-keygen -t ed25519
ssh-copy-id admin@macbook.ts.net

# 2. MacBook: Require password immediately after sleep/screen saver
System Settings → Lock Screen → Start after: Immediately

# 3. Firewall activo
System Settings → Network → Firewall → ON

# 4. FileVault encryption (ya viene en Mac con T2/Apple Silicon)
# Verificar: System Settings → Privacy & Security → FileVault

# 5. Actualizaciones automáticas
System Settings → General → Software Update → Automatic updates: ON
```

**Fiabilidad:**
```bash
# Prevenir que MacBook duerma durante tareas importantes
ssh admin@macbook.ts.net "sudo pmset -a sleep 0"

# Volver a setting normal después
ssh admin@macbook.ts.net "sudo pmset -a sleep 5"

# Alternativa user-level (no necesita sudo):
caffeinate -d -t 7200 &  # 2 horas despierto
```

**Productividad:**
```bash
# Crear aliases para acceso rápido en VPS:
alias macbook='ssh admin@macbook-de-ader.ts.net'
alias mac-status='ssh admin@macbook-de-ader.ts.net "pmset -g"'
alias mac-screenshot='ssh admin@macbook-de-ader.ts.net "screencapture -x ~/screenshot.png" && scp admin@macbook-de-ader.ts.net:~/screenshot.png ./'

# Poner en ~/.bashrc o ~/.zshrc del VPS
```

**Organización:**
```
~/openclaw-macbook/
├── README.md           # documentación del setup
├── scripts/
│   ├── backup.sh       # backup del MacBook
│   ├── screenshot.sh   # capturar pantalla
│   └── notify.sh       # enviar notificación
├── tasks/
│   ├── daily.md        # tareas diarias automáticas
│   └── projects/       # configs por proyecto
└── .env                # variables (NO commitear)
```

**Tailscale ACLs (Access Control Lists):**
```json
{
  "acls": [
    {"action": "accept", "src": ["vps"], "dst": ["macbook:22"]},
    {"action": "accept", "src": ["macbook"], "dst": ["vps:22"]}
  ],
  "ssh": [
    {
      "action": "accept",
      "src": ["vps"],
      "dst": ["macbook"],
      "users": ["admin"]
    }
  ]
}
```

### 7.3 Flujo de Trabajo Recomendado para Ader

```
┌─────────────────────────────────────────────────────────┐
│                    SETUP INICIAL                        │
├─────────────────────────────────────────────────────────┤
│ 1. MacBook: Habilitar Remote Login (SSH)               │
│ 2. MacBook: Instalar Tailscale, hacer `tailscale up`   │
│ 3. VPS: Instalar Tailscale, unir a la red             │
│ 4. VPS: Probar SSH: `ssh admin@macbook.ts.net`        │
│ 5. VPS: Configurar SSH key para acceso passwordless    │
│ 6. OpenClaw Gateway: Configurar exec SSH target        │
│ 7. Test: `ssh admin@macbook.ts.net "say prueba"`       │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                    USO DIARIO                           │
├─────────────────────────────────────────────────────────┤
│ Ader → Telegram → OpenClaw (VPS)                      │
│   "Abre Safari y busca X en el Mac"                    │
│        ↓                                                │
│ OpenClaw Gateway → SSH → MacBook                       │
│ osascript -e 'tell app "Safari" to open location "X"'  │
│        ↓                                                │
│ Safari abre en MacBook de Ader ✅                      │
└─────────────────────────────────────────────────────────┘
```

---

## Resumen: Quick Start para Ader

```bash
# --- EN EL MACBOOK (una sola vez) ---
# 1. Habilitar SSH
System Settings → General → Sharing → Remote Login: ON

# 2. Instalar Tailscale (desde tailscale.com o brew)
brew install tailscale
tailscale up

# Anotar el hostname: macbook-de-ader.ts.net (o IP Tailscale: 100.x.y.z)

# --- EN EL VPS con OpenClaw (una sola vez) ---
# 3. Instalar Tailscale
curl -fsSL https://tailscale.com/install.sh | sh
tailscale up

# 4. Configurar SSH key
ssh-keygen -t ed25519
ssh-copy-id admin@macbook-de-ader.ts.net

# 5. Test
ssh admin@macbook-de-ader.ts.net "say 'Funciona!'"

# --- PARA PROBAR APPLE AUTOMATION ---
ssh admin@macbook-de-ader.ts.net "osascript -e 'say \"OpenClaw conectado\"'"
ssh admin@macbook-de-ader.ts.net "shortcuts run 'Screenshot'"
ssh admin@macbook-de-ader.ts.net "open -a Safari"

# --- CONFIGURAR OPENCLAW ---
# Agregar SSH exec target en la config del gateway
# El exec tool de OpenClaw podrá entonces enviar comandos al MacBook
```

---

## Recursos

- [docs.openclaw.ai/platforms/mac/remote](https://docs.openclaw.ai/platforms/mac/remote)
- [docs.openclaw.ai/gateway/remote](https://docs.openclaw.ai/gateway/remote)
- [tailscale.com/docs/ssh](https://tailscale.com/docs/ssh)
- [support.apple.com/guide/mac-help/allow-a-remote-computer-to-access-your-mac-mchlp1066](https://support.apple.com/guide/mac-help/allow-a-remote-computer-to-access-your-mac-mchlp1066/mac)
- [code.claude.com/docs/en/desktop](https://code.claude.com/docs/en/desktop)

---

*Última actualización: 2026-04-05*
*Archivo: `guides/control-remoto-macbook.md`*
