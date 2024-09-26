package snakeyaml.anchors.handler;

import java.util.Optional;

import org.yaml.snakeyaml.Yaml;

import lombok.extern.slf4j.Slf4j;
import snakeyaml.anchors.common.YamlUtils;
import snakeyaml.anchors.domain.Root;
import snakeyaml.anchors.utils.FileUtils;


@Slf4j
public class RootYamlParser
{

    static Optional<Root> getRoot()
    {
        final Optional<String> fileContent = getFileContent();
        return fileContent.map(value -> {
            final Yaml yaml = YamlUtils.getYamlParser();
            try
            {
                return yaml.load(value);
            }
            catch (final Exception e)
            {
                log.info("There was an error during the parsing of the yaml file", e);
                return null;
            }
        });
    }

    private static Optional<String> getFileContent()
    {
        String fileContent = new FileUtils().loadFile("initialRoot.yaml");
        return Optional.of(fileContent);
    }
}
