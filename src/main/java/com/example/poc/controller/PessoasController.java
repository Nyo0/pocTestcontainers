package com.example.poc.controller;

import com.example.poc.domain.exceptions.ValidacaoTransicaoExeption;
import com.example.poc.domain.model.TransactionRequest;
import com.example.poc.domain.usecase.RegistraTransicao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PessoasController {

        @Autowired
        private RegistraTransicao registraTransicao;

        @PostMapping("/Transicao")
        public ResponseEntity<?> savePessoas(@Valid @RequestBody TransactionRequest request, BindingResult result) {
                try {
                        if (result.hasErrors()) {
                                List<String> errors = result.getAllErrors().stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .collect(Collectors.toList());
                                throw new ValidacaoTransicaoExeption(errors);
                        }
                        registraTransicao.registraTransicao(request);
                        System.out.println("Processo finalizado com sucesso.");
                        return ResponseEntity.ok().build();
                } catch (ValidacaoTransicaoExeption e) {
                        List<String> erros = e.getErrors();
                        for (String error : erros) {
                                System.out.println(error);
                        }
                        System.out.println(e);
                        return ResponseEntity.badRequest().body(erros);
                } catch (Exception e) {
                        System.out.println(e);
                        return ResponseEntity.badRequest().body(e.getCause());
                }
        }
}
