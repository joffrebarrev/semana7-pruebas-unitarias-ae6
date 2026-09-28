# UEES - Diseño de Software (UCOM0310)
## Actividad Evaluada Ae6: Pruebas Unitarias, Dobles de Prueba (Stub/Mock), JaCoCo y Git

Este repositorio contiene la resolución práctica de la **Actividad Evaluada Ae6** para la asignatura de **Diseño de Software** (UCOM0310) en la Universidad Espíritu Santo (UEES).

El sistema bajo prueba es un módulo de reservas (`ReservaService`) con dos tipos de comportamiento:
- **Lógica pura**, sin dependencias externas: `puedeCancelar(int)` y `calcularTotal(String, double)`.
- **Lógica con colaboradores externos**: `confirmar(Reserva)`, que depende de `DisponibilidadClient`, `ReservaRepository` y `Notificador`.

---

## 🛠️ Tecnologías y herramientas

- **Lenguaje:** Java 17+
- **Gestor de build:** Apache Maven
- **Pruebas unitarias:** JUnit 5 (Jupiter)
- **Dobles de prueba:** Mockito
- **Cobertura de código:** JaCoCo Plugin (`jacoco-maven-plugin`)
- **Control de versiones:** Git & GitHub

---

## 🌳 Ramas del repositorio

- **`main`** — Línea base del proyecto.
- **`ae6/suite-pruebas`** — Rama de trabajo principal donde se integró la suite de 12 pruebas unitarias con JUnit 5 y Mockito, se corrigió el constructor del servicio y se completó el análisis de cobertura JaCoCo.

---

## 🧪 Suite de pruebas (`ReservaServiceTest`)

Se ejecutan un total de **12 pruebas unitarias con resultado 100% exitoso (`BUILD SUCCESS`)**:

### 1. Reglas de negocio puras (Lógica de Cancelación y Descuentos)

**`puedeCancelar(int horasAnticipacion)`** — regla: se permite cancelar con 2+ horas de anticipación.
- `puedeCancelar_conAnticipacionSuficiente_retornaTrue()` (5 horas)
- `puedeCancelar_enLimiteExactoDosHoras_retornaTrue()` (2 horas - límite)
- `puedeCancelar_conMenosDeDosHoras_retornaFalse()` (1 hora - límite)
- `puedeCancelar_sinAnticipacion_retornaFalse()` (0 horas)

**`calcularTotal(String tipo, double totalBase)`** — descuentos: VIP 15%, ESTUDIANTE 10%, NORMAL 0%.
- `calcularTotal_clienteNormal_aplicaTarifaBase()`
- `calcularTotal_clienteVIP_aplicaDescuento()`
- `calcularTotal_clienteEstudiante_aplicaDescuento()`
- `calcularTotal_montoBaseCero_retornaCero()`
- `calcularTotal_montoNegativo_lanzaExcepcion()` — lanza `IllegalArgumentException`

### 2. Aislamiento con Stub y Mock (Mockito)

**`confirmar(Reserva reserva)`** — interacción con dependencias externas:
- `confirmar_reservaDisponible_confirmaGuardaYNotifica()` — Stub: `disponibilidad` responde `true`; Mock: verifica estado `CONFIRMADA` y ejecuciones de `repository.guardar(...)` y `notificador.enviar(...)`.
- `confirmar_reservaNoDisponible_lanzaExcepcionYNoGuarda()` — Stub: `disponibilidad` responde `false`; Mock: lanza `IllegalStateException` y verifica con `never()` que no se guarda ni notifica.
- `confirmar_reservaNula_lanzaExcepcionYNoInteractua()` — Mock: lanza `IllegalArgumentException` ante objeto `null` y verifica cero interacciones.

---

## 📊 Cobertura de código con JaCoCo

El reporte se genera automáticamente al ejecutar las pruebas:

```bash
mvn clean test

---

Desarrollado por **Joffre Barre Veliz** como parte del laboratorio formativo de Diseño de Software — UEES, Semana 7.
