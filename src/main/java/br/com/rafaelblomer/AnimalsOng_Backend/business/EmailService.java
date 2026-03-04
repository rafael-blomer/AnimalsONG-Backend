package br.com.rafaelblomer.AnimalsOng_Backend.business;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.VerificacaoEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${email.from}")
    private String mailFrom;

    private final RestTemplate restTemplate = new RestTemplate();

    private void enviarEmail(String destinatario, String assunto, String mensagemTexto) {

        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "name", "Equipe AnimalsOng",
                        "email", mailFrom
                ),
                "to", new Object[]{
                        Map.of("email", destinatario)
                },
                "subject", assunto,
                "textContent", mensagemTexto
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao enviar email: " + response.getBody());
        }
    }

    public void enviarVerificacaoEmail(VerificacaoEmail verificacaoEmail, Ong ong) {

        String recipientAddress = ong.getEmail();
        String subject = "Verificação de Cadastro";
        String confirmationUrl = frontendUrl + "/auth/confirmar-email?token=" + verificacaoEmail.getToken();

        String message = "Olá " + ong.getNome() + ",\n\n"
                + "Obrigado por se cadastrar no AnimalsOng. Por favor, clique no link abaixo para ativar sua conta:\n\n"
                + confirmationUrl + "\n\n"
                + "Este link irá expirar em 5 horas.\n\n"
                + "Atenciosamente,\nEquipe AnimalsOng.";

        enviarEmail(recipientAddress, subject, message);
    }

    public void enviarEmailTrocaSenha(Ong usuario, String token) {

        String recipientAddress = usuario.getEmail();
        String subject = "Alteração de Senha";
        String confirmationUrl = frontendUrl + "/auth/resetar-senha?token=" + token;

        String message = "Olá " + usuario.getNome() + ",\n\n"
                + "Houve um pedido para alterar sua senha. Se o pedido não foi feito por você, apenas ignore o email.\n\n"
                + "Para alterar sua senha, clique no link abaixo:\n\n"
                + confirmationUrl + "\n\n"
                + "Este link irá expirar em 5 horas.\n\n"
                + "Atenciosamente,\nEquipe AnimalsOng.";

        enviarEmail(recipientAddress, subject, message);
    }
}