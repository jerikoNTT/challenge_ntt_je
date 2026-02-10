# Guía de Pruebas DevOps (Manejo de Secretos con Vault)

## Estado actual (9 Feb 2026)
- ✅ Compilación local: **EXITOSA**
- ✅ Jar generado: `target/api_challenge_ntt_je-1.0.0-SNAPSHOT.jar`
- ✅ Código con validación de secretos JWT integrado
- ✅ Archivos de configuración (application-dev.properties, application-test.properties) listos para variables de entorno
- ✅ Spring Cloud Vault agregado a `pom.xml`
- ✅ `docker-compose.yml` listo
- ⏳ **Falta:** Docker Desktop instalado en Windows

## Requisitos para ejecutar pruebas completas
1. **Docker Desktop para Windows** (descarga desde https://www.docker.com/products/docker-desktop)
   - Habilita Hyper-V o WSL2 durante la instalación
   - Verifica: `docker --version` en PowerShell/cmd

## Paso 1: Instalar Docker (si no está instalado)
```powershell
# Descargar e instalar Docker Desktop desde:
# https://www.docker.com/products/docker-desktop

# Luego verificar en una terminal nueva:
docker --version
docker-compose --version
```

## Paso 2: Levantar los servicios (Postgres + Vault + App)
```bash
cd "c:\Users\Ordenador\Documents\1.DOCUMENTOS ERICK\Challenge NTT\challenge_ntt_je"
docker compose up
```

**Qué ocurre (pasos internos del docker-compose):**

1. **Servicio `db` (PostgreSQL)**
   - Imagen: `postgres:15`
   - Base de datos: `ntt_dev`
   - Usuario: `postgres` / Contraseña: `postgres`
   - Puerto: `5432`

2. **Servicio `vault` (HashiCorp Vault)**
   - Imagen: `vault:1.14.0` en modo DEV
   - Token root: `root` (modo dev, solo para pruebas locales)
   - Puerto: `8200`

3. **Servicio `vault-init` (Inicializador de secretos)**
   - Espera a que Vault esté listo
   - Crea la ruta `secret/data/application` con secretos:
     ```json
     {
       "jwt.secret": "dev-vault-secret-minimo-32-chars-xxxxxxxx",
       "spring.datasource.password": "postgres"
     }
     ```

4. **Servicio `app` (Tu aplicación Spring Boot)**
   - Compila con Maven (automático dentro del contenedor)
   - Arranca con variables de entorno que apuntan a:
     - Postgres en `db:5432`
     - Vault en `http://vault:8200`
   - Puerto: `8080`

## Paso 3: Verificar que todo funciona
Espera a que el `docker-compose up` muestre logs similares a:
```
vault      | WARNING! dev mode is enabled! In this mode, Vault runs entirely in-memory
vault      | and every restart will lose data. Never use dev mode for production.
vault-init | Vault init complete
app        | Started Application in X.XXX seconds
```

## Paso 4: Probar que la app lee secretos desde Vault

### Opción A: Verificar logs de la app
```bash
docker-compose logs app | grep -i "jwt\|secret"
```

### Opción B: Llamar a un endpoint de prueba (si existe)
```bash
curl -X GET http://localhost:8080/api/health
```

### Opción C: Verificar que Vault está poblado
```bash
# En otra terminal, dentro del contenedor vault:
docker-compose exec vault vault kv get secret/application
# Deberías ver:
# ====== Data ======
# Key       Value
# ---       -----
# jwt.secret     dev-vault-secret-minimo-32-chars-xxxxxxxx
# spring.datasource.password     postgres
```

## Paso 5: Comprobar que JwtTokenProvider validó correctamente el secreto

La aplicación arranca con la validación en `@PostConstruct` (que añadimos en `JwtTokenProvider`):
- Si `jwt.secret` **está vacío** → app falla con `IllegalStateException`
- Si `jwt.secret` **existe y tiene < 32 caracteres** → advertencia en logs
- Si `jwt.secret` **existe y tiene ≥ 32 caracteres** → ✅ OK

En el `docker-compose`:
- Vault escribe un secreto válido (32+ chars) → la app debería arrancar sin problemas
- Si eliminamos la sección `vault-init`, Vault no tendría datos y Spring Cloud Vault no encontraría el secreto (pero la app podría usar un default o fallar)

## Paso 6: Detener y limpiar
```bash
docker-compose down
docker volume rm challenge_ntt_je_db-data  # (opcional, si quieres borrar datos de BD)
```

## Qué ocurre paso a paso (Flujo de Secretos)

```
┌─────────────────────────────────────────────────────────┐
│ Docker Compose Arranca                                  │
├─────────────────────────────────────────────────────────┤
│ 1. Postgres inicia en puerto 5432                       │
│    └─ Crea BD: ntt_dev                                  │
│                                                         │
│ 2. Vault DEV inicia en puerto 8200                      │
│    └─ Token root: root                                  │
│                                                         │
│ 3. vault-init ejecuta                                   │
│    └─ Espera a que Vault esté listo                     │
│    └─ Crea secreto en vault: secret/data/application    │
│       ├─ jwt.secret = "dev-vault-secret-..."            │
│       └─ spring.datasource.password = "postgres"        │
│                                                         │
│ 4. app inicia con Dockerfile                            │
│    a) Maven compila el code                             │
│    b) Genera target/api_challenge_ntt_je-*.jar          │
│    c) Inicia con entorno:                               │
│       ├─ SPRING_CLOUD_VAULT_URI=http://vault:8200       │
│       ├─ SPRING_CLOUD_VAULT_TOKEN=root                  │
│       └─ Postgres URL: jdbc:postgresql://db:5432/ntt_dev│
│                                                         │
│ 5. Spring Boot Startup Sequence                         │
│    a) Lee application.properties (base config)          │
│    b) Lee application-prod.properties (profile prod)    │
│    c) Spring Cloud Vault intercepta                     │
│       ├─ Conecta a http://vault:8200                    │
│       ├─ Autentica con token: root                      │
│       ├─ Lee secret/data/application                    │
│       └─ Inyecta valores en Environment Spring          │
│    d) JwtTokenProvider se instancia                     │
│       ├─ @PostConstruct validateConfig() ejecuta       │
│       ├─ Verifica que jwt.secret NO esté vacío         │
│       ├─ Verifica longitud ≥ 32 caracteres              │
│       └─ Si todo OK: app continúa normalmente           │
│    e) App por fin lista en http://localhost:8080        │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

## Diferencia clave en Development vs Production

| Aspecto | Development Local | Production |
|---------|------------------|-----------|
| Base de datos | H2 (en memoria) o Postgres local | RDS, Azure, etc. (externales) |
| Vault | `vault:dev` local en docker | Vault empresarial o cloud KMS (AWS Secrets, Azure Key Vault) |
| Token Vault | `root` (modo dev, ejemplo) | AppRole / Kubernetes Auth / AWS IAM |
| Variables env | Locales en docker-compose | Inyectadas por CI/CD o secrets manager |
| SSL/TLS | No (http://localhost:8200) | Sí obligatorio (https://vault.company.com) |
| Secretos | Almacenados en contenedor Vault (se borran al `down`) | Persistentes en sistema de secretos empresarial |

## Próximos pasos (en tu máquina con Docker)

1. Instala Docker Desktop
2. Abre PowerShell/cmd en la carpeta del proyecto
3. Ejecuta `docker compose up`
4. Espera a ver "Started Application"
5. Prueba llamar a `http://localhost:8080` en tu navegador o con `curl`
6. Verifica logs con `docker-compose logs app`
7. Para más seguridad en producción, configura AppRole o Kubernetes Auth en Vault (puedo ayudarte si lo necesitas)

---

**Nota final:** Este flujo demuestra un ciclo DevOps básico pero funcional:
- ✅ CI: Compilación automatizada (GitHub Actions)
- ✅ Secrets management: Vault integrado
- ✅ Infrastructure as Code: docker-compose.yml
- ✅ Validation: @PostConstruct en JwtTokenProvider
- ⏳ Falta: CD (deployment automatizado) y observability (logs, métricas)
