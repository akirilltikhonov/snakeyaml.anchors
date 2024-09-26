package snakeyaml.anchors.domain;

import java.util.List;

import lombok.Data;


@Data
public class Root
{
    private String defaultTargetBranch;
    private List<Resource> defaultResources;
    private List<Project> projects;
}
