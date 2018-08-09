package com.dublin.restaurant;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantHomePage extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.layout)
    LinearLayout layout;

    @BindView(R.id.backArrow)
    ImageView backArrow;

    JSONArray jsonArray;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home_page);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        name.setText(intent.getStringExtra("name"));
        address.setText(intent.getStringExtra("address"));
        String url = intent.getStringExtra("url");

        Glide.with(this)
                .load(url)
                .apply(new RequestOptions().centerCrop())
                .into(imageView);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            int max = 2,min = 0;
            Random random = new Random();
            int j = random.nextInt(max - min + 1) + min;
            jsonArray = new JSONArray(JSON.json.get(j));
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                addViewToLayout(object);
            }

            View view = new View(RestaurantHomePage.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
            view.setLayoutParams(lp);
            view.setBackgroundColor(ContextCompat.getColor(RestaurantHomePage.this,R.color.white));
            layout.addView(view);


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addViewToLayout(JSONObject object) {

        try {
            int priority = object.getInt("priority");
            String title = object.getString("title");
            if(priority == 1)
            {
                final TextView textView = new TextView(RestaurantHomePage.this);
                textView.setText(title);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                lp2.setMargins(0,20,0,0);
                textView.setPadding(20, 40, 10, 40);
                textView.setTextSize(18);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setBackgroundColor(ContextCompat.getColor(RestaurantHomePage.this,R.color.white));
                textView.setLayoutParams(lp2);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout.addView(textView);
                        layout.requestLayout();
//                        layout.invalidate();
                    }
                });
            }
            else
            {

                String price = object.getString("price");
                final LinearLayout linearLayout = new LinearLayout(RestaurantHomePage.this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                final TextView textView = new TextView(RestaurantHomePage.this);
                textView.setText(title);
                textView.setPadding(20, 30, 10, 20);
                textView.setTextSize(16);
                textView.setBackgroundColor(ContextCompat.getColor(RestaurantHomePage.this,R.color.white));


                final TextView textView2 = new TextView(RestaurantHomePage.this);
                textView2.setText(price);
                textView2.setPadding(20, 30, 10, 20);
                textView2.setTextSize(16);
                textView2.setBackgroundColor(ContextCompat.getColor(RestaurantHomePage.this,R.color.white));


                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp2.setMargins(0,0,0,0);
                lp3.weight = 1;
                lp4.weight = 4;

                textView.setLayoutParams(lp3);
                textView2.setLayoutParams(lp4);
                linearLayout.setLayoutParams(lp2);
                linearLayout.setBackgroundColor(ContextCompat.getColor(RestaurantHomePage.this,R.color.white));

                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView2.setGravity(Gravity.CENTER_VERTICAL);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayout.addView(textView);
                        linearLayout.addView(textView2);
                        layout.addView(linearLayout);
                        layout.requestLayout();
//                        layout.invalidate();
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
