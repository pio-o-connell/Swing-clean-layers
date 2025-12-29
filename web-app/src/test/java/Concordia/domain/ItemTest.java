package Concordia.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class ItemTest {
        @Test
        void testGettersAndSetters() {
                ArrayList<history> histories = new ArrayList<>();
                Item item = new Item(1, 2, 3, "itemName", "note", histories);
                assertEquals(1, item.getItemId());
                assertEquals(2, item.getCompanyId());
                assertEquals(3, item.getQuantity());
                assertEquals("itemName", item.getItemName());
                assertEquals("note", item.getNotes());
                assertEquals(histories, item.getHistory());
                item.setQuantity(10);
                item.setNotes("newNote");
                assertEquals(10, item.getQuantity());
                assertEquals("newNote", item.getNotes());
        }

        @Test
        void testDefaultConstructor() {
                Item item = new Item();
                assertNotNull(item);
        }
}