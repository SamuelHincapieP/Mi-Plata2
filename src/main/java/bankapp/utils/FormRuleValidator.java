package bankapp.utils;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Predicate;

public class FormRuleValidator {

    // ── Un solo Scanner compartido ─────────────────────────────────────────
    private static final Scanner sc = ScannerHolder.get();

    // ── Lectura básica (sin regla) ─────────────────────────────────────────

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                int value = sc.nextInt();
                sc.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Ingrese un numero entero.");
                sc.nextLine();
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                double value = sc.nextDouble();
                sc.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Ingrese un numero (ejemplo: 500000 o 12500.50).");
                sc.nextLine();
            }
        }
    }

    public static boolean readBoolean(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                boolean value = sc.nextBoolean();
                sc.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Ingrese true o false.");
                sc.nextLine();
            }
        }
    }

    public static String readString(String prompt) {
        while (true) {
            System.out.println(prompt);
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("El campo no puede estar vacio.");
        }
    }

    // ── Lectura con regla de negocio (Predicate + mensaje de error) ────────

    public static String readString(String prompt, Predicate<String> rule, String errorMessage) {
        while (true) {
            String value = readString(prompt);
            if (rule.test(value)) {
                return value;
            }
            System.out.println(errorMessage);
        }
    }

    public static int readInt(String prompt, Predicate<Integer> rule, String errorMessage) {
        while (true) {
            int value = readInt(prompt);
            if (rule.test(value)) {
                return value;
            }
            System.out.println(errorMessage);
        }
    }

    public static double readDouble(String prompt, Predicate<Double> rule, String errorMessage) {
        while (true) {
            double value = readDouble(prompt);
            if (rule.test(value)) {
                return value;
            }
            System.out.println(errorMessage);
        }
    }
}
