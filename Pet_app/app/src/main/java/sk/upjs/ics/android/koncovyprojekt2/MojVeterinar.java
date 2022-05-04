package sk.upjs.ics.android.koncovyprojekt2;

public class MojVeterinar {
    String firstname;
    String lastname;
    String address;
    String phone;
    String email;
    String imageUrl;

    public MojVeterinar (String address, String email, String firstname, String imageUrl, String lastname, String phone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "MojVeterinar{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
