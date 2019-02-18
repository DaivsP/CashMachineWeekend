package rocks.zipcode.atm.bank;

import rocks.zipcode.atm.ActionResult;

import java.util.*;

/**
 * @author ZipCodeWilmington
 */
public class Bank {
    private List<Integer> accountList;

    private Map<Integer, Account> accounts = new HashMap<>();

    public Bank() {
        accounts.put(1000, new BasicAccount(new AccountData(
                1000, "Example 1", "example1@gmail.com", 500
        )));

        accounts.put(2000, new PremiumAccount(new AccountData(
                2000, "Example 2", "example2@gmail.com", 200
        )));

        accounts.put(3000, new PremiumAccount(new AccountData(
                3000, "Davis", "davis@gmail.com", 1000
        )));

        accounts.put(4000, new BasicAccount(new AccountData(
                4000, "Mike", "mike@gmail.com", -10
        )));
    }

    public ActionResult<AccountData> createAccountList(){
        List<Integer> accountList = new ArrayList<Integer>();
        Set<Integer> keySet = accounts.keySet();
        for (Integer accountNumber : keySet) {
            accountList.add(accountNumber);
        }
        this.accountList = accountList;

        if(accounts != null)
            return ActionResult.success(new AccountData(0,"","",0));
        else
            return ActionResult.fail("No accounts in Bank:");

    }

    public ActionResult<AccountData> getAccountById(int id) {
        Account account = accounts.get(id);

        if (account != null) {
            return ActionResult.success(account.getAccountData());
        } else {
            return ActionResult.fail("No account with id: " + id + "\n " + "Try account 1000, 2000, 3000, or 4000");
        }
    }

    public ActionResult<AccountData> deposit(AccountData accountData, float amount) {
        Account account = accounts.get(accountData.getId());
        account.deposit(amount);

        return ActionResult.success(account.getAccountData());
    }

    public ActionResult<AccountData> withdraw(AccountData accountData, float amount) {
        Account account = accounts.get(accountData.getId());
        boolean ok = account.withdraw(amount);

        if (ok) {
            return ActionResult.success(account.getAccountData());
        } else {
            return ActionResult.fail("Withdraw failed: " + amount + ". Account has: " + account.getBalance());
        }
    }

    public List<Integer> getAccountList() {
        return accountList;
    }
}
