package br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model;

import com.mongodb.lang.Nullable;
import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
public class Doacao {

    @Id
    private String id;
    @Nullable
    private Decimal128 valor;
    @Nullable
    private String descricao;
    private String nomeDoador;
    private String idOng;
    private LocalDate data;

    public Doacao(@Nullable String descricao, String idOng, String nomeDoador, @Nullable Decimal128 valor) {
        this.descricao = descricao;
        this.idOng = idOng;
        this.nomeDoador = nomeDoador;
        this.valor = valor;
        this.data = LocalDate.now();
    }

    public Doacao() {

    }

    @Nullable
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(@Nullable String descricao) {
        this.descricao = descricao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOng() {
        return idOng;
    }

    public void setIdOng(String idOng) {
        this.idOng = idOng;
    }

    public String getNomeDoador() {
        return nomeDoador;
    }

    public void setNomeDoador(String nomeDoador) {
        this.nomeDoador = nomeDoador;
    }

    @Nullable
    public Decimal128 getValor() {
        return valor;
    }

    public void setValor(@Nullable Decimal128 valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
}
