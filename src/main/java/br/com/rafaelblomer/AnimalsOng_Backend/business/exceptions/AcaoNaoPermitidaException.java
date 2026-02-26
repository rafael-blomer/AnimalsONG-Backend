package br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions;

public class AcaoNaoPermitidaException extends RuntimeException {
    public AcaoNaoPermitidaException(String message) {
        super(message);
    }
}
