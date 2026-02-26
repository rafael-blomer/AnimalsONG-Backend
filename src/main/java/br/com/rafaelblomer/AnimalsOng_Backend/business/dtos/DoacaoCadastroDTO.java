package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.Decimal128;

public record DoacaoCadastroDTO(@NotNull Decimal128 valor,
                                String descricao,
                                @NotBlank String nomeDoador) {
}
