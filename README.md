# api_challenge_ntt_je

Este proyecto usa Spring Boot como framework principal.

Si quieres aprender más sobre Spring Boot, visita: <https://spring.io/projects/spring-boot>.

## Ejecutar la aplicación en modo de desarrollo

Para ejecutar la aplicación en modo desarrollo (arrancar con Spring Boot y recargar cambios durante desarrollo), usa:

```shell
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Empaquetar y ejecutar la aplicación

Empaqueta la aplicación con Maven:

```shell
./mvnw package
```

El empaquetado generará un `jar` en la carpeta `target/` (por ejemplo `target/api_challenge_ntt_je-1.0.0-SNAPSHOT.jar`). Ejecuta el artefacto con:

```shell
java -jar target/*.jar --spring.profiles.active=dev
```

Si prefieres ejecutar la aplicación directamente desde Maven en un perfil distinto:

```shell
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Guías relacionadas

- Hibernate ORM ([guide](https://spring.io/guides/using-data-jpa)): Define tu modelo persistente con Spring Data JPA y Jakarta Persistence
- OpenAPI / Swagger ([guide](https://springdoc.org/)): Documenta tus APIs REST con OpenAPI y Swagger UI
- Jackson ([guide](https://spring.io/guides/gs/rest-service/)): Serialización JSON con Jackson en Spring Boot
- Conector JDBC - PostgreSQL ([guide](https://spring.io/guides/gs/accessing-data-jpa/)): Conectar a PostgreSQL usando Spring Data JPA

## Código proporcionado

### Hibernate / JPA

Ejemplos y entidades JPA ya presentes en el proyecto.

### REST

Controladores REST basados en Spring Web (Spring MVC / Spring Web).


## Manejo de secretos (credenciales)

- **Objetivo:** Evitar credenciales hardcodeadas en el código y en `application-*.properties`.
- **Estrategia mínima (recomendada):** usar variables de entorno para `spring.datasource.*` y `jwt.secret`.

Configuraciones ya aplicadas en el proyecto:

- `src/main/resources/application-dev.properties`: lee `spring.datasource.username` y `spring.datasource.password` desde `DATABASE_USER` y `DATABASE_PASSWORD`, y `jwt.secret` desde `JWT_SECRET`.
- `src/main/resources/application-test.properties`: comportamiento similar; mantiene un secreto por defecto seguro para tests.

Ejemplos para ejecutar localmente (Windows cmd):

```cmd
set JWT_SECRET=mi-secreto-muy-fuerte-de-32-chars-o-mas
set DATABASE_USER=postgres
set DATABASE_PASSWORD=MiPassLocal
java -jar target/*.jar --spring.profiles.active=dev
```

PowerShell (sesión):

```powershell
$env:JWT_SECRET = 'mi-secreto-muy-fuerte-de-32-chars-o-mas'
$env:DATABASE_USER = 'postgres'
$env:DATABASE_PASSWORD = 'MiPassLocal'
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Opcional — integración con un gestor de secretos (HashiCorp Vault):

- Añadir dependencia (pom.xml):

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-vault-config</artifactId>
</dependency>
```

- Propiedades de ejemplo para `application-prod.properties` (usar Vault como fuente):

```
spring.cloud.vault.uri=https://vault.example.com:8200
spring.cloud.vault.token=${VAULT_TOKEN}
spring.cloud.vault.generic.enabled=true
spring.cloud.vault.generic.backend=secret
spring.cloud.vault.generic.default-context=application
```

Con Vault, mapea `spring.datasource.password` y `jwt.secret` a las claves almacenadas en Vault. Esto evita exponer secretos como valores por defecto en el repositorio.

Si quieres, puedo:

- Añadir la dependencia y ejemplo de configuración de Spring Cloud Vault en `pom.xml` y `application-prod.properties`.
- Añadir un pequeño `docker-compose` o `Makefile` para levantar un Vault de desarrollo y probar la extracción de secretos.

### Implementaciones añadidas (resumen)

- **CI pipeline:** incluye un workflow de GitHub Actions en `.github/workflows/ci.yml` que compila el proyecto y ejecuta los tests en cada `push` y `pull_request`.
- **Docker + Vault:** `docker-compose.yml` en la raíz que arranca Postgres local, un Vault en modo `dev`, un contenedor `vault-init` que escribe secretos de ejemplo, y un servicio `app` que construye y ejecuta la aplicación.
- **Dockerfile para la app:** `Dockerfile.app` construye el artefacto con Maven y empaca el `jar` en una imagen de runtime.

### Cómo probar cada cosa (paso a paso)

1) CI (GitHub Actions)
- Qué hace: en cada `push` o `pull_request` compila el proyecto y ejecuta los tests.
- Ficheros: `.github/workflows/ci.yml`.

2) Docker + docker-compose (entorno reproducible de desarrollo)
- Qué hace: levanta servicios necesarios para desarrollo: Postgres, Vault (modo dev) y tu aplicación.
- Ficheros: `docker-compose.yml`, `Dockerfile.app`.
- Comandos para usarlo (desde la raíz del proyecto):

```bash
docker compose build
docker compose up
```

- Qué esperar: el servicio `vault-init` esperará a que Vault esté arriba y luego escribirá secretos de ejemplo bajo `secret/data/application`. La app se compilará (maven) y arrancará en `http://localhost:8080`.

3) Vault en modo dev (para pruebas locales)
- Qué hace: Vault en modo `dev` corre sin TLS y con un token fijo (`root`) para facilitar pruebas locales. NO usar en producción.
- Cómo la app obtiene secretos: en `docker-compose` la app tiene `SPRING_CLOUD_VAULT_URI` y `SPRING_CLOUD_VAULT_TOKEN` apuntando a Vault; si integras `spring-cloud-starter-vault-config` la app leerá propiedades directamente desde Vault al arrancar.

4) Inicializar secretos manualmente (si no quieres usar `vault-init`)
- Ejemplo con `curl` una vez Vault esté arriba:

```bash
export VAULT_ADDR='http://localhost:8200'
export VAULT_TOKEN='root'
curl --header "X-Vault-Token: $VAULT_TOKEN" --request POST --data '{"data":{"jwt.secret":"mi-secreto-de-32-caracteres-minimo-1234","spring.datasource.password":"postgres"}}' $VAULT_ADDR/v1/secret/data/application
```

5) Integración de Vault en el proyecto (dependencia)
- Nota: para que Spring Boot lea propiedades desde Vault automáticamente necesitas añadir la dependencia de Spring Cloud Vault y gestionar versiones con el BOM de Spring Cloud; puedo añadirlo si quieres.

### Notas de seguridad y límites

- El Vault que levanta `docker-compose` corre en modo `dev` (token `root`) **solo** para desarrollo local. No usar así en producción.
- La sección `vault-init` escribe secretos de ejemplo; en entornos reales los secretos los crea/gestiona el equipo de infraestructura o CI/CD.

