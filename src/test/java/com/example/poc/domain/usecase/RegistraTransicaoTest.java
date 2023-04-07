package com.example.poc.domain.usecase;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.*;
import com.example.poc.config.DynamoDBConfig;
import com.example.poc.dataaccess.entity.Transicao;
import com.example.poc.domain.model.TransactionRequest;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestConfiguration
@Testcontainers
public class RegistraTransicaoTest extends DynamoDBConfig {

    @Autowired
    private RegistraTransicao registraTransicao;

    public static AmazonDynamoDB client;

    public static DynamoDBMapper dynamoDBMapper ;

    @Container
    public static GenericContainer dynamoDB = new GenericContainer("amazon/dynamodb-local:latest")
        .withExposedPorts(8000)
        .withEnv("AWS_REGION", "us-west-2")
        .withEnv("AWS_ACCESS_KEY_ID", "fakeMyKeyId")
        .withEnv("AWS_SECRET_ACCESS_KEY", "fakeSecretAccessKey")
        .withCommand("-jar DynamoDBLocal.jar -inMemory -sharedDb")
        .withAccessToHost(true);

    @DynamicPropertySource
    public static void setDynamoDbPort(DynamicPropertyRegistry registry) {
        registry.add("amazon.service.endpoint", () -> "http://localhost:"+ dynamoDB.getFirstMappedPort());
    }

    @BeforeAll
    public static void setUpTest() {
         client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:"+ dynamoDB.getFirstMappedPort(), "us-west-2"))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials("fakeMyKeyId", "fakeSecretAccessKey")))
                .build();
        dynamoDBMapper = new DynamoDBMapper(client, DynamoDBMapperConfig.DEFAULT);

        String tableName = "Transicao";
        String partitionKeyName = "date";
        String sortKeyName = "cpf";
        CreateTableRequest request = new CreateTableRequest()
                .withTableName(tableName)
                .withKeySchema(
                        new KeySchemaElement(partitionKeyName, KeyType.HASH),
                        new KeySchemaElement(sortKeyName, KeyType.RANGE))
                .withAttributeDefinitions(
                        new AttributeDefinition(partitionKeyName, ScalarAttributeType.S),
                        new AttributeDefinition(sortKeyName, ScalarAttributeType.S))
                .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
        client.createTable(request);
    }

    @Test
    @DisplayName("Teste positivo")
    void testDynamoDB() {
        var request = TransactionRequest.builder()
                .cpf("12345678901")
                .tipo("D")
                .valor(new BigDecimal(100.10))
                .build();
        registraTransicao.registraTransicao(request);
        var transicao = dynamoDBMapper.load(Transicao.class, LocalDate.now().toString(),"12345678901");
        Assert.assertEquals(LocalDate.now().toString(),transicao.getDate());
        Assert.assertEquals("12345678901",transicao.getCpf());
        Assert.assertEquals("D",transicao.getCarteira().getTipo());
        Assert.assertEquals(new BigDecimal("530.53"),transicao.getCarteira().getSaldo());
    }

    @Test
    @DisplayName("Teste negativo")
    void testDynamoDBFail() {
        var request = TransactionRequest.builder()
                .cpf("12345678901")
                .tipo("D")
                .valor(new BigDecimal(100.10))
                .build();
        registraTransicao.registraTransicao(request);
        var transicao = dynamoDBMapper.load(Transicao.class, LocalDate.now().toString(),"12345678901");
        Assert.assertEquals(LocalDate.now().toString(),transicao.getDate());
        Assert.assertEquals("12345678901",transicao.getCpf());
        Assert.assertEquals("D",transicao.getCarteira().getTipo());
        Assert.assertEquals(new BigDecimal("100.10"),transicao.getCarteira().getSaldo());
    }
}
