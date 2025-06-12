package org.uas.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SessionManager implements Serializable {
    private static final String SESSION_FILE = "session.ser";

    private static SessionManager instance;
    private boolean isLoggedIn = false;

    // Static method to get the singleton instance
    public static SessionManager getInstance() {
        return new SessionManager();
    }

    // Method to check if the session file doesn't exist
    public void createSessionFile() {

    }

    private void loadSession() {

    }

    private void saveSession() {

    }

    // Method to check if user is logged in
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    // Method to simulate login
    public void login() {

    }

    // Method to simulate logout
    public void logout() {

    }
}
