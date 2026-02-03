Resumen de la migración a Spring Boot + Arquitectura Hexagonal

✅ Estado: Se restauró y aplicó la migración solicitada.

Cambios clave realizados:
- Migración de Quarkus a Spring Boot:
  - `pom.xml` actualizado (Spring Boot starters: web, data-jpa, springdoc, postgresql)
  - `application.properties` convertido a propiedades Spring
  - Añadido `Application.java` con `@SpringBootApplication`

- Arquitectura Hexagonal (Ports & Adapters):
  - **Ports (outbound)**: `ClientPersistencePort`, `AccountPersistencePort`, `MovementPersistencePort` (paquete `application.ports.outbound`).
  - **Adapters (outbound/jpa)**: Repositorios Spring Data y adaptadores: `JpaClientRepository`, `ClientJpaAdapter`, `JpaAccountRepository`, `AccountJpaAdapter`, `JpaMovementRepository`, `MovementJpaAdapter`.
  - **Adapters (inbound/rest)**: `ClientController`, `AccountController`, `MovementController` (Spring MVC controllers en `adapters.inbound.rest`).
  - Servicios convertidos a `@Service` que dependen de los Ports (`ClientServiceImpl`, `AccountServiceImpl`, `MovementServiceImpl`).

- Tests:
  - Pruebas unitarias añadidas: `ClientServiceImplTest`, `AccountServiceImplTest` (Mockito + JUnit 5).

Notas y siguientes pasos recomendados:
- Añadir pruebas de integración para los adaptadores JPA (H2 o Testcontainers).
- Migrar la seguridad (JWT) a Spring Security cuando lo soliciten.
- Opcional: pilotar programación reactiva (WebFlux + R2DBC) en un endpoint (p. ej. movimientos).

Si quieres, empujo estos cambios a una rama y abro un PR, o continúo con pruebas de integración o el piloto reactivo — dime cómo prefieres proceder.