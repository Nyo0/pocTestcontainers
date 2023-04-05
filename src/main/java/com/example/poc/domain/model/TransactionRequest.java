package com.example.poc.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransactionRequest {

    @NotBlank(message = "O campo CPF não deve ser Nulo.")
    @JsonProperty("cpf")
    private String cpf;

    @NotNull(message = "O campo Valor não deve ser Nulo.")
    @PositiveOrZero(message = "O campo Valor não pode ser negativo")
    @JsonProperty("valor")
    private BigDecimal valor;

    @NotBlank(message = "O campo tipo não deve ser Nulo.")
    @JsonProperty("tipo")
    @Pattern(regexp = "^[RD]+$",message = "Tipo invalido, só deve ser aceito R para real e D para dolar.")
    @Size(min = 1,max = 1,message = "O campo tipo deve conter exatamente 1 char")
    private String tipo;
}