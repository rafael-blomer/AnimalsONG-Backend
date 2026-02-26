package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import jakarta.validation.constraints.NotBlank;

public record OngLoginDTO(@NotBlank String email,
                          @NotBlank String senha) {
}
