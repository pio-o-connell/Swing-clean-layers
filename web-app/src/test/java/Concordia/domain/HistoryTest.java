package Concordia.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {
        @Test
        void testGettersAndSetters() {
                history h = new history(1, 2, 3, "loc", "prov", "2025-12-29", "note");
                assertEquals(1, h.getHistoryId());
                assertEquals(2, h.getItemId());
                assertEquals(3, h.getAmount());
                assertEquals("loc", h.getLocation());
                assertEquals("prov", h.getProvider());
                assertEquals("2025-12-29", h.getDeliveryDate());
                assertEquals("note", h.getNotes());
                h.setHistoryId(10);
                h.setItemId(20);
                h.setAmount(30);
                h.setLocation("newloc");
                h.setProvider("newprov");
                h.setDeliveryDate("2026-01-01");
                h.setNotes("newnote");
                assertEquals(10, h.getHistoryId());
                assertEquals(20, h.getItemId());
                assertEquals(30, h.getAmount());
                assertEquals("newloc", h.getLocation());
                assertEquals("newprov", h.getProvider());
                assertEquals("2026-01-01", h.getDeliveryDate());
                assertEquals("newnote", h.getNotes());
        }

        @Test
        void testSupplierCompatibility() {
                history h = new history();
                h.setSupplier("sup");
                assertEquals("sup", h.getSupplier());
        }
}

