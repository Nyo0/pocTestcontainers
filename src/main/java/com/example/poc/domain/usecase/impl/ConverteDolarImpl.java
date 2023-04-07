package com.example.poc.domain.usecase.impl;

import com.example.poc.domain.usecase.ConverteDolar;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ConverteDolarImpl implements ConverteDolar {

    private final BigDecimal dolar = new BigDecimal("5.30");

    @Override
    public BigDecimal converteValor(BigDecimal valor) {
        return valor.multiply(dolar).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
