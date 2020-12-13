package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movieapp.models.MovieModel;

public class MovieDetails extends AppCompatActivity {

    private ImageView imageViewDetails;
    private TextView titleDetails, descDetails;
    private RatingBar ratingBarDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imageViewDetails = findViewById(R.id.imageViewDetails);
        titleDetails = findViewById(R.id.textView_title_details);
        descDetails = findViewById(R.id.textView_desc_details);
        ratingBarDetails = findViewById(R.id.ratingBar_details);

        GetData();

    }

    private void GetData() {
        if(getIntent().hasExtra("movie")) {
            MovieModel movieModel = getIntent().getParcelableExtra("movie");
//            Log.v("Tag", "intent " + movieModel.getTitle());

            titleDetails.setText(movieModel.getTitle());
            descDetails.setText(movieModel.getMovie_overview());

            ratingBarDetails.setRating((movieModel.getVote_average()) / 2);

            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500/"
                    + movieModel.getPoster_path())
                    .into(imageViewDetails);

        }
    }
}