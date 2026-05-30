package bankapp.utils.date;

import bankapp.utils.FormValidator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateValidator {

    public DateValidator() {}

    public static LocalDate readDate(String prompt) {
        return readDate(prompt, DateFormats.DEFAULT);
    }

    public static LocalDate readDate(String prompt, List<String> patterns) {
        String hint = String.join("|", patterns);
        while (true) {
            System.out.printf("%s (%s): ", prompt, hint);
            String input = FormValidator.validateString("Ingrese una fecha valida: ");
            try {
                return DateParser.parse(input, patterns);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha invalido. Intente de nuevo.");
            }
        }
    }
}
