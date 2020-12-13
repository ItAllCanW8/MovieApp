package com.example.movieapp.repositories;

import androidx.lifecycle.LiveData;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.request.MovieApiClient;

import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;

    private MovieApiClient movieApiClient;

    private String mQuery;
    private int mPageNumber;

    public static MovieRepository getInstance() {
        if(instance == null)
            instance = new MovieRepository();

        return instance;
    }

    private MovieRepository(){
         movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies() {return movieApiClient.getMovies();}
    public LiveData<List<MovieModel>> getMoviesPop() {return movieApiClient.getMoviesPop();}

    public void searchMovies(String query, int pageNumber){
        mQuery = query;
        mPageNumber = pageNumber;

        movieApiClient.searchMovies(query, pageNumber);
    }

    public void searchMoviesPop(int pageNumber){
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesPop(pageNumber);
    }

    public void nextPage(){
        searchMovies(mQuery, mPageNumber + 1);
    }
}
