package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import jakarta.validation.constraints.NotBlank;

public record OngAlterarSenhaDTO(@NotBlank String token,
                                 @NotBlank String novaSenha) {
}
