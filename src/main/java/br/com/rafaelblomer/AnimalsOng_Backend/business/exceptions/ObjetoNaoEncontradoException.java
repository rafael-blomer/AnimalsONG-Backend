package br.com.rafaelblomer.AnimalsOng_Backend.business.exceptions;

public class ObjetoNaoEncontradoException extends RuntimeException {
    public ObjetoNaoEncontradoException(String message) {
        super(message);
    }
}
