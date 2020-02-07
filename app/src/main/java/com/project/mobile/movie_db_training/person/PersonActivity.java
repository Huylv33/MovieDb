package com.project.mobile.movie_db_training.person;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AppCompatActivity;

import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.data.model.Credit;
import com.project.mobile.movie_db_training.data.model.Person;
import com.project.mobile.movie_db_training.utils.Constants;
import com.squareup.picasso.Picasso;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonActivity extends AppCompatActivity implements PersonContract.View {
    @BindView(R.id.image_backdrop)
    ImageView mImageBackdrop;
    @BindView(R.id.image_profile)
    ImageView mImageProfile;
    @BindView(R.id.text_name)
    TextView mName;
    @BindView(R.id.text_birthday)
    TextView mBirthday;
    @BindView(R.id.text_biography)
    TextView mBiography;
    @BindView(R.id.rv_credits)
    MultiSnapRecyclerView creditsRv;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    private PersonContract.Presenter mPresenter;
    private CreditAdapter mAdapter;
    private List<Credit> mCredits = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        initToolbar("Person");
        Intent intent = getIntent();
        if (intent == null) return;
        mAdapter = new CreditAdapter(mCredits);
        creditsRv.setAdapter(mAdapter);
        creditsRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        String id = intent.getStringExtra("personId");
        mPresenter = new PersonPresenterImpl();
        mPresenter.setView(this);
        mPresenter.fetchPerson(id);
        mPresenter.fetchCredits(id);
    }

    @Override
    public void showPersonDetail(Person person) {
        Picasso.get().load(Constants.POSTER_BASE_URL + person.getProfilePath()).into(mImageBackdrop);
        Picasso.get().load(Constants.POSTER_BASE_URL + person.getProfilePath()).into(mImageProfile);
        mName.setText(person.getName());
        mBirthday.setText(person.getBirthday());
        mBiography.setText(person.getBiography());
    }

    @Override
    public void showCredits(List<Credit> credits) {
        mCredits.addAll(credits);
        mAdapter.notifyDataSetChanged();
    }
    private void initToolbar(String title) {
        setSupportActionBar(mToolBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(title);
        }
        mToolBar.setNavigationOnClickListener(view -> {
            finish();
        });
    }
}
