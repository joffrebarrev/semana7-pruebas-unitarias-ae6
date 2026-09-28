# Matriz de Casos de Prueba - Actividad Ae6

| ID | Método | Escenario | Entrada | Esperado | Tipo | Doble de Prueba |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| CP-01 | puedeCancelar | Cancelación normal | horas = 5 | true | Normal | N/A |
| CP-02 | puedeCancelar | Límite exacto superior | horas = 2 | true | Límite | N/A |
| CP-03 | puedeCancelar | Debajo del límite | horas = 1 | false | Límite | N/A |
| CP-04 | puedeCancelar | Horas negativas | horas = -1 | false / Excepción | Inválido | N/A |
| CP-05 | calcularTotal | Descuento NORMAL | tipo = "NORMAL", monto = 100.0 | 100.0 | Normal | N/A |
| CP-06 | calcularTotal | Descuento VIP | tipo = "VIP", monto = 100.0 | 85.0 | Alternativo | N/A |
| CP-07 | calcularTotal | Descuento ESTUDIANTE | tipo = "ESTUDIANTE", monto = 100.0 | 90.0 | Alternativo | N/A |
| CP-08 | calcularTotal | Total negativo o cero | tipo = "NORMAL", monto = -10.0 | IllegalArgumentException | Inválido | N/A |
| CP-09 | confirmar | Disponibilidad exitosa | Reserva (valida), cliente disponible = true | Reserva CONFIRMADA | Normal | Stub (Disponibilidad), Mock (Repo/Notif) |
| CP-10 | confirmar | Sin disponibilidad | Reserva (valida), cliente disponible = false | IllegalStateException | Alternativo | Stub (Disponibilidad) |
| CP-11 | confirmar | Reserva nula | null | IllegalArgumentException | Inválido | N/A |
| CP-12 | confirmar | Error al notificar | Reserva (valida), Notificador lanza excepción | Excepción propagada / Manejada | Excepción | Stub (Disponibilidad), Mock (Notificador) |