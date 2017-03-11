package com.davidcryer.trumpquotes.platformindependent.model.domain.services.implementations;

import com.davidcryer.trumpquotes.platformindependent.model.domain.services.QuoteFileService;
import com.davidcryer.trumpquotes.platformindependent.model.domain.services.QuoteNetworkService;
import com.davidcryer.trumpquotes.platformindependent.model.domain.services.ServiceFactory;
import com.davidcryer.trumpquotes.platformindependent.model.domain.services.TrumpQuizGameInitialisationService;
import com.davidcryer.trumpquotes.platformindependent.model.domain.services.TrumpQuizGameStorageService;
import com.davidcryer.trumpquotes.platformindependent.model.framework.localfiles.QuoteFile;
import com.davidcryer.trumpquotes.platformindependent.model.network.quotes.requesters.RandomQuoteRequester;
import com.davidcryer.trumpquotes.platformindependent.model.store.stores.TrumpQuizGameStore;
import com.davidcryer.trumpquotes.platformindependent.model.store.stores.TrumpQuizQuestionStore;

public class ServiceFactoryImpl implements ServiceFactory {
    private final QuoteFile gumpQuoteFile;
    private final RandomQuoteRequester trumpQuoteRequester;
    private final TrumpQuizGameStore gameStore;
    private final TrumpQuizQuestionStore questionStore;

    public ServiceFactoryImpl(
            QuoteFile gumpQuoteFile,
            RandomQuoteRequester trumpQuoteRequester,
            TrumpQuizGameStore gameStore,
            TrumpQuizQuestionStore questionStore
    ) {
        this.gumpQuoteFile = gumpQuoteFile;
        this.trumpQuoteRequester = trumpQuoteRequester;
        this.gameStore = gameStore;
        this.questionStore = questionStore;
    }

    @Override
    public QuoteFileService createQuoteFileService(final QuoteFile quoteFile) {
        return new QuoteFileServiceImpl(quoteFile);
    }

    @Override
    public QuoteNetworkService createQuoteNetworkService(final RandomQuoteRequester quoteRequester) {
        return new QuoteNetworkServiceImpl(quoteRequester);
    }

    @Override
    public TrumpQuizGameInitialisationService createTrumpQuizGameInitialisationService() {
        return new TrumpQuizGameInitialisationServiceImpl(createQuoteNetworkService(trumpQuoteRequester), createQuoteFileService(gumpQuoteFile));
    }

    @Override
    public TrumpQuizGameStorageService createTrumpQuizGameStorageService() {
        return new TrumpQuizGameStorageServiceImpl(gameStore, questionStore);
    }
}
