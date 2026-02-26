package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Especie;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Porte;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnimalCadastroDTO(@NotBlank String nome,
                                @NotBlank String raca,
                                @NotNull Integer idadeAproximada,
                                @NotNull Boolean castrado,
                                @NotNull Especie especie,
                                @NotNull Porte porte,
                                @NotNull Sexo sexo) {
}
