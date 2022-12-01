package model;

import java.util.regex.Pattern;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;
    private final String emailRegex = "^(.+)@(.+).(.+)$";
    private final Pattern pattern = Pattern.compile(emailRegex);

    public Customer(String firstName, String lastName, String email) {
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Error, Invalid Email address");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }

    // Get and Set First Name
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    // Get and Set Last Name
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    // Get and Set email address
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    @Override
    public String toString() {
        return  String.format("%-13s", firstName) +
                String.format("%-13s", lastName) +
                email;
    }
}
