package br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.VerificacaoEmail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificacaoEmailRepository extends MongoRepository<VerificacaoEmail, String> {

    Optional<VerificacaoEmail> findByToken(String token);
}
