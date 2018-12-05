package com.sg.contlistspringboot.controller;

import com.sg.contlistspringboot.dao.ContactRepository;
import com.sg.contlistspringboot.model.Contact;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ward
 */
@Controller
public class SearchController {

    @RequestMapping(value = "/displaySearchPage", method = RequestMethod.GET)
    public String displaySearchPage() {
        return "search";
    }

    @Autowired
    private ContactRepository contacts;

    @RequestMapping(value = "/search/contacts", method = RequestMethod.POST)
    @ResponseBody
    public List<Contact> searchContacts(@RequestBody Map<String, String> searchMap) {
        // Create the example object with empty data
        Contact contact = new Contact();

        // Determine which search terms have values,
        // and set the corresponding values appropriately.
        String currentTerm = searchMap.get("firstName");
        if (currentTerm != null && !currentTerm.isEmpty()) {
            contact.setFirstName(currentTerm);
        }
        currentTerm = searchMap.get("lastName");
        if (currentTerm != null && !currentTerm.isEmpty()) {
            contact.setLastName(currentTerm);
        }
        currentTerm = searchMap.get("company");
        if (currentTerm != null && !currentTerm.isEmpty()) {
            contact.setCompany(currentTerm);
        }
        currentTerm = searchMap.get("email");
        if (currentTerm != null && !currentTerm.isEmpty()) {
            contact.setEmail(currentTerm);
        }
        currentTerm = searchMap.get("phone");
        if (currentTerm != null && !currentTerm.isEmpty()) {
            contact.setPhone(currentTerm);
        }

        return contacts.findAll(Example.of(contact, ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("contactId")));
    }
}
