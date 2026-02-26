package br.com.rafaelblomer.AnimalsOng_Backend.business.dtos;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Especie;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Porte;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Sexo;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Status;

import java.time.LocalDate;

public record AnimalResponseDTO(String id,
                                String nome,
                                String raca,
                                Integer idadeAproximada,
                                Boolean castrado,
                                LocalDate dataEntrada,
                                String fotoUrl,
                                Especie especie,
                                Porte porte,
                                Sexo sexo,
                                Status status) {
}
