import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.Contact;

public class App {
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws Exception{
        while(true){
            afficherMenu();
            String choix = scan.nextLine();
            switch (choix) {
                case "1" -> {
                    Contact newContact = createContact(null);
                    newContact.enregistrer();
                    System.out.println("Contact enregistré !");
                }
                case "2" -> listeContact(null);
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
                    triParNom();
                }
                case "8" -> {
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

    public static void afficherMenu(){
        ArrayList<String> menus = new ArrayList<>();
        menus.add("-- Menu --");
        menus.add("1 - Ajouter un contact");
        menus.add("2 - Lister les contacts");
        menus.add("3 - Modifier un contact");
        menus.add("4 - Supprimer un contact");
        menus.add("5 - Trier par nom");
        menus.add("8 - Chercher par nom");
        menus.add("q - Quitter");
        menus.add("Veuillez entrer un choix");
        for (String menu : menus){
            System.out.println(menu);
        }
    }

    public static Contact createContact(Contact PlaceHolderContact) throws IOException {
        Contact nouveauContact = new Contact();
        String nomText, prenomText, mailText, telephoneText, dateNaissanceText;

        if(PlaceHolderContact != null){
            nomText = "Nom (" + PlaceHolderContact.getNom() + "):";
            prenomText = "Prénom (" + PlaceHolderContact.getPrenom() + "):";
            mailText = "Adresse mail (" + PlaceHolderContact.getMail() + "):";
            telephoneText = "Numéro de téléphone (" + PlaceHolderContact.getTelephone() + "):";
            dateNaissanceText = "Date de naissance (format dd/MM/yyyy) (" + PlaceHolderContact.getDateNaissance() + "):";
        }else{
            nomText = "Nom :";
            prenomText = "Prénom :";
            mailText = "Adresse mail :";
            telephoneText = "Numéro de téléphone :";
            dateNaissanceText = "Date de naissance (format dd/MM/yyyy) :";
        }
        System.out.println(nomText);
        nouveauContact.setNom(scan.nextLine());
        System.out.println(prenomText);
        nouveauContact.setPrenom(scan.nextLine());
        while(true) {
            try {
                System.out.println(mailText);
                nouveauContact.setMail(scan.nextLine());
                break;
            } catch (ParseException e) {
                System.out.println("Format d'email invalide");
            }
        }
        while(true) {
            try {
                System.out.println(telephoneText);
                nouveauContact.setTelephone(scan.nextLine());
                break;
            } catch (ParseException e) {
                System.out.println("Téléphone invalide");
            }
        }
        while(true) {
            try {
                System.out.println(dateNaissanceText);
                nouveauContact.setDateNaissance(scan.nextLine());
                break;
            }
            catch (ParseException e){
                System.out.println("Date de naissance non valide");
            }
        }
        return nouveauContact;
    }
    public static void listeContact(ArrayList<Contact> contactList) {
        try {
            if(contactList != null){
                for (Contact contact : contactList) {
                    System.out.println("Nom : " + contact.getNom() + "\nPrénom : " + contact.getPrenom() + "\nEmail : " + contact.getMail() + "\nTéléphone : " + contact.getTelephone() + "\nDate de naissance : " + contact.getDateNaissance());
                    System.out.println();
                }
            }else {
                ArrayList<Contact> list = Contact.lister();
                for (Contact contact : list) {
                    System.out.println("Nom : " + contact.getNom() + "\nPrénom : " + contact.getPrenom() + "\nEmail : " + contact.getMail() + "\nTéléphone : " + contact.getTelephone() + "\nDate de naissance : " + contact.getDateNaissance());
                    System.out.println();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        }
    private static void modifierContact(String mail) throws Exception {
        Contact contact = Contact.rechercher(mail);
        if(contact == null){
            System.out.println("Contact introuvable");
            return;
        }
        System.out.println("Veuillez mettre à jour les informations du contact");
        Contact.updateContact(mail, createContact(contact));
    }
    private static void supprimerContact(String mail) throws Exception {
        if(Contact.rechercher(mail) == null){
            System.out.println("Contact introuvable");
            return;
        };
        Contact.supprimer(mail);
        System.out.println("Contact supprimé");
    }
    private static void triParNom() {
        try{
            ArrayList<Contact> list = Contact.lister();
            Collections.sort(list);
            listeContact(list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static ArrayList<Contact> chercherParPrenom() {
        try{
            ArrayList<Contact> list = Contact.lister();
            System.out.println("Prénom commençant par :");
            String recherche = scan.nextLine();
            List<Contact> filteredList = list.stream().filter(c -> c.getPrenom().toLowerCase().startsWith(recherche.toLowerCase())).toList();
            if(filteredList.isEmpty()){
                System.out.println("Pas de résultat");
            }
            return new ArrayList<>(filteredList);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}