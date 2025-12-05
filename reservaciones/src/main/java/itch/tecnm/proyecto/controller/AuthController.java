package itch.tecnm.proyecto.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import itch.tecnm.proyecto.dto.auth.AuthResponse;
import itch.tecnm.proyecto.dto.auth.LoginRequest;
import itch.tecnm.proyecto.dto.auth.RegisterRequest;
import itch.tecnm.proyecto.repository.EmpleadoRepository;
import itch.tecnm.proyecto.service.AuthService;
import itch.tecnm.proyecto.service.JwtService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final EmpleadoRepository empleadoRepository; // Necesario para obtener los datos después de autenticar
    private final JwtService jwtService;
    private final AuthService authService;

    /**
     * Endpoint para iniciar sesión. Recibe username/password y devuelve un token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // 1. Autentica las credenciales. Si fallan, Spring lanza una excepción.
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        // 2. Si la autenticación es exitosa, busca el objeto Empleado (UserDetails)
        // La búsqueda aquí es segura porque ya sabemos que el usuario existe.
        UserDetails userDetails = empleadoRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado (post-auth): " + request.getUsername()));

        // 3. Genera la llave (el token JWT)
        String token = jwtService.generateToken(userDetails);

        // 4. Devuelve el token en la respuesta
        return ResponseEntity.ok(new AuthResponse(token));
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Esta línea es correcta, una vez que inyectes el servicio:
        AuthResponse response = authService.register(request); 
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login-cliente")
    public ResponseEntity<AuthResponse> loginCliente(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginCliente(request));
    }
}