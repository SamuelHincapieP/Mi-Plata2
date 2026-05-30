package bankapp;

import bankapp.Config.Config;
import bankapp.userinterface.MenuApp;

public class Main {

    public static void main(String[] args) {

        MenuApp menuApp = Config.createMenuApp();
        menuApp.showMainMenu();
    }
}
