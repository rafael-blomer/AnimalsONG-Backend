package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import java.time.LocalDate;
import java.util.List;

public record OngResponseDTO (String nome,
                              String cnpj,
                              List<String> telefone,
                              String email,
                              String cep,
                              String cidade,
                              String estado,
                              String rua,
                              String bairro,
                              String complemento,
                              Integer numero,
                              LocalDate dataCriacao){

}
