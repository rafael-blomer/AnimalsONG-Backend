package br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class VerificacaoEmail {

    @Id
    private String id;
    private String token;
    private String ongId;
    private LocalDateTime expiracao;

    public VerificacaoEmail(String token, String ongId) {
        this.token = token;
        this.ongId = ongId;
        this.expiracao = LocalDateTime.now().plusHours(5);
    }

    public VerificacaoEmail() {
    }

    public LocalDateTime getExpiracao() {
        return expiracao;
    }

    public void setExpiracao(LocalDateTime expiracao) {
        this.expiracao = expiracao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOngId() {
        return ongId;
    }

    public void setOngId(String ongId) {
        this.ongId = ongId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
