package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import com.mongodb.lang.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OngCadastroDTO(
        @NotBlank String nome,
        @Nullable String cnpj,
        @NotNull List<String> telefone,
        @NotBlank @Email String email,
        @NotBlank String senha,
        @NotBlank String cep,
        @NotBlank String cidade,
        @NotBlank String estado,
        @NotBlank String rua,
        @NotBlank String bairro,
        @Nullable String complemento,
        @NotNull Integer numero ){
}
