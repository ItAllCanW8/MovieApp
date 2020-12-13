package com.example.movieapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieRepository movieRepository;

    public MovieListViewModel(){
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movieRepository.getMovies();
    }
    public LiveData<List<MovieModel>> getMoviesPop() {
        return movieRepository.getMoviesPop();
    }

    public void searchMovies(String query, int pageNumber){
        movieRepository.searchMovies(query, pageNumber);
    }

    public void searchMoviesPop(int pageNumber){
        movieRepository.searchMoviesPop(pageNumber);
    }

    public void nextPage(){
        movieRepository.nextPage();
    }
}
