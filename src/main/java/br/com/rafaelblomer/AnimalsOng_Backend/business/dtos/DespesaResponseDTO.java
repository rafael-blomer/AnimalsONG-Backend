package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.TipoDespesa;
import org.bson.types.Decimal128;

import java.time.LocalDate;

public record DespesaResponseDTO (String id,
                                  Decimal128 valor,
                                  String descricao,
                                  TipoDespesa tipoDespesa,
                                  LocalDate data){
}
