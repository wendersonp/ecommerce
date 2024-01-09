package com.wendersonp.receiver.domain.util;

public class ValidationMessages {

    public static final String NOT_BLANK = "n\u00E3o deve estar em branco, n\u00E3o deve ser nulo";
    public static final String NOT_EMPTY = "n\u00E3o deve estar vazio, n\u00E3o deve ser nulo";
    public static final String NOT_NULL = "n\u00E3o deve ser nulo";

    public static final String ORDER_EXTERNAL_FORMAT = "deve segui o padr\u00E3o numerico positivo com d\u00EDgito separador (exemplo, 123456789101-1)";


    public static final String POSITIVE = "deve ser positivo";
    public static final String EIGHT_DIGITS = "O campo deve ter 8 digitos";
    public static final String PHONE_FORMAT = "deve estar no formato (xx) xxxxx-xxxx";
    public static final String EMAIL_FORMAT = "O email deve estar em um formato valido";
    public static final String INTEGER_FORMAT = "deve ser um n\u00FAmero inteiro";
    public static final String ELEVEN_DIGITS = "deve ter no m\u00EDnimo 11 d\u00EDgitos";

    public static final String ELEVEN_FOURTEEN_DIGITS = "deve ter 11 ou 14 d\u00EDgitos";


    private ValidationMessages() {
    }
}
