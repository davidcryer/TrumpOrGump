package com.davidcryer.trumpquotes.android.model.network.retrofit.quotes.trumpapi;

import com.davidcryer.trumpquotes.android.model.network.retrofit.quotes.RetrofitQuoteRequest;
import com.davidcryer.trumpquotes.platformindependent.model.framework.network.Request;
import com.davidcryer.trumpquotes.platformindependent.model.framework.network.RequestCallback;
import com.davidcryer.trumpquotes.platformindependent.model.network.quotes.Quote;
import com.davidcryer.trumpquotes.platformindependent.model.network.quotes.QuoteRequestFactory;

import java.lang.ref.WeakReference;

public class RetrofitTrumpQuoteRequestFactory implements QuoteRequestFactory {
    private final RetrofitTrumpQuoteService quoteService;

    public RetrofitTrumpQuoteRequestFactory(RetrofitTrumpQuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @Override
    public Request randomQuoteRequest(final RequestCallback<Quote> requestCallback) {
        return new RetrofitQuoteRequest<>(quoteService.randomQuote(), new WeakReference<>(requestCallback));
    }
}
