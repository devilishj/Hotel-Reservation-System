package model;

import java.util.Objects;
import java.util.regex.Pattern;

/**Customer model
 *
 * @author Jason Renek
 */
public class Customer {
    public String firstName;   // Add variable String firstName
    public String lastName;    // Add variable String lastName
    public String email;       // Add variable String email

    public Customer(String firstName,
                    String lastName,
                    String email) {

        // Add regex
        String emailRegex = "^(.+)@(.+).com$";
        Pattern pattern = Pattern.compile(emailRegex);
        if(!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Error, Invalid Email");  // throw illegal argument
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public boolean equals (Object that) {               // Override equals and hashcode... prevent duplicate customers
        if (this == that) return true;
        if(!(that instanceof Customer thatCustomer)) return false;

        return Objects.equals(this.email, ((Customer) that).email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.email);
    }

    @Override
    public String toString() {
        return "First Name: " + this.firstName + ",  "
                + "Last Name: " + this.lastName + ",  "
                + "\nEmail: " + this.email +".\n";
    }
}