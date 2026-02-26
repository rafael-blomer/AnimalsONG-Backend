package br.com.rafaelblomer.AnimalsOng_Backend.business.converters;

import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.OngCadastroDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.OngResponseDTO;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import org.springframework.stereotype.Component;

@Component
public class OngConverter {

    public Ong cadastroDTOparaEntity(OngCadastroDTO ongCadastroDTO) {
        return new Ong(ongCadastroDTO.bairro(), ongCadastroDTO.cep(), ongCadastroDTO.cidade(),
                ongCadastroDTO.cnpj(), ongCadastroDTO.complemento(), ongCadastroDTO.email(),
                ongCadastroDTO.estado(), ongCadastroDTO.nome(), ongCadastroDTO.numero(),
                ongCadastroDTO.rua(), ongCadastroDTO.senha(), ongCadastroDTO.telefone());
    }

    public OngResponseDTO entityParaResponseDTO(Ong ong) {
        return new OngResponseDTO(ong.getNome(), ong.getCnpj(), ong.getTelefone(),
                ong.getEmail(), ong.getCep(), ong.getCidade(), ong.getEstado(),
                ong.getRua(), ong.getBairro(), ong.getComplemento(),
                ong.getNumero(), ong.getDataCriacao());
    }
}
