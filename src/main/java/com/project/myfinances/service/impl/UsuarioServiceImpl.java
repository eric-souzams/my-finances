package com.project.myfinances.service.impl;

import com.project.myfinances.exceptions.ErroAutenticacaoException;
import com.project.myfinances.exceptions.RegraNegocioException;
import com.project.myfinances.model.entity.Usuario;
import com.project.myfinances.repository.UsuarioRepository;
import com.project.myfinances.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (usuario.isEmpty()) {
            throw new ErroAutenticacaoException("Usuário não encontrado.");
        }

        if (!usuario.get().getSenha().equals(senha)) {
            throw new ErroAutenticacaoException("Email ou senha inválida.");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(@RequestBody Usuario usuario) {
        validarEmail(usuario.getEmail());
        usuario.setDataCadastro(LocalDateTime.now());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }
    }
}
