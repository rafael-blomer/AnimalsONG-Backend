package br.com.rafaelblomer.AnimalsOng_Backend.business.converters;

import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.AnimalCadastroDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.AnimalResponseDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Animal;
import org.springframework.stereotype.Component;

@Component
public class AnimalConverter {

    public Animal dtoCadastroParaEntity(AnimalCadastroDTO animalCadastroDTO, String fotoUrl, String ongId) {
        return new Animal(animalCadastroDTO.castrado(), animalCadastroDTO.especie(), fotoUrl,
                animalCadastroDTO.idadeAproximada(), ongId, animalCadastroDTO.nome(), animalCadastroDTO.porte(),
                animalCadastroDTO.raca(), animalCadastroDTO.sexo());
    }

    public AnimalResponseDTO entityParaResponseDto(Animal animal) {
        return new AnimalResponseDTO(animal.getId(), animal.getNome(), animal.getRaca(),
                animal.getIdadeAproximada(), animal.getCastrado(), animal.getDataEntrada(),
                animal.getFotoUrl(), animal.getEspecie(), animal.getPorte(), animal.getSexo(), animal.getStatus());
    }
}
