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

    // ATM DB -> bank_acc INT, pin INT, balance INT
    // Bankbook DB -> action *引出・預入・振込 amout INT recipient VARCHAR(15)

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
            System.out.println("ATMにエラーが発生しました。");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("ATMにエラーが発生しました。");
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
            System.out.println("ATMにエラーが発生しました。");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("ATMにエラーが発生しました。");
            }
        }
        return userBalance;
    }
    public static void withdrawal(int userAcc) {
        String sql = "UPDATE atm_accounts SET balance = ? WHERE bank_acc = ?";
        System.out.println("お引出しの金額を入力してください");
        int amount = new java.util.Scanner(System.in).nextInt();
        int balance = getUserBalance(userAcc) - amount;
        String action = "引出";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo","root","");
            stmt = con.prepareStatement(sql);
        	stmt.setInt(1, balance);
        	stmt.setInt(2, userAcc);
            int rc = stmt.executeUpdate();
            if (rc > 0) {
                updateBook(action, amount, null);
                System.out.println("お引出しに完了しました");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードでエラーが発生しました");
        } catch (SQLException e) {
            System.out.println("ATMにエラーが発生しました。");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("ATMにエラーが発生しました。");
            }
        }
    }
    public static void deposit(int userAcc) {
        String sql = "UPDATE atm_accounts SET balance = ? WHERE bank_acc = ?";
        System.out.println("お預入れの金額を入力してください");
        int amount = new java.util.Scanner(System.in).nextInt();
        int balance = getUserBalance(userAcc) + amount;
        String action = "預入";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo","root","");
            stmt = con.prepareStatement(sql);
        	stmt.setInt(1, balance);
        	stmt.setInt(2, userAcc);
            int rc = stmt.executeUpdate();
            if (rc > 0) {
                updateBook(action, amount, null);
                System.out.println("お預入れに完了しました");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードでエラーが発生しました");
        } catch (SQLException e) {
            System.out.println("ATMにエラーが発生しました。");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("ATMにエラーが発生しました。");
            }
        }
    }
    public static void transfer(int userAcc) {
        String sql = "UPDATE atm_accounts SET balance = ? WHERE bank_acc = ?";
        System.out.println("お振込の金額を入力してください");
        int amount = new java.util.Scanner(System.in).nextInt();
        int balance = getUserBalance(userAcc) - amount;
        System.out.println("お振込先の口座番号を入力してください");
        Integer payee = new java.util.Scanner(System.in).nextInt();
        String action = "お振込";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo","root","");
            stmt = con.prepareStatement(sql);
        	stmt.setInt(1, balance);
        	stmt.setInt(2, userAcc);
            int rc = stmt.executeUpdate();
            if (rc > 0) {
                updateBook(action, amount, payee);
                System.out.println("お引出に完了しました");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードでエラーが発生しました");
        } catch (SQLException e) {
            System.out.println("ATMにエラーが発生しました。");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("ATMにエラーが発生しました。");
            }
        }
    }
    public static void updatePIN(int userAcc) {
        String sql = "UPDATE atm_accounts SET pin = ? WHERE bank_acc = ?";
        System.out.println("新しい暗証番号を入力してください");
        int newPIN = new java.util.Scanner(System.in).nextInt();

        try {
            // JDBCドライバのロード
            Class.forName("com.mysql.cj.jdbc.Driver");
            // データベース接続
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo", "root", "");
            // SQL実行準備
            stmt = con.prepareStatement(sql);
        	stmt.setInt(1, newPIN);
        	stmt.setInt(2, userAcc);
            // 実行結果取得
        	int rc = stmt.executeUpdate();
            if (rc > 0) {
                System.out.println("変更に成功しました");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードでエラーが発生しました");
        } catch (SQLException e) {
            System.out.println("ATMにエラーが発生しました。");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("ATMにエラーが発生しました。");
            }
        }
    };
    public static void updateBook(String action, int amount, Integer recepient) {
        String sql = "INSERT INTO bankbook(action, amount, recipient) VALUES(?, ?, ?)";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo", "root", "");
            
            stmt = con.prepareStatement(sql);
        	stmt.setString(1, action);
        	stmt.setInt(2, amount);
            stmt.setInt(3, recepient);
            stmt.executeUpdate();
        	// int rc = stmt.executeUpdate();
            // if (rc > 0) {
            // }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードでエラーが発生しました");
        } catch (SQLException e) {
            System.out.println("ATMにエラーが発生しました。");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("ATMにエラーが発生しました。");
            }
        }
    };
    public static void menuATM(int userAcc) {
        boolean prog = true;

        while (prog == true) {
            System.out.println("メニューから選択してください\n 1．お引出し\n 2．お預入れ\n 3．お振込\n 4．残高照会\n 5．暗証番号変更\n 6．通帳記入\n 7．終了");
            int menuNum = new java.util.Scanner(System.in).nextInt();

            switch (menuNum) {
            case 1: {
                if (getUserBalance(userAcc) <= 0) {
                    System.out.println("残高照会は０以下の場合はお引出しができません");
                    break;
                }
                System.out.println("お引出しが行われます");
                withdrawal(userAcc);
                break;
            }
            case 2: {
                System.out.println("お預入れが行われます");
                deposit(userAcc);
                break;
            }
            case 3: {
                if (getUserBalance(userAcc) <= 0) {
                    System.out.println("残高照会は０以下の場合はお振込ができません");
                    break;
                }
                System.out.println("お振込が行われます");
                transfer(userAcc);
                break;
            }
            case 4: {
                System.out.println("残高照会");
                System.out.println(getUserBalance(userAcc));
                break;
            }
            case 5:{
                System.out.println("暗証番号変更が行われます");
                updatePIN(userAcc);
                break;
            }
            case 6:
                break;
            case 7:
                prog = false;
                break;
            default:
                System.out.println("１～７の中で選んでください");
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
            System.out.println("口座確認が行われます \n口座番号と暗証番号を入力してください");
            System.out.print("口座番号：");
            bankAcc = new java.util.Scanner(System.in).nextInt();
            System.out.print("暗証番号：");
            accPIN = new java.util.Scanner(System.in).nextInt();
            isLogged = checkingAcc(bankAcc, accPIN);
        }
        menuATM(bankAcc);
    }
    public static void main(String[] args) {
        startATM();
    }
}