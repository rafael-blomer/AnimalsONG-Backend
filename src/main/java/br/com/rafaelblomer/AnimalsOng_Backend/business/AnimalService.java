package br.com.rafaelblomer.AnimalsOng_Backend.business;

import br.com.rafaelblomer.AnimalsOng_Backend.business.converters.AnimalConverter;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.AnimalAtualizacaoDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.AnimalCadastroDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.AnimalResponseDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.MensagemResponseDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions.AcaoNaoPermitidaException;
import br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions.ObjetoNaoEncontradoException;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Animal;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Especie;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Porte;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Sexo;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Status;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories.AnimalRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalConverter animalConverter;

    @Autowired
    private OngService ongService;

    @Autowired
    private CloudinaryService cloudinaryService;

    public AnimalResponseDTO create (String token, AnimalCadastroDTO animalCadastroDTO, MultipartFile foto) {
        Ong ong = buscarOngPorToken(token);
        String fotoUrl = cloudinaryService.uploadImagem(foto);
        Animal animal = animalConverter.dtoCadastroParaEntity(animalCadastroDTO, fotoUrl, ong.getId());
        animalRepository.save(animal);
        return animalConverter.entityParaResponseDto(animal);
    }

    public Page<AnimalResponseDTO> findAll(String token, Pageable pageable) {
        Ong ong = buscarOngPorToken(token);
        return animalRepository
                .findByIdOngAndStatusNot(ong.getId(), Status.DESATIVADO, pageable)
                .map(animalConverter::entityParaResponseDto);
    }

    public void delete(String token, String idAnimal) {
        Ong ong = buscarOngPorToken(token);
        Animal animal = buscarAnimalPorId(idAnimal);
        verificarAnimalOng(ong, animal);
        animal.setStatus(Status.DESATIVADO);
        animalRepository.save(animal);
    }

    public AnimalResponseDTO updateData(String token, String idAnimal,
                                          AnimalAtualizacaoDTO atualizacaoDTO, MultipartFile novaFoto) {
        Ong ong = buscarOngPorToken(token);
        Animal animal = buscarAnimalPorId(idAnimal);
        verificarAnimalOng(ong, animal);
        atualizarDados(animal, atualizacaoDTO, novaFoto);
        animalRepository.save(animal);
        return animalConverter.entityParaResponseDto(animal);
    }

    public AnimalResponseDTO findById(String token, String id) {
        Ong ong = buscarOngPorToken(token);
        Animal animal = buscarAnimalPorId(id);
        verificarAnimalOng(ong, animal);
        return animalConverter.entityParaResponseDto(animal);
    }

    //MÉTODOS ÚTEIS


    private Ong buscarOngPorToken(String token) {
        return ongService.buscarOngEntityPorToken(token);
    }

    private Animal buscarAnimalPorId(String id) {
        return animalRepository.findById(id).orElseThrow(
                () -> new ObjetoNaoEncontradoException("Animal com id " + id + "não encontrado"));
    }

    private void verificarAnimalOng(Ong ong, Animal animal) {
        if (!ong.getId().equals(animal.getIdOng()))
            throw new AcaoNaoPermitidaException("Você não tem permissão para realizar essa ação.");
    }

    private void atualizarDados(Animal animal, AnimalAtualizacaoDTO atualizacaoDTO, MultipartFile novaFoto) {
        animal.setCastrado(atualizacaoDTO.castrado());
        animal.setPorte(atualizacaoDTO.porte());
        animal.setStatus(atualizacaoDTO.status());
        animal.setIdadeAproximada(atualizacaoDTO.idadeAproximada());
        if (novaFoto != null && !novaFoto.isEmpty()) {
            if (animal.getFotoUrl() != null)
                cloudinaryService.deletarPorUrl(animal.getFotoUrl());
            String novaUrl = cloudinaryService.uploadImagem(novaFoto);
            animal.setFotoUrl(novaUrl);
        }
    }
}
