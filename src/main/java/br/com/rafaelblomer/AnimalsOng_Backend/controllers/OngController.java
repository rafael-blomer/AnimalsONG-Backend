package br.com.rafaelblomer.AnimalsOng_Backend.controllers;

import br.com.rafaelblomer.AnimalsOng_Backend.business.OngService;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ongs")
public class OngController {

    @Autowired
    private OngService ongService;

    @PostMapping("/cadastro")
    public ResponseEntity<MensagemResponseDTO> cadastrarinstrutor(@RequestBody @Valid OngCadastroDTO CadastroDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(ongService.create(CadastroDTO));
    }

    @PostMapping("/confirmacao-email/{token}")
    public ResponseEntity<MensagemResponseDTO> confirmacaoEmail(@PathVariable String token){
        return ResponseEntity.ok().body(ongService.verifyEmail(token));
    }

    @PostMapping("/login")
    public ResponseEntity<MensagemResponseDTO> login(@RequestBody @Valid OngLoginDTO LoginDTO){
        return ResponseEntity.ok().body(ongService.login(LoginDTO));
    }

    @GetMapping("/buscar-por-token")
    public ResponseEntity<OngResponseDTO> buscarPorToken(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(ongService.findOngDtobyToken(token));
    }

    @PatchMapping("/desativar")
    public ResponseEntity<Void> desativar(@RequestHeader("Authorization") String token){
        ongService.disable(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/solicitar-alteracao-senha/{email}")
    public ResponseEntity<MensagemResponseDTO>  solicitarAlteracaoSenha(@PathVariable String email){
        return ResponseEntity.ok().body(ongService.requestSwapPassword(email));
    }

    @PatchMapping("/alterar-senha")
    public ResponseEntity<MensagemResponseDTO> alterarSenha(@RequestBody @Valid OngAlterarSenhaDTO alterarSenhaDTO) {
        System.out.println(alterarSenhaDTO.novaSenha());
        return ResponseEntity.ok().body(ongService.swapPassword(alterarSenhaDTO));
    }

    @PatchMapping("/atualizar")
    public ResponseEntity<OngResponseDTO> atualizarDadosOng(@RequestHeader("Authorization") String token,
                                                            @RequestBody OngAtualizacaoDTO OngAtualizacaoDTO) {
        return ResponseEntity.ok().body(ongService.updateData(token, OngAtualizacaoDTO));
    }
}
