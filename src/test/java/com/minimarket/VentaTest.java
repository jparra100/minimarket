package com.minimarket;

import com.minimarket.entity.Venta;
import com.minimarket.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VentaTest {

    private static final String VENTA_JSON = "{\"fecha\":\"2026-07-02\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VentaService ventaService;

    @BeforeEach
    void setUp() {
        // Se configura el mock del servicio antes de cada test para simular
        // la capa de persistencia sin acceder a la base de datos real.
        // save() retorna una Venta vacía para que el controlador devuelva 200 OK.
        // findById(1L) retorna una Venta existente para que GET /{id} encuentre el recurso.
        Venta venta = new Venta();
        given(ventaService.save(any())).willReturn(venta);
    }

    // --- POST /api/ventas --- seguridad ya implementada

    @Test
    @WithMockUser(roles = "CAJERO")
    public void testCajeroPuedeGenerarVenta() throws Exception {
        // Simular usuario autenticado con rol CAJERO
        // Enviar solicitud de creación de una nueva venta
        // Verificar que el servidor permite la operación (200 OK)
        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VENTA_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void testClienteNoPuedeGenerarVenta() throws Exception {
        // Simular usuario autenticado con rol CLIENTE
        // Enviar solicitud de creación de una nueva venta
        // Verificar que el servidor rechaza la operación (403 Forbidden)
        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VENTA_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminNoPuedeGenerarVenta() throws Exception {
        // Simular usuario autenticado con rol ADMIN
        // Enviar solicitud de creación de una nueva venta
        // Verificar que el servidor rechaza la operación (403 Forbidden)
        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VENTA_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testSinAutenticacionNoPuedeGenerarVenta() throws Exception {
        // No se proporciona autenticación
        // Enviar solicitud de creación de una nueva venta
        // Verificar que el servidor rechaza la operación por falta de credenciales (redirige al login)
        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VENTA_JSON))
                .andExpect(status().is3xxRedirection());
    }

}
