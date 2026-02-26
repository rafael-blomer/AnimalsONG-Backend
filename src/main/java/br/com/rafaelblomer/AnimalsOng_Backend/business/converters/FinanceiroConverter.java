package br.com.rafaelblomer.AnimalsOng_Backend.business.converters;

import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.DespesaCadastroDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.DespesaResponseDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.DoacaoCadastroDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.DoacaoResponseDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Despesa;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Doacao;
import org.springframework.stereotype.Component;

@Component
public class FinanceiroConverter {

    public Doacao doacaoDtoParaEntity(DoacaoCadastroDTO doacaoCadastroDTO, String idOng) {
        return new Doacao(doacaoCadastroDTO.descricao(), idOng,
                doacaoCadastroDTO.nomeDoador(), doacaoCadastroDTO.valor());
    }

    public Despesa despesaDtoParaEntity(DespesaCadastroDTO despesaCadastroDTO, String idOng) {
        return new Despesa(despesaCadastroDTO.descricao(), idOng,
                despesaCadastroDTO.tipoDespesa(), despesaCadastroDTO.valor());
    }

    public DoacaoResponseDTO entityDoacaoParaDto(Doacao doacao) {
        return new DoacaoResponseDTO(doacao.getId(), doacao.getValor(),
                doacao.getDescricao(), doacao.getNomeDoador(), doacao.getData());
    }

    public DespesaResponseDTO entityDespesaParaDto (Despesa despesa) {
        return new DespesaResponseDTO(despesa.getId(), despesa.getValor(),
                despesa.getDescricao(), despesa.getTipoDespesa(), despesa.getData());
    }
}
