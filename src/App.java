import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import model.Contact;

public class App {
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws Exception{
        while(true){
            afficherMenu();
            String choix = scan.nextLine();
            switch (choix) {
                case "1" -> ajouterContact();
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
        menus.add("q - Quitter");
        menus.add("Veuillez entrer un choix");
        for (String menu : menus){
            System.out.println(menu);
        }
    }

    public static void ajouterContact() throws IOException {
        Contact nouveauContact = new Contact();
        System.out.println("Nom :");
        nouveauContact.setNom(scan.nextLine());
        System.out.println("Prénom :");
        nouveauContact.setPrenom(scan.nextLine());
        while(true) {
            try {
                System.out.println("Adresse mail :");
                nouveauContact.setMail(scan.nextLine());
                break;
            } catch (ParseException e) {
                System.out.println("Format d'email invalide");
            }
        }
        while(true) {
            try {
                System.out.println("Numéro de téléphone :");
                nouveauContact.setTelephone(scan.nextLine());
                break;
            } catch (ParseException e) {
                System.out.println("Téléphone invalide");
            }
        }
        while(true) {
            try {
                System.out.println("Date de naissance (format dd/MM/yyyy) :");
                nouveauContact.setDateNaissance(scan.nextLine());
                break;
            }
            catch (ParseException e){
                System.out.println("Date de naissance non valide");
            }
        }
        nouveauContact.enregistrer();
        System.out.println("Contact enregistré !");
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
        //à améliorer
        Contact.supprimer(mail);
        System.out.println("Veuillez mettre à jour les informations du contact");
        ajouterContact();
    }
    private static void supprimerContact(String mail) throws Exception {
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
}