package br.com.rafaelblomer.AnimalsOng_Backend.controllers;

import br.com.rafaelblomer.AnimalsOng_Backend.business.AnimalService;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.AnimalAtualizacaoDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.AnimalCadastroDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.AnimalResponseDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.MensagemResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @PostMapping(value = "/cadastro", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnimalResponseDTO> cadastrarAnimal(@RequestHeader("Authorization") String token,
                                                             @RequestPart("animal") @Valid AnimalCadastroDTO animalCadastroDTO,
                                                             @RequestPart("foto") MultipartFile foto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(animalService.create(token, animalCadastroDTO, foto));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<AnimalResponseDTO> buscarAnimal(@RequestHeader("Authorization") String token,
                                                          @PathVariable String id) {
        return ResponseEntity.ok().body(animalService.findById(token, id));
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<AnimalResponseDTO>> listarAnimais(@RequestHeader("Authorization") String token, Pageable pageable) {
        return ResponseEntity.ok().body(animalService.findAll(token, pageable));
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<MensagemResponseDTO> desativarAnimal(@RequestHeader("Authorization") String token,
                                                               @PathVariable String id) {
        animalService.delete(token, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/atualizar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnimalResponseDTO> atualizarDadosAnimal(@RequestHeader("Authorization") String token,
                                                                  @PathVariable String id,
                                                                  @RequestPart("animal") @Valid AnimalAtualizacaoDTO atualizacaoDTO,
                                                                  @RequestPart(value = "foto", required = false) MultipartFile novaFoto) {
        return ResponseEntity.ok().body(animalService.updateData(token, id, atualizacaoDTO, novaFoto));
    }
}
