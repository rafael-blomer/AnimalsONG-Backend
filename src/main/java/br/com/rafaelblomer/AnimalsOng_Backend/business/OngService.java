package br.com.rafaelblomer.AnimalsOng_Backend.business;

import br.com.rafaelblomer.AnimalsOng_Backend.business.converters.OngConverter;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.*;
import br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions.DadoIrregularException;
import br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions.ObjetoInativoException;
import br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions.ObjetoNaoEncontradoException;
import br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions.VerificacaoEmailException;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.VerificacaoEmail;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories.OngRepository;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories.VerificacaoEmailRepository;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class OngService {

    @Autowired
    private OngRepository ongRepository;

    @Autowired
    private OngConverter ongConverter;

    @Autowired
    private VerificacaoEmailRepository verificacaoRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public MensagemResponseDTO create(OngCadastroDTO ongCadastroDTO) {
        Ong ong = ongConverter.cadastroDTOparaEntity(ongCadastroDTO);
        verificarCadastro(ong);
        ong.setSenha(passwordEncoder.encode(ong.getSenha()));
        ongRepository.save(ong);
        VerificacaoEmail verificacaoEmail = criarTokenVerificacao(ong.getId());
        emailService.enviarVerificacaoEmail(verificacaoEmail, ong);
        return new MensagemResponseDTO("Sucesso");
    }

    public MensagemResponseDTO verifyEmail(String token) {
        VerificacaoEmail verificacaoEmail = buscarVericacao(token);
        Ong ong = buscarOngPorId(verificacaoEmail.getOngId());
        ong.setAtivo(true);
        ongRepository.save(ong);
        verificacaoRepository.delete(verificacaoEmail);
        return new MensagemResponseDTO("Sucesso");
    }

    public MensagemResponseDTO login(OngLoginDTO ongLoginDTO) {
        Ong ong = buscarOngPorEmail(ongLoginDTO.email());
        verificarOngAtiva(ong);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(ongLoginDTO.email(), ongLoginDTO.senha()));
        return new MensagemResponseDTO("Bearer " + jwtService.generateToken(authentication));
    }

    public OngResponseDTO findOngDtobyToken(String token) {
        Ong ong = buscarOngEntityPorToken(token);
        return ongConverter.entityParaResponseDTO(ong);
    }

    public void disable(String token) {
        Ong ong = buscarOngEntityPorToken(token);
        ong.setAtivo(false);
        ongRepository.save(ong);
    }

    public MensagemResponseDTO requestSwapPassword(String email) {
        Ong ong = buscarOngPorEmail(email);
        VerificacaoEmail verificacaoEmail = criarTokenVerificacao(ong.getId());
        emailService.enviarEmailTrocaSenha(ong,verificacaoEmail.getToken());
        return new MensagemResponseDTO("Sucesso");
    }

    public MensagemResponseDTO swapPassword(OngAlterarSenhaDTO ongAlterarSenhaDTO) {
        VerificacaoEmail verificacaoEmail = buscarVericacao(ongAlterarSenhaDTO.token());
        Ong ong =  buscarOngPorId(verificacaoEmail.getOngId());
        ong.setSenha(passwordEncoder.encode(ongAlterarSenhaDTO.novaSenha()));
        ongRepository.save(ong);
        verificacaoRepository.delete(verificacaoEmail);
        return new MensagemResponseDTO("Sucesso");
    }

    public OngResponseDTO updateData(String token, OngAtualizacaoDTO atualizacaoDTO) {
        Ong ong = buscarOngEntityPorToken(token);
        atualizarDados(ong, atualizacaoDTO);
        ongRepository.save(ong);
        return ongConverter.entityParaResponseDTO(ong);
    }

    //MÉTODOS ÚTEIS


    private void verificarCadastro(Ong entity) {
        if (ongRepository.existsByEmail(entity.getEmail()))
            throw new DadoIrregularException("Já existe um usuario cadastrado com esse email.");

        if (entity.getCnpj() != null) {
            if (ongRepository.existsByCnpj(entity.getCnpj()))
                throw new DadoIrregularException("Já existe um usuario cadastrado com esse documento.");
        }
    }

    private VerificacaoEmail criarTokenVerificacao(String ongId) {
        String uuid = UUID.randomUUID().toString();
        return verificacaoRepository.save(new VerificacaoEmail(uuid, ongId ));
    }

    private VerificacaoEmail buscarVericacao(String token) {
        return verificacaoRepository.findByToken(token).orElseThrow(() -> new VerificacaoEmailException("Token inválido."));
    }

    private Ong buscarOngPorId(String ongId) {
        return ongRepository.findById(ongId).orElseThrow(
                () -> new ObjetoNaoEncontradoException("Ong com id " + ongId + " não foi encontrado."));
    }

    private Ong buscarOngPorEmail (String email) {
        return ongRepository.findByEmail(email).orElseThrow(
                () -> new ObjetoNaoEncontradoException("Ong com email " + email + " não foi encontrado."));
    }

    private void verificarOngAtiva(Ong ong) {
        if(!ong.getAtivo())
            throw new ObjetoInativoException("A Ong foi desativada ou ainda não foi ativa.");
    }

    public Ong buscarOngEntityPorToken(String token) {
        String email = jwtService.extrairEmailToken(token.substring(7));
        return  ongRepository.findByEmail(email).orElseThrow(
                () -> new ObjetoNaoEncontradoException("Ong com email " + email + " não foi encontrada."));
    }

    private void atualizarDados(Ong ong, OngAtualizacaoDTO dto) {
        ong.setNome(dto.nome());
        ong.setCnpj(dto.cnpj());
        ong.setTelefone(dto.telefone());
        ong.setCep(dto.cep());
        ong.setCidade(dto.cidade());
        ong.setEstado(dto.estado());
        ong.setRua(dto.rua());
        ong.setBairro(dto.bairro());
        ong.setComplemento(dto.complemento());
        ong.setNumero(dto.numero());
    }

}
