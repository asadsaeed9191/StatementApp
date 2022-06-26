package com.nagarro.statementapp.service.implementation;

import com.nagarro.statementapp.dao.AccountDao;
import com.nagarro.statementapp.dataobjects.Account;
import com.nagarro.statementapp.dataobjects.Statement;
import com.nagarro.statementapp.dto.request.AccStatementReqDto;
import com.nagarro.statementapp.dto.response.AccStatementRspDto;
import com.nagarro.statementapp.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    AccountDao accountDao;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d-M-yyyy");

    @Override
    public AccStatementRspDto getAccountStatement(AccStatementReqDto accStatementReqDto) {

        AccStatementRspDto accStatementRspDto = new AccStatementRspDto();
        try {
            if (null != accStatementReqDto.getAccountID()) {
                if (isDateRangePresent(accStatementReqDto)) {
                    if (isDateRangeValid(accStatementReqDto)) {
                        Account accountDetails = accountDao.getAccountDetails(accStatementReqDto.getAccountID());
                        if (null != accountDetails.getId()) {
                            List<Statement> accountStatementList = accountDao.getAccountStatement(accountDetails.getId());
                            if (isAmountRangePresent(accStatementReqDto)) {
                                accountStatementList.stream().
                                        filter(item -> LocalDate.parse(item.getDateField().replace(".", "-"), dateFormatter).
                                                isAfter(LocalDate.parse(accStatementReqDto.getFromDate(), dateFormatter)) &&
                                                LocalDate.parse(item.getDateField().replace(".", "-"), dateFormatter).
                                                        isBefore(LocalDate.parse(accStatementReqDto.getToDate(), dateFormatter)) &&
                                                (Double.parseDouble(item.getAmount()) >= accStatementReqDto.getFromAmount()
                                                        && (Double.parseDouble(item.getAmount()) <= accStatementReqDto.getToAmount()))).collect(Collectors.toList());

                            } else {
                                accountStatementList = accountStatementList.stream().
                                        filter(item -> LocalDate.parse(item.getDateField().replace(".", "-"), dateFormatter).isAfter(LocalDate.parse(accStatementReqDto.getFromDate(), dateFormatter))
                                                && LocalDate.parse(item.getDateField().replace(".", "-"), dateFormatter).isBefore(LocalDate.parse(accStatementReqDto.getToDate(), dateFormatter))).
                                        collect(Collectors.toList());
                            }
                            accStatementRspDto.setStatements(accountStatementList);
                        } else {
                            accStatementRspDto.setErrMsg("Bad Request, please check the given account ID");
                            accStatementRspDto.setErrCode(400);
                        }

                    } else {
                        accStatementRspDto.setErrMsg("Bad Request, please check the given date range");
                        accStatementRspDto.setErrCode(400);
                    }


                } else {

                    Account accountDetails = accountDao.getAccountDetails(accStatementReqDto.getAccountID());
                    if (null != accountDetails.getId()) {
                        List<Statement> accountStatementList = accountDao.getAccountStatement(accountDetails.getId());
                        if (isAmountRangePresent(accStatementReqDto)) {
                            accountStatementList = accountStatementList.stream().filter(item -> Double.parseDouble(item.getAmount()) >= accStatementReqDto.getFromAmount()
                                    && (Double.parseDouble(item.getAmount()) <= accStatementReqDto.getToAmount())).collect(Collectors.toList());
                        }
                        accStatementRspDto.setStatements(accountStatementList);
                    }

                }
            } else {
                List<Statement> accountStatementList = accountDao.getAccountStatement();
                accountStatementList = accountStatementList.stream().filter(item -> LocalDate.parse(item.getDateField().replace(".", "-"), dateFormatter).
                        isAfter(LocalDate.now().minusMonths(3))
                        && LocalDate.parse(item.getDateField().replace(".", "-"), dateFormatter).
                        isBefore(LocalDate.now())).collect(Collectors.toList());
                accStatementRspDto.setStatements(accountStatementList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            accStatementRspDto.setErrCode(500);
            accStatementRspDto.setErrMsg("Error while processing request => " + e.getMessage());
        }
        if (accStatementRspDto.getStatements().size() > 0) {
            accStatementRspDto.getStatements().stream().forEach(item -> item.setAccountNo(getMd5(item.getAccountNo())));
        }
        return accStatementRspDto;
    }

    private boolean isAmountRangePresent(AccStatementReqDto accStatementReqDto) {
        return null != accStatementReqDto.getFromAmount() && null != accStatementReqDto.getToAmount();
    }

    private boolean isDateRangeValid(AccStatementReqDto accStatementReqDto) {
        return isValidDate(accStatementReqDto.getFromDate()) && isValidDate(accStatementReqDto.getToDate());
    }

    private boolean isDateRangePresent(AccStatementReqDto accStatementReqDto) {
        return null != accStatementReqDto.getFromDate() && null != accStatementReqDto.getToDate();
    }

    private boolean isValidDate(String date) {
        boolean isDateValid = false;
        if (date != null && !date.equalsIgnoreCase("")) {
            try {
                // The following will take under consideration the days of the month and leap year.
                LocalDate.parse(date, DateTimeFormatter.ofPattern("d-M-uuuu").withResolverStyle(ResolverStyle.STRICT));
                isDateValid = true;
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                isDateValid = false;
            }
        }
        return isDateValid;
    }

    public String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
