package websiteschema.mpsegment.web.ui.model;

import javax.persistence.*;

@Entity
@Table(
    name = "USERS",
    uniqueConstraints = {@UniqueConstraint(name = "email_unique", columnNames = "email")}
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name="users_id_seq", sequenceName="users_id_seq")
    private int id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;


    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


