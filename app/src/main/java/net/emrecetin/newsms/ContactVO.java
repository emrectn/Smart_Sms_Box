package net.emrecetin.newsms;


public class ContactVO {
    private int id;
    private int ContactImage;
    private String ContactName;
    private String ContactNumber;

    public ContactVO(int contactImage, String contactName, String contactNumber) {

        ContactImage = contactImage;
        ContactName = contactName;
        ContactNumber = contactNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContactImage() {
        return ContactImage;
    }

    public void setContactImage(int contactImage) {
        ContactImage = contactImage;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String toString() {
        return "id : " + id + " name : " + ContactName + " tel : " + ContactNumber;
    }
}

