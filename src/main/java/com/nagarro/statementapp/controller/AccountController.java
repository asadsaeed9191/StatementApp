package com.nagarro.statementapp.controller;

import com.nagarro.statementapp.dto.request.AccStatementReqDto;
import com.nagarro.statementapp.dto.response.AccStatementRspDto;
import com.nagarro.statementapp.security.UserDetailsImpl;
import com.nagarro.statementapp.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IAccountService accService;

    @RequestMapping(path = "/statements", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AccStatementRspDto getAccountStatement(@AuthenticationPrincipal UserDetailsImpl user,
                                                  @RequestBody(required = false) AccStatementReqDto accStatementReqDto) {
        AccStatementRspDto accStatementRspDto = new AccStatementRspDto();
        //if the current user is Admin then get the account statements with the req object
        if (user.getUsername().equalsIgnoreCase("admin")) {
            accStatementRspDto = accService.getAccountStatement(accStatementReqDto);
        } else {
            //This block of code will be executed if logged in user not equals to Admin
            //if user has sent some req params then send error back
            if (isRequestParamNotNull(accStatementReqDto)) {
                accStatementRspDto.setErrCode(401);
                accStatementRspDto.setErrMsg("Unauthorized! (401)");
            } else {
                //if there are no params in the request then send previous 3 months statement of this account to user
                accStatementRspDto = accService.getAccountStatement(accStatementReqDto);
            }
        }
        return accStatementRspDto;
    }

    private boolean isRequestParamNotNull(AccStatementReqDto accStatementReqDto) {
        return null != accStatementReqDto.getAccountID() || null != accStatementReqDto.getFromAmount() ||
                null != accStatementReqDto.getToAmount() || null != accStatementReqDto.getToDate() || null != accStatementReqDto.getFromDate();
    }
}
