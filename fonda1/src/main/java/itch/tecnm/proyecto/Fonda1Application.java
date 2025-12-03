package itch.tecnm.proyecto;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import itch.tecnm.proyecto.config.filter.JwtAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableFeignClients
@SpringBootApplication
@Configuration
@EnableWebSecurity
public class Fonda1Application {

	public static void main(String[] args) {
		SpringApplication.run(Fonda1Application.class, args);
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                
                // --- 1. REGLAS PÚBLICAS ---
                .requestMatchers(HttpMethod.GET, "/api/producto").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/producto/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tipoProducto").permitAll() 
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers("/api/files/**").permitAll()
                

                // --- 2. GESTIÓN DE PRODUCTOS (ADMIN/SUPERVISOR) ---
                .requestMatchers(HttpMethod.GET, "/api/producto/todos").hasAnyRole("ADMINISTRADOR", "SUPERVISOR")
                .requestMatchers(HttpMethod.POST, "/api/producto").hasAnyRole("ADMINISTRADOR", "SUPERVISOR")
                .requestMatchers(HttpMethod.POST, "/api/files/upload").hasAnyRole("ADMINISTRADOR", "SUPERVISOR")
                
                .requestMatchers(HttpMethod.PUT, "/api/producto/**").hasAnyRole("ADMINISTRADOR","SUPERVISOR")
                .requestMatchers(HttpMethod.DELETE, "/api/producto/**").hasAnyRole("ADMINISTRADOR", "SUPERVISOR")
                .requestMatchers("/api/tipoProducto/**").hasAnyRole("ADMINISTRADOR", "SUPERVISOR")
                .requestMatchers(HttpMethod.PUT, "/api/tipoProducto/**").hasAnyRole("ADMINISTRADOR", "SUPERVISOR")
                .requestMatchers(HttpMethod.DELETE, "/api/tipoProducto/**").hasAnyRole("ADMINISTRADOR", "SUPERVISOR")

                // --- 3. REGLAS DE VENTAS (CORREGIDAS) ---
                
                // Realizar Venta (POST): Cajero, Admin y Mesero
                .requestMatchers(HttpMethod.POST, "/api/venta/**").hasAnyRole("ADMINISTRADOR", "CAJERO","MESERO")

                // Ver Historial y Tickets (GET): Cajero, Admin y Mesero
                // (El Mesero necesita GET si va a imprimir el ticket después de la venta)
                .requestMatchers(HttpMethod.GET, "/api/venta/**").hasAnyRole("ADMINISTRADOR", "CAJERO", "MESERO")
                
                // Editar/Eliminar Venta: SOLO Admin
                .requestMatchers(HttpMethod.PUT, "/api/venta/**").hasAnyRole("ADMINISTRADOR", "CAJERO", "MESERO")
                .requestMatchers(HttpMethod.DELETE, "/api/venta/**").hasRole("ADMINISTRADOR")

                // --- 4. BLOQUEO FINAL ---
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); 
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}