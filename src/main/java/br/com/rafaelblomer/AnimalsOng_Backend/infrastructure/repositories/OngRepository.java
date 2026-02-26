package br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OngRepository extends MongoRepository<Ong, String> {

    boolean existsByEmail(String email);

    boolean existsByCnpj(String documento);

    Optional<Ong> findByEmail(String email);
}
