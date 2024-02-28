package com.suleware.microservices.loans.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.suleware.microservices.loans.constants.LoansConstants;
import com.suleware.microservices.loans.dto.LoansDto;
import com.suleware.microservices.loans.entity.Loans;
import com.suleware.microservices.loans.exception.LoanAlreadyExistsException;
import com.suleware.microservices.loans.exception.ResourceNotFoundException;
import com.suleware.microservices.loans.mapper.LoansMapper;
import com.suleware.microservices.loans.repository.LoansRepository;
import com.suleware.microservices.loans.service.ILoanService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements ILoanService {

    private LoansRepository repository;

    @Override
    public List<LoansDto> findAll() {
        List<Loans> loans = repository.findAll();

        return loans.stream().map(loan -> LoansMapper.mapToLoanDto(loan, new LoansDto())).toList();
    }

    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> oLoan = repository.findByMobileNumber(mobileNumber);
        if (oLoan.isPresent()) {
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber " + mobileNumber);
        }
        repository.save(createNewLoan(mobileNumber));

    }

    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Optional<Loans> oLoan = repository.findByMobileNumber(mobileNumber);
        if (!oLoan.isPresent()) {
            throw new ResourceNotFoundException("fetch", "mobileNumber", mobileNumber);
        }

        return LoansMapper.mapToLoanDto(oLoan.get(), new LoansDto());

    }

    @Override
    public boolean updateLoan(LoansDto loanDto) {
        Optional<Loans> oLoan = repository.findByLoanNumber(loanDto.getLoanNumber());
        if (!oLoan.isPresent()) {
            throw new ResourceNotFoundException("update", "mobileNumber", loanDto.getMobileNumber());
        }

        Loans updatedLoan = LoansMapper.mapToLoan(loanDto, oLoan.get());
        repository.save(updatedLoan);
        return true;
    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        Optional<Loans> oLoan = repository.findByMobileNumber(mobileNumber);
        if (!oLoan.isPresent()) {
            throw new ResourceNotFoundException("update", "mobileNumber", mobileNumber);
        }
        repository.deleteById(oLoan.get().getLoanId());
        return true;
    }

    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }

}
