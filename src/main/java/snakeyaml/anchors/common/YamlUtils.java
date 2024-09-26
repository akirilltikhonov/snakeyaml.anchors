package snakeyaml.anchors.common;

import static snakeyaml.anchors.common.YamlRepresenter.RESOURCES_ANCHOR_YAML_PROPERTY;
import static snakeyaml.anchors.common.YamlRepresenter.TARGET_BRANCH_ANCHOR_YAML_PROPERTY;

import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import snakeyaml.anchors.domain.Root;


public class YamlUtils
{
    private YamlUtils()
    {
        // empty constructor
    }

    public static Yaml getYamlParser()
    {
        // by default the max number of aliases is set to 50 in order to prevent billion laughs attacks
        // in yamls around 150 aliases are used, so the limit must be increased
        final LoaderOptions options = new LoaderOptions();
        options.setMaxAliasesForCollections(1000);

        // mapping dash separated names with the matching getters and setters
        final TypeDescription rootDescription = new TypeDescription(Root.class);
        rootDescription.substituteProperty(TARGET_BRANCH_ANCHOR_YAML_PROPERTY,
            String.class,
            "getDefaultTargetBranch",
            "setDefaultTargetBranch");
        rootDescription.substituteProperty(RESOURCES_ANCHOR_YAML_PROPERTY,
            List.class,
            "getDefaultResources",
            "setDefaultResources");

        final Constructor constructor = new Constructor(Root.class, options);
        constructor.addTypeDescription(rootDescription);

        // setting custom indentation to match the yaml files
        final DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setIndent(2);
        dumperOptions.setIndicatorIndent(2);
        dumperOptions.setIndentWithIndicator(true);

        final YamlRepresenter customRepresenter = new YamlRepresenter(dumperOptions);
        customRepresenter.addTypeDescription(rootDescription);

        return new Yaml(constructor, customRepresenter, dumperOptions, options);
    }
}
