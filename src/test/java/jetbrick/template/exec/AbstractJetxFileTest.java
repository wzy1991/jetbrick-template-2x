package jetbrick.template.exec;

import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import jetbrick.io.resource.Resource;
import jetbrick.template.*;
import jetbrick.template.resource.SourceResource;
import jetbrick.template.resource.loader.AbstractResourceLoader;
import jetbrick.util.ExceptionUtils;
import org.junit.BeforeClass;

public abstract class AbstractJetxFileTest extends AbstractJetxSourceTest {
    protected static final Map<String, String> sourceMap = new HashMap<String, String>();

    @BeforeClass
    public static void initialize() {
        Properties config = new Properties();
        config.put(JetConfig.TEMPLATE_LOADER, SourceResourceLoader.class.getName());
        engine = JetEngine.create(config);
    }

    @Override
    protected String eval(String name, Map<String, Object> context) {
        JetTemplate template = engine.getTemplate(name);

        Writer out = new StringWriter();
        try {
            template.render(context, out);
            return out.toString();
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    public static class SourceResourceLoader extends AbstractResourceLoader {
        @Override
        public Resource load(String name) {
            String source = sourceMap.get(name);
            if (source != null) {
                return new SourceResource(source);
            }
            return null;
        }
    }
}
