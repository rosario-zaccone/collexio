package org.collexio.utilities;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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

    public static Connection getConnection() throws SQLException, IOException {
        var props = new Properties();
        try (Reader in = Files.newBufferedReader(
                Path.of("src/main/resources/"))) {
            props.load(in);
        }
        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) System.setProperty("jdbc.drivers", drivers);

        String url = props.getProperty("jdbc.url");
        return DriverManager.getConnection(url);
    }
}
