package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import org.bson.types.Decimal128;

import java.time.LocalDate;

public record DoacaoResponseDTO (String id,
                                 Decimal128 valor,
                                 String descricao,
                                 String nomeDoador,
                                 LocalDate data){
}
