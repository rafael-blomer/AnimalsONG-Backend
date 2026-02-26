package br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.TipoDespesa;
import com.mongodb.lang.Nullable;
import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
public class Despesa {

    @Id
    private String id;
    private LocalDate data;
    private Decimal128 valor;
    @Nullable
    private String descricao;
    private TipoDespesa tipoDespesa;
    private String idOng;

    public Despesa(@Nullable String descricao, String idOng, TipoDespesa tipoDespesa, Decimal128 valor) {
        this.data = LocalDate.now();
        this.descricao = descricao;
        this.idOng = idOng;
        this.tipoDespesa = tipoDespesa;
        this.valor = valor;
    }

    public Despesa() {
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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

    public TipoDespesa getTipoDespesa() {
        return tipoDespesa;
    }

    public void setTipoDespesa(TipoDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public Decimal128 getValor() {
        return valor;
    }

    public void setValor(Decimal128 valor) {
        this.valor = valor;
    }
}
