package itch.tecnm.proyecto.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User; // Para crear el usuario temporal del cliente
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// --- Imports de tus Clientes y DTOs ---
import itch.tecnm.proyecto.client.ClienteClient; // Tu cliente Feign existente
import itch.tecnm.proyecto.client.ClienteClient.ClienteDto; // El DTO interno del cliente
import itch.tecnm.proyecto.dto.auth.AuthResponse;
import itch.tecnm.proyecto.dto.auth.LoginRequest;
import itch.tecnm.proyecto.dto.auth.RegisterRequest;
import itch.tecnm.proyecto.entity.Empleado;
import itch.tecnm.proyecto.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final EmpleadoRepository empleadoRepository; 
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    
    // --- Inyección para conectar con el servicio de Restaurante ---
    private final ClienteClient clienteClient; 

    // ==========================================
    // 1. LOGIN DE EMPLEADOS
    // ==========================================
    public AuthResponse login(LoginRequest request) {
        // Autenticación automática de Spring Security (busca en BD local de empleados)
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        UserDetails userDetails = empleadoRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + request.getUsername()));

        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }
    
    // ==========================================
    // 2. REGISTRO DE EMPLEADOS
    // ==========================================
    public AuthResponse register(RegisterRequest request) {
        if (empleadoRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado.");
        }

        Empleado empleado = new Empleado();
        empleado.setNombre(request.getNombre());
        empleado.setUsername(request.getUsername());
        
        try {
            empleado.setPuesto(Empleado.Puesto.valueOf(request.getPuesto().toUpperCase())); 
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El puesto '" + request.getPuesto() + "' no es válido.");
        }
        
        // Encriptar contraseña (añadirá {noop} o hash según tu configuración)
        empleado.setPassword(passwordEncoder.encode(request.getPassword()));
        
        empleadoRepository.save(empleado);
        
        String token = jwtService.generateToken(empleado);
        return new AuthResponse(token);
    }

    // ==========================================
    // 3. LOGIN DE CLIENTES (Vía Feign)
    // ==========================================
    public AuthResponse loginCliente(LoginRequest request) {
        // A. Buscar el cliente en el microservicio restaurante1 (Puerto 7072)
        ClienteDto cliente = null;
        try {
            // request.getUsername() aquí contiene el CORREO del cliente
            cliente = clienteClient.findByCorreo(request.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con servicio de clientes o credenciales inválidas.");
        }
        
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado.");
        }

        // B. Verificar contraseña manualmente
        // Como el cliente no está en nuestra BD local, Spring no lo hace automático.
        String inputPassword = request.getPassword();
        
        // Asegúrate de que tu DTO ClienteDto tenga el método getPassword() o getPasswordCliente()
        // Si usaste el código anterior, debería ser getPassword()
        String dbPassword = cliente.getPassword(); 

        if (dbPassword == null) {
             throw new RuntimeException("Error de seguridad: El cliente no tiene contraseña registrada.");
        }

        boolean isMatch = false;

        // Lógica para soportar pruebas ({noop}) y producción (BCrypt)
        if (dbPassword.startsWith("{noop}")) {
            String cleanDbPass = dbPassword.replace("{noop}", "");
            isMatch = cleanDbPass.equals(inputPassword);
        } else {
            isMatch = passwordEncoder.matches(inputPassword, dbPassword);
        }

        if (!isMatch) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // C. Generar Token
        // Creamos un usuario temporal en memoria con el rol CLIENTE
        UserDetails userDetails = User.builder()
                .username(cliente.getCorreoCliente()) // Usamos el correo como username en el token
                .password(cliente.getPassword())
                .roles("CLIENTE") 
                .build();

        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }
}