package KfhTask1.Task1.service;

import KfhTask1.Task1.entity.AccountEntity;
import KfhTask1.Task1.entity.UserEntity;
import KfhTask1.Task1.repository.AccountRepository;
import KfhTask1.Task1.repository.UserRepository;
import KfhTask1.Task1.util.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountServiceImp implements UserAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void addUserWithAccount(UserEntity user) {
        if (user.getAccounts() == null || user.getAccounts().isEmpty()) {
            throw new IllegalArgumentException("User must have at least one account");
        }
        userRepository.save(user);
    }

    @Override
    @Cacheable(value = "activeAccounts", key = "#username")
    public List<AccountEntity> getActiveAccounts(String username) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return accountRepository.findByUserAndStatusOrderByLastTransactionDateDesc(user, "ACTIVE");
    }

    @Override
    public void addAccount(String username, AccountEntity account) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        account.setUser(user);
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(String username, Integer accountNumber) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        AccountEntity account = accountRepository.findByUserAndAccountNumber(user, accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        accountRepository.delete(account);
    }

    @Override
    public void updateAccount(String username, String lastTransactionAmount) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        AccountEntity account = accountRepository.findTopByUserOrderByLastTransactionDateDesc(user);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        account.setLastTransactionAmount(lastTransactionAmount);
        account.setLastTransactionDate(new Date());
        accountRepository.save(account);
    }
}
