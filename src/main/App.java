package main;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import model.Contact;

/**
 * App is the main class of the application.
 * 
 * @author Alexandre, Kenza, Kevin, Mathys
 * @version 1.0
 */
public class App {
    /**
     * Attribute scan: Scanner to read user input.
     */
    private static Scanner scan = new Scanner(System.in);

    /**
     * Main loop of the application.
     * @param args
     *       Arguments passed to the application.
     * @throws Exception
     *      Exception thrown by the application.
     */
    public static void main(String[] args) throws Exception {
        while (true) {
            showMenu();
            String choice = scan.nextLine();
            switch (choice) {
                case "1" -> {
                    Contact newContact = createContact(null);
                    newContact.save();
                    System.out.println("Contact enregistré !");
                }
                case "2" -> filterSubMenu();
                case "3" -> {
                    System.out.println("Entrez l'adresse email actuelle du contact à modifier");
                    String mail = scan.nextLine();
                    modifyContact(mail);
                }
                case "4" -> {
                    System.out.println("Entrez l'adresse email du contact à supprimer");
                    String mail = scan.nextLine();
                    deleteContact(mail);
                }
                case "5" -> {
                    listContacts(searchByName());
                }
                case "q" -> {
                    System.out.println("Fermeture --");
                    return;
                }
                default -> System.out.println("Veuillez choisir une option valable");
            }
        }
    }
/**
 * Display the main menu.
 */
    public static void showMenu() {
        ArrayList<String> menus = new ArrayList<>();
        menus.add("-- Menu --");
        menus.add("1 - Ajouter un contact");
        menus.add("2 - Lister les contacts");
        menus.add("3 - Modifier un contact");
        menus.add("4 - Supprimer un contact");
        menus.add("5 - Chercher par prénom");
        menus.add("q - Quitter");
        menus.add("Veuillez entrer un choix");
        for (String menu : menus) {
            System.out.println(menu);
        }
    }
/**
 * Display the submenu.
 */
    public static void showFilterSubMenu() {
        ArrayList<String> menus = new ArrayList<>();
        menus.add("-- Sous Menu --");
        menus.add("1 - Pas de tri");
        menus.add("2 - Trier par nom");
        menus.add("3 - Trier par date de naissance");
        menus.add("4 - Trier par mail");
        menus.add("q - Quitter");
        menus.add("Veuillez entrer un choix");
        for (String menu : menus) {
            System.out.println(menu);
        }
    }
/**
 * Logic of the submenu.
 */
    public static void filterSubMenu() {
        while (true) {
            showFilterSubMenu();
            String choice = scan.nextLine();
            switch (choice) {
                case "1" -> {
                    listContacts(null);
                    return;
                }
                case "2" -> {
                    sortBySurname();
                    return;
                }
                case "3" -> {
                    sortByBirthDate();
                    return;
                }
                case "4" -> {
                    sortByMail();
                    return;
                }
                case "q" -> {
                    System.out.println("Retour au menu principal --");
                    return;
                }
                default -> System.out.println("Veuillez choisir une option valable");
            }
        }
    }
    /**
     * Create a new contact with user input.
     * @param PlaceHolderContact
     *     Contact to be modified (null if new contact).
     * @return Contact
     *    The new contact.
     */
    public static Contact createContact(Contact PlaceHolderContact){
        Contact newContact = new Contact();
        String[] contactInfoTypes = {"nom", "prénom", "email", "téléphone", "date de naissance"};
        String[] contactInfoText;
        if (PlaceHolderContact != null) {
            contactInfoText = new String[]{"Nom (" + PlaceHolderContact.getSurname() + "):", "Prénom (" + PlaceHolderContact.getName() + "):",
                    "Adresse mail (" + PlaceHolderContact.getMail() + "):", "Numéro de téléphone (" + PlaceHolderContact.getTelephone() + "):",
                    "Date de naissance (format dd/MM/yyyy) (" + PlaceHolderContact.getBirthDate() + "):"};
        } else {
            contactInfoText = new String[]{"Nom:", "Prénom:", "Adresse mail:", "Numéro de téléphone:", "Date de naissance (format dd/MM/yyyy):"};
        }
        for (int i = 0; i < contactInfoTypes.length; i++) {
            while (true) {
                try {
                    System.out.println(contactInfoText[i]);
                    switch (i) {
                        case 0 -> {
                          String surname = scan.nextLine();
                          if(surname.equals("") && PlaceHolderContact != null){
                            newContact.setSurname(PlaceHolderContact.getSurname());
                          } else {
                            newContact.setSurname(surname);
                          }
                        }
                        case 1 -> {
                          String name = scan.nextLine();
                          if(name.equals("") && PlaceHolderContact != null){
                            newContact.setName(PlaceHolderContact.getName());
                          } else {
                            newContact.setName(name);
                          }
                        }
                        case 2 -> {
                            String mail = scan.nextLine();
                            if(mail.equals("") && PlaceHolderContact != null){
                              newContact.setMail(PlaceHolderContact.getMail());
                            } else if(Contact.search(mail) == null || (PlaceHolderContact != null && mail.equals(PlaceHolderContact.getMail()))){
                              newContact.setMail(mail);
                            } else {
                              throw new Exception("Email déjà utilisé");
                            }
                        }
                        case 3 -> {
                          String telephone = scan.nextLine();
                          if(telephone.equals("") && PlaceHolderContact != null){
                            newContact.setTelephone(PlaceHolderContact.getTelephone());
                          } else {
                            newContact.setTelephone(telephone);
                          }
                        }
                        case 4 -> {
                          String birthDate = scan.nextLine();
                          if(birthDate.equals("") && PlaceHolderContact != null){
                            newContact.setBirthDate(PlaceHolderContact.getBirthDate());
                          } else {
                            newContact.setBirthDate(birthDate);
                          }
                        }
                    }
                    break;
                } catch (ParseException e) {
                    System.out.println("Format " + contactInfoTypes[i] + " invalide");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return newContact;
    }
/**
 * Display the selected list of contacts, or all contacts if null.
 * @param contactList
 *    List of contacts to be displayed.
 */
    public static void listContacts(ArrayList<Contact> contactList) {
        try {
            if (contactList != null) {
                for (Contact contact : contactList) {
                    System.out.println("Nom : " + contact.getSurname() + "\nPrénom : " + contact.getName() + "\nEmail : "
                            + contact.getMail() + "\nTéléphone : " + contact.getTelephone() + "\nDate de naissance : "
                            + contact.getBirthDate());
                    System.out.println();
                }
            } else {
                ArrayList<Contact> list = Contact.list();
                for (Contact contact : list) {
                    System.out.println("Nom : " + contact.getSurname() + "\nPrénom : " + contact.getName() + "\nEmail : "
                            + contact.getMail() + "\nTéléphone : " + contact.getTelephone() + "\nDate de naissance : "
                            + contact.getBirthDate());
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
 * Update the contact informations with the given mail.
 * @param mail
 *   Mail of the contact to be updated.
 * @throws Exception
 */
    private static void modifyContact(String mail) throws Exception {
        Contact contact = Contact.search(mail);
        if (contact == null) {
            System.out.println("Contact introuvable");
            return;
        }
        System.out.println("Veuillez mettre à jour les informations du contact");
        Contact.updateContact(mail, createContact(contact));
    }
/**
 * Delete the contact with the given mail.
 * @param mail
 *  Mail of the contact to be deleted.
 * @throws Exception
 */
    private static void deleteContact(String mail) throws Exception {
        if (Contact.search(mail) == null) {
            System.out.println("Contact introuvable");
            return;
        }
        ;
        Contact.delete(mail);
        System.out.println("Contact supprimé");
    }
/**
 * Sort the contact list by surname.
 */
    private static void sortBySurname() {
        try {
            ArrayList<Contact> list = Contact.list();
            Collections.sort(list);
            listContacts(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
 * Search a contact by name.
 * @return List of contacts matching the search.
 */
    private static ArrayList<Contact> searchByName() {
        try {
            ArrayList<Contact> list = Contact.list();
            System.out.println("Prénom commençant par :");
            String search = scan.nextLine();
            List<Contact> filteredList = list.stream()
                    .filter(c -> c.getName().toLowerCase().startsWith(search.toLowerCase())).toList();
            if (filteredList.isEmpty()) {
                System.out.println("Pas de résultat");
            }
            return new ArrayList<>(filteredList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
/**
 * Sort the contact list by birth date.
 */
    private static void sortByBirthDate() {
        try {
            ArrayList<Contact> list = Contact.list();
            Collections.sort(list, new Contact());
            listContacts(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
 * Sort the contact list by mail.
 */
    private static void sortByMail() {
        try {
            ArrayList<Contact> list = Contact.list();
            Collections.sort(list, new Comparator<Contact>(){
                @Override
                public int compare(Contact c1, Contact c2) {
                    return c1.getMail().toLowerCase().compareTo(c2.getMail().toLowerCase());
                }
            });
            listContacts(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
