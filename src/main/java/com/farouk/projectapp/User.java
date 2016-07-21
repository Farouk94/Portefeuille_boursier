/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farouk.projectapp;

import java.math.BigDecimal;

/**
 *
 * @author farou_000
 */
class User {

    private int id;
    private String login;
    private String password;
    private int clearance;
    private BigDecimal budget;

    public User(int id, String login, String password, int clearance, BigDecimal budget) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.clearance = clearance;
        this.budget = budget;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getClearance() {
        return clearance;
    }

    public void setClearance(int clearance) {
        this.clearance = clearance;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", login=" + login + ", password=" + password + ", clearance=" + clearance + ", budget=" + budget + '}';
    }
    
    

}
