package KfhTask1.Task1.bo;

import java.util.Date;

public class UserAccount {

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getLastTransactionAmount() {
        return lastTransactionAmount;
    }

    public void setLastTransactionAmount(String lastTransactionAmount) {
        this.lastTransactionAmount = lastTransactionAmount;
    }

    public Date getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(Date lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private Long userid;
    private Integer accountNumber;
    private Double balance;
    private String  lastTransactionAmount ;
    private Date lastTransactionDate ;
    private String status ;

}


