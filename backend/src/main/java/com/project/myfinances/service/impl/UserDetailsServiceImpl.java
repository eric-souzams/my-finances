package com.project.myfinances.service.impl;

import com.project.myfinances.model.entity.Usuario;
import com.project.myfinances.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor
@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario result = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado"));

        return User.builder()
                .username(result.getEmail())
                .password(result.getSenha())
                .roles("USER")
                .build();
    }

}
