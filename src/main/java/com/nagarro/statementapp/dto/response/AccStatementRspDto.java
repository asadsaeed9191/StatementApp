package com.nagarro.statementapp.dto.response;

import com.nagarro.statementapp.dataobjects.Statement;
import lombok.Data;

import java.util.List;

@Data
public class AccStatementRspDto {

    private List<Statement> statements;
    private int errCode;
    private String errMsg;
}
