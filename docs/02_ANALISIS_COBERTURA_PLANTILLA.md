# Análisis de Cobertura JaCoCo - Actividad 2

## 1. Observación de Cobertura
- **¿Qué método tiene menor cobertura o líneas/ramas no ejecutadas?**
  El método `calcularTotal` o las validaciones de excepciones específicas ante parámetros inválidos.

- **¿Qué comportamiento falta?**
  Validar el comportamiento del sistema cuando se ingresan días de reserva negativos o valores no permitidos.

- **¿Qué prueba nueva aportaría valor?**
  Una prueba unitaria que verifique que `calcularTotal` lance `IllegalArgumentException` al pasarle una cantidad de días negativa o igual a cero.

- **¿Existe código cubierto pero mal probado?**
  Sí. La cobertura de código indica qué líneas se ejecutaron durante la prueba, pero no garantiza la calidad de las verificaciones. Si no existen aserciones adecuadas (`assertEquals`, `assertThrows`, etc.), una falla en la lógica podría pasar desapercibida aunque la línea aparezca en verde.

## 2. Reflexión Técnica: Stub vs. Mock
- **Stub:** Provee respuestas preprogramadas/controladas a las llamadas realizadas durante la prueba (ejemplo: `when(disponibilidad.estaDisponible(any())).thenReturn(true)`) para aislar la ejecución sin depender de la lógica interna de la dependencia.
- **Mock:** Se enfoca en la verificación de comportamiento, permitiendo comprobar si se realizaron o no las llamadas e interacciones esperadas hacia los colaboradores (ejemplo: `verify(repository).guardar(reserva)` o `verify(notificador, never()).enviarConfirmacion(any())`).

# Análisis de Cobertura de Código - JaCoCo (Actividad Ae6)

## 1. Resumen de Métricas
- **Proyecto:** `semAna7-proyecto-base-ae6`
- **Clase Evaluada:** `edu.uees.testing.service.ReservaService`
- **Instrucciones Cubiertas:** 100%
- **Ramas (Branches) Cubiertas:** 100%
- **Métodos Probados:** 3/3 (`puedeCancelar`, `calcularTotal`, `confirmar`)

## 2. Análisis por Comportamiento y Método
1. **`puedeCancelar(int horas)`:** 
   - Cobertura completa de la condición lógica (`horas >= 2`).
   - Se validaron casos por encima del límite (5 horas), límite exacto (2 horas), debajo del límite (1 hora) y valores límite/inválidos (0 horas).
2. **`calcularTotal(String tipo, double totalBase)`:** 
   - Cobertura total de los caminos condicionales (`VIP`, `ESTUDIANTE`, `NORMAL`).
   - Cobertura de la guarda de validación para montos inválidos (`totalBase < 0`) asegurando que se lance `IllegalArgumentException`.
3. **`confirmar(Reserva reserva)`:** 
   - Cobertura de la verificación de nulidad (`reserva == null`).
   - Cobertura de la bifurcación condicional según la disponibilidad informada por el Stub (`true` vs `false`).

## 3. Reflexión Técnica: ¿Por qué 100% de cobertura no garantiza la ausencia de errores?
Un porcentaje del 100% en JaCoCo únicamente garantiza que **cada línea y rama condicional de código fue ejecutada al menos una vez durante la suite de pruebas**. Sin embargo, no garantiza:
- **Reglas omitidas:** Si la lógica del negocio requiere validar que el código de cliente no sea nulo o vacío y esa validación no está programada en el código fuente, la cobertura seguirá marcando 100% aunque exista un fallo de diseño.
- **Concurrencia y estado externo:** La prueba con Mocks simula un entorno aislado sin considerar bloqueos de base de datos o latencia en servicios reales.