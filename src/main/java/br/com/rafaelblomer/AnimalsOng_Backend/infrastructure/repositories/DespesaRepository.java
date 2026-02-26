package br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Despesa;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespesaRepository extends MongoRepository<Despesa, String> {

    List<Despesa> findByIdOngAndData(String ongId, LocalDate data);
}
