package com.baktas.issuerbanking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ErrorModel implements Serializable {
    private static final long serialVersionUID = 8943679894087198640L;

    private String code;
    private String reason;
    private String message;
}