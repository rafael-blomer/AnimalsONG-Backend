package br.com.rafaelblomer.AnimalsOng_Backend.business;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.VerificacaoEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${frontend.url}")
    private String frontendUrl;

    public void enviarVerificacaoEmail(VerificacaoEmail verificacaoEmail, Ong ong) {
        String recipientAddress = ong.getEmail();
        String subject = "Verificação de Cadastro";
        String confirmationUrl = frontendUrl + "/confirmar-email?token=" + verificacaoEmail.getToken();
        String message = "Olá " + ong.getNome() + ",\n\n"
                + "Obrigado por se cadastrar no AnimalsOng. Por favor, clique no link abaixo para ativar sua conta:\n\n"
                + confirmationUrl + "\n\n"
                + "Este link irá expirar em 5 horas.\n\n"
                + "Atenciosamente,\nEquipe AnimalsOng.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

    public void enviarEmailTrocaSenha(Ong usuario, String token) {
        String recipientAddress = usuario.getEmail();
        String subject = "Alteração de Senha";
        String confirmationUrl = frontendUrl + "/resetar-senha?token=" + token;
        String message = "Olá " + usuario.getNome() + ",\n\n"
                + "Houve um pedido para alterar sua senha. Se o pedido não foi feito por você, apenas ignore o email.\n\n"
                + "Para alterar sua senha, clique no link abaixo:\n\n"
                + confirmationUrl + "\n\n"
                + "Este link irá expirar em 5 horas.\n\n"
                + "Atenciosamente,\nEquipe AnimalsOng.";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
