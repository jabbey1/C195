package model;

public class Customer {
    private int id;
    private String name;
    private int addressId;
    private String address;
    private String address2;
    private String city;
    private String country;
    private String postal;
    private String phone;

    public Customer(int id, String name, int addressId, String address, String address2, String city, String country, String postal, String phone) {
        this.id = id;
        this.name = name;
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.country = country;
        this.postal = postal;
        this.phone = phone;
    }

    //gettersetters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



}
