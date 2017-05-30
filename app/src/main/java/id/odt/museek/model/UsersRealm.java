package id.odt.museek.model;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by arrival on 5/2/17.
 */

@RealmClass
public class UsersRealm extends RealmObject {
    public String id;
    public String email;
    public String firstname;
    public String lastname;
    public String location;
    public String type;
    public String works;

    public UsersRealm() {
        this.id = "";
        this.email = "";
        this.firstname = "";
        this.lastname = "";
        this.location = "";
        this.type = "";
        this.works = "";
    }

    public UsersRealm(String id, String email, String firstname, String lastname,
                      String location, String type, String works) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.location = location;
        this.type = type;
        this.works = works;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorks() {
        return works;
    }

    public void setWorks(String works) {
        this.works = works;
    }
}
