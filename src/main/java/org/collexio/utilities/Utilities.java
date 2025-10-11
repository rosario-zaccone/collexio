package org.collexio.utilities;

public class Utilities {
    public static boolean validateId(String prefix, String id) {
        //validate id in form prefix-number
        if (!id.startsWith(prefix))
            return false;
        String numId = id.substring(prefix.length());
        char[] numbers = numId.toCharArray();
        if (numbers.length == 0 || numbers[0] == 0)
            return false;
        for (char c: numbers) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }
}
