import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class GameData {
    static HashMap<Integer, String> DB_PARAMS = new HashMap<>();
    static {
        DB_PARAMS.put(1, "title");
        DB_PARAMS.put(2, "publisher");
        DB_PARAMS.put(3, "publ_year");
        DB_PARAMS.put(4, "genre");
    };

    public static void insertGame(Connection con, PreparedStatement stmt, String id, String title, String publisher, String publ_year, String genre) {
        String sql = "INSERT INTO game_data(id, title, publisher, publ_year, genre) VALUES(?, ?, ?, ?, ?)";
        // id -> PRIMARY KEY
        try {
            // JDBCドライバのロード
            Class.forName("com.mysql.cj.jdbc.Driver");
            // データベース接続
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo", "root", "");
            // SQL実行準備
            stmt = con.prepareStatement(sql);
        	stmt.setString(1, id);
        	stmt.setString(2, title);
            stmt.setString(3, publisher);
        	stmt.setString(4, publ_year); // 2024年1月...
        	stmt.setString(5, genre);
            // 実行結果取得
        	int rc = stmt.executeUpdate();
            if (rc > 0) {
                System.out.println("ゲーム追加に成功しました");
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
    };
    public static void updateGameData(Connection con, PreparedStatement stmt, String id, HashMap<Integer, String> newData) {
        String sql = "UPDATE game_data SET ";
        Set<Integer> keysArray = newData.keySet();
        List<Integer> keysList = new ArrayList<>(keysArray);
        System.out.println("size = " + newData.size());
        newData.forEach((k, v) -> System.out.println(k + ':' + v));
        switch (newData.size()) {
            case 1:
                sql += DB_PARAMS.get(keysList.get(0)) + " = ? WHERE id = ?";
                break;
            case 2:
                sql += DB_PARAMS.get(keysList.get(0)) + " = ? , " + DB_PARAMS.get(keysList.get(1)) + " = ? WHERE id = ?";
                break;
            case 3:
                sql += DB_PARAMS.get(keysList.get(0)) + " = ? , " + DB_PARAMS.get(keysList.get(1)) + " = ? , " + DB_PARAMS.get(keysList.get(2)) + " = ? WHERE id = ?";
                break;
            case 4:
                sql += DB_PARAMS.get(keysList.get(0)) + " = ? , " + DB_PARAMS.get(keysList.get(1)) + " = ? , " + DB_PARAMS.get(keysList.get(2)) + " = ? , " + DB_PARAMS.get(keysList.get(3)) + " = ? WHERE id = ?";
                break;
            default:
                break;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo", "root", "");
            stmt = con.prepareStatement(sql);
            switch (newData.size()) {
                case 1:
                    stmt.setString(1, newData.get(keysList.get(0)));
                    stmt.setString(2, id);
                    break;
                case 2:
                    stmt.setString(1, newData.get(keysList.get(0)));
                    stmt.setString(2, newData.get(keysList.get(1)));
                    stmt.setString(3, id);
                    break;
                case 3:
                    stmt.setString(1, newData.get(keysList.get(0)));
                    stmt.setString(2, newData.get(keysList.get(1)));
                    stmt.setString(3, newData.get(keysList.get(2)));
                    stmt.setString(4, id);
                    break;
                case 4:
                    stmt.setString(1, newData.get(keysList.get(0)));
                    stmt.setString(2, newData.get(keysList.get(1)));
                    stmt.setString(3, newData.get(keysList.get(2)));
                    stmt.setString(4, newData.get(keysList.get(3)));
                    stmt.setString(5, id);
                    break;
                default:
                    break;
            }
        	int rc = stmt.executeUpdate();
            if (rc > 0) {
                System.out.println("更新に成功しました！");
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
    };
    public static void deleteGame(Connection con, PreparedStatement stmt, String id){
        String sql = "DELETE FROM game_data WHERE id = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo", "root", "");
            stmt = con.prepareStatement(sql);
        	stmt.setString(1, id);
        	int rc = stmt.executeUpdate();
            
            if (rc > 0) {
                System.out.println("ゲーム削除に成功しました");
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
    };
    public static void showGameRecord(Connection con, PreparedStatement stmt, ResultSet rs, String param, String value) {    
        String sql = "SELECT * FROM game_data WHERE " + param + " = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo","root","");
            stmt = con.prepareStatement(sql);
            stmt.setString(1, value);
            rs = stmt.executeQuery();

            while(rs.next()){
                String id = rs.getString("id");
                String title = rs.getString("title");
                String publisher = rs.getString("publisher");
                String publ_year = rs.getString("publ_year");
                String genre = rs.getString("genre");
                System.out.println(id + " | " + title + " | " + publisher  + " | " + publ_year  + " | " + genre);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードでエラーが発生しました。");
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
    };
    public static void showAllGames(Connection con, PreparedStatement stmt, ResultSet rs) {    
        String sql = "SELECT * FROM game_data";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo","root","");
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                String id = rs.getString("id");
                String title = rs.getString("title");
                String publisher = rs.getString("publisher");
                String publ_year = rs.getString("publ_year");
                String genre = rs.getString("genre");
                System.out.println(id + " | " + title + " | " + publisher  + " | " + publ_year  + " | " + genre);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードでエラーが発生しました。");
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
    };
    public static HashMap<Integer, String> getGamesId(Connection con, PreparedStatement stmt, ResultSet rs) {    
        String sql = "SELECT id FROM game_data";
        HashMap<Integer, String> ID_LIST = new HashMap<Integer, String>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo","root","");
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            for(int i = 1; rs.next(); i++) {
                String id = rs.getString("id");
                ID_LIST.put(i, id);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードでエラーが発生しました。");
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
        return ID_LIST;
    };
    public static void createRecord(Connection con, PreparedStatement stmt) {
        String uniqueID = UUID.randomUUID().toString();
        System.out.println(uniqueID);
        String id = uniqueID.toString();
        System.out.println("ゲームタイトルを入力してください");
        String title = new java.util.Scanner(System.in, "Shift-JIS").nextLine();
        System.out.println("出版社を入力してください");
        String publisher = new java.util.Scanner(System.in, "Shift-JIS").nextLine();
        System.out.println("出版年を入力してください");
        String publ_year = new java.util.Scanner(System.in).nextLine();
        System.out.println("ゲームジャンルを入力してください");
        String genre = new java.util.Scanner(System.in, "Shift-JIS").nextLine();
        insertGame(con, stmt, id, title, publisher, publ_year, genre);
    }
    public static void updateRecord(Connection con, PreparedStatement stmt, ResultSet rs) {
        System.out.println("更新したいカラムを入力してください");
        System.out.println("(1つ以上更新したい場合はスペースを開けてください)");
        String[] showParams = new String[]{"ゲームタイトル", "ゲーム出版社", "ゲーム出版年", "ゲームジャンル"};
        DB_PARAMS.forEach( (index, param) -> System.out.println(index + ": "+ showParams[index-1] + "\t"));
        HashMap<Integer, String> UPDATE_PARAMS = new HashMap<>();
        String userInput = new java.util.Scanner(System.in).nextLine();
            String[] inputSplit = userInput.split(" ");

            int[] userParams = new int[inputSplit.length];
                for (int i = 0; i < inputSplit.length; i++) {
                    userParams[i] = Integer.valueOf(inputSplit[i]);
                }
        System.out.println("新しい値を入力してください");
        for(int param:userParams){
            System.out.println(param + ": ");
            String newDataString = new java.util.Scanner(System.in, "Shift-JIS").nextLine();
            UPDATE_PARAMS.put(param, newDataString);
        }

        HashMap<Integer,String> gameIdList = getGamesId(con, stmt, rs);
        gameIdList.forEach( (index, id) -> System.out.println(index + ": "+ id + "\t"));

        System.out.println("更新したいゲームレコードの番号を入力してください");
        int gameIdIndex = new java.util.Scanner(System.in).nextInt();

        updateGameData(con, stmt, gameIdList.get(gameIdIndex), UPDATE_PARAMS);
    }
    public static void menu() {
        Scanner scan = new Scanner(System.in);
        //変数の準備
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int menuNum;
        boolean prog = true;

        while (prog == true) {
            System.out.println("メニューを選択してください\n 1．ゲーム追加\n 2．ゲーム更新\n 3．ゲーム削除\n 4．ゲーム情報の検索\n 5．全ゲームのレコード表示\n 6．終了");
            menuNum = scan.nextInt();

            switch (menuNum) {
            case 1: {
                System.out.println("ゲームレコード追加を行います");
                createRecord(con, stmt);
                break;
            }
            case 2: {
                System.out.println("ゲームレコード更新を行います");
                updateRecord(con, stmt, rs);
                break;
            }
            case 3: {
                System.out.println("ゲームレコード削除を行います");
                showAllGames(con, stmt, rs);
                HashMap<Integer,String> gameIdList = getGamesId(con, stmt, rs);
                gameIdList.forEach( (index, id) -> System.out.println(index + ": "+ id + "\t"));
                System.out.println("削除したいゲームレコードの番号を選択してください");
                int toDeleteId = new java.util.Scanner(System.in).nextInt();
                deleteGame(con, stmt, gameIdList.get(toDeleteId));
                break;
            }
            case 4: {
                System.out.println("どんなカラムで検索をするのか入力してください");
                System.out.println("1．ゲームタイトル\n 2．ゲーム出版社\n 3．ゲーム出版年\n 4．ゲームジャンル\n");
                int userNum = new java.util.Scanner(System.in).nextInt();
                String param = DB_PARAMS.get(userNum);
                System.out.println("検索される値を入力してください");
                String searchValue = new java.util.Scanner(System.in).nextLine();
                showGameRecord(con, stmt, rs, param, searchValue);
                break;
            }
            case 5:{
                showAllGames(con, stmt, rs);
                break;
            }
            case 6:
                prog = false;
                break;
            default:
                System.out.println("１～６の中で選んでください");
                break;
            }
        }
    }
    public static void main(String[] args) {
        menu();
    }
}