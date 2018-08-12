package com.dublin.restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestaurantAdapter extends BaseAdapter {

    Context context;
    JSONArray jsonArray;

    public RestaurantAdapter(Context context, JSONArray jsonArray)
    {
        this.context = context;
        this.jsonArray = jsonArray;
    }
    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return jsonArray.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.resturant_element, null, true);

        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView ratingText = (TextView) convertView.findViewById(R.id.ratingText);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        ImageView navigate = (ImageView) convertView.findViewById(R.id.navigate);
        RatingBar ratings = (RatingBar) convertView.findViewById(R.id.ratings);


        try {
            JSONObject obj = (JSONObject) jsonArray.get(position);
            name.setText(obj.getString("name"));
            ratingText.setText(obj.getString("rating"));
            ratings.setFocusable(false);
            ratings.setRating((float) obj.getDouble("rating"));

            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=300&photoreference="+obj.getJSONArray("photos").getJSONObject(0).getString("photo_reference")+"&sensor=false&key=AIzaSyDc3QXM7OvzfTvmZb_tx1VF-phhCV3md-8";

            Glide.with(context)
                    .load(url)
                    .apply(new RequestOptions().centerCrop())
                    .into(imageView);

            JSONObject object = obj.getJSONObject("geometry").getJSONObject("location");
            navigate.setTag(obj.getString("name")+","+obj.getString("vicinity")+"");
            navigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String address = view.getTag().toString();
                    address = address.replace('#','N');
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("geo:0,0?q="+address));
                    context.startActivity(intent);
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
}
