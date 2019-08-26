package com.example.androidmyrestaurant.Common;

import com.example.androidmyrestaurant.Model.Restaurant;
import com.example.androidmyrestaurant.Model.User;

public class Common {
    public static final String API_RESTAURANT_ENDPOINT = "http://10.0.2.2:3000/";
    public static final String API_KEY = "1234";
    public static final int DEFAULT_COLUMN_COUNT = 1;
    public static final int FULL_WIDTH_COLUMN = 0;
    public static User currentUser;
    public static Restaurant currentRestaurant;
}
