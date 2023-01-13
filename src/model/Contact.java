package model;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.Comparator;
/**
 * This class represents a contact.<br>
 * It contains the contact's surname, name, mail, telephone and birth date.<br>
 * It also contains methods to save, search, delete and update a contact.<br>
 * Description of static attributes:<br>
 * - SEPARATOR: the separator used in the CSV file.<br>
 * - CSV_PATTERN: the pattern used to split the CSV file.<br>
 * - inputPattern: the pattern used to check the input.<br>
 * Overriden methods:<br>
 * - toString(): returns a string representation of the contact.<br>
 * - compareTo(Contact c): compares the contact to another contact, used to sort the contacts by surname.<br>
 * - compare(Contact c1, Contact c2): compares two contacts, used to sort the contacts by birth date.<br>
 * Interfaces:<br>
 * - Comparable: allows to compare two contacts.<br>
 * - Comparator: allows to compare two contacts.<br>
 * @author Alexandre, Kenza, Kevin, Mathys
 * @version 1.0
 */
 
public class Contact implements Comparable<Contact>, Comparator<Contact> {
    private static final String SEPARATOR = ";";
    private static final Pattern CSV_PATTERN = Pattern.compile(SEPARATOR);
    private static final String inputPattern = "^(?:(?<=^)[^\s" + SEPARATOR + "]|^\s;)(?:[^" + SEPARATOR + "]*[^\s;])?$";
    /**
     * Attribute surname: the contact's surname.
     * type: String
     */
    private String surname;
    /**
     * Attribute name: the contact's name.
     * type: String
     */
    private String name;
    /**
     * Attribute mail: the contact's mail.
     * type: String
     */
    private String mail;
    /**
     * Attribute telephone: the contact's telephone.
     * type: String
     */
    private String telephone;
    /**
     * Attribute birthDate: the contact's birth date.
     * type: Date
     */
    private Date birthDate;
    /**
     * Getter for the surname attribute.
     * @return The contact's surname as a String.
     */
    public String getSurname() {
        return this.surname;
    }
    /**
     * Getter for the name attribute.
     * @return The contact's name as a String.
     */
    public String getName() {
        return this.name;
    }
    /**
     * Getter for the mail attribute.
     * @return The contact's mail as a String.
     */
    public String getMail() {
        return this.mail;
    }
    /**
     * Getter for the telephone attribute.
     * @return The contact's telephone as a String.
     */
    public String getTelephone() {
        return this.telephone;
    }
    /**
     * Getter for the birthDate attribute.<br>
     * It converts the birth date from Date to a String.
     * @return The contact's birth date as a String.
     */
    public String getBirthDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(this.birthDate);
    }
    /**
     * Setter for the surname attribute/ <br>
     * It checks if the surname is valid.
     * @param surname
     * The contact's surname as a String.
     * @throws ParseException
     * If the surname is not valid.
     */
    public void setSurname(String surname) throws ParseException {
        if (patternMatches(surname, inputPattern)) {
            this.surname = surname;
        } else {
            throw new ParseException("Format nom invalide", 0);
        }
    }
    /**
     * Setter for the name attribute.<br>
     * It checks if the name is valid.
     * @param name
     * The contact's name as a String.
     * @throws ParseException
     * If the name is not valid.
     */
    public void setName(String name) throws ParseException {
        if (patternMatches(name, inputPattern)) {
            this.name = name;
        } else {
            throw new ParseException("Format prenom invalide", 0);
        }
    }
    /**
     * Setter for the mail attribute.<br>
     * It checks if the mail is valid.
     * @param mail
     * The contact's mail as a String.
     * @throws ParseException
     * If the mail is not valid.
     */
    public void setMail(String mail) throws ParseException {
        if (patternMatches(mail, "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            this.mail = mail;
        } else {
            throw new ParseException("Format mail invalide", 0);
        }
    }
    /**
     * Setter for the telephone attribute.<br>
     * It checks if the telephone is valid.
     * @param telephone
     * The contact's telephone as a String.
     * @throws ParseException
     * If the telephone is not valid.
     */
    public void setTelephone(String telephone) throws ParseException {
        if (patternMatches(telephone, "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$")) {
            this.telephone = telephone;
        } else {
            throw new ParseException("Format invalide", 0);
        }
    }
    /**
     * Setter for the birthDate attribute.<br>
     * It converts the birth date from a String to a Date.
     * @param birthDate
     * The contact's birth date as a String.
     * @throws ParseException
     * If the birth date is not valid.
     */
    public void setBirthDate(String birthDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.birthDate = dateFormat.parse(birthDate);
    }
    /**
     * Checks if the input matches the pattern.
     * @param input
     * The input to check.
     * @param pattern
     * The pattern to match.
     * @return True if the input matches the pattern, false otherwise.
     */
    public static boolean patternMatches(String input, String pattern) {
        return Pattern.compile(pattern).matcher(input).matches();
    }
    /**
     * Saves the contact in the contacts.csv file.
     * @throws IOException
     *  If the file is not found.
     */
    public void save() throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("contacts.csv", true)));
        try {
            pw.println(this.toString());
        } finally {
            pw.close();
        }
    }
    /**
     * Searches a contact in the contacts.csv file.
     * @param mail
     * Mail of the contact to search.
     * @return The contact if found, null otherwise.
     */
    public static Contact search(String mail) throws ParseException {
        if (!patternMatches(mail, "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
        throw new ParseException("Format de mail invalide", 0);
        }
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
        } catch (FileNotFoundException e) {
            System.out.println("Il n'y a pas de contact.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche du contact");

        }
        return null;
    }
    /**
     * Deletes a contact from the contacts.csv file.
     * @param mail
     * Mail of the contact to delete.
     */
    public static void delete(String mail) {
      try {
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
      } catch (FileNotFoundException e) {
        System.out.println("Il n'y a pas de contact.");
      } catch (Exception e) {
        System.out.println("Erreur lors de la suppression du contact");
    }
  }
  /**
   * Updates a contact in the contacts.csv file.
   * @param mail
   * Mail of the contact to update.
   * @param updatedContact
   * The updated contact.
   */
    public static void updateContact(String mail, Contact updatedContact) {
        try {
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
      } catch (FileNotFoundException e) {
        System.out.println("Il n'y a pas de contact.");
      } catch (Exception e) {
        System.out.println("Erreur lors de la mise à jour du contact");
      }
    }
    /**
     * Lists all the contacts in the contacts.csv file.
     * @return An ArrayList of all the contacts.
     */
    public static ArrayList<Contact> list() {
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
        } catch (FileNotFoundException e) {
            System.out.println("Il n'y a pas de contact.");
        } catch (Exception e) {
            System.out.println("Erreur lors du listing des contacts");
        }
        return list;
    }
    /**
     * Overrides the toString method.
     * @return a string representation of the contact.
     */
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
    /**
     * Overrides the compareTo method of the Comparable interface.<br>
     * This is used to sort the contacts by surname and then by name if the surnames are the same.
     * @param other
     * The contact to compare to.
     * @return An integer representing the result of the comparison.<br>
     * If the result is negative, the current contact is before the other contact.<br>
     * If the result is positive, the current contact is after the other contact.<br>
     * If the result is 0, the current contact is the same as the other contact, then the comparison is done by name.
     */
    @Override
    public int compareTo(Contact other) {
        int nomCompare = this.surname.toLowerCase().compareTo(other.surname.toLowerCase());
        if (nomCompare != 0)
            return nomCompare;
        else
            return this.name.toLowerCase().compareTo(other.name.toLowerCase());
    }
    /**
     * Overrides the compare method of the Comparator interface.<br>
     * This is used to sort the contacts by birth date.
     * @param c1
     * The first contact to compare.
     * @param c2
     * The second contact to compare.
     * @return an integer representing the result of the comparison.<br>
     * If the result is negative, the first contact is before the second contact.<br>
     * If the result is positive, the first contact is after the second contact.<br>
     * If the result is 0, the first contact is the same as the second contact.
     */
    @Override
    public int compare(Contact c1, Contact c2) {
        if (c1.getBirthDate() == null || c2.getBirthDate() == null)
            return 0;
        return c1.birthDate.compareTo(c2.birthDate);
    }
}
