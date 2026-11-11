import java.util.InputMismatchException;
import java.util.Scanner;

public class Bank {
     static class InsufficientFundsException extends Exception {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

    static class BankAccount {
        private final String accNo;
        private final String holderName;
        private double balance;

        public BankAccount(String accNo, String holderName, double initialBalance) {
            if(initialBalance < 0) {
                throw new IllegalArgumentException("initial balance cannot be negative");
            }
            this.accNo = accNo;
            this.holderName = holderName;
            this.balance = initialBalance;
        }

        public void deposit(double amount) throws InsufficientFundsException {
            if(amount < 0) {
                throw new IllegalArgumentException("amount cannot be negative");
            }
            this.balance += amount;
            System.out.printf("deposited: %.2f%n ", amount);
        }

        public void withdraw(double amount) throws InsufficientFundsException {
            if(amount <= 0) {
                throw new IllegalArgumentException("withdrawal amount cannot be negative and greater than zero");
            }
            if(amount > balance) {
                throw new InsufficientFundsException(
                        String.format("Insufficient funds! Balance: %.2f, requested: %.2f", balance, amount));
            }
            balance -= amount;
            System.out.printf("withdrawn: %.2f%n ", amount);
        }

        public double getBalance() {
            return balance;
        }

        public String getAccNo() {
            return accNo;
        }

        public String getHolderName() {
            return holderName;
        }

    }
    void main() throws InsufficientFundsException {
        Scanner input = new Scanner(System.in);
        System.out.println("-------------Bank------------");
        System.out.print("Enter account holder name: ");
        String name = input.nextLine().trim();
        if(name.isEmpty()) {
            name = "Customer";
        }

        BankAccount bk = new BankAccount("ACC001", name, 0.0);
        System.out.println("Account: " + bk.getAccNo() + ", created for: " + bk.getHolderName());

        boolean running = true;
        while(running) {
            System.out.println("---------Menu--------");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Exit");
            System.out.print("Please enter your choice: ");

            try{
                int choice = input.nextInt();

                switch(choice) {
                    case 1:
                        System.out.print("Enter amount to deposit: ");
                        bk.deposit(input.nextDouble());
                        break;
                    case 2:
                        System.out.print("Enter amount to withdraw: ");
                        bk.withdraw(input.nextDouble());
                        break;
                    case 3:
                        System.out.printf("Current Balance: %.2f%n", bk.getBalance());
                        break;
                    case 4:
                        System.exit(0);
                        running = false;
                        break;
                        default:
                            System.out.println("Invalid choice, Choose between 1 - 4");
                }
            } catch (InsufficientFundsException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter valid number");
                input.nextLine();
            }
        }
    }
}
