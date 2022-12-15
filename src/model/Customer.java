package model;

import java.util.regex.Pattern;

public class Customer {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private final String emailRegexPattern = "^(.+)@(.+).com$";
    private final Pattern pattern = Pattern.compile(emailRegexPattern);

    public Customer(String firstName, String lastName, String emailAddress) {
        if (!pattern.matcher(emailAddress).matches()) {
            throw new IllegalArgumentException("Sorry, your e-mail address is improperly formatted.");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;

    }

    // get first name
    public String getFirstName()
    {return firstName;}


    // get last name
    public String getLastName()
    {return lastName;}


    // get e-mail address
    public String getEmail()
    {return emailAddress;}


    @Override
    public String toString() {
        return "First Name: " + firstName +
                " Last Name: " + lastName +
                " E-mail Address: " + emailAddress;
    }
}
