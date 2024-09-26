package snakeyaml.anchors.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import snakeyaml.anchors.utils.FileUtils;


@ExtendWith(MockitoExtension.class)
class RootHandlerTest
{

    @InjectMocks
    private RootHandler rootHandler;
    @Spy
    private AnchorsService anchorsService;
    private final FileUtils FileUtils = new FileUtils();

    @Test
    void addProjectToRootWithoutCustomAnchorsNamesReplacing()
    {
        final String expectedYaml = FileUtils.loadFile("rootExpectedWithoutCustomAnchorsNameReplacing.yaml");

//        return the same fileContent
        doAnswer(invocation -> invocation.getArgument(0))
            .when(anchorsService).replaceAnchorNames(anyString(), anyString());

        final String projectName3 = "project3";
        final Optional<String> updatedContent = rootHandler.addProjectToRoot(projectName3);
        assertThat(updatedContent).isPresent();
        final String content = updatedContent.get();
        assertThat(content).isEqualTo(expectedYaml);
    }

    @Test
    void addProjectToRoot()
    {
        final String expectedYaml = FileUtils.loadFile("rootExpected.yaml");

        final String projectName3 = "project3";
        final Optional<String> updatedContent = rootHandler.addProjectToRoot(projectName3);
        assertThat(updatedContent).isPresent();
        final String content = updatedContent.get();
        assertThat(content).isEqualTo(expectedYaml);
    }
}