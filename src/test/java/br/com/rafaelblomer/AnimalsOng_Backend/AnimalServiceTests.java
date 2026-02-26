package br.com.rafaelblomer.AnimalsOng_Backend;

import br.com.rafaelblomer.AnimalsOng_Backend.business.AnimalService;
import br.com.rafaelblomer.AnimalsOng_Backend.business.CloudinaryService;
import br.com.rafaelblomer.AnimalsOng_Backend.business.OngService;
import br.com.rafaelblomer.AnimalsOng_Backend.business.converters.AnimalConverter;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.AnimalAtualizacaoDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.AnimalCadastroDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.AnimalResponseDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.MensagemResponseDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions.AcaoNaoPermitidaException;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Animal;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Porte;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Status;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories.AnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTests {

    @InjectMocks
    private AnimalService animalService;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private AnimalConverter animalConverter;

    @Mock
    private OngService ongService;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private MultipartFile foto;

    @Test
    void deveCriarAnimalComSucesso() {
        String token = "Bearer token";
        String fotoUrl = "http://foto.com/img.png";

        Ong ong = new Ong();
        ong.setId("ong123");

        AnimalCadastroDTO dto = mock(AnimalCadastroDTO.class);
        Animal animal = new Animal();
        AnimalResponseDTO responseDTO = mock(AnimalResponseDTO.class);

        when(ongService.buscarOngEntityPorToken(token)).thenReturn(ong);
        when(cloudinaryService.uploadImagem(foto)).thenReturn(fotoUrl);
        when(animalConverter.dtoCadastroParaEntity(dto, fotoUrl, "ong123")).thenReturn(animal);
        when(animalConverter.entityParaResponseDto(animal)).thenReturn(responseDTO);

        AnimalResponseDTO resposta = animalService.create(token, dto, foto);

        assertNotNull(resposta);
        verify(animalRepository).save(animal);
        verify(cloudinaryService).uploadImagem(foto);
    }

    @Test
    void deveBuscarTodosAnimaisDaOngExcetoDesativados() {
        String token = "Bearer token";
        Pageable pageable = PageRequest.of(0, 10);

        Ong ong = new Ong();
        ong.setId("ong123");

        Animal a1 = new Animal();
        Animal a2 = new Animal();

        Page<Animal> pageAnimais = new PageImpl<>(List.of(a1, a2));

        when(ongService.buscarOngEntityPorToken(token))
                .thenReturn(ong);

        when(animalRepository.findByIdOngAndStatusNot(
                "ong123",
                Status.DESATIVADO,
                pageable
        )).thenReturn(pageAnimais);

        when(animalConverter.entityParaResponseDto(any(Animal.class)))
                .thenReturn(mock(AnimalResponseDTO.class));

        Page<AnimalResponseDTO> resposta =
                animalService.findAll(token, pageable);

        assertEquals(2, resposta.getTotalElements());

        verify(animalRepository)
                .findByIdOngAndStatusNot("ong123", Status.DESATIVADO, pageable);

        verify(animalConverter, times(2))
                .entityParaResponseDto(any(Animal.class));
    }


    @Test
    void deveDesativarAnimal() {
        String token = "Bearer token";

        Ong ong = new Ong();
        ong.setId("ong123");

        Animal animal = new Animal();
        animal.setIdOng("ong123");
        animal.setStatus(Status.ATIVO);

        when(ongService.buscarOngEntityPorToken(token)).thenReturn(ong);
        when(animalRepository.findById("animal1")).thenReturn(Optional.of(animal));

        assertEquals(Status.DESATIVADO, animal.getStatus());
        verify(animalRepository).save(animal);
    }

    @Test
    void naoDeveDeletarAnimalDeOutraOng() {
        Ong ong = new Ong();
        ong.setId("ong123");

        Animal animal = new Animal();
        animal.setIdOng("outra-ong");

        when(ongService.buscarOngEntityPorToken(any())).thenReturn(ong);
        when(animalRepository.findById("animal")).thenReturn(Optional.of(animal));

        assertThrows(
                AcaoNaoPermitidaException.class,
                () -> animalService.delete("token", "animal")
        );

        verify(animalRepository, never()).save(any());
    }

    @Test
    void deveAtualizarDadosDoAnimalSemFoto() {
        Ong ong = new Ong();
        ong.setId("ong123");

        Animal animal = new Animal();
        animal.setIdOng("ong123");

        AnimalAtualizacaoDTO dto = new AnimalAtualizacaoDTO(
                true, Status.ATIVO, Porte.PEQUENO, 1
        );

        when(ongService.buscarOngEntityPorToken(any())).thenReturn(ong);
        when(animalRepository.findById("animal")).thenReturn(Optional.of(animal));
        when(animalConverter.entityParaResponseDto(animal))
                .thenReturn(mock(AnimalResponseDTO.class));

        AnimalResponseDTO resposta =
                animalService.updateData("token", "animal", dto, null);

        assertNotNull(resposta);
        assertEquals(Status.ATIVO, animal.getStatus());
    }

    @Test
    void deveAtualizarDadosDoAnimalComNovaFoto() {
        Ong ong = new Ong();
        ong.setId("ong123");

        Animal animal = new Animal();
        animal.setIdOng("ong123");
        animal.setFotoUrl("url-antiga");

        AnimalAtualizacaoDTO dto = new AnimalAtualizacaoDTO(
                false, Status.ADOTADO,  Porte.GRANDE, 5
        );

        MultipartFile novaFoto = mock(MultipartFile.class);

        when(novaFoto.isEmpty()).thenReturn(false);
        when(ongService.buscarOngEntityPorToken(any())).thenReturn(ong);
        when(animalRepository.findById("animal")).thenReturn(Optional.of(animal));
        when(cloudinaryService.uploadImagem(novaFoto)).thenReturn("nova-url");
        when(animalConverter.entityParaResponseDto(animal))
                .thenReturn(mock(AnimalResponseDTO.class));

        animalService.updateData("token", "animal", dto, novaFoto);

        assertEquals("nova-url", animal.getFotoUrl());
        verify(cloudinaryService).deletarPorUrl("url-antiga");
    }

}
