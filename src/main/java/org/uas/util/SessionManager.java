package org.uas.util;

import org.uas.data.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class SessionManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String SESSION_FILE = "session.ser";
    private static SessionManager instance;
    private transient String currentUser;
    private boolean isLoggedIn = false;

    private SessionManager() {
        loadSession();
    }

    // Static method to get the singleton instance
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Method to check if the session file doesn't exist
    public void createSessionFile() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void loadSession() {
        if (Files.exists(Paths.get(SESSION_FILE))) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SESSION_FILE))) {
                Object obj = ois.readObject();
                if (obj instanceof String) {
                    String username = (String) obj;
                    this.isLoggedIn = true;
                    this.currentUser = username;
                } else {
                    Files.delete(Paths.get(SESSION_FILE));
                    this.isLoggedIn = false;
                    this.currentUser = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Files.delete(Paths.get(SESSION_FILE));
                } catch (IOException ignored) {}
                this.isLoggedIn = false;
                this.currentUser = null;
            }
        }
    }

    private void saveSession(String username) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
            oos.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    // Method to check if user is logged in
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    // Method to simulate login
    public void login() {
        this.isLoggedIn = true;
        this.currentUser = "admin"; // Hardcoded for test simplicity
        saveSession(this.currentUser);

    }

    // Method to simulate logout
    public void logout() {
        this.isLoggedIn = false;
        this.currentUser = null;
        try {
            Files.deleteIfExists(Paths.get(SESSION_FILE));
        } catch (Exception ignored) {}
    }
}
