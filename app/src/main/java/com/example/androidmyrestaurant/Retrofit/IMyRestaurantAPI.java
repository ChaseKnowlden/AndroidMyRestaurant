package com.example.androidmyrestaurant.Retrofit;

import com.example.androidmyrestaurant.Model.FoodModel;
import com.example.androidmyrestaurant.Model.MenuModel;
import com.example.androidmyrestaurant.Model.RestaurantModel;
import com.example.androidmyrestaurant.Model.UpdateUserModel;
import com.example.androidmyrestaurant.Model.UserModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IMyRestaurantAPI {
    @GET("user")
    Observable<UserModel> getUser(@Query("key") String apiKey,
                                  @Query("fbid") String fbid);

    @GET("restaurant")
    Observable<RestaurantModel> getRestaurant(@Query("key") String apiKey);

    @GET("food")
    Observable<FoodModel> getFoodOfMenu(@Query("key") String apiKey,
                                        @Query("menuId") int menuId);

    @GET("menu")
    Observable<MenuModel> getCategories(@Query("key") String apiKey,
                                        @Query("restaurantId") int restaurantId);

    @POST("user")
    @FormUrlEncoded
    Observable<UpdateUserModel> updateUserInfo(@Field("key") String apiKey,
                                               @Field("userPhone") String userPhone,
                                               @Field("userName") String userName,
                                               @Field("userAddress") String userAddress,
                                               @Field("fbid") String fbid);
}
