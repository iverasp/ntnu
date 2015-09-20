/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import org.jdesktop.swingx.mapviewer.GeoPosition;

/**
 * Instances of this class contain information about {@link Sheep}.
 */

public class Sheep {
    private int id;
    private Farmer farmer;
    private String name;
    private String health;
    private char gender;
    private double weight;
    private String dateOfBirth;
    private String dateOfDeath;
    private GeoPosition location;

    /**
     * Constructor for creating a new {@link Sheep}.
     * @param farmer
     * @param name
     * @param dateOfBirth
     * @param gender
     * @param health
     * @param weight
     */
    public Sheep(Farmer farmer, String name, String dateOfBirth, 
            char gender, String health, double weight) {
        this.farmer = farmer;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.weight = weight;
        this.gender = gender;
        this.health = health;
    }
  
    /**
     * Constructor used on already created {@link Sheep}.
     * @param id
     * @param farmer
     * @param name
     * @param dateOfBirth
     * @param location
     * @param dateOfDeath
     * @param gender
     * @param health
     * @param weight
     */
    public Sheep(int id, Farmer farmer, String name, String dateOfBirth, 
            GeoPosition location, String dateOfDeath, char gender, 
            String health, double weight) {
        this.id = id;
        this.farmer = farmer;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.dateOfDeath = dateOfDeath;
        this.weight = weight;
        this.gender = gender;
        this.health = health;
        this.location = location;
    }

    @Override
    public String toString(){
        return "#" + id + " - " + name;
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the {@link Farmer}
     */
    public Farmer getFarmer() {
        return farmer;
    }

    /**
     * @param farmer the {@link Farmer} to set
     */
    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the health
     */
    public String getHealth() {
        return health;
    }

    /**
     * @param health the health to set
     */
    public void setHealth(String health) {
        this.health = health;
    }

    /**
     * @return the gender
     */
    public char getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return the dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return the dateOfDeath
     */
    public String getDateOfDeath() {
        return dateOfDeath;
    }

    /**
     * @param dateOfDeath the dateOfDeath to set
     */
    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    /**
     * @return the location
     */
    public GeoPosition getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(GeoPosition location) {
        this.location = location;
    }
}
