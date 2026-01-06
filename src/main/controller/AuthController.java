package main.controller;

public class AuthController {

    public static main.model.User login(String username, String password) {
        try (java.sql.Connection conn = main.util.DatabaseUtil.getConnection()) {
            if (conn == null)
                return null;

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new main.model.User(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("role"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
