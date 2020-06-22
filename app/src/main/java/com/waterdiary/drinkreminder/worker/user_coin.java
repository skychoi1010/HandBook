package com.waterdiary.drinkreminder.worker;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class user_coin {

    public String coins;

    public user_coin(){}
    public user_coin(String coins)
    {
        this.coins = coins;
    }
}