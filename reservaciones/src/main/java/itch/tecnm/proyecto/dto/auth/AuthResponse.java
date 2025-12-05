package itch.tecnm.proyecto.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    // El token (la "llave") que React debe guardar
    private String token;
}