package com.example.profilesave1.Models;

public class model
{
    String city,name,Url,age, whyAreYouHere,haveAnimals,sex , maritalStatus , haveChildren , favoriteMoviesCategory;

    public model() {
    }
    public model( String name,String Url,String age) {
        this.name = name;
        this.Url = Url;
        this.age=age;
    }
    public model(String city, String name, String Url,String age,String whyAreYouHere,String haveAnimals ,  String sex , String maritalStatus , String haveChildren, String favoriteMoviesCategory ) {
        this.city = city;
        this.name = name;
        this.Url = Url;
        this.age=age;
        this.whyAreYouHere = whyAreYouHere;
        this.haveAnimals = haveAnimals;
        this.sex = sex;
        this.maritalStatus = maritalStatus;
        this.haveChildren = haveChildren;
        this.favoriteMoviesCategory = favoriteMoviesCategory;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getMaritalStatus() {
        return maritalStatus;
    }
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
    public String getHaveChildren() {
        return haveChildren;
    }
    public void setHaveChildren(String haveChildren) {
        this.haveChildren = haveChildren;
    }
    public String getFavoriteMoviesCategory() {
        return favoriteMoviesCategory;
    }
    public void setFavoriteMoviesCategory(String favoriteMoviesCategory) {
        this.favoriteMoviesCategory = favoriteMoviesCategory;
    }
    public String getHaveAnimals() {
        return haveAnimals;
    }
    public void setHaveAnimals(String haveAnimals) {
        this.haveAnimals = haveAnimals;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String email) {
        this.city = email;
    }
    public String getWhyAreYouHere() {
        return whyAreYouHere;
    }
    public void setWhyAreYouHere(String whyAreYouHere) {
        this.whyAreYouHere = whyAreYouHere;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return Url;
    }
    public void setUrl(String url) {
        this.Url = url;
    }
}