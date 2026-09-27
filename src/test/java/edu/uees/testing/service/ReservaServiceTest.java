package edu.uees.testing.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import edu.uees.testing.availability.DisponibilidadClient;
import edu.uees.testing.domain.EstadoReserva;
import edu.uees.testing.domain.Reserva;
import edu.uees.testing.notification.Notificador;
import edu.uees.testing.repository.ReservaRepository;


/**
 * Laboratorio 1 | Diseño de casos y JUnit 5
 *
 * Cubre las dos reglas de negocio puras del sistema de reservas:
 *  - puedeCancelar(int): frontera de 2 horas de anticipacion.
 *  - calcularTotal(String, double): descuentos por tipo de cliente.
 *
 * Se usa null para los colaboradores del servicio porque ninguno de los
 * dos metodos bajo prueba los consulta. En el Laboratorio 2 esta
 * estrategia sera reemplazada por Stub y Mock.
 */
class ReservaServiceTest {

    private final ReservaService servicio = new ReservaService(null, null, null);

    // ---------------------------------------------------------------
    // Regla de cancelacion: puedeCancelar(int horasAnticipacion)
    // Regla: se permite cancelar con 2 horas de anticipacion o mas.
    // ---------------------------------------------------------------

    @Test
    void cincoHorasPermitenCancelar() {
        // Arrange
        int horas = 5;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertTrue(resultado);
    }

    @Test
    void dosHorasEsElLimitePermitido() {
        // Arrange
        int horas = 2;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertTrue(resultado);
    }

    @Test
    void unaHoraNoPermiteCancelar() {
        // Arrange
        int horas = 1;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void sinAnticipacionNoPermiteCancelar() {
        // Arrange
        int horas = 0;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertFalse(resultado);
    }

    // ---------------------------------------------------------------
    // Regla de descuentos: calcularTotal(String tipo, double totalBase)
    // Regla: VIP 15%, ESTUDIANTE 10%, NORMAL sin descuento.
    // ---------------------------------------------------------------

    @Test
    void normalNoRecibeDescuento() {
        // Arrange
        String tipo = "NORMAL";
        double totalBase = 100;

        // Act
        double total = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(100.0, total, 0.001);
    }

    @Test
    void vipRecibeQuincePorCiento() {
        // Arrange
        String tipo = "VIP";
        double totalBase = 100;

        // Act
        double total = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(85.0, total, 0.001);
    }

    @Test
    void estudianteRecibeDiezPorCiento() {
        // Arrange
        String tipo = "ESTUDIANTE";
        double totalBase = 100;

        // Act
        double total = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(90.0, total, 0.001);
    }

    @Test
    void vipConTotalBaseCeroDevuelveCero() {
        // Arrange
        String tipo = "VIP";
        double totalBase = 0;

        // Act
        double total = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(0.0, total, 0.001);
    }

    @Test
    void totalNegativoEsInvalido() {
        // Arrange
        String tipo = "NORMAL";
        double totalBase = -1;

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.calcularTotal(tipo, totalBase)
        );
        assertEquals("Total base inválido", ex.getMessage());
    }

    @Test
    void reservaDisponibleSeConfirmaGuardaYNotifica() {
        DisponibilidadClient disponibilidad = mock(DisponibilidadClient.class);
        ReservaRepository repository = mock(ReservaRepository.class);
        Notificador notificador = mock(Notificador.class);

        when(disponibilidad.estaDisponible(any())).thenReturn(true);

        ReservaService servicio = new ReservaService(disponibilidad, repository, notificador);
        Reserva reserva = new Reserva("R-001", "NORMAL");

        servicio.confirmar(reserva);

        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
        verify(repository).guardar(reserva);
        verify(notificador).enviarConfirmacion(reserva);
    }

    @Test
    void reservaNoDisponibleNoSeGuardaNiNotifica() {
        DisponibilidadClient disponibilidad = mock(DisponibilidadClient.class);
        ReservaRepository repository = mock(ReservaRepository.class);
        Notificador notificador = mock(Notificador.class);

        when(disponibilidad.estaDisponible(any())).thenReturn(false);

        ReservaService servicio = new ReservaService(disponibilidad, repository, notificador);
        Reserva reserva = new Reserva("R-002", "NORMAL");

        assertThrows(IllegalStateException.class, () -> servicio.confirmar(reserva));

        verify(repository, never()).guardar(any());
        verify(notificador, never()).enviarConfirmacion(any());
    }

    @Test
    void reservaNulaNoConsultaDependencias() {
        DisponibilidadClient disponibilidad = mock(DisponibilidadClient.class);
        ReservaRepository repository = mock(ReservaRepository.class);
        Notificador notificador = mock(Notificador.class);

        ReservaService servicio = new ReservaService(disponibilidad, repository, notificador);

        assertThrows(IllegalArgumentException.class, () -> servicio.confirmar(null));

        verify(disponibilidad, never()).estaDisponible(any());
        verify(repository, never()).guardar(any());
        verify(notificador, never()).enviarConfirmacion(any());
    }
}
