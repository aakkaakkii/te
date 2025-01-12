package org.example.testruleengine.ruleImpl;

import java.util.ArrayList;

public class LoanDetails {
    Long accountNumber;
    Boolean approvalStatus;
    Float interestRate;
    Float sanctionedPercentage;
    Double processingFees;
    ArrayList<String> data = new ArrayList<>();

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Boolean getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Float interestRate) {
        this.interestRate = interestRate;
    }

    public Float getSanctionedPercentage() {
        return sanctionedPercentage;
    }

    public void setSanctionedPercentage(Float sanctionedPercentage) {
        this.sanctionedPercentage = sanctionedPercentage;
    }

    public Double getProcessingFees() {
        return processingFees;
    }

    public void setProcessingFees(Double processingFees) {
        this.processingFees = processingFees;
    }

    public void addData(String v){
        data.add(v +" suffix");
    }

    public ArrayList<String> getData() {
        return data;
    }
}
