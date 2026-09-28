# UEES - Diseño de Software (UCOM0310)
## Semana 7: Pruebas Unitarias, Dobles de Prueba (Mocks/Stubs), JaCoCo y Git

Este repositorio contiene la evolución y resolución práctica de los laboratorios formativos de la **Semana 7** para la asignatura de **Diseño de Software** en la Universidad Espíritu Santo (UEES).

---

## 🛠️ Tecnologías y Herramientas Utilizadas
- **Lenguaje de Programación:** Java 17+
- **Gestor de Dependencias y Construcción:** Apache Maven
- **Framework de Pruebas Unitarias:** JUnit 5 (Jupiter)
- **Framework de Dobles de Prueba:** Mockito
- **Análisis de Cobertura de Código:** JaCoCo Plugin (`0.8.12`)
- **Control de Versiones:** Git & GitHub

---

## 🌳 Estructura del Repositorio y Ramas (Branches)

- **`main`**: Contiene la línea base del proyecto y el desarrollo correspondiente al **Laboratorio 1** (matriz de 9 pruebas unitarias con patrón AAA).
- **`test/lab2-dobles-cobertura`**: Rama dedicada a la implementación de aislamiento de dependencias usando **Stubs y Mocks** con Mockito, simulación de flujos de interacción y análisis de cobertura de JaCoCo.

---

## 🧪 Resumen de Pruebas Unitarias (`ReservaServiceTest`)

Se ejecutan un total de **12 pruebas unitarias con resultado 100% exitoso (`BUILD SUCCESS`)**, divididas en dos fases principales:

### **1. Validación de Reglas de Negocio (Actividad 1 - AAA)**
- `puedeCancelar_reservaPendiente_retornaTrue()`
- `puedeCancelar_reservaConfirmada_retornaFalse()`
- `puedeCancelar_reservaCancelada_retornaFalse()`
- `calcularTotal_tipoNormal_aplicaTarifaBase()`
- `calcularTotal_tipoVIP_aplicaDescuentoVIP()`
- `calcularTotal_tipoCorporativo_aplicaDescuentoCorporativo()`
- `calcularTotal_diasCeroOMenos_lanzaExcepcion()`
- `calcularTotal_tipoDesconocido_lanzaExcepcion()`
- `calcularTotal_tipoNulo_lanzaExcepcion()`

### **2. Aislamiento e Interacción con Dobles de Prueba (Actividad 2 - Mockito)**
- **Caso 1 (`reservaDisponibleSeConfirmaGuardaYNotifica`):**
  - **Stub:** Controla que `disponibilidad.estaDisponible(...)` retorne `true`.
  - **Verificación (Mock):** Comprueba el cambio de estado a `CONFIRMADA` y verifica que se ejecuten `repository.guardar(...)` y `notificador.enviarConfirmacion(...)`.
- **Caso 2 (`reservaNoDisponibleNoSeGuardaNiNotifica`):**
  - **Stub:** Controla que `disponibilidad.estaDisponible(...)` retorne `false`.
  - **Verificación (Mock):** Asegura que se lance `IllegalStateException` y verifica que **nunca** (`never()`) se llame a `guardar(...)` ni a `enviarConfirmacion(...)`.
- **Caso 3 (`reservaNulaNoConsultaDependencias`):**
  - **Verificación (Mock):** Confirma que al recibir un objeto nulo se lance `IllegalArgumentException` y no se consulte a **ningún** colaborador.

---

## 📊 Cobertura de Código con JaCoCo

El reporte de cobertura se genera automáticamente al ejecutar las pruebas:

```bash
mvn clean test

### Ubicación del Reporte:
target/site/jacoco/index.html
---

### Conclusiones del Análisis de Cobertura:
Diferencia entre Stub y Mock:

Stub: Provee respuestas preprogramadas para controlar el camino de ejecución durante la prueba (when(...).thenReturn(...)).

Mock: Se enfoca en la verificación del comportamiento y llamadas realizadas entre objetos (verify(...)).
---
### Interpretación de Cobertura:

Obtener un alto porcentaje de cobertura en líneas/ramas garantiza qué código fue ejecutado, pero el valor real de la prueba radica en las aserciones (assertEquals, assertThrows, verify) que protegen directamente las reglas de negocio.
---
### ⚙️ Instrucciones de Ejecución Local
---
###**1. Clonar el repositorio:**

git clone [https://github.com/joffrebarrev/semana7-pruebas-unitarias-ae6.git](https://github.com/joffrebarrev/semana7-pruebas-unitarias-ae6.git)
cd semana7-pruebas-unitarias-ae6
---
###** 2. Cambiar a la rama de desarrollo:**

Bash
git switch test/lab2-dobles-cobertura
---
###**3. Ejecutar la suite completa de pruebas:**

Bash
mvn clean test
---