package com.fuli19.utils;

import java.util.regex.Pattern;

public class RegexUtils {

    private static final String REGEX_MOBILE = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";


    public static boolean isMobile (String mobile){
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

}
