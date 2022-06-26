package com.nagarro.statementapp.dataobjects;

import lombok.Data;

@Data
public class Statement {


    private Long id;
    private String accountNo;
    private String dateField;
    private String amount;

    public Statement(Long id, String accountNo, String dateField, String amount) {
        this.id = id;
        this.accountNo = accountNo;
        this.dateField = dateField;
        this.amount = amount;
    }

}
