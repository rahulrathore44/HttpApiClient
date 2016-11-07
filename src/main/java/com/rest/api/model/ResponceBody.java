package com.rest.api.model;


import java.util.ArrayList;
import java.util.List;

class Apartment
{
    public String id;
    public String address;
    public int price;
    public int square;
    public List<String> features = new ArrayList<String>();
    public boolean active;
}

public class ResponceBody
{
    public String id;
    public String firstName;
    public String lastName;
    public boolean trusted;
    public List<Apartment> apartments = new ArrayList<Apartment>(); 
}
