import org.clawd.data.Generator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class GeneratorTests {

    private Generator generator;

    @Before
    public void setup() {
        this.generator = new Generator(new ArrayList<>());
    }

    @Test
    public void testDoubleTransformation() {
        double d = 10.0;

        for (int i = 0; i < 10; i++) {
            d -= 1.1;
            System.out.println(d);
            System.out.println(generator.transformDouble(d));
        }
    }

    @Test
    public void testDoubleTransformation2() {
        System.out.println(generator.transformDouble(1.2000000000000015));
        System.out.println(generator.transformDouble(1.2));
    }
}
