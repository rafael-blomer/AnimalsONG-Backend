package br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.repositories;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Animal;
import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends MongoRepository<Animal, String> {

    Page<Animal> findByIdOngAndStatusNot(String id, Status status, Pageable pageable);
}
