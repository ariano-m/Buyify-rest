package com.buyify.rest.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(message = "Date may not be null")
    private Date date;

    @Size(min = 10, max = 144)
    private String text;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    protected Review() {
    }

    public Review(Product product, User user, Date date, String text) {
        this.product = product;
        this.user = user;
        this.date = date;
        this.text = text;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String review) {
        this.text = review;
    }

}
