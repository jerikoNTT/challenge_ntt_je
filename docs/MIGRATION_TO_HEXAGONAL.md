# Migración a Arquitectura Hexagonal — Notas rápidas

Objetivo: explicar en términos simples cómo pasamos de la arquitectura en capas a la arquitectura hexagonal en este proyecto.

1) Conceptos
- Capas (layered): Controller -> Service -> Repository (JPA) -> DB. Las dependencias fluyen de arriba hacia abajo.
- Hexagonal (Ports & Adapters): la lógica de dominio (Use Cases) define *Ports* (interfaces) que no dependen de detalles. *Adapters* implementan los ports hacia fuera (persistence, REST, messaging).

2) Mapeo de nombres en este proyecto
- Controller (antes en `api_ntt_challenge.controller`) → Adapter inbound (REST) en `api_ntt_challenge.adapters.inbound.rest`.
- Service (antes en `api_ntt_challenge.service`) → Use Case / Application service (se mantiene en `service`) que ahora depende **solo** de Ports en `application.ports.outbound`.
- IClientRepo / IAccountRepo (antes interfaces repo en `repository`) → están marcadas como *legadas* (`@Deprecated`) y su funcionalidad se representa por `ClientPersistencePort` / `AccountPersistencePort`.
- ClientRepoImpl / AccountRepoImpl (antes impl) → Adapter outbound JPA, ahora preferiblemente en `adapters.outbound.jpa` (ej: `ClientJpaAdapter`, `AccountJpaAdapter`).

3) Estrategia incremental seguida
- Crear Ports (interfaces) en `application.ports.outbound` replicando métodos necesarios.
- Crear Adapters JPA en `adapters.outbound.jpa` que implementan esos ports.
- Actualizar Services para depender de los Ports en vez de repositorios concretos.
- Crear Adapters inbound (REST) en `adapters.inbound.rest` y marcar los controllers antiguos como `@Deprecated`.
- Añadir tests unitarios para Use Cases (mockear los Ports) para garantizar comportamiento durante refactor.

4) Qué queda por hacer para completar la migración
- Se han eliminado las implementaciones legadas de repositorios (`ClientRepoImpl`, `AccountRepoImpl`, `MovementRepoImpl`) y sus responsabilidades ahora están en los adapters `adapters.outbound.jpa`.
- Eliminar o revisar interfaces legadas (`IClientRepo`, `IAccountRepo`) si ya no son necesarias.
- Mover las últimas clases restantes a paquetes `adapters.*` o `application.*` según su rol.
- Añadir integración (DataJpaTest) y documentación final.

5) Beneficios inmediatos
- Mejor separación de responsabilidades
- Use Cases agnósticos a infraestructura
- Más simples de testear y cambiar adaptadores (ej. reemplazar JPA por otro mecanismo)

---

Si quieres, puedo crear una rama con estos cambios o limpiar/retirar las clases legadas una vez tengas señal para eliminar deuda técnica.