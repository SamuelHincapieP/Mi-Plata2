package bankapp.domain.validations;

import java.util.function.Predicate;

public class ValidationRules {

    // IDs y números
    public static final Predicate<Integer> POSITIVE_NUMBER = value -> value > 0;
    public static final Predicate<Double>  POSITIVE_AMOUNT = value -> value > 0;
    public static final Predicate<Integer> VALID_CUOTAS    = value -> value >= 1;

    // Texto
    public static final Predicate<String> MIN_LENGTH_3  = value -> value.length() >= 3;
    public static final Predicate<String> NO_NUMBERS    = value -> value.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");

    // Email: contiene @ y un punto después del @
    public static final Predicate<String> VALID_EMAIL =
            value -> value.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    // Password: mínimo 8 caracteres, al menos una mayúscula, un número y un especial
    public static final Predicate<String> VALID_PASSWORD =
            value -> value.length() >= 8
                    && value.matches(".*[A-Z].*")
                    && value.matches(".*\\d.*")
                    && value.matches(".*[@#$%^&+=!*].*");

    // Puedes combinar reglas con and() / or()
    public static final Predicate<String> VALID_NAME =
            MIN_LENGTH_3.and(NO_NUMBERS);

    // Número de cuenta: formato MP######
    public static final Predicate<String> VALID_NUMERO_CUENTA =
            value -> value.matches("MP\\d{6}");
}
