package com.project.mobile.movie_db_training.person;

import com.project.mobile.movie_db_training.BuildConfig;
import com.project.mobile.movie_db_training.data.model.CreditResponse;
import com.project.mobile.movie_db_training.data.model.Person;
import com.project.mobile.movie_db_training.network.NetworkModule;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonPresenterImpl implements PersonContract.Presenter {
    private PersonContract.View mView;

    @Override
    public void fetchPerson(String personId) {
        NetworkModule.getTMDbService()
                .getPersonDetail(personId, BuildConfig.TMDB_API_KEY)
                .enqueue(new Callback<Person>() {
                    @Override
                    public void onResponse(Call<Person> call, Response<Person> response) {
                        if (response.body() != null & response.isSuccessful()) {
                            mView.showPersonDetail(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Person> call, Throwable t) {

                    }
                });
    }

    @Override
    public void fetchCredits(String personId) {
        NetworkModule.getTMDbService().getMovieCredits(personId, BuildConfig.TMDB_API_KEY)
                .enqueue(new Callback<CreditResponse>() {
                    @Override
                    public void onResponse(Call<CreditResponse> call, Response<CreditResponse> response) {
                        if (response.body() != null)
                            mView.showCredits(response.body().getCredits());
                    }

                    @Override
                    public void onFailure(Call<CreditResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void setView(PersonContract.View view) {
        mView = view;
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
