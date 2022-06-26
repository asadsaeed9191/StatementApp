package com.nagarro.statementapp.dataobjects;

import lombok.Data;

@Data
public class Account {
    private Long id;
    private String accType;
    private String accNo;

    public Account(Long id, String accType, String accNo) {
        this.id = id;
        this.accType = accType;
        this.accNo = accNo;
    }
}
