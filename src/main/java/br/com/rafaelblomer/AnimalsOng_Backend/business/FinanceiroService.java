package br.com.rafaelblomer.AnimalsOng_Backend.business;

import br.com.rafaelblomer.AnimalsOng_Backend.business.converters.FinanceiroConverter;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.*;
import br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions.AcaoNaoPermitidaException;
import br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions.ObjetoNaoEncontradoException;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Despesa;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Doacao;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories.DespesaRepository;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories.DoacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FinanceiroService {

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private FinanceiroConverter financeiroConverter;

    @Autowired
    private OngService ongService;

    public MensagemResponseDTO createDoacao(String token, DoacaoCadastroDTO doacaoCadastroDTO) {
        Ong ong = buscarOngPorToken(token);
        Doacao doacao = financeiroConverter.doacaoDtoParaEntity(doacaoCadastroDTO, ong.getId());
        doacaoRepository.save(doacao);
        return new MensagemResponseDTO("Sucesso");
    }

    public MensagemResponseDTO createDespesa(String token, DespesaCadastroDTO despesaCadastroDTO) {
        Ong ong = buscarOngPorToken(token);
        Despesa despesa = financeiroConverter.despesaDtoParaEntity(despesaCadastroDTO, ong.getId());
        despesaRepository.save(despesa);
        return new MensagemResponseDTO("Sucesso");
    }

    public List<DoacaoResponseDTO> retornarDoacoesPorDia(String token, LocalDate dia) {
        Ong ong = buscarOngPorToken(token);
        return doacaoRepository.findByIdOngAndData(ong.getId(), dia)
                .stream().map(financeiroConverter::entityDoacaoParaDto).toList();
    }

    public List<DespesaResponseDTO> retornarDespesasPorDia(String token, LocalDate dia) {
        Ong ong = buscarOngPorToken(token);
        return despesaRepository.findByIdOngAndData(ong.getId(), dia)
                .stream().map(financeiroConverter::entityDespesaParaDto).toList();
    }

    public void deletarDoacao(String token, String idDoacao) {
        Ong ong = buscarOngPorToken(token);
        Doacao doacao = buscarDoacaoPorId(idDoacao);
        verificarOngDoacao(ong, doacao);
        doacaoRepository.delete(doacao);
    }

    public void deletarDespesa(String token, String idDespesa) {
        Ong ong = buscarOngPorToken(token);
        Despesa despesa = buscarDespesaPorId(idDespesa);
        verificarOngDespesa(ong, despesa);
        despesaRepository.delete(despesa);
    }


    //MÉTODOS ÚTEIS


    private Ong buscarOngPorToken(String token) {
        return ongService.buscarOngEntityPorToken(token);
    }

    private Doacao buscarDoacaoPorId(String id) {
        return doacaoRepository.findById(id).orElseThrow(
                () -> new ObjetoNaoEncontradoException("Doação com id " + id + "não encontrada."));
    }

    private Despesa buscarDespesaPorId(String id) {
        return despesaRepository.findById(id).orElseThrow(
                () -> new ObjetoNaoEncontradoException("Depesa com id " + id + "não encontrada."));
    }

    private void verificarOngDoacao(Ong ong, Doacao doacao) {
        if(!ong.getId().equals(doacao.getIdOng()))
            throw new AcaoNaoPermitidaException("Você não tem permissão para realizar essa ação.");
    }

    private void verificarOngDespesa(Ong ong, Despesa despesa) {
        if(!ong.getId().equals(despesa.getIdOng()))
            throw new AcaoNaoPermitidaException("Você não tem permissão para realizar essa ação.");
    }
}
