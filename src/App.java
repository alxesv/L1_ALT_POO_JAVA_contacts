import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import model.Contact;

public class App {
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        while (true) {
            afficherMenu();
            String choix = scan.nextLine();
            switch (choix) {
                case "1" -> {
                    Contact newContact = createContact(null);
                    newContact.enregistrer();
                    System.out.println("Contact enregistré !");
                }
                case "2" -> filterSubMenu();
                case "3" -> {
                    System.out.println("Entrez l'adresse email actuelle du contact à modifier");
                    String mail = scan.nextLine();
                    modifierContact(mail);
                }
                case "4" -> {
                    System.out.println("Entrez l'adresse email du contact à supprimer");
                    String mail = scan.nextLine();
                    supprimerContact(mail);
                }
                case "5" -> {
                    listeContact(chercherParPrenom());
                }
                case "q" -> {
                    System.out.println("Exiting --");
                    return;
                }
                default -> System.out.println("Veuillez choisir une option valable");
            }
        }
    }

    public static void afficherMenu() {
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

    public static void afficherFilterSubMenu() {
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

    public static void filterSubMenu() {
        while (true) {
            afficherFilterSubMenu();
            String choix = scan.nextLine();
            switch (choix) {
                case "1" -> {
                    listeContact(null);
                    return;
                }
                case "2" -> {
                    triParNom();
                    return;
                }
                case "3" -> {
                    triParDateNaissance();
                    return;
                }
                case "4" -> {
                    triParMail();
                    return;
                }
                case "q" -> {
                    System.out.println("Back to main menu --");
                    return;
                }
                default -> System.out.println("Veuillez choisir une option valable");
            }
        }
    }

    public static Contact createContact(Contact PlaceHolderContact){
        Contact nouveauContact = new Contact();
        String[] contactInfoTypes = {"nom", "prénom", "email", "téléphone", "date de naissance"};
        String[] contactInfoText;
        if (PlaceHolderContact != null) {
            contactInfoText = new String[]{"Nom (" + PlaceHolderContact.getNom() + "):", "Prénom (" + PlaceHolderContact.getPrenom() + "):",
                    "Adresse mail (" + PlaceHolderContact.getMail() + "):", "Numéro de téléphone (" + PlaceHolderContact.getTelephone() + "):",
                    "Date de naissance (format dd/MM/yyyy) (" + PlaceHolderContact.getDateNaissance() + "):"};
        } else {
            contactInfoText = new String[]{"Nom:", "Prénom:", "Adresse mail:", "Numéro de téléphone:", "Date de naissance (format dd/MM/yyyy):"};
        }
        for (int i = 0; i < contactInfoTypes.length; i++) {
            while (true) {
                try {
                    System.out.println(contactInfoText[i]);
                    switch (i) {
                        case 0 -> {
                          String nom = scan.nextLine();
                          if(nom.equals("") && PlaceHolderContact != null){
                            nouveauContact.setNom(PlaceHolderContact.getNom());
                          } else {
                            nouveauContact.setNom(nom);
                          }
                        }
                        case 1 -> {
                          String prenom = scan.nextLine();
                          if(prenom.equals("") && PlaceHolderContact != null){
                            nouveauContact.setPrenom(PlaceHolderContact.getPrenom());
                          } else {
                            nouveauContact.setPrenom(prenom);
                          }
                        }
                        case 2 -> {
                            String mail = scan.nextLine();
                            if(mail.equals("") && PlaceHolderContact != null){
                              nouveauContact.setMail(PlaceHolderContact.getMail());
                            } else if(Contact.rechercher(mail) == null || (PlaceHolderContact != null && mail.equals(PlaceHolderContact.getMail()))){
                              nouveauContact.setMail(mail);
                            } else {
                              throw new Exception("Email déjà utilisé");
                            }
                        }
                        case 3 -> {
                          String telephone = scan.nextLine();
                          if(telephone.equals("") && PlaceHolderContact != null){
                            nouveauContact.setTelephone(PlaceHolderContact.getTelephone());
                          } else {
                            nouveauContact.setTelephone(telephone);
                          }
                        }
                        case 4 -> {
                          String dateNaissance = scan.nextLine();
                          if(dateNaissance.equals("") && PlaceHolderContact != null){
                            nouveauContact.setDateNaissance(PlaceHolderContact.getDateNaissance());
                          } else {
                            nouveauContact.setDateNaissance(dateNaissance);
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
        return nouveauContact;
    }

    public static void listeContact(ArrayList<Contact> contactList) {
        try {
            if (contactList != null) {
                for (Contact contact : contactList) {
                    System.out.println("Nom : " + contact.getNom() + "\nPrénom : " + contact.getPrenom() + "\nEmail : "
                            + contact.getMail() + "\nTéléphone : " + contact.getTelephone() + "\nDate de naissance : "
                            + contact.getDateNaissance());
                    System.out.println();
                }
            } else {
                ArrayList<Contact> list = Contact.lister();
                for (Contact contact : list) {
                    System.out.println("Nom : " + contact.getNom() + "\nPrénom : " + contact.getPrenom() + "\nEmail : "
                            + contact.getMail() + "\nTéléphone : " + contact.getTelephone() + "\nDate de naissance : "
                            + contact.getDateNaissance());
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void modifierContact(String mail) throws Exception {
        Contact contact = Contact.rechercher(mail);
        if (contact == null) {
            System.out.println("Contact introuvable");
            return;
        }
        System.out.println("Veuillez mettre à jour les informations du contact");
        Contact.updateContact(mail, createContact(contact));
    }

    private static void supprimerContact(String mail) throws Exception {
        if (Contact.rechercher(mail) == null) {
            System.out.println("Contact introuvable");
            return;
        }
        ;
        Contact.supprimer(mail);
        System.out.println("Contact supprimé");
    }

    private static void triParNom() {
        try {
            ArrayList<Contact> list = Contact.lister();
            Collections.sort(list);
            listeContact(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Contact> chercherParPrenom() {
        try {
            ArrayList<Contact> list = Contact.lister();
            System.out.println("Prénom commençant par :");
            String recherche = scan.nextLine();
            List<Contact> filteredList = list.stream()
                    .filter(c -> c.getPrenom().toLowerCase().startsWith(recherche.toLowerCase())).toList();
            if (filteredList.isEmpty()) {
                System.out.println("Pas de résultat");
            }
            return new ArrayList<>(filteredList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void triParDateNaissance() {
        try {
            ArrayList<Contact> list = Contact.lister();
            Collections.sort(list, new Contact());
            listeContact(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void triParMail() {
        try {
            ArrayList<Contact> list = Contact.lister();
            Collections.sort(list, new Comparator<Contact>(){
                @Override
                public int compare(Contact c1, Contact c2) {
                    return c1.getMail().toLowerCase().compareTo(c2.getMail().toLowerCase());
                }
            });
            listeContact(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
