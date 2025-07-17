package com.poly.DTO;

public class TopProductDTO {

    private Integer productID;
    private String productName;
    private String imageURL;
    private Long soldQuantity;

    public TopProductDTO() {
    }

    // Constructor matching query result
    public TopProductDTO(Integer productID, String productName, String imageURL, Long soldQuantity) {
        this.productID = productID;
        this.productName = productName;
        this.imageURL = imageURL;
        this.soldQuantity = soldQuantity;
    }

    // Getters & Setters
    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Long getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Long soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}
