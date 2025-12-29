package Concordia.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class CompanyTest {
	@Test
	void testGettersAndSetters() {
		ArrayList<Item> items = new ArrayList<>();
		ArrayList<User> users = new ArrayList<>();
		Company company = new Company(1, "TestCo", items, users);

		assertEquals(1, company.getCompanyId());
		assertEquals("TestCo", company.getCompanyName());
		assertEquals(items, company.getItems());
		assertEquals(users, company.getUsers());

		ArrayList<Item> newItems = new ArrayList<>();
		company.setItems(newItems);
		assertEquals(newItems, company.getItems());

		ArrayList<User> newUsers = new ArrayList<>();
		company.setUsers(newUsers);
		assertEquals(newUsers, company.getUsers());
	}

	@Test
	void testGetNameCompatibility() {
		Company company = new Company(2, "CompatCo", new ArrayList<>(), new ArrayList<>());
		assertEquals("CompatCo", company.getName());
	}

	@Test
	void testAddItem() {
		Company company = new Company(3, "AddItemCo", new ArrayList<>(), new ArrayList<>());
		int initialSize = company.getItems().size();
		company.addItem();
		assertEquals(initialSize + 1, company.getItems().size());
		assertNotNull(company.getItems().get(0));
	}
}
    
