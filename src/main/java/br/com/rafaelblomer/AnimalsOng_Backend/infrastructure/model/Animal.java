package br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Especie;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Porte;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Sexo;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
public class Animal {

    @Id
    private String id;
    private String nome;
    private String raca;
    private Integer idadeAproximada;
    private Boolean castrado;
    private LocalDate dataEntrada;
    private String fotoUrl;
    private Status status;
    private Especie especie;
    private Porte porte;
    private Sexo sexo;
    private String idOng;

    public Animal(Boolean castrado, Especie especie, String fotoUrl, Integer idadeAproximada, String idOng, String nome, Porte porte, String raca, Sexo sexo) {
        this.castrado = castrado;
        this.dataEntrada = LocalDate.now();
        this.especie = especie;
        this.fotoUrl = fotoUrl;
        this.idadeAproximada = idadeAproximada;
        this.idOng = idOng;
        this.nome = nome;
        this.porte = porte;
        this.raca = raca;
        this.sexo = sexo;
        this.status = Status.ATIVO;
    }

    public Animal() {
    }

    public Boolean getCastrado() {
        return castrado;
    }

    public void setCastrado(Boolean castrado) {
        this.castrado = castrado;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdadeAproximada() {
        return idadeAproximada;
    }

    public void setIdadeAproximada(Integer idadeAproximada) {
        this.idadeAproximada = idadeAproximada;
    }

    public String getIdOng() {
        return idOng;
    }

    public void setIdOng(String idOng) {
        this.idOng = idOng;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Porte getPorte() {
        return porte;
    }

    public void setPorte(Porte porte) {
        this.porte = porte;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
