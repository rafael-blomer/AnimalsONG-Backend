package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Porte;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Status;

public record AnimalAtualizacaoDTO(Boolean castrado,
                                   Status status,
                                   Porte porte,
                                   Integer idadeAproximada) {
}
