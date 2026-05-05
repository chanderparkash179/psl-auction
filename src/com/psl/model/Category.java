package com.psl.model;

public class Category {

    private int categoryId;
    private String categoryName;
    private double basePrice;
    private boolean isDeleted;

    public Category() {}

    public Category(int categoryId, String categoryName, double basePrice, boolean isDeleted) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.basePrice = basePrice;
        this.isDeleted = isDeleted;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}