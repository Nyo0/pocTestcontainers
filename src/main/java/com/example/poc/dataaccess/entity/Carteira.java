package com.example.poc.dataaccess.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDBDocument
public class Carteira {

    @DynamoDBAttribute
    private BigDecimal saldo;

    @DynamoDBAttribute
    private String tipo;

}
