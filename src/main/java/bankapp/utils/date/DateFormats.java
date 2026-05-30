package bankapp.utils.date;

import java.util.List;

public class DateFormats {

    private DateFormats() {}

    public static final List<String> DEFAULT = List.of(
            "dd/MM/yyyy",
            "d/M/yyyy",
            "yyyy-MM-dd",
            "dd-MM-yyyy",
            "ddMMyyyy"
    );

    public static final List<String> WITH_TIME = List.of(
            "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss"
    );
}
