# UEES · Diseño de Software (UCOM0310)

## Semana 7: Pruebas unitarias, dobles de prueba (Stub/Mock), JaCoCo y flujo con Git

Repositorio de la Semana 7 de **Diseño de Software** (Universidad Espíritu Santo). Contiene una suite de pruebas unitarias con **JUnit 5** y **Mockito** para `ReservaService` del Sistema de gestión de tutorías, el análisis de cobertura con **JaCoCo** y la documentación del flujo de trabajo con Git y Pull Request (actividad evaluada **Ae6**).

**Estudiante:** Joffre Barre Veliz · **Período:** PEL 4 – 2026

---

## Tecnologías

| Herramienta | Uso |
| --- | --- |
| Java 17+ | Lenguaje |
| Apache Maven | Construcción y dependencias |
| JUnit 5 (Jupiter) | Pruebas unitarias |
| Mockito | Dobles de prueba (Stub y Mock) |
| JaCoCo `0.8.12` | Cobertura de código |
| Git y GitHub | Control de versiones, ramas y Pull Request |

---

## Estructura del proyecto

```text
semana7-pruebas-unitarias-ae6/
├── docs/
│   ├── 01_MATRIZ_CASOS_PLANTILLA.md        # Matriz de casos de prueba
│   ├── 02_ANALISIS_COBERTURA_PLANTILLA.md  # Análisis del reporte JaCoCo
│   └── 03_PULL_REQUEST_PLANTILLA.md        # Descripción y autorrevisión del PR
├── src/
│   ├── main/java/edu/uees/testing/
│   │   ├── availability/   # DisponibilidadClient
│   │   ├── domain/         # Reserva, EstadoReserva
│   │   ├── notification/   # Notificador
│   │   ├── repository/     # ReservaRepository
│   │   └── service/        # ReservaService
│   └── test/java/edu/uees/testing/service/
│       └── ReservaServiceTest.java         # Suite de 12 pruebas
├── pom.xml
├── .gitignore
└── README.md
```

---

## Ramas

| Rama | Contenido |
| --- | --- |
| `main` | Línea base del proyecto y Laboratorio 1 (matriz de casos y pruebas AAA de `puedeCancelar` y `calcularTotal`). |
| `test/lab2-dobles-cobertura` | Laboratorio 2: Stub y Mock con Mockito y primer análisis de cobertura. |
| `ae6/suite-pruebas` | **Actividad evaluada Ae6:** suite completa de 12 pruebas, análisis JaCoCo y Pull Request hacia `main`. |

---

## Reglas de negocio protegidas

- **Cancelación:** solo se permite cancelar con **2 o más horas** de anticipación (`horas >= 2`).
- **Descuentos:** VIP 15 %, ESTUDIANTE 10 %, NORMAL 0 %. Un total base **negativo** lanza `IllegalArgumentException("Total base inválido")`; un total base de `0.0` es válido y devuelve `0.0`.
- **Confirmación:** consulta `DisponibilidadClient`. Si hay disponibilidad, la reserva pasa a `CONFIRMADA`, se guarda con `ReservaRepository` y se notifica con `Notificador`. Si no la hay, lanza `IllegalStateException` sin guardar ni notificar. Una reserva `null` lanza `IllegalArgumentException` sin consultar a ningún colaborador.

---

## Suite de pruebas (`ReservaServiceTest`): 12 pruebas

