package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.TipoDespesa;
import jakarta.validation.constraints.NotNull;
import org.bson.types.Decimal128;

public record DespesaCadastroDTO(@NotNull Decimal128 valor,
                                 String descricao,
                                 @NotNull TipoDespesa tipoDespesa) {
}
