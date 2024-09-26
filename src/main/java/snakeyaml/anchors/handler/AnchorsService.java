package snakeyaml.anchors.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AnchorsService
{

    // regex pattern used to identify anchor names
    private static final String ANCHOR_REGEX_SUFFIX = ":\\s*&(.*)";

    public String replaceAnchorNames(final String fileContent, final String yamlProperty)
    {
        final Pattern pattern = Pattern.compile(yamlProperty + ANCHOR_REGEX_SUFFIX);
        final Matcher matcher = pattern.matcher(fileContent);
        // if the anchor is found then it will be replaced with the provided yamlProperty
        return matcher.find() ? fileContent.replace(matcher.group(1), yamlProperty) : fileContent;
    }
}
