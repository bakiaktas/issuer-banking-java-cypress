package com.baktas.issuerbanking.repository.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "ACCOUNT", schema = "XYZ")
public class Account implements Serializable {
    private static final long serialVersionUID = -5457872707966266914L;

    @Id
    @Column(name = "id")
    private Long id;

    @PositiveOrZero
    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance;
}