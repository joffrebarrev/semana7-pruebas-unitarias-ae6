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