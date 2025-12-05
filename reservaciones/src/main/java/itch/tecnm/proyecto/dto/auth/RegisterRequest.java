package itch.tecnm.proyecto.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    // Campos que el frontend enviará al registrar un nuevo empleado
    
    private String nombre;
    private String username;
    private String password;
    
    // El puesto (rol) se recibe como String
    private String puesto; 
}