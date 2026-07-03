package com.minimarket;

import com.minimarket.entity.Producto;
import com.minimarket.service.ProductoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductoTest {

    private static final String PRODUCTO_JSON = "{\"nombre\":\"Leche\",\"precio\":1500.0,\"stock\":10}";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        // Se configura el mock del servicio antes de cada test para simular
        // la capa de persistencia sin acceder a la base de datos real.
        // save() retorna un Producto vacío para que el controlador devuelva 200 OK.
        // findById(1L) retorna un Producto existente para que PUT encuentre el recurso.
        Producto producto = new Producto();
        given(productoService.save(any())).willReturn(producto);
        given(productoService.findById(1L)).willReturn(producto);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminPuedeActualizarProducto() throws Exception {
        // Simular usuario autenticado con rol ADMIN
        // Enviar solicitud de actualización al producto con id 1
        // Verificar que el servidor permite la modificación (200 OK)
        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUCTO_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void testClienteNoPuedeActualizarProducto() throws Exception {
        // Simular usuario autenticado con rol CLIENTE
        // Enviar solicitud de actualización al producto con id 1
        // Verificar que el servidor rechaza la operación (403 Forbidden)
        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUCTO_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CAJERO")
    public void testCajeroNoPuedeActualizarProducto() throws Exception {
        // Simular usuario autenticado con rol CAJERO
        // Enviar solicitud de actualización al producto con id 1
        // Verificar que el servidor rechaza la operación (403 Forbidden)
        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUCTO_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testSinAutenticacionNoPuedeActualizarProducto() throws Exception {
        // No se proporciona autenticación
        // Enviar solicitud de actualización al producto con id 1
        // Verificar que el servidor rechaza la operación por falta de credenciales (redirige al login)
        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUCTO_JSON))
                .andExpect(status().is3xxRedirection());
    }

}
