package br.com.rafaelblomer.AnimalsOng_Backend;

import br.com.rafaelblomer.AnimalsOng_Backend.business.FinanceiroService;
import br.com.rafaelblomer.AnimalsOng_Backend.business.OngService;
import br.com.rafaelblomer.AnimalsOng_Backend.business.converters.FinanceiroConverter;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.*;
import br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions.AcaoNaoPermitidaException;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Despesa;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Doacao;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories.DespesaRepository;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories.DoacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinanceiroServiceTests {

    @InjectMocks
    private FinanceiroService financeiroService;

    @Mock
    private DoacaoRepository doacaoRepository;

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private FinanceiroConverter financeiroConverter;

    @Mock
    private OngService ongService;

    private Ong ong;

    @BeforeEach
    void setup() {
        ong = new Ong();
        ong.setId("ong123");
    }

    @Test
    void deveCriarDoacaoComSucesso() {
        String token = "Bearer token";
        DoacaoCadastroDTO dto = mock(DoacaoCadastroDTO.class);
        Doacao doacao = new Doacao();

        when(ongService.buscarOngEntityPorToken(token)).thenReturn(ong);
        when(financeiroConverter.doacaoDtoParaEntity(dto, "ong123"))
                .thenReturn(doacao);

        MensagemResponseDTO resposta =
                financeiroService.createDoacao(token, dto);

        assertEquals("Sucesso", resposta.mensagem());
        verify(doacaoRepository).save(doacao);
    }

    @Test
    void deveCriarDespesaComSucesso() {
        String token = "Bearer token";
        DespesaCadastroDTO dto = mock(DespesaCadastroDTO.class);
        Despesa despesa = new Despesa();

        when(ongService.buscarOngEntityPorToken(token)).thenReturn(ong);
        when(financeiroConverter.despesaDtoParaEntity(dto, "ong123"))
                .thenReturn(despesa);

        MensagemResponseDTO resposta =
                financeiroService.createDespesa(token, dto);

        assertEquals("Sucesso", resposta.mensagem());
        verify(despesaRepository).save(despesa);
    }

    @Test
    void deveRetornarDoacoesPaginadas() {
        String token = "Bearer token";
        Pageable pageable = PageRequest.of(0, 10);

        Doacao doacao = new Doacao();
        Page<Doacao> page = new PageImpl<>(List.of(doacao));

        when(ongService.buscarOngEntityPorToken(token)).thenReturn(ong);
        when(doacaoRepository.findByIdOng("ong123", pageable))
                .thenReturn(page);
        when(financeiroConverter.entityDoacaoParaDto(any()))
                .thenReturn(mock(DoacaoResponseDTO.class));

        Page<DoacaoResponseDTO> resposta =
                financeiroService.retornaDoacoes(token, pageable);

        assertEquals(1, resposta.getTotalElements());
    }

    @Test
    void deveRetornarDespesasPaginadas() {
        String token = "Bearer token";
        Pageable pageable = PageRequest.of(0, 10);

        Despesa despesa = new Despesa();
        Page<Despesa> page = new PageImpl<>(List.of(despesa));

        when(ongService.buscarOngEntityPorToken(token)).thenReturn(ong);
        when(despesaRepository.findByIdOng("ong123", pageable))
                .thenReturn(page);
        when(financeiroConverter.entityDespesaParaDto(any()))
                .thenReturn(mock(DespesaResponseDTO.class));

        Page<DespesaResponseDTO> resposta =
                financeiroService.retornarDespesas(token, pageable);

        assertEquals(1, resposta.getTotalElements());
    }

    @Test
    void deveRetornarDoacoesPorDia() {
        String token = "Bearer token";
        LocalDate dia = LocalDate.now();

        when(ongService.buscarOngEntityPorToken(token)).thenReturn(ong);
        when(doacaoRepository.findByIdOngAndData("ong123", dia))
                .thenReturn(List.of(new Doacao(), new Doacao()));
        when(financeiroConverter.entityDoacaoParaDto(any()))
                .thenReturn(mock(DoacaoResponseDTO.class));

        List<DoacaoResponseDTO> resposta =
                financeiroService.retornarDoacoesPorDia(token, dia);

        assertEquals(2, resposta.size());
    }

    @Test
    void deveDeletarDoacaoQuandoPertencerOng() {
        String token = "Bearer token";
        Doacao doacao = new Doacao();
        doacao.setIdOng("ong123");

        when(ongService.buscarOngEntityPorToken(token)).thenReturn(ong);
        when(doacaoRepository.findById("1"))
                .thenReturn(Optional.of(doacao));

        verify(doacaoRepository).delete(doacao);
    }

    @Test
    void deveLancarExcecaoQuandoDeletarDoacaoDeOutraOng() {
        String token = "Bearer token";
        Doacao doacao = new Doacao();
        doacao.setIdOng("outra");

        when(ongService.buscarOngEntityPorToken(token)).thenReturn(ong);
        when(doacaoRepository.findById("1"))
                .thenReturn(Optional.of(doacao));

        assertThrows(AcaoNaoPermitidaException.class,
                () -> financeiroService.deletarDoacao(token, "1"));
    }

}
