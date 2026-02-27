package br.com.rafaelblomer.AnimalsOng_Backend;

import br.com.rafaelblomer.AnimalsOng_Backend.business.EmailService;
import br.com.rafaelblomer.AnimalsOng_Backend.business.OngService;
import br.com.rafaelblomer.AnimalsOng_Backend.business.converters.OngConverter;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.*;
import br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions.ObjetoNaoEncontradoException;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.VerificacaoEmail;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories.OngRepository;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories.VerificacaoEmailRepository;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OngServiceTests {

    @InjectMocks
    private OngService ongService;

    @Mock
    private OngRepository ongRepository;

    @Mock
    private OngConverter ongConverter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private VerificacaoEmailRepository verificacaoRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    private OngCadastroDTO ongCadastroDTO;
    private Ong ong;

    @BeforeEach
    public void setup() {
        ongCadastroDTO = new OngCadastroDTO("ONG Amigos dos Animais", null, List.of("11999999999"),
                "contato@amigosdosanimais.org", "senha123", "01001-000", "Lages",
                "Santa Catarina", "rua 1", "bairro1", null, 1);

        ong = new Ong("bairro1", "01001-000", "Lages", null, null,
                "contato@amigosdosanimais.org", "Santa Catarina", "ONG Amigos dos Animais",
                1, "rua 1", "senha123", List.of("11999999999"));
    }

    @Test
    void deveCriarOng() {
        when(ongConverter.cadastroDTOparaEntity(any(OngCadastroDTO.class)))
                .thenReturn(ong);
        when(passwordEncoder.encode(anyString()))
                .thenReturn("senha-criptografada");
        when(ongRepository.save(any(Ong.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(verificacaoRepository.save(any(VerificacaoEmail.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MensagemResponseDTO resposta = ongService.create(ongCadastroDTO);

        assertEquals("Sucesso", resposta.mensagem());
        verify(ongRepository).save(ong);
        verify(passwordEncoder).encode(anyString());
        verify(verificacaoRepository).save(any());
        verify(emailService).enviarVerificacaoEmail(any(), eq(ong));
    }

    @Test
    void deveVerificarEmailEAtivarOng() {
        String token = "token-valido";

        VerificacaoEmail verificacaoEmail = new VerificacaoEmail();
        verificacaoEmail.setToken(token);
        verificacaoEmail.setOngId("ong123");

        Ong ongInativa = new Ong();
        ongInativa.setId("ong123");
        ongInativa.setAtivo(false);

        when(verificacaoRepository.findByToken(token))
                .thenReturn(Optional.of(verificacaoEmail));

        when(ongRepository.findById("ong123"))
                .thenReturn(Optional.of(ongInativa));

        when(ongRepository.save(any(Ong.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MensagemResponseDTO resposta = ongService.verifyEmail(token);

        assertEquals("Sucesso", resposta.mensagem());
        assertTrue(ongInativa.getAtivo());

        verify(verificacaoRepository).findByToken(token);
        verify(ongRepository).findById("ong123");
        verify(ongRepository).save(ongInativa);
        verify(verificacaoRepository).delete(verificacaoEmail);
    }

    @Test
    void deveRealizarLoginComSucesso() {
        OngLoginDTO loginDTO = new OngLoginDTO("contato@ong.com", "123456");

        Ong ongAtiva = new Ong();
        ongAtiva.setEmail("contato@ong.com");
        ongAtiva.setAtivo(true);

        Authentication authentication = mock(Authentication.class);

        when(ongRepository.findByEmail("contato@ong.com"))
                .thenReturn(Optional.of(ongAtiva));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(authentication))
                .thenReturn("jwt-token");

        MensagemResponseDTO resposta = ongService.login(loginDTO);

        assertEquals("Bearer jwt-token", resposta.mensagem());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(authentication);
    }

    @Test
    void naoDevePermitirLoginQuandoOngInativa() {
        OngLoginDTO loginDTO = new OngLoginDTO("contato@ong.com", "123456");

        Ong ongInativa = new Ong();
        ongInativa.setEmail("contato@ong.com");
        ongInativa.setAtivo(false);

        when(ongRepository.findByEmail("contato@ong.com"))
                .thenReturn(Optional.of(ongInativa));

        assertThrows(
                RuntimeException.class,
                () -> ongService.login(loginDTO)
        );

        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void naoDeveLogarQuandoSenhaInvalida() {
        OngLoginDTO loginDTO = new OngLoginDTO("contato@ong.com", "senha-errada");

        Ong ongAtiva = new Ong();
        ongAtiva.setEmail("contato@ong.com");
        ongAtiva.setAtivo(true);

        when(ongRepository.findByEmail("contato@ong.com"))
                .thenReturn(Optional.of(ongAtiva));

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThrows(
                BadCredentialsException.class,
                () -> ongService.login(loginDTO)
        );

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void deveBuscarOngDtoPorToken() {
        String token = "Bearer jwt-token";

        Ong ong = new Ong();
        ong.setNome("ONG Amigos dos Animais");
        ong.setEmail("contato@ong.com");

        OngResponseDTO responseDTO = new OngResponseDTO(
                "ONG Amigos dos Animais",
                null,
                List.of("11999999999"),
                "contato@ong.com",
                "01001-000",
                "São Paulo",
                "SP",
                "Rua das Flores",
                "Centro",
                null,
                100,
                LocalDate.of(2024, 1, 1)
        );

        when(jwtService.extrairEmailToken("jwt-token"))
                .thenReturn("contato@ong.com");

        when(ongRepository.findByEmail("contato@ong.com"))
                .thenReturn(Optional.of(ong));

        when(ongConverter.entityParaResponseDTO(ong))
                .thenReturn(responseDTO);

        OngResponseDTO resposta = ongService.findOngDtobyToken(token);

        assertNotNull(resposta);
        assertEquals("ONG Amigos dos Animais", resposta.nome());
        assertEquals("contato@ong.com", resposta.email());

        verify(jwtService).extrairEmailToken("jwt-token");
        verify(ongRepository).findByEmail("contato@ong.com");
        verify(ongConverter).entityParaResponseDTO(ong);
    }


    @Test
    void deveLancarExcecaoQuandoOngNaoEncontradaPorToken() {
        String token = "Bearer jwt-token";

        when(jwtService.extrairEmailToken("jwt-token"))
                .thenReturn("contato@ong.com");

        when(ongRepository.findByEmail("contato@ong.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                ObjetoNaoEncontradoException.class,
                () -> ongService.findOngDtobyToken(token)
        );

        verify(jwtService).extrairEmailToken("jwt-token");
        verify(ongRepository).findByEmail("contato@ong.com");
        verify(ongConverter, never()).entityParaResponseDTO(any());
    }

    @Test
    void deveDesativarOng() {
        String token = "Bearer jwt-token";

        Ong ong = new Ong();
        ong.setAtivo(true);

        when(jwtService.extrairEmailToken("jwt-token"))
                .thenReturn("contato@ong.com");

        when(ongRepository.findByEmail("contato@ong.com"))
                .thenReturn(Optional.of(ong));

        when(ongRepository.save(any(Ong.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ongService.disable(token);

        assertFalse(ong.getAtivo());

        verify(ongRepository).save(ong);
    }

    @Test
    void deveSolicitarTrocaDeSenha() {
        Ong ong = new Ong();
        ong.setId("ong123");
        ong.setEmail("contato@ong.com");

        VerificacaoEmail verificacao = new VerificacaoEmail();
        verificacao.setToken("token-troca");

        when(ongRepository.findByEmail("contato@ong.com"))
                .thenReturn(Optional.of(ong));

        when(verificacaoRepository.save(any(VerificacaoEmail.class)))
                .thenReturn(verificacao);

        MensagemResponseDTO resposta = ongService.requestSwapPassword("contato@ong.com");

        assertEquals("Sucesso", resposta.mensagem());

        verify(verificacaoRepository).save(any(VerificacaoEmail.class));
        verify(emailService).enviarEmailTrocaSenha(ong, "token-troca");
    }

    @Test
    void deveAlterarSenhaDaOng() {
        OngAlterarSenhaDTO dto = new OngAlterarSenhaDTO("token", "novaSenha");

        Ong ong = new Ong();
        ong.setId("ong123");

        VerificacaoEmail verificacao = new VerificacaoEmail();
        verificacao.setToken("token");
        verificacao.setOngId("ong123");

        when(verificacaoRepository.findByToken("token"))
                .thenReturn(Optional.of(verificacao));

        when(ongRepository.findById("ong123"))
                .thenReturn(Optional.of(ong));

        when(passwordEncoder.encode("novaSenha"))
                .thenReturn("senha-criptografada");

        when(ongRepository.save(any(Ong.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MensagemResponseDTO resposta = ongService.swapPassword(dto);

        assertEquals("Sucesso", resposta.mensagem());

        verify(passwordEncoder).encode("novaSenha");
        verify(ongRepository).save(ong);
        verify(verificacaoRepository).delete(verificacao);
    }

    @Test
    void deveAlterarDadosDaOng() {
        String token = "Bearer jwt-token";

        Ong ong = new Ong();
        ong.setNome("Nome antigo");

        OngAtualizacaoDTO dto = new OngAtualizacaoDTO(
                "Novo nome",
                "12345678000199",
                List.of("11999999999"),
                "01001-000",
                "São Paulo",
                "SP",
                "Rua Nova",
                "Centro",
                "Sala 1",
                10
        );

        OngResponseDTO responseDTO = mock(OngResponseDTO.class);

        when(jwtService.extrairEmailToken("jwt-token"))
                .thenReturn("contato@ong.com");

        when(ongRepository.findByEmail("contato@ong.com"))
                .thenReturn(Optional.of(ong));

        when(ongConverter.entityParaResponseDTO(ong))
                .thenReturn(responseDTO);

        OngResponseDTO resposta = ongService.updateData(token, dto);

        assertNotNull(resposta);
        assertEquals("Novo nome", ong.getNome());

        verify(ongConverter).entityParaResponseDTO(ong);
    }

}
