package itch.tecnm.proyecto;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import itch.tecnm.proyecto.config.filter.JwtAuthenticationFilter;
import itch.tecnm.proyecto.repository.EmpleadoRepository;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootApplication
@Configuration
@EnableWebSecurity
@EnableFeignClients
public class ReservacionesApplication {
	
	private final EmpleadoRepository empleadoRepository;

    public ReservacionesApplication(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

	public static void main(String[] args) {
		SpringApplication.run(ReservacionesApplication.class, args);
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, 
            JwtAuthenticationFilter jwtAuthFilter, 
            AuthenticationProvider authenticationProvider
    ) throws Exception {
        
        http
            .csrf(csrf -> csrf.disable())
            .cors(withDefaults())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                
                // ==========================================
                // 1. REGLAS PÚBLICAS (ACCESO TOTAL)
                // ==========================================
                .requestMatchers("/api/auth/**").permitAll() 
                .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                
                // RESERVAS (Crear)
                .requestMatchers(HttpMethod.POST, "/api/reserva").permitAll()

                // MESAS (Ver disponibilidad y lista)
                // 👇 ESTO ES LO QUE ARREGLA EL ERROR 👇
                // Permitimos CUALQUIER lectura (GET) de mesas
                .requestMatchers(HttpMethod.GET, "/api/mesa/**").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/mesa").permitAll() 

                // ==========================================
                // 2. REGLAS PROTEGIDAS (SOLO LOGUEADOS)
                // ==========================================

                // EMPLEADOS
                .requestMatchers(HttpMethod.GET, "/api/empleado/**").hasAnyRole("ADMINISTRADOR", "SUPERVISOR", "CAJERO", "MESERO", "CLIENTE")
                .requestMatchers(HttpMethod.POST, "/api/empleado").hasAnyRole("ADMINISTRADOR", "SUPERVISOR")
                .requestMatchers(HttpMethod.PUT, "/api/empleado/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/empleado/**").hasRole("ADMINISTRADOR")

                // RESERVAS (Gestión)
                // Nota: El POST es público arriba.
                .requestMatchers(HttpMethod.GET, "/api/reserva/**").hasAnyRole("ADMINISTRADOR", "CAJERO", "MESERO", "CLIENTE")
                .requestMatchers(HttpMethod.PUT, "/api/reserva/**").hasAnyRole("ADMINISTRADOR", "CAJERO")
                .requestMatchers(HttpMethod.DELETE, "/api/reserva/**").hasAnyRole("ADMINISTRADOR", "CAJERO")

                // MESAS (Gestión - SOLO ESCRITURA)
                // ⚠️ IMPORTANTE: ELIMINÉ EL "GET" DE AQUÍ PORQUE YA ES PÚBLICO ARRIBA
                .requestMatchers(HttpMethod.PATCH, "/api/mesa/**").hasAnyRole("ADMINISTRADOR", "CAJERO", "MESERO")
                .requestMatchers(HttpMethod.POST, "/api/mesa").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/mesa/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/mesa/**").hasRole("ADMINISTRADOR")

                // ATENDER
                .requestMatchers("/api/atender/**").authenticated()

                .anyRequest().authenticated() 
            )
            .exceptionHandling(exceptions -> 
                exceptions.authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
                })
            )
            .authenticationProvider(authenticationProvider) 
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

    // ... (El resto de beans se quedan igual) ...
    @Bean
    public PasswordEncoder passwordEncoder() {
    	String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> empleadoRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}