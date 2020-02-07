package com.project.mobile.movie_db_training.person;

import com.project.mobile.movie_db_training.data.model.Credit;
import com.project.mobile.movie_db_training.data.model.Person;

import java.util.List;

public interface PersonContract {
    interface View {
        void showPersonDetail(Person person);

        void showCredits(List<Credit> credits);
    }

    interface Presenter {
        void fetchPerson(String personId);

        void fetchCredits(String personId);

        void setView(PersonContract.View view);

        void destroy();
    }
}
