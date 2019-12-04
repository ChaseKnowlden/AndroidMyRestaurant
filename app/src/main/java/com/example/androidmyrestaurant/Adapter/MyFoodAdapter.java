package com.example.androidmyrestaurant.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmyrestaurant.Common.Common;
import com.example.androidmyrestaurant.Database.CartDataSource;
import com.example.androidmyrestaurant.Database.CartDatabase;
import com.example.androidmyrestaurant.Database.CartItem;
import com.example.androidmyrestaurant.Database.LocalCartDataSource;
import com.example.androidmyrestaurant.Interface.IFoodDetailOrCartClickListener;
import com.example.androidmyrestaurant.Model.Food;
import com.example.androidmyrestaurant.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MyFoodAdapter extends RecyclerView.Adapter<MyFoodAdapter.MyViewHolder> {

    Context context;
    List<Food> foodList;
    CompositeDisposable compositeDisposable;
    CartDataSource cartDataSource;

    public void onStop(){
        compositeDisposable.clear();
    }

    public MyFoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
        compositeDisposable = new CompositeDisposable();
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_food,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(foodList.get(position).getImage())
                .placeholder(R.drawable.app_icon)
                .into(holder.img_food);
        holder.txt_food_name.setText(foodList.get(position).getName());
        holder.txt_food_price.setText(new StringBuilder(context.getString(R.string.money_sign)).append(foodList.get(position).getPrice()));

        holder.setListener((view, position1, isDetail) -> {
            if (isDetail)
                Toast.makeText(context, "Detail Click", Toast.LENGTH_SHORT).show();
            else
            {
                CartItem cartItem = new CartItem();
                cartItem.setFoodId(foodList.get(position).getId());
                cartItem.setFoodName(foodList.get(position).getName());
                cartItem.setFoodPrice(foodList.get(position).getPrice());
                cartItem.setFoodImage(foodList.get(position).getImage());
                cartItem.setFoodQuantity(1);
                cartItem.setUserPhone(Common.currentUser.getUserPhone());
                cartItem.setRestaurantId(Common.currentRestaurant.getId());
                cartItem.setFoodAddon("NORMAL");
                cartItem.setFoodSize("NORMAL");
                cartItem.setFoodExtraPrice(0.0);

                compositeDisposable.add(
                        cartDataSource.insertOrReplaceAll(cartItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
                                },
                                throwable -> {
                                    Toast.makeText(context, "[ADD CART]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                })
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_food)
        ImageView img_food;
        @BindView(R.id.txt_food_name)
        TextView txt_food_name;
        @BindView(R.id.txt_food_price)
        TextView txt_food_price;
        @BindView(R.id.img_detail)
        ImageView img_detail;
        @BindView(R.id.img_cart)
        ImageView img_add_cart;

        IFoodDetailOrCartClickListener listener;

        public void setListener(IFoodDetailOrCartClickListener listener) {
            this.listener = listener;
        }

        Unbinder unbinder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);

            img_detail.setOnClickListener(this);
            img_add_cart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.img_detail)
                listener.onFoodItemClickListener(v,getAdapterPosition(),true);
            else if (v.getId() == R.id.img_cart)
                listener.onFoodItemClickListener(v,getAdapterPosition(),false);

        }
    }
}
