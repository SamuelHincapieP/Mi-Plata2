package bankapp.utils;

import java.util.Scanner;


public class ScannerHolder {
    private static final Scanner INSTANCE = new Scanner(System.in);

    private ScannerHolder() {}

    public static Scanner get() {
        return INSTANCE;
    }
}