| ID | Método de prueba | Qué verifica |
| --- | --- | --- |
| CP-01 | `cincoHorasPermitenCancelar` | 5 horas → `true` |
| CP-02 | `dosHorasEsElLimitePermitido` | 2 horas (límite exacto) → `true` |
| CP-03 | `unaHoraNoPermiteCancelar` | 1 hora → `false` |
| CP-04 | `sinAnticipacionNoPermiteCancelar` | 0 horas → `false` |
| CP-05 | `normalNoRecibeDescuento` | NORMAL, 100.0 → 100.0 |
| CP-06 | `vipRecibeQuincePorCiento` | VIP, 100.0 → 85.0 |
| CP-07 | `estudianteRecibeDiezPorCiento` | ESTUDIANTE, 100.0 → 90.0 |
| CP-08 | `vipConTotalBaseCeroDevuelveCero` | VIP, 0.0 → 0.0 |
| CP-08b | `totalNegativoEsInvalido` | -1.0 → `IllegalArgumentException` |
| CP-09 | `reservaDisponibleSeConfirmaGuardaYNotifica` | Estado `CONFIRMADA`; `guardar` y `enviarConfirmacion` invocados |
| CP-10 | `reservaNoDisponibleNoSeGuardaNiNotifica` | `IllegalStateException`; `guardar` y `enviarConfirmacion` nunca invocados |
| CP-11 | `reservaNulaNoConsultaDependencias` | `IllegalArgumentException`; ningún colaborador consultado |

Todas las pruebas siguen el patrón **Arrange-Act-Assert** y usan `@BeforeEach` para recrear el servicio y sus dobles antes de cada ejecución. El constructor de `ReservaService` recibe los colaboradores en el orden `(repository, notificador, disponibilidad)`.

### Stub y Mock

- **Stub** (`DisponibilidadClient`): entrega respuestas preprogramadas con `when(...).thenReturn(...)` para forzar el camino de la prueba.
- **Mock** (`ReservaRepository`, `Notificador`): se usa para verificar interacciones con `verify(...)` y `verify(..., never())`.

---

## Cobertura con JaCoCo

Al ejecutar `mvn clean test` se genera el reporte en:

```text
target/site/jacoco/index.html
```

Resultado para `edu.uees.testing.service.ReservaService`: **100 % de instrucciones** y **100 % de ramas** (3 de 3 métodos).

> Un 100 % de cobertura indica qué código se ejecutó, no que esté bien probado. El valor de la suite está en sus aserciones (`assertEquals`, `assertThrows`, `verify`). Tampoco detecta reglas de negocio omitidas ni problemas de concurrencia o de integración real.

---

## Cómo ejecutar

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/joffrebarrev/semana7-pruebas-unitarias-ae6.git
   cd semana7-pruebas-unitarias-ae6
   ```

2. Cambiar a la rama de la actividad Ae6:

   ```bash
   git switch ae6/suite-pruebas
   ```

3. Ejecutar la suite y generar el reporte de cobertura:

   ```bash
   mvn clean test
   ```

   Resultado esperado: `Tests run: 12, Failures: 0, Errors: 0, Skipped: 0` y `BUILD SUCCESS`.

4. Abrir el reporte de cobertura (Windows PowerShell):

   ```powershell
   start target\site\jacoco\index.html
   ```

---

## Flujo de trabajo con Git

```bash
git switch -c ae6/suite-pruebas
git add .
git commit -m "test: corregir constructor ReservaService e integrar suite JUnit 5 con mocks"
git push -u origin ae6/suite-pruebas
```

Después se abre un **Pull Request** de `ae6/suite-pruebas` hacia `main`, con la descripción y la autorrevisión de `docs/03_PULL_REQUEST_PLANTILLA.md`. La carpeta `target/` está excluida con `.gitignore`.

---

## Declaración de uso de IA

<<<<<<< HEAD
Se utilizó un asistente de inteligencia artificial como apoyo para estructurar los Mocks con Mockito, corregir el orden de los argumentos del constructor y redactar la documentación. El código fue ejecutado y verificado por el estudiante con `mvn clean test`.
=======
Se utilizó un asistente de inteligencia artificial como apoyo para estructurar los Mocks con Mockito, corregir el orden de los argumentos del constructor y redactar la documentación. El código fue ejecutado y verificado por el estudiante con `mvn clean test`.
>>>>>>> origin/main
