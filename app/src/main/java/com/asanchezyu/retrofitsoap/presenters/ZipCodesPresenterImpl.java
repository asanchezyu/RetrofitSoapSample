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

package com.asanchezyu.retrofitsoap.presenters;

import com.asanchezyu.retrofitsoap.domain.interactors.search.SearchCodesByCityInteractor;
import com.asanchezyu.retrofitsoap.domain.model.ZipCodeDomain;
import com.asanchezyu.retrofitsoap.ui.views.CityZipsView;
import com.asanchezyu.retrofitsoap.util.ZipCodeMapperUi;

import java.util.List;

import javax.inject.Inject;

/**
 * Class Description.
 *
 * @author asanchezyu@gmail.com.
 * @version 1.0.
 * @since 15/6/16.
 */
public class ZipCodesPresenterImpl implements ZipCodesPresenter, SearchCodesByCityInteractor.Callback {

    @Inject
    CityZipsView view;

    @Inject
    ZipCodeMapperUi zipCodeMapper;

    @Inject
    SearchCodesByCityInteractor searchCodesByCityInteractor;

    @Inject
    public ZipCodesPresenterImpl() {

    }

    @Override
    public void makeSearch(String city) {

        if( city == null || city.isEmpty() ){

            view.showCityNeededErrorMessage();

        }else{

            view.showWaitingDialog();

            searchCodesByCityInteractor.execute( city, this );

        }
    }

    @Override
    public void onSuccess(List<ZipCodeDomain> list) {

        view.hideWaitingDialog();

        view.setCityZips( zipCodeMapper.reverseMapList( list ) );

    }

    @Override
    public void onError() {

        view.hideWaitingDialog();

        view.showErrorInRequest();

    }
}
