package com.sg.contlistspringboot.dao;

import com.sg.contlistspringboot.model.Contact;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author ward
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactListDaoTest {

    @Autowired
    private ContactRepository contacts;

    public ContactListDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        // remove all of the Contacts
        contacts.deleteAll();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void addGetDeleteContact() {
        // Create new contact
        Contact nc = new Contact();
        nc.setFirstName("John");
        nc.setLastName("Doe");
        nc.setCompany("Oracle");
        nc.setEmail("john@doe.com");
        nc.setPhone("1234445678");
        nc = contacts.save(nc);
        Contact fromDb = contacts.findById(nc.getContactId()).orElse(null);
        assertEquals(fromDb, nc);
        contacts.deleteById(nc.getContactId());
        assertNull(contacts.findById(nc.getContactId()).orElse(null));
    }

    @Test
    public void addUpdateContact() {
        // Create new contact
        Contact nc = new Contact();
        nc.setFirstName("Jimmy");
        nc.setLastName("Smith");
        nc.setCompany("Sun");
        nc.setEmail("jimmy@smith.com");
        nc.setPhone("1112223333");
        nc = contacts.save(nc);
        nc.setPhone("9999999999");
        nc = contacts.save(nc);
        Contact fromDb = contacts.findById(nc.getContactId()).orElse(null);
        assertEquals(fromDb, nc);
    }

    @Test
    public void getAllContacts() {
        // Create new contact
        Contact nc = new Contact();
        nc.setFirstName("Jimmy");
        nc.setLastName("Smith");
        nc.setCompany("Sun");
        nc.setEmail("jimmy@smith.com");
        nc.setPhone("1112223333");
        nc = contacts.save(nc);
        // Create new contact
        Contact nc2 = new Contact();
        nc2.setFirstName("John");
        nc2.setLastName("Jones");
        nc2.setCompany("Apple");
        nc2.setEmail("john@jones.com");
        nc2.setPhone("5556667777");
        nc2 = contacts.save(nc2);

        List<Contact> cList = contacts.findAll();
        assertEquals(cList.size(), 2);
    }

    @Test
    public void searchContacts() {
        // Create new contact
        Contact nc = new Contact();
        nc.setFirstName("Jimmy");
        nc.setLastName("Smith");
        nc.setCompany("Sun");
        nc.setEmail("jimmy@smith.com");
        nc.setPhone("1112223333");
        nc = contacts.save(nc);
        // Create new contact
        Contact nc2 = new Contact();
        nc2.setFirstName("John");
        nc2.setLastName("Jones");
        nc2.setCompany("Apple");
        nc2.setEmail("john@jones.com");
        nc2.setPhone("5556667777");
        nc2 = contacts.save(nc2);
        // Create new contact - same last name as first 
        // contact but different company
        Contact nc3 = new Contact();
        nc3.setFirstName("Steve");
        nc3.setLastName("Smith");
        nc3.setCompany("Microsoft");
        nc3.setEmail("steve@msft.com");
        nc3.setPhone("5552221234");
        nc3 = contacts.save(nc3);
        // Create search criteria

        Contact contact = new Contact();
        contact.setLastName("Jones");
        List<Contact> cList = contacts.findAll(Example.of(contact, ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("contactId")));
        assertEquals(1, cList.size());
        assertEquals(nc2, cList.get(0));

        // New search criteria - look for Smith
        contact.setLastName("Smith");
        cList = contacts.findAll(Example.of(contact, ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("contactId")));
        assertEquals(2, cList.size());

        // Add company to search criteria
        contact.setCompany("Sun");
        cList = contacts.findAll(Example.of(contact, ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("contactId")));
        assertEquals(1, cList.size());
        assertEquals(nc, cList.get(0));

        // Change company to Microsoft, should get back nc3
        contact.setCompany("Microsoft");
        cList = contacts.findAll(Example.of(contact, ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("contactId")));
        assertEquals(1, cList.size());
        assertEquals(nc3, cList.get(0));

        // Change company to Apple, should get back nothing
        contact.setCompany("Apple");
        cList = contacts.findAll(Example.of(contact, ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("contactId")));
        assertEquals(0, cList.size());
    }
}
