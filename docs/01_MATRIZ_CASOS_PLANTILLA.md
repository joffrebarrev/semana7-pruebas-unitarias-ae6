# Matriz de casos

| ID | Regla | Escenario | Entrada | Esperado | Tipo | Riesgo |
|---|---|---|---|---|---|---|
| CP-01 | puedeCancelar | Anticipación habitual | 5 horas | true | Normal | Comprueba la regla general de cancelación |
| CP-02 | puedeCancelar | Límite permitido | 2 horas | true | Límite | Detecta un uso incorrecto de `>` en vez de `>=` |
| CP-03 | puedeCancelar | Debajo del límite | 1 hora | false | Límite | Protege la frontera inferior de la regla |
| CP-04 | puedeCancelar | Sin anticipación | 0 horas | false | Extremo | No debe permitirse cancelación inmediata |
| CP-05 | calcularTotal | Cliente NORMAL | ("NORMAL", 100) | 100.0 | Normal | Confirma que no se aplica descuento por defecto |
| CP-06 | calcularTotal | Cliente VIP | ("VIP", 100) | 85.0 | Alternativo | Confirma el 15% de descuento VIP |
| CP-07 | calcularTotal | Cliente ESTUDIANTE | ("ESTUDIANTE", 100) | 90.0 | Alternativo | Confirma el 10% de descuento estudiante |
| CP-08 | calcularTotal | Total base en cero | ("VIP", 0) | 0.0 | Límite | Verifica que un descuento sobre 0 sigue siendo 0 |
| CP-09 | calcularTotal | Total base negativo | ("NORMAL", -1) | IllegalArgumentException | Inválido | Protege contra una entrada fuera del contrato del método |
