package edu.uees.testing.service;

import edu.uees.testing.availability.DisponibilidadClient;
import edu.uees.testing.domain.EstadoReserva;
import edu.uees.testing.domain.Reserva;
import edu.uees.testing.notification.Notificador;
import edu.uees.testing.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservaServiceTest {

    private DisponibilidadClient disponibilidad;
    private ReservaRepository repository;
    private Notificador notificador;
    private ReservaService servicio;

    @BeforeEach
    void setUp() {
        disponibilidad = mock(DisponibilidadClient.class);
        repository = mock(ReservaRepository.class);
        notificador = mock(Notificador.class);
        
        // Orden correcto del constructor: (disponibilidad, repository, notificador)
        servicio = new ReservaService(disponibilidad, repository, notificador);
    }

    // ---------------------------------------------------------------
    // Regla de cancelación: puedeCancelar(int horasAnticipacion)
    // ---------------------------------------------------------------

    @Test
    @DisplayName("CP-01: Cancelar con 5 horas de anticipación")
    void cincoHorasPermitenCancelar() {
        assertTrue(servicio.puedeCancelar(5));
    }

    @Test
    @DisplayName("CP-02: Límite exacto de 2 horas de anticipación")
    void dosHorasEsElLimitePermitido() {
        assertTrue(servicio.puedeCancelar(2));
    }

    @Test
    @DisplayName("CP-03: 1 hora no permite cancelar")
    void unaHoraNoPermiteCancelar() {
        assertFalse(servicio.puedeCancelar(1));
    }

    @Test
    @DisplayName("CP-04: Sin anticipación (0 horas) no permite cancelar")
    void sinAnticipacionNoPermiteCancelar() {
        assertFalse(servicio.puedeCancelar(0));
    }

    // ---------------------------------------------------------------
    // Regla de descuentos: calcularTotal(String tipo, double totalBase)
    // ---------------------------------------------------------------

    @Test
    @DisplayName("CP-05: Cliente NORMAL no recibe descuento")
    void normalNoRecibeDescuento() {
        assertEquals(100.0, servicio.calcularTotal("NORMAL", 100.0), 0.001);
    }

    @Test
    @DisplayName("CP-06: Cliente VIP recibe 15% de descuento")
    void vipRecibeQuincePorCiento() {
        assertEquals(85.0, servicio.calcularTotal("VIP", 100.0), 0.001);
    }

    @Test
    @DisplayName("CP-07: Cliente ESTUDIANTE recibe 10% de descuento")
    void estudianteRecibeDiezPorCiento() {
        assertEquals(90.0, servicio.calcularTotal("ESTUDIANTE", 100.0), 0.001);
    }

    @Test
    @DisplayName("CP-08: Cliente VIP con total base cero devuelve cero")
    void vipConTotalBaseCeroDevuelveCero() {
        assertEquals(0.0, servicio.calcularTotal("VIP", 0.0), 0.001);
    }

    @Test
    @DisplayName("CP-08b: Total base negativo lanza IllegalArgumentException")
    void totalNegativoEsInvalido() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.calcularTotal("NORMAL", -1.0)
        );
        assertEquals("Total base inválido", ex.getMessage());
    }

    // ---------------------------------------------------------------
    // Confirmación y Dobles de Prueba (Stub / Mock)
    // ---------------------------------------------------------------

    @Test
    @DisplayName("CP-09: Reserva disponible se confirma, guarda y notifica")
    void reservaDisponibleSeConfirmaGuardaYNotifica() {
        when(disponibilidad.estaDisponible(any())).thenReturn(true);
        Reserva reserva = new Reserva("R-001", "NORMAL");

        servicio.confirmar(reserva);

        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
        verify(repository).guardar(reserva);
        verify(notificador).enviarConfirmacion(reserva);
    }

    @Test
    @DisplayName("CP-10: Reserva no disponible lanza excepción y no se guarda ni notifica")
    void reservaNoDisponibleNoSeGuardaNiNotifica() {
        when(disponibilidad.estaDisponible(any())).thenReturn(false);
        Reserva reserva = new Reserva("R-002", "NORMAL");

        assertThrows(IllegalStateException.class, () -> servicio.confirmar(reserva));

        verify(repository, never()).guardar(any());
        verify(notificador, never()).enviarConfirmacion(any());
    }

    @Test
    @DisplayName("CP-11: Reserva nula lanza excepción y no consulta dependencias")
    void reservaNulaNoConsultaDependencias() {
        assertThrows(IllegalArgumentException.class, () -> servicio.confirmar(null));

        verify(disponibilidad, never()).estaDisponible(any());
        verify(repository, never()).guardar(any());
        verify(notificador, never()).enviarConfirmacion(any());
    }
}