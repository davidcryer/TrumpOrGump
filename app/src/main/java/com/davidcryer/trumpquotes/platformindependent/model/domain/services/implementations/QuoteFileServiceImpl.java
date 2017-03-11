package com.davidcryer.trumpquotes.platformindependent.model.domain.services.implementations;

import com.davidcryer.trumpquotes.platformindependent.model.domain.services.QuoteFileService;
import com.davidcryer.trumpquotes.platformindependent.model.framework.localfiles.QuoteFile;
import com.davidcryer.trumpquotes.platformindependent.model.network.quotes.Quote;

import java.io.IOException;
import java.util.Random;

public class QuoteFileServiceImpl implements QuoteFileService {
    private final QuoteFile quoteFile;
    private final Random random;
    private final QuoteCache cache;

    public QuoteFileServiceImpl(QuoteFile quoteFile, Random random) {
        this.quoteFile = quoteFile;
        this.random = random;
        cache = new QuoteCache();
    }

    @Override
    public Quote randomQuote() throws IOException {
        final Quote[] quotes;
        if (cache.haveCachedQuotes()) {
            quotes = cache.quotes();
        } else {
            quotes = quotesFromFile();
            cache.cache(quotes);
        }
        return quotes[randomIndex(quotes.length)];
    }

    private Quote[] quotesFromFile() throws IOException {
        return quoteFile.quotes();
    }

    private int randomIndex(final int max) {
        return random.nextInt(max);
    }

    @Override
    public void clearCaches() {
        cache.clear();
    }

    private final static class QuoteCache {
        private Quote[] quotes;

        private void cache(final Quote[] quotes) {
            this.quotes = quotes;
        }

        private boolean haveCachedQuotes() {
            return quotes != null && quotes.length > 0;
        }

        private Quote[] quotes() {
            return quotes;
        }

        private void clear() {
            quotes = null;
        }
    }
}