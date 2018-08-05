package com.dublin.restaurant;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dublin.restaurant.ServerCall.MakeServerCall;
import com.dublin.restaurant.ServerCall.ServerResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ServerResponse{

    @BindView(R.id.listView)
    ListView listView;

    private FusedLocationProviderClient mFusedLocationClient;

    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLocation();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    if (jsonArray != null) {
                        JSONObject object= jsonArray.getJSONObject(i);

                        String name = object.getString("name");
                        String address = object.getString("vicinity");
                        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+object.getJSONArray("photos").getJSONObject(0).getString("photo_reference")+"&sensor=false&key=AIzaSyBhA5Zpk8a9j0obu5LgS4gWZvYHGpYpN0g";

                        Intent intent = new Intent(MainActivity.this,RestaurantHomePage.class);
                        intent.putExtra("name",name);
                        intent.putExtra("address",address);
                        intent.putExtra("url",url);
                        startActivity(intent);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        setJson();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+location.getLatitude()+","+location.getLongitude()+"&radius=5000&type=restaurant&key=AIzaSyAX968fyMNrdmJnbcW5luao0a4g2zK0cH0";

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("","");

                new MakeServerCall(url,MainActivity.this,builder).getResposeByServer();


            }
        });
    }

    @Override
    public void serverResponseProcess(String response, String atType) {

        try{

            JSONObject jsonObject = new JSONObject(response);
            jsonArray = jsonObject.getJSONArray("results");
            RestaurantAdapter adapter = new RestaurantAdapter(MainActivity.this,jsonArray);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);

        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public void setJson()
    {
        JSON.json.add("" +
                "[" +
                "{\"priority\" : 1, \"title\" : \"Starters\"}," +
                "{\"priority\" : 2, \"title\" : \"Homemade Soup Of The Evening\", \"price\" : \"€4.65\"}," +
                "{\"priority\" : 2, \"title\" : \"Roast Vegetable Crostini\", \"price\" : \"€5.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Seasons Classic Caesar Salad\", \"price\" : \"€6.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Garlic Mushrooms\", \"price\" : \"€6.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Lemon And Ginger Chicken Tempura\", \"price\" : \"€6.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Chicken Wings\", \"price\" : \"€6.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Gambertetti\", \"price\" : \"€7.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Oven Baked Garlic Bread\", \"price\" : \"€4.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Mixed Starter\", \"price\" : \"€7.95\"}," +
                "{\"priority\" : 1, \"title\" : \"Main Courses\"},"+
                "{\"priority\" : 2, \"title\" : \"Dust Of Steamed Slamon And Hake\", \"price\" : \"€16.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Char Grilled Sword Fish\", \"price\" : \"€17.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Grilled Dame Of Salmon\", \"price\" : \"€16.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Deep Fried Place\", \"price\" : \"€15.00\"}," +
                "{\"priority\" : 1, \"title\" : \"Chicken Dishes\"}," +
                "{\"priority\" : 2, \"title\" : \"Roast Breast Of Chicken\", \"price\" : \"€15.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Saute Of Chicken With Clonakilty Black Pudding\", \"price\" : \"€15.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Chicken Conemara\", \"price\" : \"€15.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Classic Chicken Curry\", \"price\" : \"€13.50\"}," +
                "{\"priority\" : 1, \"title\" : \"Grills\"}," +
                "{\"priority\" : 2, \"title\" : \"Seared Saddle Of Lamb\", \"price\" : \"€21.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Pan Seared Prime Sirloin Steak\", \"price\" : \"€23.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Pan- Fried Medallions Of Pork\", \"price\" : \"€17.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Glazed Half Roast Duckling\", \"price\" : \"€18.95\"}," +
                "{\"priority\" : 2, \"title\" : \"6Oz Char Grilled Burger\", \"price\" : \"€13.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Classic Caesar Salad\", \"price\" : \"€11.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Classic Greek Salad\", \"price\" : \"€11.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Herb Roasted Vegetable Tartlet\", \"price\" : \"€13.95\"}," +
                "{\"priority\" : 1, \"title\" : \"Sides\"}," +
                "{\"priority\" : 2, \"title\" : \"French Fry Potatoes\", \"price\" : \"€3.65\"}," +
                "{\"priority\" : 2, \"title\" : \"French Fry Potatoes\", \"price\" : \"€3.65\"}," +
                "{\"priority\" : 2, \"title\" : \"Sauteed Onions\", \"price\" : \"€3.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Sauteed Onions\", \"price\" : \"€3.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Fresh Selection Of Vegetables\", \"price\" : \"€3.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Fresh Selection Of Vegetables\", \"price\" : \"€3.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Seasonal Side Salad\", \"price\" : \"€3.65\"}," +
                "{\"priority\" : 2, \"title\" : \"Seasonal Side Salad\", \"price\" : \"€3.65\"}," +
                "{\"priority\" : 2, \"title\" : \"Sauteed Potatoes\", \"price\" : \"€3.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Sauteed Potatoes\", \"price\" : \"€3.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Sauteed Mushrooms\", \"price\" : \"€3.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Sauteed Mushrooms\", \"price\" : \"€3.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Beer Battered Onion Rings\", \"price\" : \"€3.95\"}," +
                "{\"priority\" : 1, \"title\" : \"White wine\"}," +
                "{\"priority\" : 2, \"title\" : \"Bodegas Campos de Enanzo Basiano Blanco 2014, Navarra, Spain \", \"price\" : \"€7\"}," +
                "{\"priority\" : 2, \"title\" : \"Castellari Bergaglio, Cortese 2014 Gavi, Italy \", \"price\" : \"€11\"}," +
                "{\"priority\" : 1, \"title\" : \"Red wine\"}," +
                "{\"priority\" : 2, \"title\" : \"Domaine de Bertie 2014 Cotes de Thongue, France\", \"price\" : \"€7\"}," +
                "{\"priority\" : 2, \"title\" : \"Clos la Coutale Cahors 2014, Cahors, France\", \"price\" : \"€11\"}" +
        "]");

        JSON.json.add("[" +
                "{\"priority\" : 1, \"title\" : \"Soups\"}," +
                "{\"priority\" : 2, \"title\" : \"Coconut Lemongrass Chicken Soup\", \"price\" : \"€4.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Thai Tom Yam Goong Ho\", \"price\" : \"€4.95\"}," +
                "{\"priority\" : 1, \"title\" : \"House Special Dishes\"}," +
                "{\"priority\" : 2, \"title\" : \"Crispy Aromatic Paking Duck\", \"price\" : \"€15.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Bamboo Steamed Seabass In Banana Leaves\", \"price\" : \"€14.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Soy Giazed Roast Salmon Ho\", \"price\" : \"€14.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Teryake Beef & Shlltake\", \"price\" : \"€13.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Ayam Pangang\", \"price\" : \"€13.50\"}," +
                "{\"priority\" : 2, \"title\" : \"The Crying Tieger\", \"price\" : \"€14.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Wok-Fried Black Bean Chicken Ho\", \"price\" : \"€12.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Cantonese ‘Style’ Chop Suey Ho\", \"price\" : \"€10.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Thai Red Chicken Curry\", \"price\" : \"€13.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Kampung Rendang Lamb Curry\", \"price\" : \"€13.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Green Curry Black Tiger Prawns\", \"price\" : \"€14.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Kudos Best Ever Curry\", \"price\" : \"€14.95\"}," +
                "{\"priority\" : 1, \"title\" : \"Light Options\"}," +
                "{\"priority\" : 2, \"title\" : \"Thai Fish Cakes Ho\", \"price\" : \"€9.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Chargrilled Skewered King Prawns\", \"price\" : \"€8.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Kumpung Satay Delights\", \"price\" : \"€8.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Vietnamese Crispy Spring Rolls\", \"price\" : \"€8.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Thai Beef Salad\", \"price\" : \"€8.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Aromatic Crisp Roast Peking Duck\", \"price\" : \"€8.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Symphony Of Oriental Bites.. For Two\", \"price\" : \"€9.95\"}," +
                "{\"priority\" : 1, \"title\" : \"Rice & Noodle Dishes\"}," +
                "{\"priority\" : 2, \"title\" : \"Chilli Chicken Chow Mein Ho\", \"price\" : \"€12.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Singapore Noodle Ho\", \"price\" : \"€15.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Yaki Soba Ho\", \"price\" : \"€13.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Malaysian Nasi Goreng Ho\", \"price\" : \"€14.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Shanghai Duck Hoisin\", \"price\" : \"€12.95\"}," +
                "{\"priority\" : 1, \"title\" : \"Sides\"}," +
                "{\"priority\" : 2, \"title\" : \"Steamed Fragrant Thai Rice\", \"price\" : \"€2.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Garlic Fried Rice\", \"price\" : \"€2.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Wok Seasoned Egg Noodles\", \"price\" : \"€2.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Soy Sauteed Oriertal Vegetables\", \"price\" : \"€2.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Green Salad\", \"price\" : \"€2.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Shoestring Fries\", \"price\" : \"€3.00\"}," +
                "{\"priority\" : 1, \"title\" : \"European Dishes\"}," +
                "{\"priority\" : 2, \"title\" : \"Irish Seafood Cream Chowder\", \"price\" : \"€6.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Baked Terrine Of Duck Pistachio\", \"price\" : \"€8.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Classic Caesar Salad\", \"price\" : \"€8.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Fresh Fish Plate\", \"price\" : \"€9.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Melon & Orange Sorbet\", \"price\" : \"€7.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Succulent Irish Cutlets\", \"price\" : \"€24.50\"}," +
                "{\"priority\" : 2, \"title\" : \"10Oz Sirloin Steak\", \"price\" : \"€23.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Classic 8Oz Fillet Steak\", \"price\" : \"€24.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Tray Baked Crispy Supreme Of Chicken\", \"price\" : \"€15.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Breaded Plaice With Lemon Tartar\", \"price\" : \"€15.60\"}," +
                "{\"priority\" : 2, \"title\" : \"Poached Salmon\", \"price\" : \"€15.50\"}," +
                "{\"priority\" : 1, \"title\" : \"Desserts\"}," +
                "{\"priority\" : 2, \"title\" : \"Oven Baked Coconut Tart\", \"price\" : \"€5.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Mille Feulle Of Melon And Banana Ice Cream\", \"price\" : \"€5.75\"}," +
                "{\"priority\" : 2, \"title\" : \"Varlety Of Irish Cheeses\", \"price\" : \"€8.95\"}," +
                "{\"priority\" : 1, \"title\" : \"White wine\"}," +
                "{\"priority\" : 2, \"title\" : \"Bodegas Campos de Enanzo Basiano Blanco 2014, Navarra, Spain \", \"price\" : \"€7\"}," +
                "{\"priority\" : 2, \"title\" : \"Castellari Bergaglio, Cortese 2014 Gavi, Italy \", \"price\" : \"€11\"}," +
                "{\"priority\" : 1, \"title\" : \"Red wine\"}," +
                "{\"priority\" : 2, \"title\" : \"Domaine de Bertie 2014 Cotes de Thongue, France\", \"price\" : \"€7\"}," +
                "{\"priority\" : 2, \"title\" : \"Clos la Coutale Cahors 2014, Cahors, France\", \"price\" : \"€11\"}" +
        "]");

        JSON.json.add("[" +
                "{\"priority\" : 1, \"title\" : \"STARTERS\"}," +
                "{\"priority\" : 2, \"title\" : \" Cream of Mushroom Soup\", \"price\" : \"€4.95\"}," +
                "{\"priority\" : 2, \"title\" : \" Red Onion and Goats Cheese Tartlet\", \"price\" : \"€4.95\"}," +
                "{\"priority\" : 2, \"title\" : \" Chicken & Fois Gras Terrine\", \"price\" : \"€4.95\"}," +
                "{\"priority\" : 1, \"title\" : \"MAIN COURSES\"}," +
                "{\"priority\" : 2, \"title\" : \"Stuffed Turkey and Ham\", \"price\" : \"€15.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Pan Fried Hake\", \"price\" : \"€14.95\"}," +
                "{\"priority\" : 2, \"title\" : \"Braised Shank of Lamb\", \"price\" : \"€14.50\"}," +
                "{\"priority\" : 2, \"title\" : \"Saute Wild Mushroom & Herb Gnocchi\", \"price\" : \"€13.95\"}," +
                "{\"priority\" : 1, \"title\" : \"DESSERTS\"}," +
                "{\"priority\" : 2, \"title\" : \"Trio of Christmas Desserts\", \"price\" : \"€9.50\"}," +
                "{\"priority\" : 1, \"title\" : \"White wine\"}," +
                "{\"priority\" : 2, \"title\" : \"Bodegas Campos de Enanzo Basiano Blanco 2014, Navarra, Spain \", \"price\" : \"€7\"}," +
                "{\"priority\" : 2, \"title\" : \"Castellari Bergaglio, Cortese 2014 Gavi, Italy \", \"price\" : \"€11\"}," +
                "{\"priority\" : 1, \"title\" : \"Red wine\"}," +
                "{\"priority\" : 2, \"title\" : \"Domaine de Bertie 2014 Cotes de Thongue, France\", \"price\" : \"€7\"}," +
                "{\"priority\" : 2, \"title\" : \"Clos la Coutale Cahors 2014, Cahors, France\", \"price\" : \"€11\"}" +
        "]");
    }
}
