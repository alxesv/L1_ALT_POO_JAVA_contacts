package model;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class Contact implements Comparable<Contact>{
    private static final String SEPARATEUR = ";";
    private static final Pattern CSV_PATTERN = Pattern.compile(SEPARATEUR);
    private String nom;
    private String prenom;
    private String mail;
    private String telephone;
    private Date dateNaissance;

    public String getNom(){
        return this.nom;
    }
    public String getPrenom(){
        return this.prenom;
    }
    public String getMail(){
        return this.mail;
    }
    public String getTelephone(){
        return this.telephone;
    }
    public String getDateNaissance(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(this.dateNaissance);
    }

    public void setNom(String nom){
        this.nom = nom;
    }
    public void setPrenom(String prenom){
        this.prenom = prenom;
    }
    public void setMail(String mail) throws ParseException {
        if(patternMatches(mail,"^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            this.mail = mail;
        }else{
            throw new ParseException("Format invalide", 0);
        }
    }
    public void setTelephone(String telephone) throws ParseException {
        if(patternMatches(telephone,"^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$")) {
            this.telephone = telephone;
        }else{
            throw new ParseException("Format invalide", 0);
        }
    }
    public void setDateNaissance(String dateNaissance) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.dateNaissance = dateFormat.parse(dateNaissance);
    }
    public static boolean patternMatches(String input, String pattern){
        return Pattern.compile(pattern).matcher(input).matches();
    }

    public void enregistrer() throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("contacts.csv", true)));
        try {
            pw.println(this.toString());
        }finally {
            pw.close();
        }
    }

    public static Contact rechercher(String mail) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("contacts.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains(mail)){
                    Contact c = new Contact();
                    String[] fields = CSV_PATTERN.split(line);
                    c.setNom(fields[0]);
                    c.setPrenom(fields[1]);
                    c.setMail(fields[2]);
                    c.setTelephone(fields[3]);
                    c.setDateNaissance(fields[4]);
                    return c;
                }
            }
        }
        return null;
    }

    public static void supprimer(String mail) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("contacts.csv"));
        String line;
        ArrayList<String> fileContents = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if(line.contains(mail)){
                continue;
            }
            fileContents.add(line);
        }
        br.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter("contacts.csv"));
        for (String s : fileContents) {
            bw.write(s);
            bw.newLine();
        }
        bw.close();
    }

    public static void updateContact(String mail, Contact updatedContact) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("contacts.csv"));
        String line;
        ArrayList<String> fileContents = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if(line.contains(mail)){
                line = updatedContact.toString();
            }
            fileContents.add(line);
        }
        br.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter("contacts.csv"));
        for (String s : fileContents) {
            bw.write(s);
            bw.newLine();
        }
        bw.close();
    }

    public static ArrayList<Contact> lister() throws Exception {
        ArrayList<Contact> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("contacts.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Contact c = new Contact();
                String[] fields = CSV_PATTERN.split(line);
                c.setNom(fields[0]);
                c.setPrenom(fields[1]);
                c.setMail(fields[2]);
                c.setTelephone(fields[3]);
                c.setDateNaissance(fields[4]);
                list.add(c);
            }
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append(this.getNom());
        build.append(SEPARATEUR);
        build.append(this.getPrenom());
        build.append(SEPARATEUR);
        build.append(this.getMail());
        build.append(SEPARATEUR);
        build.append(this.getTelephone());
        build.append(SEPARATEUR);
        build.append(this.getDateNaissance());
        return build.toString();
    }

    @Override
    public int compareTo(Contact other) {
       int nomCompare = this.nom.compareTo(other.nom);
        if(nomCompare != 0)
            return nomCompare;
        else
            return this.prenom.compareTo(other.prenom);
    }
}