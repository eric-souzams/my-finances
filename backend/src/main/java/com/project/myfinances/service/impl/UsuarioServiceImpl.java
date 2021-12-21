package com.project.myfinances.service.impl;

import com.project.myfinances.exceptions.ErroAutenticacaoException;
import com.project.myfinances.exceptions.RegraNegocioException;
import com.project.myfinances.model.entity.Usuario;
import com.project.myfinances.repository.UsuarioRepository;
import com.project.myfinances.service.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;
    private PasswordEncoder encoder;

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (usuario.isEmpty()) {
            log.info("Not found user '{}' into the database", email);
            throw new ErroAutenticacaoException("Usuário não encontrado.");
        }

        boolean isMatcher = encoder.matches(senha, usuario.get().getSenha());

        if (!isMatcher) {
            log.info("Password from '{}' doesn't matcher with saved password", email);
            throw new ErroAutenticacaoException("Email ou senha inválida.");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        usuario.setDataCadastro(LocalDateTime.now());

        String encryptedPassword = encoder.encode(usuario.getSenha());
        usuario.setSenha(encryptedPassword);

        log.info("Saving new user '{}' to the database", usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }
}
