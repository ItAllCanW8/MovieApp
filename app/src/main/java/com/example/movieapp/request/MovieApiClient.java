package com.example.movieapp.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.AppExecutors;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    private MutableLiveData<List<MovieModel>> mMovies;
    private MutableLiveData<List<MovieModel>> mMoviesPop;
    private static MovieApiClient instance;

    private RetrieveMoviesRunnable retrieveMoviesRunnable;
    private RetrieveMoviesPopRunnable retrieveMoviesPopRunnable;

    public static MovieApiClient getInstance(){
        if(instance == null)
            instance = new MovieApiClient();

        return instance;
    }

    private MovieApiClient(){
        mMovies = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }
    public LiveData<List<MovieModel>> getMoviesPop(){
        return mMoviesPop;
    }

    public void searchMovies(String query, int pageNumber){
        if(retrieveMoviesRunnable != null)
        {
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // timeout, cancel the call
                myHandler.cancel(true);
            }
        }, 5000, TimeUnit.MILLISECONDS);

    }

    public void searchMoviesPop(int pageNumber){
        if(retrieveMoviesPopRunnable != null)
        {
            retrieveMoviesPopRunnable = null;
        }

        retrieveMoviesPopRunnable = new RetrieveMoviesPopRunnable(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveMoviesPopRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // timeout, cancel the call
                myHandler2.cancel(true);
            }
        }, 5000, TimeUnit.MILLISECONDS);

    }

    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancel;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancel = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovies(query, pageNumber).execute();

                if (cancel) {
                    return;
                }

                if(response.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if(pageNumber == 1){
                        mMovies.postValue(list);
//                        Log.v("Tagg","page num = 1");
                    }
                    else{
//                        Log.v("Tagg","page num != 1");
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
//                        Log.v("Tagg","posted");
                    }

                } else {
                    String error = response.errorBody().string();
//                    Log.v("Tag","Error " + error);
                    mMovies.postValue(null);

                }

            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }


        }

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber){
            return Servicey.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );
        }

        private void cancelRequest(){
//            Log.v("Tag","Cancelling search request");
            cancel = true;
        }

    }

    private class RetrieveMoviesPopRunnable implements Runnable {

        private int pageNumber;
        boolean cancel;

        public RetrieveMoviesPopRunnable(int pageNumber) {
            this.pageNumber = pageNumber;
            cancel = false;
        }

        @Override
        public void run() {
            try {
                Response response2 = getMoviesPop(pageNumber).execute();

                if (cancel) {
                    return;
                }

                if(response2.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if(pageNumber == 1){
                        mMoviesPop.postValue(list);
//                        Log.v("Tagg","page num = 1");
                    }
                    else{
//                        Log.v("Tagg","page num != 1");
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
//                        Log.v("Tagg","posted");
                    }

                } else {
                    String error = response2.errorBody().string();
                    mMovies.postValue(null);

                }

            } catch (IOException e) {
                e.printStackTrace();
                mMoviesPop.postValue(null);
            }
        }

        private Call<MovieSearchResponse> getMoviesPop(int pageNumber){
            return Servicey.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );
        }

        private void cancelRequest(){
//            Log.v("Tag","Cancelling search request");
            cancel = true;
        }

    }
}
