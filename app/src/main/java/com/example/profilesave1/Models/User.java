package com.example.profilesave1.Models;

public class User {
    private String name,email,city,phone,sex,age,haveAnimals,haveChildren,maritalStatus,favoriteMoviesCategory, whyAreYouHere,latitude,longitude,Url;

    public User(){}
    public User(String name, String email, String city, String phone, String sex, String age, String haveAnimals, String haveChildren, String maritalStatus,
                String favoriteMoviesCategory, String whyAreYouHere, String latitude, String longitude, String Url) {
        this.name = name;
        this.email = email;
        this.city = city;
        this.phone = phone;
        this.sex = sex;
        this.age = age;
        this.haveAnimals = haveAnimals;
        this.haveChildren = haveChildren;
        this.maritalStatus = maritalStatus;
        this.favoriteMoviesCategory = favoriteMoviesCategory;
        this.whyAreYouHere = whyAreYouHere;
        this.latitude = latitude;
        this.longitude = longitude;
        this.Url="https://startplay.online/files/avatars/no_avatar.jpg";
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getHaveAnimals() {
        return haveAnimals;
    }
    public void setHaveAnimals(String haveAnimals) {
        this.haveAnimals = haveAnimals;
    }
    public String getHaveChildren() {
        return haveChildren;
    }
    public void setHaveChildren(String haveChildren) {
        this.haveChildren = haveChildren;
    }
    public String getMaritalStatus() {
        return maritalStatus;
    }
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
    public String getFavoriteMoviesCategory() {
        return favoriteMoviesCategory;
    }
    public void setFavoriteMoviesCategory(String favoriteMoviesCategory) {
        this.favoriteMoviesCategory = favoriteMoviesCategory;
    }
    public String getWhyAreYouHere() {
        return whyAreYouHere;
    }
    public void setWhyAreYouHere(String whyYouHere) {
        this.whyAreYouHere = whyYouHere;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getUrl() {
        return Url;
    }
    public void setUrl(String url) {
        this.Url = url;
    }
}