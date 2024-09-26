package snakeyaml.anchors.utils;

import java.nio.charset.StandardCharsets;

import lombok.SneakyThrows;


/**
 * Utility class to load resources
 */
public class FileUtils
{

    @SneakyThrows
    public String loadFile(final String fileName)
    {
        return new String(getClass().getClassLoader().getResourceAsStream(fileName).readAllBytes(), StandardCharsets.UTF_8);
    }
}
