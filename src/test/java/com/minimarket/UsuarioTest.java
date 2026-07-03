package com.minimarket;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void testLoginValido() {
        Rol rolAdmin = new Rol("ADMIN");
        Usuario usuarioPrueba = new Usuario();
        usuarioPrueba.setUsername("cajero1");
        usuarioPrueba.setPassword("pass123");
        usuarioPrueba.setRoles(Set.of(rolAdmin));

        when(usuarioRepository.findByUsername("cajero1")).thenReturn(Optional.of(usuarioPrueba));

        UserDetails resultado = customUserDetailsService.loadUserByUsername("cajero1");

        assertNotNull(resultado);
        assertEquals("cajero1", resultado.getUsername());
    }

    @Test
    public void testLoginUsuarioNoExiste() {
        when(usuarioRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("fantasma");
        });
    }

    @Test
    public void testLoginContrasenaIncorrecta() {
        // Codificar la contraseña real del usuario con BCrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String contrasenaCorrecta = encoder.encode("password123");

        // Crear usuario con contraseña codificada en base de datos
        Usuario usuarioPrueba = new Usuario();
        usuarioPrueba.setUsername("cajero1");
        usuarioPrueba.setPassword(contrasenaCorrecta);
        usuarioPrueba.setRoles(Set.of(new Rol("CAJERO")));

        when(usuarioRepository.findByUsername("cajero1")).thenReturn(Optional.of(usuarioPrueba));

        // Configurar DaoAuthenticationProvider, que es el componente que
        // Spring Security usa internamente para verificar credenciales
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(encoder);

        // Intentar autenticar con contraseña incorrecta
        // Verificar que se lanza BadCredentialsException
        UsernamePasswordAuthenticationToken credencialesErroneas =
                new UsernamePasswordAuthenticationToken("cajero1", "contrasenaEquivocada");

        assertThrows(BadCredentialsException.class, () -> {
            provider.authenticate(credencialesErroneas);
        });
    }
}
