package com.davidcryer.trumpquotes.platformindependent.view;

import com.davidcryer.trumpquotes.platformindependent.view.viewmodels.QuotesMvpViewModel;
import com.davidcryer.trumpquotes.platformindependent.view.viewmodels.models.ViewQuote;

import java.util.List;

public interface QuotesView<ViewQuoteType extends ViewQuote> extends MvpView<QuotesMvpViewModel<ViewQuoteType>> {
    void showLoadingNewQuote();
    void showNewQuote(final ViewQuoteType quote);
    void addNewQuoteToHistory(final ViewQuoteType quote);
    void showQuoteHistory(final List<ViewQuoteType> quotes);
    void removeQuoteInHistory(final ViewQuoteType quote);
    void removeAllQuotesInHistory();

    interface EventsListener extends MvpView.EventsListener {
        void onCreate();
        void onNewQuoteSwipedLeft();
        void onNewQuoteSwipedRight();
        void onDeleteQuoteInHistoryClicked(final int index);
        void onDeleteAllQuotesClicked();
    }
}
