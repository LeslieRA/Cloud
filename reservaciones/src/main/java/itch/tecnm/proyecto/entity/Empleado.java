package itch.tecnm.proyecto.entity;

import java.util.Collection;
import java.util.List;

// --- 👇 IMPORTS DE SPRING SECURITY FALTANTES 👇 ---
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="empleado")
public class Empleado implements UserDetails { // Implementación
	
	// Enum anidado (un solo archivo)
	public enum Puesto { 
    	CAJERO,
        COCINERO,
        MESERO,
        ADMINISTRADOR,
        SUPERVISOR 
    }
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idempleado")
    private Integer idEmpleado;

    @Column(name = "nombre")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "puesto", nullable = false)
    private Puesto puesto; 
    
 	// --- CAMPOS DE SEGURIDAD ---
  	@Column(name="username", nullable = false, unique = true)
    private String username;
  	
  	@Column(name="password", nullable = false)
    private String password;
     
    // --- MÉTODOS REQUERIDOS POR USERDETAILS ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Asigna el rol con el prefijo ROLE_ (Ej. ROLE_ADMINISTRADOR)
        return List.of(new SimpleGrantedAuthority("ROLE_" + puesto.name()));
    }

    @Override
    public String getPassword() {
        return this.password; 
    }
    
    @Override
    public String getUsername() {
        return this.username; 
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
