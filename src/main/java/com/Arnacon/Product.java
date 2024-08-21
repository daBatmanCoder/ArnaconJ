package com.Arnacon;


public class Product {

    private String ens;
    // private String serviceProvider;
    private ProductType state; // PORTING // Status 
    private String  type; // ENS/ GSM/ FREE...

    // private String alias;
    // private String description;
    // private boolean inviteBySource; 

    // private String  startDate;
    // private String  expirationDate;
    // private int     groupID;
    // private int     limit; // how many allowed in a specific group

  
    public Product(String ens, ProductType state) {
        this.ens = ens;
        this.state = state;
    }

    public String getENS() {
        return ens;
    }   

    public ProductType getState() {
        return state;
    }

    public void setState(ProductType state) {
        this.state = state;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    

    // public String getAlias() {
    //     return alias;
    // }

    // public String getDescription() {
    //     return description;
    // }

    // public boolean inviteBySource() {
    //     return sourceOrNot;
    // }


}

