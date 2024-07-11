package KfhTask1.Task1.repository;

import KfhTask1.Task1.entity.AccountEntity;
import KfhTask1.Task1.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findByUserAndStatusOrderByLastTransactionDateDesc(UserEntity user, String status);
    AccountEntity findByUserAndAccountNumber(UserEntity user, Integer accountNumber);
    AccountEntity findTopByUserOrderByLastTransactionDateDesc(UserEntity user);
}
