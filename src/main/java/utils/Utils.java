package utils;

/**
 * Created by yin on 04/07/16.
 */
public class Utils {

    public static String filterDataType(String s, String datatype) {

        if (s.endsWith(datatype)) {
            return s.substring(1, s.length() - datatype.length() - 3);
        } else {
            return s;
        }

    }

    public static String filterDataType(String s) {
        if (s.contains("xsd:")) {
            return s.substring(1, s.lastIndexOf("^^xsd:") - 1);
        }
        return s;
    }

}
