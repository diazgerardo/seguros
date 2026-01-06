# Comparación: Emisión Síncrona vs Saga en el Dominio de Seguros

## Contexto
Este documento compara dos enfoques para la **emisión de pólizas** en un sistema de seguros:
1. **Flujo síncrono** (actual en el PoC)
2. **Saga mínima** (orquestada)

La comparación se centra en **garantías técnicas**, **impacto en negocio** y **costes operativos**.

---

## 1. Flujo Síncrono (request-response)

### Descripción
- El `policy-service` invoca al `rating-service` de forma **bloqueante**.
- La emisión ocurre **solo si** el rating responde correctamente.
- Todo se decide antes del `commit` de base de datos.

### Propiedades técnicas
- Atomicidad lógica fuerte (a nivel de caso de uso).
- Consistencia fuerte.
- Sin estados intermedios visibles.
- Concurrencia controlada con *optimistic locking*.
- Fallo = rollback completo.

### Ventajas
- Modelo mental simple (similar a Oracle Forms).
- Fácil de auditar y razonar.
- Invariantes de negocio explícitos.
- Menor superficie legal.

### Desventajas
- Acoplamiento temporal entre servicios.
- Latencia directa en la emisión.
- Menor disponibilidad ante fallos del rating.

### Cuándo usar
- Seguros tradicionales.
- Procesos críticos y auditables.
- Baja tolerancia a estados intermedios.

---

## 2. Saga Orquestada (mínima)

### Descripción
- La emisión se divide en pasos distribuidos.
- Un **orquestador** coordina el proceso.
- Los pasos se ejecutan de forma **asíncrona**.
- Se aceptan estados intermedios.

### Estados típicos
- DRAFT
- PENDING_RATING
- ISSUED
- CANCELLED (compensación)

### Propiedades técnicas
- Eventual consistency.
- No hay transacción global.
- Reintentos e idempotencia obligatorios.
- Compensaciones explícitas.

### Ventajas
- Mayor disponibilidad.
- Menor acoplamiento temporal.
- Mejor tolerancia a fallos parciales.
- Escala mejor en volumen.

### Desventajas
- Mayor complejidad técnica.
- Estados intermedios visibles.
- Compensaciones no siempre perfectas.
- Auditoría más compleja.

### Cuándo usar
- Alto volumen.
- Integraciones lentas o inestables.
- Necesidad de resiliencia extrema.
- Negocio acepta eventual consistency.

---

## Comparación resumida

| Aspecto                     | Síncrono                  | Saga Orquestada           |
|-----------------------------|---------------------------|---------------------------|
| Consistencia                | Fuerte                    | Eventual                  |
| Atomicidad global           | Lógica fuerte             | No                         |
| Estados intermedios         | No                        | Sí                         |
| Complejidad                 | Baja                      | Alta                      |
| Auditoría                   | Simple                    | Compleja                  |
| Disponibilidad              | Media                     | Alta                      |
| Similitud con Oracle Forms  | Alta                      | Baja                      |

---

## Conclusión

El flujo síncrono **no es inferior**: es una elección consciente que prioriza
consistencia y simplicidad.

Las sagas **no son un upgrade automático**, sino una respuesta a problemas
concretos de disponibilidad y escala.

> Recomendación: implementar primero una **saga mínima** sobre el flujo actual
> para evaluar impacto real antes de adoptarla en producción.

---

Documento generado para apoyar la decisión técnica en la migración
de Oracle Forms a microservicios.
