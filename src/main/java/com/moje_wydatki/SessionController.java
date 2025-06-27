package com.moje_wydatki;


public class SessionController {

    private static SessionController instance;
    public int userId;

    public SessionController() {
        // Prywatny konstruktor, aby zapobiec tworzeniu nowych instancji
    }

    public static SessionController getInstance() {
        if (instance == null) {
            instance = new SessionController();
        }
        return instance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

}
