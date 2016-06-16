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

package com.asanchezyu.retrofitsoap.injection.zip;

import android.support.v7.widget.RecyclerView;

import com.asanchezyu.retrofitsoap.api.model.response.TableElement;
import com.asanchezyu.retrofitsoap.domain.interactors.search.SearchCodesByCityInteractor;
import com.asanchezyu.retrofitsoap.domain.model.ZipCodeDomain;
import com.asanchezyu.retrofitsoap.domain.threads.InteractorExecutor;
import com.asanchezyu.retrofitsoap.domain.threads.MainThread;
import com.asanchezyu.retrofitsoap.injection.ActivityScope;
import com.asanchezyu.retrofitsoap.injection.application.ApplicationComponent;
import com.asanchezyu.retrofitsoap.presenters.ZipCodesPresenter;
import com.asanchezyu.retrofitsoap.ui.model.ZipCodeData;
import com.asanchezyu.retrofitsoap.ui.views.MainActivity;
import com.asanchezyu.retrofitsoap.util.EntityMapper;

import dagger.Component;

/**
 * Class Description.
 *
 * @author asanchezyu@gmail.com.
 * @version 1.0.
 * @since 15/6/16.
 */
@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = ZipCodeModule.class
)
public interface ZipCodeComponent {

    void inject(MainActivity activity);

    RecyclerView.Adapter providesZipCodesAdapter();

    ZipCodesPresenter providesPresenter();

    EntityMapper<ZipCodeData,ZipCodeDomain> providesUiMapper();

    EntityMapper<ZipCodeDomain,TableElement> providesDomainMapper();

    SearchCodesByCityInteractor providesInteractor();

    InteractorExecutor providesInteractorExecutor();

    MainThread providesMainThread();

}
