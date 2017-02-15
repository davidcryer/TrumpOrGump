package com.davidcryer.trumpquotes.android.model.quotes.store.tasks;

import com.davidcryer.trumpquotes.android.framework.tasks.Task;
import com.davidcryer.trumpquotes.android.framework.tasks.factories.TaskFactory;
import com.davidcryer.trumpquotes.platformindependent.model.quotes.store.QuoteStore;

public class UpdateQuoteAsJudgedTask extends Task<UpdateQuoteAsJudgedTask.RequestValues, Void> {
    private final QuoteStore quoteStore;

    private UpdateQuoteAsJudgedTask(RequestValues requestValues, Callback<Void> callback, QuoteStore quoteStore) {
        super(requestValues, callback);
        this.quoteStore = quoteStore;
    }

    @Override
    protected void doTask(RequestValues requestValues) {
        if (quoteStore.updateQuoteAsJudged(requestValues.quoteId)) {
            onSuccess(null);
        } else {
            onError();
        }
    }

    public static class RequestValues {
        private final String quoteId;

        public RequestValues(String quoteId) {
            this.quoteId = quoteId;
        }
    }

    public static class Factory implements TaskFactory<UpdateQuoteAsJudgedTask.RequestValues, Void> {
        private final QuoteStore quoteStore;

        public Factory(QuoteStore quoteStore) {
            this.quoteStore = quoteStore;
        }

        @Override
        public Task<UpdateQuoteAsJudgedTask.RequestValues, Void> create(UpdateQuoteAsJudgedTask.RequestValues requestValues, Task.Callback<Void> callback) {
            return new UpdateQuoteAsJudgedTask(requestValues, callback, quoteStore);
        }
    }
}