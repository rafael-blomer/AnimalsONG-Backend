package br.com.rafaelblomer.AnimalsOng_Backend.controllers;

import br.com.rafaelblomer.AnimalsOng_Backend.business.FinanceiroService;
import br.com.rafaelblomer.AnimalsOng_Backend.business.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/financeiro")
public class FinanceiroController {

    @Autowired
    private FinanceiroService financeiroService;

    @PostMapping("/doacao/cadastro")
    public ResponseEntity<MensagemResponseDTO> cadastrarNovaDoacao(@RequestHeader("Authorization") String token,
                                                                   @RequestBody DoacaoCadastroDTO doacaoCadastroDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeiroService.createDoacao(token, doacaoCadastroDTO));
    }

    @PostMapping("/despesa/cadastro")
    public ResponseEntity<MensagemResponseDTO> cadastrarNovaDespesa(@RequestHeader("Authorization") String token,
                                                                    @RequestBody DespesaCadastroDTO despesaCadastroDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeiroService.createDespesa(token, despesaCadastroDTO));
    }

    @GetMapping("/doacao/{dia}")
    public ResponseEntity<List<DoacaoResponseDTO>> buscarDoacoesPorDia(@RequestHeader("Authorization") String token,
                                                                       @PathVariable LocalDate dia) {
        return ResponseEntity.ok().body(financeiroService.retornarDoacoesPorDia(token, dia));
    }

    @GetMapping("/despesa/{dia}")
    public ResponseEntity<List<DespesaResponseDTO>> buscarDespesasPorDia(@RequestHeader("Authorization") String token,
                                                                       @PathVariable LocalDate dia) {
        return ResponseEntity.ok().body(financeiroService.retornarDespesasPorDia(token, dia));
    }

    @DeleteMapping("/doacao/{id}")
    public ResponseEntity<MensagemResponseDTO> deletarDoacao(@RequestHeader("Authorization") String token,
                                                             @PathVariable String id) {
        financeiroService.deletarDoacao(token, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/despesa/{id}")
    public ResponseEntity<MensagemResponseDTO> deletarDespesa(@RequestHeader("Authorization") String token,
                                                             @PathVariable String id) {
        financeiroService.deletarDespesa(token, id);
        return ResponseEntity.noContent().build();
    }
}
