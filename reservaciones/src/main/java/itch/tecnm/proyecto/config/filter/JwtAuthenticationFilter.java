package itch.tecnm.proyecto.config.filter;

import itch.tecnm.proyecto.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    // ❌ ELIMINAMOS UserDetailsService para no forzar la búsqueda en BD

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, 
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            // 1. Leemos usuario y rol DIRECTAMENTE del token
            final String username = jwtService.extractUsername(jwt);
            String role = jwtService.extractRole(jwt); 

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // 2. Normalizamos el rol (asegurar prefijo ROLE_)
                if (role == null) role = "ROLE_USER"; // Por si acaso
                if (!role.startsWith("ROLE_")) role = "ROLE_" + role;

                // 3. Creamos la autoridad
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

                // 4. Autenticamos al usuario en memoria (sin ir a la BD de empleados)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(authority)
                );
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Si el token es falso o expiró, no autenticamos y dejamos pasar (Spring dará 403 si es necesario)
            System.err.println("Token inválido: " + e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
}