import org.clawd.data.items.Item;
import org.clawd.data.items.UtilItem;
import org.clawd.parser.ItemParser;
import org.clawd.parser.exceptions.FailedItemsParseException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class ParserTests {

    @Test
    public void testParserBasic() {
        List<Item> items = new ArrayList<>();

        try {

            items = new ItemParser().parseItems();

        } catch (FailedItemsParseException e) {
            e.printStackTrace();
        }

        assertFalse(items.isEmpty());
    }
}
