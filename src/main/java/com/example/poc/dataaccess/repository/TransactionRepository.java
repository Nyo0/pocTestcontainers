package com.example.poc.dataaccess.repository;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;


import com.example.poc.dataaccess.entity.Transicao;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;


@Repository
public class TransactionRepository  {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Transicao save(Transicao transicao) {
        dynamoDBMapper.save(transicao);
        return transicao;
    }


}
