package models;


import at.ac.tuwien.big.we15.lab2.api.Avatar;
import play.data.format.Formats;
import play.data.validation.ValidationError;
import play.i18n.Messages;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class User{

    private static final String TEXT = Messages.get("required.userName");

    @Id
    private String username;

    private String password;

    private String firstname;

    private String lastname;

    private Avatar avatar;

    private String avatarName;

    @Formats.DateTime(pattern = "dd.MM.yyyy")
    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (username == null || username.isEmpty()) {
            errors.add(new ValidationError("username", Messages.get("required.username")));
        } else if(username.length() < 4) {
            errors.add(new ValidationError("username", Messages.get("toShort.username")));
        } else if(username.length() > 8) {
            errors.add(new ValidationError("username", Messages.get("toLong.username")));
        }

        if (password == null || password.isEmpty()) {
            errors.add(new ValidationError("password", Messages.get("required.password")));
        } else if(password.length() < 4) {
            errors.add(new ValidationError("password", Messages.get("toShort.password")));
        } else if(password.length() > 8) {
            errors.add(new ValidationError("password", Messages.get("toLong.password")));
        }
        if(errors.isEmpty()) {
            errors = null;
        }
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User that = (User) o;

        if (birthdate != null ? !birthdate.equals(that.birthdate) : that.birthdate != null) return false;
        if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) return false;
        if (gender != that.gender) return false;
        if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (birthdate != null ? birthdate.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleUser{" +
                "name='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthdate=" + birthdate +
                ", gender=" + gender +
                ", avatar=" + avatar +
                ",avatarName=" + avatarName+
                '}';
    }
}
