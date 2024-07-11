package KfhTask1.Task1.controllers;

import KfhTask1.Task1.entity.AccountEntity;
import KfhTask1.Task1.entity.UserEntity;
import KfhTask1.Task1.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @PostMapping("/addUser")
    public void addUser(@RequestBody UserEntity user) {
        userAccountService.addUserWithAccount(user);
    }

    @GetMapping("/userAccounts")
    public List<AccountEntity> getUserAccounts(@RequestParam String username, HttpSession session) {
        session.setAttribute("username", username);
        return userAccountService.getActiveAccounts(username);
    }

    @PostMapping("/addAccount")
    public void addAccount(@RequestBody AccountEntity account, HttpSession session) {
        String username = (String) session.getAttribute("username");
        userAccountService.addAccount(username, account);
    }

    @DeleteMapping("/deleteAccount")
    public void deleteAccount(@RequestParam Integer accountNumber, HttpSession session) {
        String username = (String) session.getAttribute("username");
        userAccountService.deleteAccount(username, accountNumber);
    }

    @PutMapping("/updateAccount")
    public void updateAccount(@RequestParam String lastTransactionAmount, HttpSession session) {
        String username = (String) session.getAttribute("username");
        userAccountService.updateAccount(username, lastTransactionAmount);
    }
}
