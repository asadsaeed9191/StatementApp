package com.nagarro.statementapp.service;

import com.nagarro.statementapp.dto.request.AccStatementReqDto;
import com.nagarro.statementapp.dto.response.AccStatementRspDto;

public interface IAccountService {

    public AccStatementRspDto getAccountStatement(AccStatementReqDto accStatementReqDto);

}
