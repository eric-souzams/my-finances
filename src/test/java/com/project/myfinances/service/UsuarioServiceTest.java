package com.project.myfinances.service;

import com.project.myfinances.exceptions.ErroAutenticacaoException;
import com.project.myfinances.exceptions.RegraNegocioException;
import com.project.myfinances.model.entity.Usuario;
import com.project.myfinances.repository.UsuarioRepository;

import com.project.myfinances.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    private static final Long ID = 1L;
    private static final String NOME = "Usuario";
    private static final String EMAIL = "usuario@email.com";
    private static final String SENHA = "1234";

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository repository;

    @Test
    void deveSalvarUsuario() {
        //given
        Usuario usuario = Usuario.builder().id(ID).nome(NOME).email(EMAIL).senha(SENHA).build();

        //when
        doNothing().when(service).validarEmail(anyString());
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        //then
        Usuario result = assertDoesNotThrow(() -> service.salvarUsuario(new Usuario()));
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ID);
        assertThat(result.getNome()).isEqualTo(NOME);
        assertThat(result.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
        //given
        Usuario usuario = Usuario.builder().nome(NOME).email(EMAIL).senha(SENHA).build();

        //when
        doThrow(RegraNegocioException.class).when(service).validarEmail(EMAIL);

        //then
        assertThrows(RegraNegocioException.class, () -> service.salvarUsuario(usuario));
        verify(repository, never()).save(usuario);
    }

    @Test
    void deveAutenticarUmUsuarioComSucesso() {
        //given
        Usuario usuario = Usuario.builder().id(ID).nome(NOME).email(EMAIL).senha(SENHA).build();

        //when
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));

        //then
        Usuario result = assertDoesNotThrow(() -> service.autenticar(EMAIL, SENHA));
        assertThat(result).isNotNull();
    }

    @Test
    void deveLancarErroExceptionQuandoNaoEncontrarUsuarioComEmailInformado() {
        //when
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

         //then
        Throwable exception = catchThrowable(() -> service.autenticar(EMAIL, SENHA));
        assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Usuário não encontrado.");
    }

    @Test
    void deveLancarErroExceptionQuandoSenhasNaoForemIguais() {
        //given
        Usuario usuario = Usuario.builder().id(ID).nome(NOME).email(EMAIL).senha(SENHA).build();

        //when
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        //then
        Throwable exception = catchThrowable(() -> service.autenticar(EMAIL, "123"));
        assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Email ou senha inválida.");
    }

    @Test
    void deveValidarEmail() {
        //when
        when(repository.existsByEmail(anyString())).thenReturn(false);

        //then
        assertDoesNotThrow(() -> service.validarEmail(EMAIL));
    }

    @Test
    void deveLancarErroExceptionAoValidarEmailQuandoExistirEmailCadastrado() {
        //when
        when(repository.existsByEmail(anyString())).thenReturn(true);

        //then
        assertThrows(RegraNegocioException.class, () -> service.validarEmail(EMAIL));
    }
}
