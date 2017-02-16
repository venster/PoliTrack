package acm2017polyhack.politrack;

/**
 * Created by clarios6 on 2/12/17.
 */

public class Person {
    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getEmail() {
        return email;
    }

    public String getParty() {
        return party;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public String fName;
    public String lName;
    public String email;
    public String party;
    public String phone;
    public String website;



    public Person(String fName, String lName, String email, String party, String phone, String website){
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.party = party;
        this.phone = phone;
        this.website = website;
    }

}


