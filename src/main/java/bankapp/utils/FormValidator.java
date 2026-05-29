package bankapp.utils;

import java.util.Scanner;

public class FormValidator {

    // Scanner estatico unico para toda la aplicacion
    // Asi no hay conflictos de buffers entre clases
    private static final Scanner sc = new Scanner(System.in);

    // ── NUNCA se rompe: captura cualquier entrada invalida ─────────────────
    public static int validateInt(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String line = sc.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Debe ingresar un numero entero. Intente de nuevo.");
            }
        }
    }

    public static double validateDouble(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                String line = sc.nextLine().trim();
                double value = Double.parseDouble(line);
                if (value < 0) {
                    System.out.println("  [!] El valor no puede ser negativo.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Debe ingresar un numero valido (ej: 1000.50). Intente de nuevo.");
            }
        }
    }

    public static String validateString(String prompt) {
        while (true) {
            System.out.println(prompt);
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("  [!] El campo no puede estar vacio.");
        }
    }

    public static boolean validatePassword(String password) {
        if (password == null || password.length() < 8) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[0-9].*")) return false;
        if (!password.matches(".*[@#$%^&+=!*].*")) return false;
        return true;
    }

    // Exponer el scanner para que MenuApp lo use en lecturas simples
    public static String readLine(String prompt) {
        System.out.println(prompt);
        return sc.nextLine().trim();
    }
}
