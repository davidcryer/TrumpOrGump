package com.davidcryer.trumpquotes.android.model.quotes.store.tasks;

import com.davidcryer.trumpquotes.android.framework.tasks.Task;
import com.davidcryer.trumpquotes.android.framework.tasks.factories.TaskFactory;
import com.davidcryer.trumpquotes.platformindependent.model.quotes.store.QuoteStore;

public class ClearQuotesTask extends Task<ClearQuotesTask.RequestValues, Void> {
    private final QuoteStore quoteStore;

    private ClearQuotesTask(RequestValues requestValues, Callback<Void> callback, QuoteStore quoteStore) {
        super(requestValues, callback);
        this.quoteStore = quoteStore;
    }

    @Override
    protected void doTask(RequestValues requestValues) {
        if (quoteStore.clear(requestValues.quoteIds)) {
            onSuccess(null);
        } else {
            onError();
        }
    }

    public final static class RequestValues {
        private final String[] quoteIds;

        public RequestValues(String[] quoteIds) {
            this.quoteIds = quoteIds;
        }
    }

    public static class Factory implements TaskFactory<ClearQuotesTask.RequestValues, Void> {
        private final QuoteStore quoteStore;

        public Factory(QuoteStore quoteStore) {
            this.quoteStore = quoteStore;
        }

        @Override
        public Task<ClearQuotesTask.RequestValues, Void> create(ClearQuotesTask.RequestValues requestValues, Task.Callback<Void> callback) {
            return new ClearQuotesTask(requestValues, callback, quoteStore);
        }
    }
}
