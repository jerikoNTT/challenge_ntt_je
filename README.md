# api_challenge_ntt_je

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/api_challenge_ntt_je-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and Jakarta Persistence
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing Jakarta REST and more
- REST JAXB ([guide](https://quarkus.io/guides/resteasy-reactive#xml-serialisation)): JAXB serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)



### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)

## Manejo de secretos (credenciales)

- **Objetivo:** Evitar credenciales hardcodeadas en el código y en `application-*.properties`.
- **Estrategia mínima (recomendada):** usar variables de entorno para `spring.datasource.*` y `jwt.secret`.

Configuraciones ya aplicadas en el proyecto:

- `src/main/resources/application-dev.properties`: ahora lee `spring.datasource.username` y `spring.datasource.password` desde `DATABASE_USER` y `DATABASE_PASSWORD`, y `jwt.secret` desde `JWT_SECRET`.
- `src/main/resources/application-test.properties`: mismo comportamiento; mantiene un secreto por defecto seguro para tests (`JWT_SECRET` con valor por defecto de testing).

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

Con Vault, mapee `spring.datasource.password` y `jwt.secret` a las claves almacenadas en Vault. Esto evita exponer secretos como valores por defecto en el repo.

Si quieres, puedo:

- Añadir dependencia y ejemplo de configuración de Spring Cloud Vault en `pom.xml` y `application-prod.properties`.
- Añadir un pequeño script `docker-compose` o `Makefile` para levantar un Vault de desarrollo y probar la extracción de secretos.

### Implementaciones añadidas (lo que acabo de crear)

- **CI pipeline:** añadí un workflow de GitHub Actions en `.github/workflows/ci.yml` que compila el proyecto y ejecuta los tests en cada `push` y `pull_request`.
- **Docker + Vault:** añadí `docker-compose.yml` en la raíz que arranca un Postgres local, un servidor Vault en modo `dev`, un contenedor `vault-init` que escribe secretos de ejemplo en Vault, y un servicio `app` que construye y ejecuta la aplicación.
- **Dockerfile para la app:** `Dockerfile.app` construye el artefacto con Maven y empaca el `jar` en una imagen de runtime.

### Cómo funcionan y cómo probar cada cosa (paso a paso)

1) CI (GitHub Actions)
- Qué hace: en cada `push` o `pull_request` compila el proyecto y ejecuta los tests. Esto detecta fallos antes de mezclar código.
- Ficheros: `.github/workflows/ci.yml`.
- Cómo probar localmente: pushing a tu repo (GitHub) activará el workflow automáticamente. Puedes revisar la ejecución en la pestaña "Actions" del repo.

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
- Nota: para que Spring Boot escriba/lea propiedades desde Vault automáticamente necesitas añadir la dependencia de Spring Cloud Vault.
- Ejemplo a añadir en `pom.xml` (si quieres que lo haga, lo añado):

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-vault-config</artifactId>
</dependency>
```

- Además es recomendable alinear la versión de Spring Cloud con tu Spring Boot mediante el BOM de Spring Cloud; puedo añadirlo automáticamente si quieres.

### Notas de seguridad y límites de esta implementación
- El Vault que levanta `docker-compose` corre en modo `dev` (token `root`) **solo** para desarrollo local. No usar así en producción.
- La sección `vault-init` escribe secretos de ejemplo; en entornos reales los secretos los crea/gestiona el equipo de infraestructura o CI/CD.

