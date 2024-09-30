package snakeyaml.anchors.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import snakeyaml.anchors.domain.Project;
import snakeyaml.anchors.domain.Root;


/**
 * Representer used to handle custom field transformations during yaml dumping
 */
public class YamlRepresenter extends Representer
{
    public static final String TARGET_BRANCH_ANCHOR_YAML_PROPERTY = "default-targetBranch";
    public static final String RESOURCES_ANCHOR_YAML_PROPERTY = "default-resources";

    public YamlRepresenter(final DumperOptions options)
    {
        super(options);
    }

    @Override
    protected Set<Property> getProperties(final Class<? extends Object> type)
    {
        Set<Property> propertySet;
        if (typeDefinitions.containsKey(type))
        {
            propertySet = typeDefinitions.get(type).getProperties();
        }

        propertySet = getPropertyUtils().getProperties(type);

        final List<Property> propsList = new ArrayList<>(propertySet);
        if (type.equals(Root.class))
        {
            Collections.sort(propsList, new BeanPropertyComparator());
        }
        else if (type.equals(Project.class))
        {
            Collections.sort(propsList, new ProjectsPropertyComparator());
        }
        return new LinkedHashSet<>(propsList);
    }

    static class BeanPropertyComparator implements Comparator<Property>
    {
        // list matching the fields from Root.class so their order is preserved at dumping
        private final List<String> rootPropertyOrderList = Arrays.asList(
            "defaultTargetBranch",
            "defaultResources",
            "projects");

        @Override
        public int compare(final Property p1, final Property p2)
        {
            return Integer.compare(rootPropertyOrderList.indexOf(p1.getName()), rootPropertyOrderList.indexOf(p2.getName()));
        }
    }

    static class ProjectsPropertyComparator implements Comparator<Property>
    {
        // list matching the fields from Project.class so their order is preserved at dumping
        private final List<String> projectsPropertyOrderList = Arrays.asList(
            "name",
            "targetBranch",
            "resources"
        );

        @Override
        public int compare(final Property p1, final Property p2)
        {
            return Integer.compare(projectsPropertyOrderList.indexOf(p1.getName()), projectsPropertyOrderList.indexOf(p2.getName()));
        }
    }

    @Override
    protected NodeTuple representJavaBeanProperty(final Object javaBean, final Property property, final Object propertyValue, final Tag customTag)
    {
        // if value of property is null, ignore it.
        if (propertyValue == null)
        {
            return null;
        }
        else
        {
            // replacing the names of fields where camel casing is not respected in the yaml files
            final NodeTuple defaultNode = super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            if ("defaultResources".equals(property.getName()))
            {
                return new NodeTuple(representData(RESOURCES_ANCHOR_YAML_PROPERTY), defaultNode.getValueNode());
            }
            else if ("defaultTargetBranch".equals(property.getName()))
            {
                return new NodeTuple(representData(TARGET_BRANCH_ANCHOR_YAML_PROPERTY),
                    representData("\\&" + TARGET_BRANCH_ANCHOR_YAML_PROPERTY + " " + ((ScalarNode) defaultNode.getValueNode()).getValue()));
            }
            else if ("targetBranch".equals(property.getName()))
            {
                return new NodeTuple(representData("targetBranch"), representData("\\*" + TARGET_BRANCH_ANCHOR_YAML_PROPERTY));
            }
            else
            {
                return defaultNode;
            }
        }
    }
}
