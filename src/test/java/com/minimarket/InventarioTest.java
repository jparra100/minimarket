package com.minimarket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.oneOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InventarioTest {

    private static final String INVENTARIO_JSON = "{\"cantidad\":10,\"tipoMovimiento\":\"Entrada\",\"fechaMovimiento\":\"2026-07-02\"}";

    @Autowired
    private MockMvc mockMvc;

    // --- POST /api/inventario --- seguridad ya implementada

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminPuedeRegistrarMovimiento() throws Exception {
        // Simular usuario autenticado con rol ADMIN
        // Enviar solicitud de registro de un movimiento de inventario
        // Verificar que el servidor permite la operación (200 OK o 400 si datos incompletos)
        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(INVENTARIO_JSON))
                .andExpect(status().is(oneOf(200, 400)));
    }

    @Test
    @WithMockUser(roles = "CAJERO")
    public void testCajeroPuedeRegistrarMovimiento() throws Exception {
        // Simular usuario autenticado con rol CAJERO
        // Enviar solicitud de registro de un movimiento de inventario
        // Verificar que el servidor permite la operación (200 OK o 400 si datos incompletos)
        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(INVENTARIO_JSON))
                .andExpect(status().is(oneOf(200, 400)));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void testClienteNoPuedeRegistrarMovimiento() throws Exception {
        // Simular usuario autenticado con rol CLIENTE
        // Enviar solicitud de registro de un movimiento de inventario
        // Verificar que el servidor rechaza la operación (403 Forbidden)
        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(INVENTARIO_JSON))
                .andExpect(status().isForbidden());
    }

}
