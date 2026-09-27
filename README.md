# UEES - Diseño de Software (UCOM0310)
## Semana 7: Pruebas Unitarias, Dobles de Prueba (Stub/Mock) y Cobertura con JaCoCo

Este repositorio contiene la resolución práctica de los laboratorios formativos de la **Semana 7**, para la asignatura de **Diseño de Software** (UCOM0310) en la Universidad Espíritu Santo (UEES).

El sistema bajo prueba es un módulo de reservas (`ReservaService`) con dos tipos de comportamiento:
- **Lógica pura**, sin dependencias externas: `puedeCancelar(int)` y `calcularTotal(String, double)`.
- **Lógica con colaboradores externos**: `confirmar(Reserva)`, que depende de `DisponibilidadClient`, `ReservaRepository` y `Notificador`.

---

## 🛠️ Tecnologías y herramientas

- **Lenguaje:** Java 21
- **Gestor de build:** Apache Maven
- **Pruebas unitarias:** JUnit 5 (Jupiter) `5.10.2`
- **Dobles de prueba:** Mockito `5.12.0`
- **Cobertura de código:** JaCoCo `0.8.12`
- **Control de versiones:** Git & GitHub

---

## 🌳 Ramas del repositorio

- **`main`** — Laboratorio 1: diseño de casos y suite JUnit 5 con estructura AAA para la lógica pura del servicio.
- **`test/lab2-dobles-cobertura`** — Laboratorio 2: aislamiento de dependencias con Stub y Mock (Mockito) para `confirmar(Reserva)`, y análisis de cobertura con JaCoCo.

---

## 🧪 Suite de pruebas (`ReservaServiceTest` + `ReservaServiceConfirmarTest`)

### 1. Reglas de negocio puras — `ReservaServiceTest` (Laboratorio 1, 9 pruebas)

**`puedeCancelar(int horasAnticipacion)`** — regla: se permite cancelar con 2+ horas de anticipación.
- `cincoHorasPermitenCancelar()`
- `dosHorasEsElLimitePermitido()`
- `unaHoraNoPermiteCancelar()`
- `sinAnticipacionNoPermiteCancelar()`

**`calcularTotal(String tipo, double totalBase)`** — descuentos: VIP 15%, ESTUDIANTE 10%, NORMAL sin descuento.
- `normalNoRecibeDescuento()`
- `vipRecibeQuincePorCiento()`
- `estudianteRecibeDiezPorCiento()`
- `vipConTotalBaseCeroDevuelveCero()`
- `totalNegativoEsInvalido()` — lanza `IllegalArgumentException`

### 2. Aislamiento con Stub y Mock — `ReservaServiceConfirmarTest` (Laboratorio 2, 4 pruebas)

**Estrategia Stub** (dobles caseros, sin Mockito):
- `reservaDisponibleSeConfirmaConStubs()` — usa un `DisponibilidadStub`, un repositorio en memoria y un notificador silencioso; verifica el estado final (`CONFIRMADA`) y que la reserva quedó guardada.

**Estrategia Mock con Mockito** (verificación de interacción):
- `reservaDisponibleSeConfirmaGuardaYNotifica()` — `disponibilidad` responde `true`; se verifica `repository.guardar(...)` y `notificador.enviarConfirmacion(...)`.
- `reservaNoDisponibleLanzaExcepcionYNoGuardaNiNotifica()` — `disponibilidad` responde `false`; se lanza `IllegalStateException` y se verifica con `never()` que nunca se guarda ni se notifica.
- `reservaNulaLanzaExcepcionYNoConsultaColaboradores()` — se lanza `IllegalArgumentException` y se verifica con `verifyNoInteractions(...)` que no se consulta a ningún colaborador.

**Total: 13 pruebas — `BUILD SUCCESS`.**

---

## 📊 Cobertura de código con JaCoCo

El reporte se genera automáticamente al ejecutar las pruebas:

```bash
mvn clean test
```

Ubicación del reporte: `target/site/jacoco/index.html`

**Conclusión del análisis:** un porcentaje alto de cobertura solo indica qué líneas se ejecutaron; no garantiza que las pruebas verifiquen correctamente el comportamiento. Las aserciones (`assertEquals`, `assertThrows`) y las verificaciones de Mockito (`verify`, `never`, `verifyNoInteractions`) son las que realmente protegen las reglas de negocio, en especial los casos límite (2 horas exactas) y los caminos de error de `confirmar(Reserva)`.

---

## ⚙️ Instrucciones de ejecución local

Clonar el repositorio:

```bash
git clone https://github.com/joffrebarrev/semana7-pruebas-unitarias-ae6.git
cd semana7-pruebas-unitarias-ae6
```

Cambiar a la rama del Laboratorio 2:

```bash
git checkout test/lab2-dobles-cobertura
```

Ejecutar la suite completa:

```bash
mvn clean test
```

---

## 📁 Entregables

- `docs/01_MATRIZ_CASOS_PLANTILLA.md` — matriz de 9 casos de prueba diseñados.
- `entregables/UCOM0310 Barre Veliz Semana7 Act1 Laboratorio JUnit Casos.docx` — informe del Laboratorio 1 con evidencia de ejecución y microexperimento del bug de frontera.

---

Desarrollado por **Joffre Barre Veliz** como parte del laboratorio formativo de Diseño de Software — UEES, Semana 7.
