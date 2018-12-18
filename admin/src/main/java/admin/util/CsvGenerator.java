package admin.util;

import admin.util.Reflector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CsvGenerator {

    public static <T> void generate(List<T> list, String outputPath) throws IOException {
        if (list.size() == 0) return;
        var properties = Reflector.getPropertiesNamesOf(list.get(0).getClass());

        var header = getHeaderFrom(properties);
        Files.write(Paths.get(outputPath), header.getBytes());

        var lines = getLinesFrom(list, properties);
        Files.write(Paths.get(outputPath), lines.getBytes(), StandardOpenOption.APPEND);
    }

    private static <T> String getHeaderFrom(List<String> properties) {
        var builder = new StringBuilder();
        properties.forEach(i -> builder.append(i).append(","));
        builder.setCharAt(builder.length() - 1, '\n');
        return builder.toString();
    }

    private static <T> String getLinesFrom(List<T> list, List<String> properties) {
        var builder = new StringBuilder();
        list.stream()
            .map(Reflector::getPropertiesWithValues)
            .forEach(map -> {
                properties.forEach(i -> builder.append(map.get(i)).append(","));
                builder.setCharAt(builder.length() - 1, '\n');
            });
        return builder.toString();
    }
}
