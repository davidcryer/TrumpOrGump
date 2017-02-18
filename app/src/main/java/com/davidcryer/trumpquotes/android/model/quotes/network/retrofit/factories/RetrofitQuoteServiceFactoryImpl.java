package com.davidcryer.trumpquotes.android.model.quotes.network.retrofit.factories;

import android.content.res.Resources;

import com.davidcryer.trumpquotes.R;
import com.davidcryer.trumpquotes.android.model.quotes.network.retrofit.RetrofitQuoteService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitQuoteServiceFactoryImpl implements RetrofitQuoteServiceFactory {
    private final Resources resources;

    public RetrofitQuoteServiceFactoryImpl(Resources resources) {
        this.resources = resources;
    }

    @Override
    public RetrofitQuoteService create() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(resources.getString(R.string.quotes_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RetrofitQuoteService.class);
    }
}
