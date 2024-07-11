package KfhTask1.Task1.service;

import KfhTask1.Task1.entity.AccountEntity;
import KfhTask1.Task1.entity.UserEntity;

import java.util.List;

public interface UserAccountService {
    void addUserWithAccount(UserEntity user);
    List<AccountEntity> getActiveAccounts(String username);
    void addAccount(String username, AccountEntity account);
    void deleteAccount(String username, Integer accountNumber);
    void updateAccount(String username, String lastTransactionAmount);
}
