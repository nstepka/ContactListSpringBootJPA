package com.sg.contlistspringboot.dao;

import com.sg.contlistspringboot.model.Contact;
import java.util.List;
import java.util.Map;


public interface ContactListDao {
    // add the given Contact to the data store
    public Contact addContact(Contact contact);

    // remove the Contact with the given id from the data store
    public void removeContact(long contactId);

    // update the given Contact in the data store
    public void updateContact(Contact contact);

    // retrieve all Contacts from the data store
    public List<Contact> getAllContacts();

    // retrieve the Contact with the given id from the data store
    public Contact getContactById(long contactId);

    // search for Contacts by the given search criteria values
    public List<Contact> searchContacts(Map<SearchTerm, String> criteria);
}