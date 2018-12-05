package com.sg.contlistspringboot.controller;

import com.sg.contlistspringboot.dao.ContactRepository;
import com.sg.contlistspringboot.model.Contact;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author ward
 */
@Controller
public class ContactController {

    @Autowired
    ContactRepository contacts;

    @RequestMapping(value = "/displayContactsPage", method = RequestMethod.GET)
    public String displayContactsPage(Model model) {
        // Get all the Contacts from the DAO
        List<Contact> contactList = contacts.findAll();

        // Put the List of Contacts on the Model
        model.addAttribute("contactList", contactList);

        // Return the logical name of our View component
        return "contacts";
    }

    @RequestMapping(value = "/createContact", method = RequestMethod.POST)
    public String createContact(@Valid Contact contact, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            throw new Exception("Bad Stuff");
        }

        // persist the new Contact
        contacts.save(contact);

        // we don't want to forward to a View component - we want to
        // redirect to the endpoint that displays the Contacts Page
        // so it can display the new Contact in the table.
        return "redirect:displayContactsPage";
    }

    @RequestMapping(value = "/displayContactDetails", method = RequestMethod.GET)
    public String displayContactDetails(HttpServletRequest request, Model model) {
        String contactIdParameter = request.getParameter("contactId");
        long contactId = Integer.parseInt(contactIdParameter);

        Contact contact = contacts.findById(contactId).orElse(null);;

        model.addAttribute("contact", contact);

        return "contactDetails";
    }

    @RequestMapping(value = "/deleteContact", method = RequestMethod.GET)
    public String deleteContact(HttpServletRequest request) {
        String contactIdParameter = request.getParameter("contactId");
        long contactId = Long.parseLong(contactIdParameter);
        contacts.deleteById(contactId);
        return "redirect:displayContactsPage";
    }

    @RequestMapping(value = "/displayEditContactForm", method = RequestMethod.GET)
    public String displayEditContactForm(HttpServletRequest request, Model model) {
        String contactIdParameter = request.getParameter("contactId");
        long contactId = Long.parseLong(contactIdParameter);
        Contact contact = contacts.findById(contactId).orElse(null);;
        model.addAttribute("contact", contact);
        return "editContactForm";
    }

    @RequestMapping(value = "/editContact", method = RequestMethod.POST)
    public String editContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result) {

        if (result.hasErrors()) {
            return "editContactForm";
        }

        contacts.save(contact);

        return "redirect:displayContactsPage";
    }

}
