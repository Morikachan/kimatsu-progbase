import java.util.Scanner;

public class ATM {
    public static void startATM() {
        Scanner scan = new Scanner(System.in);
        System.out.println("～ATMへようこそ～");
        System.out.println("口座確認行われます \n　口座番号とPINを入力してください");
        int bankAcc = scan.nextInt();
        String accPIN = scan.nextLine();
    }
    public static void main(String[] args) {
        
    }
}
