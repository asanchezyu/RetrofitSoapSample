/*
 * Copyright 2016. Alejandro Sánchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.asanchezyu.retrofitsoap.ui.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.asanchezyu.retrofitsoap.R;
import com.asanchezyu.retrofitsoap.RetrofitSoapApplication;
import com.asanchezyu.retrofitsoap.injection.zip.DaggerZipCodeComponent;
import com.asanchezyu.retrofitsoap.injection.zip.ZipCodeComponent;
import com.asanchezyu.retrofitsoap.injection.zip.ZipCodeModule;
import com.asanchezyu.retrofitsoap.presenters.ZipCodesPresenter;
import com.asanchezyu.retrofitsoap.ui.adapter.ZipCodesAdapter;
import com.asanchezyu.retrofitsoap.ui.model.ZipCodeData;

import java.util.List;

import javax.inject.Inject;

/**
 * Class Description.
 *
 * @author asanchezyu@gmail.com.
 * @version 1.0.
 * @since 15/6/16.
 */
public class MainActivity extends AppCompatActivity implements CityZipsView {

    @Inject
    ZipCodesPresenter presenter;

    @Inject
    ZipCodesAdapter adapter;


    private ProgressDialog progressDialog;

    private RecyclerView rvElements;

    private EditText etCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initInjection();

        etCityName = (EditText) findViewById(R.id.et_city_name);

        rvElements = (RecyclerView) findViewById(R.id.rv_elements);

        rvElements.setLayoutManager(new LinearLayoutManager(this));

        rvElements.setAdapter(adapter);

        (findViewById(R.id.bt_make_request)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hideKeyboard();

                presenter.makeSearch(etCityName.getText().toString());

            }

        });

    }

    @Override
    public void setCityZips(List<ZipCodeData> cityZips) {

        adapter.setZipCodeDataList(cityZips);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void showCityNeededErrorMessage() {

        Toast.makeText(this, R.string.enter_city_name, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showErrorInRequest() {
        Toast.makeText(this, R.string.request_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWaitingDialog() {

        if (progressDialog == null) {

            progressDialog = new ProgressDialog( this );

            progressDialog.setIndeterminate( true );

            progressDialog.setProgressStyle( ProgressDialog.STYLE_SPINNER );

            progressDialog.setTitle( getString( R.string.dialog_title ) );

            progressDialog.setMessage( getString( R.string.dialog_message ) );

        }

        progressDialog.show();
    }

    @Override
    public void hideWaitingDialog() {

        if (progressDialog.isShowing()) {

            progressDialog.hide();

        }

    }


    private void initInjection() {

        ZipCodeComponent zipCodeComponent = DaggerZipCodeComponent.builder()
                .applicationComponent(((RetrofitSoapApplication) getApplication()).getComponent())
                .zipCodeModule(new ZipCodeModule(this))
                .build();

        zipCodeComponent.inject(this);

    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
