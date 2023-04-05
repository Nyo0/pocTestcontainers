package com.example.poc.domain.usecase.impl;

import com.example.poc.dataaccess.entity.Carteira;
import com.example.poc.dataaccess.entity.Transicao;
import com.example.poc.dataaccess.repository.TransactionRepository;
import com.example.poc.domain.model.TransactionRequest;
import com.example.poc.domain.usecase.ConverteDolar;
import com.example.poc.domain.usecase.RegistraTransicao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class RegistraTransicaoImpl implements RegistraTransicao {

    @Autowired
    private ConverteDolar converteDolar;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void registraTransicao(TransactionRequest transactionRequest) {
       var transicao = buildTransicao(transactionRequest);
        if (transicao.getCarteira().getTipo().equals("D")){
            transicao.getCarteira().setSaldo(converteDolar.converteValor(transactionRequest.getValor()));
        }
        transactionRepository.save(transicao);

    }
    public Transicao buildTransicao(TransactionRequest transactionRequest){
      return Transicao.builder()
                .carteira(buildCarteira(transactionRequest.getValor(),transactionRequest.getTipo()))
                .cpf(transactionRequest.getCpf())
                .date(LocalDate.now().toString())
                .build();
    }
    public Carteira buildCarteira(BigDecimal saldo, String tipo){
        return Carteira.builder()
                .saldo(saldo)
                .tipo(tipo)
                .build();
    }
}
