package com.davidcryer.trumpquotes.platformindependent.model.domain.interactors;

import com.davidcryer.trumpquotes.platformindependent.model.domain.entities.QuizGame;
import com.davidcryer.trumpquotes.platformindependent.model.domain.entities.QuizQuestion;
import com.davidcryer.trumpquotes.platformindependent.model.domain.entities.TrumpQuizGameImpl;
import com.davidcryer.trumpquotes.platformindependent.model.domain.services.TrumpQuizGameInitialisationService;
import com.davidcryer.trumpquotes.platformindependent.model.domain.services.TrumpQuizGameStorageService;
import com.davidcryer.trumpquotes.platformindependent.model.domain.services.errors.InitialisationError;
import com.davidcryer.trumpquotes.platformindependent.model.domain.services.errors.StorageError;
import com.davidcryer.trumpquotes.platformindependent.model.framework.tasks.Task;
import com.davidcryer.trumpquotes.platformindependent.model.framework.tasks.TaskScheduler;

import java.lang.ref.WeakReference;

public final class InitialiseGameInteractor extends Interactor {
    private final static int QUESTION_COUNT = 10;
    private final InteractorFactory interactorFactory;
    private final TrumpQuizGameInitialisationService initialisationService;

    InitialiseGameInteractor(
            TaskScheduler taskScheduler,
            InteractorFactory interactorFactory,
            TrumpQuizGameInitialisationService initialisationService
    ) {
        super(taskScheduler);
        this.interactorFactory = interactorFactory;
        this.initialisationService = initialisationService;
    }

    public void runTask(final WeakReference<Callback> callback) {
        executeOnWorkerThread(new Task() {
            @Override
            public void execute() {
                tryToInitialiseNewGameInstance(callback);
            }
        });
    }

    private void tryToInitialiseNewGameInstance(final WeakReference<Callback> callback) {
        initialisationService.initialiseNewGame(QUESTION_COUNT, new TrumpQuizGameInitialisationService.Callback() {//TODO replace hardcoded int
            @Override
            public void onSuccess(QuizGame game) {
                tryToStartGame(game, true, callback);
            }

            @Override
            public void onFailure(InitialisationError error) {
                onError(callback);
            }
        });
    }

    private void tryToStartGame(final QuizGame game, final boolean isNewGame, final WeakReference<Callback> callback) {
        game.startGame(new QuizGame.StartCallback() {
            @Override
            public void onReturn(int correctAnswers, int questionsAnswered) {
                tryToGetNextQuote(game, correctAnswers, questionsAnswered, isNewGame, callback);
            }
        });
    }

    private void tryToGetNextQuote(
            final QuizGame game,
            final int correctAnswers,
            final int questionsAnswered,
            final boolean isNewGame,
            final WeakReference<Callback> callback
    ) {
        game.nextQuestion(new QuizGame.NextQuestionCallback() {
            @Override
            public void onGameFinished() {
                if (isNewGame) {
                    onError(callback);
                } else {
                    tryToInitialiseNewGameInstance(callback);
                }
            }

            @Override
            public void nextQuestion(QuizQuestion quizQuestion) {
                onSuccess(payload(game, quizQuestion, correctAnswers, questionsAnswered, isNewGame), callback);
            }
        });
    }

    private void onSuccess(final Payload payload, final WeakReference<Callback> callback) {
        executeOnMainThread(new Task() {
            @Override
            public void execute() {
                if (callback.get() != null) {
                    callback.get().onInitialiseGame(payload);
                }
            }
        });
    }

    private void onError(final WeakReference<Callback> callback) {
        executeOnMainThread(new Task() {
            @Override
            public void execute() {
                if (callback.get() != null) {
                    callback.get().onError();
                }
            }
        });
    }

    private Payload payload(
            final QuizGame game,
            final QuizQuestion quizQuestion,
            final int correctAnswers,
            final int questionsAnswered,
            final boolean isNewGame
    ) {
        return new Payload(
                new ActiveGameInteractors(
                        interactorFactory.createAnswerNotTrumpInteractor(game),
                        interactorFactory.createGetNextQuoteInteractor(game)
                ),
                quizQuestion,
                correctAnswers,
                questionsAnswered,
                isNewGame
        );
    }

    public final static class Payload {
        public final ActiveGameInteractors activeGameInteractors;
        public final QuizQuestion quizQuestion;
        public final int correctAnswers;
        public final int questionsAnswered;
        public final boolean isNewGame;

        private Payload(
                ActiveGameInteractors activeGameInteractors,
                QuizQuestion quizQuestion,
                int correctAnswers,
                int questionsAnswered,
                boolean isNewGame
        ) {
            this.activeGameInteractors = activeGameInteractors;
            this.quizQuestion = quizQuestion;
            this.correctAnswers = correctAnswers;
            this.questionsAnswered = questionsAnswered;
            this.isNewGame = isNewGame;
        }
    }

    public interface Callback {
        void onInitialiseGame(final Payload payload);
        void onError();
    }
}
