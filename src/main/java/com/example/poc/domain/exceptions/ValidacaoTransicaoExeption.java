package com.example.poc.domain.exceptions;

import java.util.List;

public class ValidacaoTransicaoExeption extends RuntimeException{

    private List<String> errors;

    public ValidacaoTransicaoExeption(List<String> errors) {
        super("Ocorreram erros na tentativa de validar os campos");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
