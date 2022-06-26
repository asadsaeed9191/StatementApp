package com.nagarro.statementapp.dto.request;

import lombok.Data;

@Data
public class AccStatementReqDto {

    private Long accountID;
    private Double fromAmount;
    private Double toAmount;
    private String toDate;
    private String fromDate;

}
