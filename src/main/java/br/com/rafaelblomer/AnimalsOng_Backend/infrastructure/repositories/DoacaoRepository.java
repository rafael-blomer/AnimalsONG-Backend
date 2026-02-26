package br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Doacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoacaoRepository extends MongoRepository<Doacao, String> {

        List<Doacao> findByIdOngAndData(String ongId, LocalDate data);
}
