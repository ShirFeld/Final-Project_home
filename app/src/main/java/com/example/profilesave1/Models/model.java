package com.example.profilesave1.Models;

public class model
{
    String city,name,Url,age, whyAreYouHere,haveAnimals;

    public model() {
    }
    public model( String name,String Url,String age) {
        this.name = name;
        this.Url = Url;
        this.age=age;
    }

    public model(String city, String name, String Url,String age,String whyAreYouHere,String haveAnimals) {
        this.city = city;
        this.name = name;
        this.Url = Url;
        this.age=age;
        this.whyAreYouHere = whyAreYouHere;
        this.haveAnimals = haveAnimals;
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