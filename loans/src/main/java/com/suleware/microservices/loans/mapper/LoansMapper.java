package com.suleware.microservices.loans.mapper;

import com.suleware.microservices.loans.dto.LoansDto;
import com.suleware.microservices.loans.entity.Loans;

public class LoansMapper {
    public static LoansDto mapToLoanDto(Loans loan, LoansDto LoanDto) {
        LoanDto.setLoanNumber(loan.getLoanNumber());
        LoanDto.setLoanType(loan.getLoanType());
        LoanDto.setMobileNumber(loan.getMobileNumber());
        LoanDto.setTotalLoan(loan.getTotalLoan());
        LoanDto.setAmountPaid(loan.getAmountPaid());
        LoanDto.setOutstandingAmount(loan.getOutstandingAmount());
        return LoanDto;
    }

    public static Loans mapToLoan(LoansDto LoanDto, Loans Loan) {
        Loan.setLoanNumber(LoanDto.getLoanNumber());
        Loan.setLoanType(LoanDto.getLoanType());
        Loan.setMobileNumber(LoanDto.getMobileNumber());
        Loan.setTotalLoan(LoanDto.getTotalLoan());
        Loan.setAmountPaid(LoanDto.getAmountPaid());
        Loan.setOutstandingAmount(LoanDto.getOutstandingAmount());
        return Loan;
    }
}
