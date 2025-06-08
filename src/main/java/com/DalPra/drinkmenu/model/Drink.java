package com.DalPra.drinkmenu.model;

import java.sql.Timestamp;

public class Drink {
    private int id;
    private String name;
    private String ingredients;
    private String instructions;
    private String imageUrl;
    private int rating;
    private Timestamp createdAt;

    public Drink() {}

    public Drink(int id, String name, String ingredients, String instructions, String imageUrl, int rating, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.createdAt = createdAt;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}