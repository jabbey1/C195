package model;

public class User {

    static private String userName = "admin";  // change later
    static private int userId;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        User.userName = userName;
    }

    public static int getUserId() {
        //FIX ME
        return userId;
        //return 1;
    }

    public static void setUserId(int userId) {
        User.userId = userId;
    }
}
