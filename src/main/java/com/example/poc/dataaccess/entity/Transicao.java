package com.example.poc.dataaccess.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDBTable(tableName = "Transicao")
public class Transicao{

    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = "date")
    private String date;

    @DynamoDBRangeKey
    @DynamoDBAttribute(attributeName = "cpf")
    private String cpf;


    @DynamoDBAttribute(attributeName = "carteira")
    private Carteira carteira;


}
