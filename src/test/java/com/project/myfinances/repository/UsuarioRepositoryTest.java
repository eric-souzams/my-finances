package com.project.myfinances.repository;

import com.project.myfinances.model.entity.Usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {
    //Testes de integração

    private static String NOME = "Usuario";
    private static String EMAIL = "usuario@email.com";
    private static String SENHA = "1234";

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        //given
        Usuario usuario = criarMockUsuario();

        //when
        entityManager.persist(usuario);
        boolean result = repository.existsByEmail(EMAIL);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComEmail() {
        //when
        boolean result = repository.existsByEmail(EMAIL);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void devePersistirUmUsuarioNoBancoDeDados() {
        //given
        Usuario usuario = criarMockUsuario();

        //when
        Usuario usuarioSalvo = entityManager.persist(usuario);

        //then
        assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    void deveBuscarUmUsuarioPorEmail() {
        //given
        Usuario usuario = criarMockUsuario();

        //when
        entityManager.persist(usuario);
        Optional<Usuario> result = repository.findByEmail(EMAIL);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void deveRetornarVazioAoBuscarUmUsuarioPorEmailNoBancoDeDados() {
        //when
        Optional<Usuario> result = repository.findByEmail(EMAIL);

        //then
        assertThat(result.isPresent()).isFalse();
    }

    public static Usuario criarMockUsuario() {
        return Usuario.builder().nome(NOME).email(EMAIL).senha(SENHA).build();
    }
}
