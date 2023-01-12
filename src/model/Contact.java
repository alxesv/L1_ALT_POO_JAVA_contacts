package model;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.Comparator;

public class Contact implements Comparable<Contact>, Comparator<Contact> {
    private static final String SEPARATOR = ";";
    private static final Pattern CSV_PATTERN = Pattern.compile(SEPARATOR);
    private static final String inputPattern = "^(?:(?<=^)[^\s" + SEPARATOR + "]|^\s;)(?:[^" + SEPARATOR + "]*[^\s;])?$";

    private String surname;
    private String name;
    private String mail;
    private String telephone;
    private Date birthDate;

    public String getSurname() {
        return this.surname;
    }

    public String getName() {
        return this.name;
    }

    public String getMail() {
        return this.mail;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public String getBirthDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(this.birthDate);
    }

    public void setSurname(String surname) throws ParseException {
        if (patternMatches(surname, inputPattern)) {
            this.surname = surname;
        } else {
            throw new ParseException("Format nom invalide", 0);
        }
    }

    public void setName(String name) throws ParseException {
        if (patternMatches(name, inputPattern)) {
            this.name = name;
        } else {
            throw new ParseException("Format prenom invalide", 0);
        }
    }

    public void setMail(String mail) throws ParseException {
        if (patternMatches(mail, "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            this.mail = mail;
        } else {
            throw new ParseException("Format mail invalide", 0);
        }
    }

    public void setTelephone(String telephone) throws ParseException {
        if (patternMatches(telephone, "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$")) {
            this.telephone = telephone;
        } else {
            throw new ParseException("Format invalide", 0);
        }
    }

    public void setBirthDate(String birthDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.birthDate = dateFormat.parse(birthDate);
    }

    public static boolean patternMatches(String input, String pattern) {
        return Pattern.compile(pattern).matcher(input).matches();
    }

    public void save() throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("contacts.csv", true)));
        try {
            pw.println(this.toString());
        } finally {
            pw.close();
        }
    }

    public static Contact search(String mail) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("contacts.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(mail)) {
                    Contact c = new Contact();
                    String[] fields = CSV_PATTERN.split(line);
                    c.setSurname(fields[0]);
                    c.setName(fields[1]);
                    c.setMail(fields[2]);
                    c.setTelephone(fields[3]);
                    c.setBirthDate(fields[4]);
                    return c;
                }
            }
        }
        return null;
    }

    public static void delete(String mail) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("contacts.csv"));
        String line;
        ArrayList<String> fileContents = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (line.contains(mail)) {
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
            if (line.contains(mail)) {
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

    public static ArrayList<Contact> list() throws Exception {
        ArrayList<Contact> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("contacts.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Contact c = new Contact();
                String[] fields = CSV_PATTERN.split(line);
                c.setSurname(fields[0]);
                c.setName(fields[1]);
                c.setMail(fields[2]);
                c.setTelephone(fields[3]);
                c.setBirthDate(fields[4]);
                list.add(c);
            }
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append(this.getSurname());
        build.append(SEPARATOR);
        build.append(this.getName());
        build.append(SEPARATOR);
        build.append(this.getMail());
        build.append(SEPARATOR);
        build.append(this.getTelephone());
        build.append(SEPARATOR);
        build.append(this.getBirthDate());
        return build.toString();
    }

    @Override
    public int compareTo(Contact other) {
        int nomCompare = this.surname.toLowerCase().compareTo(other.surname.toLowerCase());
        if (nomCompare != 0)
            return nomCompare;
        else
            return this.name.toLowerCase().compareTo(other.name.toLowerCase());
    }

    @Override
    public int compare(Contact c1, Contact c2) {
        if (c1.getBirthDate() == null || c2.getBirthDate() == null)
            return 0;
        return c1.birthDate.compareTo(c2.birthDate);
    }
}