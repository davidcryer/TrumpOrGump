package com.davidcryer.trumpquotes.platformindependent.view;

import com.davidcryer.trumpquotes.platformindependent.view.viewmodels.SwipeQuoteMvpViewModel;
import com.davidcryer.trumpquotes.platformindependent.view.viewmodels.models.ViewQuestion;

public interface SwipeQuestionView<ViewQuestionType extends ViewQuestion> extends MvpView<SwipeQuoteMvpViewModel> {
    void showScore(final int correctAnswerCount, final int questionCount);
    void showStartNewGameState();
    void showLoadingState();
    void showFailureToStartGameState();
    void showNewGameTutorial();
    void dismissNewGameTutorial();
    void showQuestionState(final ViewQuestionType quotes);
    void showFinishedGameState();

    interface EventsListener extends MvpView.EventsListener {
        void onInitialise();
        void onClickStartNewGame();
        void onDismissNewGameTutorial();
        void onAnswerOptionA();
        void onAnswerOptionB();
    }
}
