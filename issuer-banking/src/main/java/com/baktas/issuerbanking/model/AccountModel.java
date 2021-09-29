package com.baktas.issuerbanking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AccountModel implements Serializable {
    private static final long serialVersionUID = -7302135987542713981L;

    private Long id;
    private BigDecimal balance;
}