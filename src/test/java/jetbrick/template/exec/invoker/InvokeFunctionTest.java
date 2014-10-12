package jetbrick.template.exec.invoker;

import jetbrick.template.exec.AbstractJetxSourceTest;
import jetbrick.util.StringUtils;
import org.junit.*;

public class InvokeFunctionTest extends AbstractJetxSourceTest {

    @BeforeClass
    public static void beforeClass() {
        engine.getGlobalResolver().registerFunctions(StringUtils.class);
    }

    @Test
    public void test() {
        Assert.assertEquals("123", eval("${trim(' 123 ')}"));
        Assert.assertEquals("000", eval("${repeat('0', 3)}"));
        Assert.assertEquals("?,?,?", eval("${repeat('?', ',', 3)}"));
    }

}
