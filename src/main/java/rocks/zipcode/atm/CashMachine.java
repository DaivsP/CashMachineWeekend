package rocks.zipcode.atm;

import rocks.zipcode.atm.bank.AccountData;
import rocks.zipcode.atm.bank.Bank;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author ZipCodeWilmington
 */
public class CashMachine {

    private final Bank bank;
    private AccountData accountData = null;
    private String myReturnMessage = "Davis";

    public CashMachine(Bank bank) {
        this.bank = bank;
    }

    private Consumer<AccountData> update = data -> {
        accountData = data;
    };

    public void accounts (){
        tryCall(
                ()->bank.createAccountList(),
                update
        );
        myReturnMessage = bank.getAccountList().toString();
    }

    public void login(int id) {
        tryCall(
                () -> bank.getAccountById(id),
                update
        );
        myReturnMessage = accountData != null ? accountData.toString() : "Try account 1000, 2000, 3000, or 4000 and click Set Account ID. Modified by Davis";
    }

    public void deposit(float amount) {
        if (accountData != null) {
            tryCall(
                    () -> bank.deposit(accountData, amount),
                    update
            );
        }
        myReturnMessage = accountData != null ? "Your Deposit is successful" + "\n" + accountData.toString() : "Deposit Error";
    }

    public void withdraw(float amount) {
        if (accountData != null) {
            tryCall(
                    () -> bank.withdraw(accountData, amount),
                    update
            );
        }
        String overDraftMessage = "";
        if (accountData.getBalance() < 0){
            overDraftMessage = "Your Withdraw was successful" + "\n" + "Your account is overdrawn" + "\n";
        }
        else{
            overDraftMessage = "Your Withdraw was successful" + "\n";
        }
        myReturnMessage = accountData != null ? overDraftMessage +
                accountData.toString() : "Withdraw Error";
    }

    public void exit() {
        if (accountData != null) {
            accountData = null;
        }
        myReturnMessage = "Successfully exited Account" + "\n" +
                "Please log into another Account";
    }

    @Override
    public String toString() {
        return myReturnMessage;
    }

    private <T> void tryCall(Supplier<ActionResult<T> > action, Consumer<T> postAction) {
        try {
            ActionResult<T> result = action.get();
            if (result.isSuccess()) {
                T data = result.getData();
                postAction.accept(data);
            } else {
                String errorMessage = result.getErrorMessage();
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
