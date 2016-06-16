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

package com.asanchezyu.retrofitsoap.domain.interactors.search;

import com.asanchezyu.retrofitsoap.api.UsStatesApi;
import com.asanchezyu.retrofitsoap.api.model.request.UsStatesRequestBody;
import com.asanchezyu.retrofitsoap.api.model.request.UsStatesRequestData;
import com.asanchezyu.retrofitsoap.api.model.request.UsStatesRequestEnvelope;
import com.asanchezyu.retrofitsoap.api.model.response.UsStatesResponseEnvelope;
import com.asanchezyu.retrofitsoap.domain.threads.InteractorExecutor;
import com.asanchezyu.retrofitsoap.domain.threads.MainThread;
import com.asanchezyu.retrofitsoap.util.ZipCodeMapperDomain;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Class Description.
 *
 * @author asanchezyu@gmail.com.
 * @version 1.0.
 * @since 15/6/16.
 */
public class SearchCodesByCityInteractorImpl implements SearchCodesByCityInteractor {

    private String cityName;

    private Callback callback;

    @Inject
    UsStatesApi usStatesApi;

    @Inject
    ZipCodeMapperDomain zipCodeMapperDomain;

    @Inject
    MainThread mainThread;

    @Inject
    InteractorExecutor interactorExecutor;

    @Inject
    public SearchCodesByCityInteractorImpl() {
    }

    @Override
    public void execute(String cityName, Callback callback) {

        this.cityName = cityName;

        this.callback = callback;

        interactorExecutor.run( this );

    }

    @Override
    public void run() {

        //Creation of the envelope and the message.
        UsStatesRequestEnvelope envelope = new UsStatesRequestEnvelope();

        UsStatesRequestBody body = new UsStatesRequestBody();

        UsStatesRequestData data = new UsStatesRequestData();

        data.setCity( cityName );

        body.setUsStatesRequestData(data);

        envelope.setBody(body);

        Call<UsStatesResponseEnvelope> call = usStatesApi.requestStateInfo(envelope);

        call.enqueue(new retrofit2.Callback<UsStatesResponseEnvelope>() {
            @Override
            public void onResponse(Call<UsStatesResponseEnvelope> call, final Response<UsStatesResponseEnvelope> response) {

                mainThread.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        callback.onSuccess(zipCodeMapperDomain.reverseMapList(response.body().getBody().getData().getData().getElements()));

                    }

                });

            }

            @Override
            public void onFailure(Call<UsStatesResponseEnvelope> call, Throwable t) {

                mainThread.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        callback.onError();

                    }
                });


            }
        });


    }
}
