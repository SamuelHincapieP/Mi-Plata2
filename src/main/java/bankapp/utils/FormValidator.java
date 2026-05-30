package bankapp.utils;

import bankapp.domain.Client;

import java.util.InputMismatchException;
import java.util.Scanner;

public class FormValidator {

    // ── Un solo Scanner compartido ─────────────────────────────────────────
    static Scanner sc = ScannerHolder.get();

    public static void validateId(Client client) {
        while (true) {
            try {
                System.out.println("Ingrese el id del cliente");
                int id = sc.nextInt();
                sc.nextLine();
                client.setId(id);
                return;
            } catch (InputMismatchException e) {
                System.out.println("Error al ingresar el id del cliente, por favor ingrese un numero entero");
                sc.nextLine();
            }
        }
    }

    public static int validateInt(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                int value = sc.nextInt();
                sc.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Error al ingresar el valor, este debe ser un numero entero");
                sc.nextLine();
            }
        }
    }

    public static double validateDouble(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                double value = sc.nextDouble();
                sc.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Error al ingresar el valor, este debe ser un numero decimal");
                sc.nextLine();
            }
        }
    }

    public static boolean validateBoolean(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                boolean value = sc.nextBoolean();
                sc.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Error al ingresar el valor, este debe ser un booleano (true/false)");
                sc.nextLine();
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
            System.out.println("Error al ingresar el valor, el campo no debe estar vacio");
        }
    }

    public static boolean validateClientForm(String name, String email, String password) {
        if (name == null || name.trim().isEmpty()) return false;
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) return false;
        if (password == null || password.length() < 8) return false;
        return true;
    }
}
