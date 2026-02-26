package br.com.rafaelblomer.AnimalsOng_Backend;

import br.com.rafaelblomer.AnimalsOng_Backend.business.EmailService;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.VerificacaoEmail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTests {


    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Test
    void deveEnviarEmailDeVerificacao() {
        Ong ong = new Ong();
        ong.setEmail("teste@ong.com");
        ong.setNome("ONG Teste");

        VerificacaoEmail verificacaoEmail = new VerificacaoEmail();
        verificacaoEmail.setToken("token123");

        emailService.enviarVerificacaoEmail(verificacaoEmail, ong);

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage emailEnviado = captor.getValue();

        assertEquals("teste@ong.com", emailEnviado.getTo()[0]);
        assertEquals("Verificação de Cadastro", emailEnviado.getSubject());
        assertTrue(emailEnviado.getText().contains("token123"));
        assertTrue(emailEnviado.getText().contains("ONG Teste"));
        assertTrue(emailEnviado.getText().contains("confirmacao-email.html"));
    }

    @Test
    void deveEnviarEmailDeTrocaDeSenha() {
        Ong ong = new Ong();
        ong.setEmail("usuario@ong.com");
        ong.setNome("ONG Segurança");

        String token = "token-reset-456";

        emailService.enviarEmailTrocaSenha(ong, token);

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage emailEnviado = captor.getValue();

        assertEquals("usuario@ong.com", emailEnviado.getTo()[0]);
        assertEquals("Alteração de Senha", emailEnviado.getSubject());
        assertTrue(emailEnviado.getText().contains(token));
        assertTrue(emailEnviado.getText().contains("reset-senha.html"));
        assertTrue(emailEnviado.getText().contains("ONG Segurança"));
    }

}
