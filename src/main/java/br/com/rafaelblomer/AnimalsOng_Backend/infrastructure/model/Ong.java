package br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model;

import com.mongodb.lang.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document
public class Ong {

    @Id
    private String id;
    private String nome;
    @Nullable
    private String cnpj;
    private List<String> telefone;
    private String email;
    private String senha;
    private LocalDate dataCriacao;
    private Boolean ativo;
    private String cep;
    private String cidade;
    private String estado;
    private String rua;
    private String bairro;
    @Nullable
    private String complemento;
    private Integer numero;

    public Ong(String bairro, String cep, String cidade,
               @Nullable String cnpj, @Nullable String complemento,
               String email, String estado, String nome, Integer numero, String rua,
               String senha, List<String> telefone) {
        this.bairro = bairro;
        this.cep = cep;
        this.cidade = cidade;
        this.cnpj = cnpj;
        this.complemento = complemento;
        this.dataCriacao = LocalDate.now();
        this.email = email;
        this.estado = estado;
        this.nome = nome;
        this.numero = numero;
        this.rua = rua;
        this.senha = senha;
        this.ativo = false;
        this.telefone = telefone;
    }

    public Ong() {
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Nullable
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(@Nullable String cnpj) {
        this.cnpj = cnpj;
    }

    @Nullable
    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(@Nullable String complemento) {
        this.complemento = complemento;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<String> getTelefone() {
        return telefone;
    }

    public void setTelefone(List<String> telefone) {
        this.telefone = telefone;
    }
}
