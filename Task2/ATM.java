import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ATM {
    static Connection con = null;
    static PreparedStatement stmt = null;
    static ResultSet rs = null;

    // DB -> bank_acc int, pin int, balance int
    public static boolean checkingAcc(int accNumber, int PIN) {
        String sql = "SELECT * FROM atm_accounts WHERE bank_acc = ? AND pin = ?";
        boolean accExists = false;
        try {
            //JDBCドライバのロード
            Class.forName("com.mysql.cj.jdbc.Driver");
            //データベース接続
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo","root","");
            // //SQL実行準備
            stmt = con.prepareStatement(sql);
        	stmt.setInt(1, accNumber);
        	stmt.setInt(2, PIN);
            //実行結果取得
            rs = stmt.executeQuery();
            //データがなくなるまで（rn.next()がfalseになるまで）繰り返す
            while(rs.next()){
                accExists = true;
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードでエラーが発生しました");
        } catch (SQLException e) {
            System.out.println("データベースへのアクセスでエラーが発生しました。");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("データベースへのアクセスでエラーが発生しました。");
            }
        }
        return accExists;
    }
    public static int getUserBalance(int userAcc) {
        String sql = "SELECT balance FROM atm_accounts WHERE bank_acc = ?";
        int userBalance = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo","root","");
            stmt = con.prepareStatement(sql);
        	stmt.setInt(1, userAcc);
            rs = stmt.executeQuery();

            while(rs.next()){
                userBalance = rs.getInt("balance");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードでエラーが発生しました");
        } catch (SQLException e) {
            System.out.println("データベースへのアクセスでエラーが発生しました。");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("データベースへのアクセスでエラーが発生しました。");
            }
        }
        return userBalance;
    }
    public static void withdrawal(int userAcc) {
        String sql = "UPDATE atm_accounts SET balance = ? WHERE bank_acc = ?";
        System.out.println("お引出しの金額を入力してください");
        int amount = new java.util.Scanner(System.in).nextInt();
        int balance = getUserBalance(userAcc) - amount;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo","root","");
            stmt = con.prepareStatement(sql);
        	stmt.setInt(1, balance);
        	stmt.setInt(2, userAcc);
            int rc = stmt.executeUpdate();
            if (rc > 0) {
                System.out.println("お引出に完了しました");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードでエラーが発生しました");
        } catch (SQLException e) {
            System.out.println("データベースへのアクセスでエラーが発生しました。");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("データベースへのアクセスでエラーが発生しました。");
            }
        }
    }
    public static void menuATM(int userAcc) {
        boolean prog = true;

        while (prog == true) {
            System.out.println("メニューから選択してください\n 1．お引出し\n 2．お預入れ\n 3．お振込\n 4．残高照会\n 5．暗証番号変更\n 6．通帳記入\n 7．終了");
            int menuNum = new java.util.Scanner(System.in).nextInt();

            switch (menuNum) {
            case 1: {
                System.out.println("お引出しが行われます");
                withdrawal(userAcc);
                break;
            }
            case 2: {
                System.out.println("お預入れ");
                System.out.println("");
                break;
            }
            case 3: {
                System.out.println("");
                System.out.println("");
                break;
            }
            case 4: {
                System.out.println(getUserBalance(userAcc));
                System.out.println("");
                break;
            }
            case 5:{
                break;
            }
            case 6:
                prog = false;
                break;
            case 7:
                prog = false;
                break;
            default:
                System.out.println("１～６の中で選んでください");
                break;
            }
        }
    }
    public static void startATM() {
        Scanner scan = new Scanner(System.in);
        System.out.println("～ATMへようこそ～");
        int bankAcc = 0; //to 10 char
        int accPIN; //to 4 char
        boolean isLogged = false;
        while (!isLogged) {
            System.out.println("口座確認が行われます \n口座番号とPINを入力してください");
            System.out.print("口座番号：");
            bankAcc = new java.util.Scanner(System.in).nextInt();
            System.out.print("PIN：");
            accPIN = new java.util.Scanner(System.in).nextInt();
            isLogged = checkingAcc(bankAcc, accPIN);
        }
        menuATM(bankAcc);
    }
    public static void main(String[] args) {
        startATM();
    }
}
