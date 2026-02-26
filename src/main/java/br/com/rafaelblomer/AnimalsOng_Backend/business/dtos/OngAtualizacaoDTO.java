package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import java.util.List;

public record OngAtualizacaoDTO(String nome,
                                String cnpj,
                                List<String> telefone,
                                String cep,
                                String cidade,
                                String estado,
                                String rua,
                                String bairro,
                                String complemento,
                                Integer numero) {
}
