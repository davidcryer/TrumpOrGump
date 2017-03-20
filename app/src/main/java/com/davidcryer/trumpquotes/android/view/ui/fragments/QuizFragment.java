package com.davidcryer.trumpquotes.android.view.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.davidcryer.trumpquotes.R;
import com.davidcryer.trumpquotes.android.framework.viewwrapperrepositories.ViewUnbindType;
import com.davidcryer.trumpquotes.android.framework.viewwrapperrepositories.ViewWrapperRepository;
import com.davidcryer.trumpquotes.android.view.ui.QuizAndroidView;
import com.davidcryer.trumpquotes.android.view.ui.components.QuoteCard;
import com.davidcryer.trumpquotes.android.view.ui.components.SwipeLayout;
import com.davidcryer.trumpquotes.android.view.ui.helpers.AlphaAnimationHelper;
import com.davidcryer.trumpquotes.android.view.ui.helpers.OnGlobalLayoutHelper;
import com.davidcryer.trumpquotes.android.view.ui.helpers.nongeneric.StartNewGameContainerAnimationHelper;
import com.davidcryer.trumpquotes.android.view.ui.swipe.SwipeDelegate;
import com.davidcryer.trumpquotes.android.view.viewmodels.models.AndroidViewQuestion;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class QuizFragment extends ViewBindingFragment<QuizAndroidView.EventsListener> implements QuizAndroidView {
    private Unbinder unbinder;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.swipe_layout)
    SwipeLayout swipeLayout;
    @BindView(R.id.start_new_game_container)
    View startNewGameContainer;
    @BindView(R.id.start_new_game_info)
    TextView startNewGameInfoTextView;
    @BindView(R.id.start_new_game_button)
    Button startNewGameButton;
    @BindView(R.id.score)
    TextView scoreTextView;

    public static QuizFragment newInstance() {
        return new QuizFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_quotes, container, false);
        initialiseViewReferences(view);
        setupViews();
        return view;
    }

    private void initialiseViewReferences(final View root) {
        unbinder = ButterKnife.bind(this, root);
    }

    private void setupViews() {
        swipeRefreshLayout.setEnabled(false);
        swipeLayout.swipeListener(new SwipeDelegate.Listener() {
            @Override
            public void onViewEscapedLeft(View child) {
                if (hasEventsListener()) {
                    eventsListener().onAnswerOptionA();
                }
                swipeLayout.removeView(child);
            }

            @Override
            public void onViewEscapedRight(View child) {
                if (hasEventsListener()) {
                    eventsListener().onAnswerOptionB();
                }
                swipeLayout.removeView(child);
            }

            public void onViewMoved(final View child, float percentageOffsetFromCentreX) {
                if (child instanceof QuoteCard) {
                    ((QuoteCard) child).updateSignatureAlpha(percentageOffsetFromCentreX);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.quotes_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (hasEventsListener()) {
            eventsListener().onViewCreated();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (hasEventsListener()) {
            eventsListener().onSaveInstance(outState);
        }
    }

    @Override
    public void showScore(int correctAnswerCount, int questionCount) {
        scoreTextView.setText(String.format(getString(R.string.score_format), correctAnswerCount, questionCount));
    }

    @Override
    public void showNewGameStateStart() {
        showStartNewGameViews();
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        startNewGameInfoTextView.setText(getString(R.string.start_game_info_description));
        AlphaAnimationHelper.fadeIn(startNewGameButton, getResources().getInteger(R.integer.max_duration_fade_score_ms));
        startNewGameButton.setEnabled(true);
        startNewGameButton.setText(getString(R.string.start_game_button));
    }

    @Override
    public void showNewGameStateLoading() {
        showStartNewGameViews();
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        AlphaAnimationHelper.fadeOut(startNewGameButton, getResources().getInteger(R.integer.max_duration_fade_score_ms));
    }

    @Override
    public void showNewGameStateError() {
        showStartNewGameViews();
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        startNewGameInfoTextView.setText(getString(R.string.start_game_info_description));
        AlphaAnimationHelper.fadeIn(startNewGameButton, getResources().getInteger(R.integer.max_duration_fade_score_ms));
        startNewGameButton.setEnabled(true);
        startNewGameButton.setText(getString(R.string.start_game_button));
    }

    @Override
    public void hideNewGameState() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        hideStartNewGameContainer();
    }

    @Override
    public void showPlayGameStateTutorial() {
        showPlayGameViews();
    }

    @Override
    public void showPlayGameStateRunning() {
        showPlayGameViews();
    }

    @Override
    public void showPlayGameStateFinished(int correctAnswerCount, int questionCount) {
        showStartNewGameViews();
        startNewGameInfoTextView.setText(String.format(getString(R.string.start_game_info_finished), correctAnswerCount, questionCount));
        AlphaAnimationHelper.fadeIn(startNewGameButton, 0);
        startNewGameButton.setEnabled(true);
        startNewGameButton.setText(getString(R.string.start_game_button));
    }

    @Override
    public void hidePlayGameState() {
        hidePlayGameViews();
    }

    @Override
    public void showQuestion(final AndroidViewQuestion question) {
        final int horizontalMargin = getResources().getDimensionPixelOffset(R.dimen.card_margin_horizontal);
        final int offscreenY = swipeRefreshLayout.getHeight() + getResources().getDimensionPixelOffset(R.dimen.card_buffer_offscreen);
        final QuoteCard card = new QuoteCard(getActivity());
        OnGlobalLayoutHelper.listen(card, new OnGlobalLayoutHelper.PreLayoutCallback() {
            @Override
            public void onPreLayout() {
                card.quote(question.quote());
                card.signatures(question.optionA(), question.optionB());
                card.updateSignatureAlpha(0);
                final FrameLayout.LayoutParams cardLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cardLp.leftMargin = horizontalMargin;
                cardLp.rightMargin = horizontalMargin;
                card.setLayoutParams(cardLp);
                card.setY(offscreenY);
                swipeLayout.addView(card);
            }
        }, new OnGlobalLayoutHelper.PostLayoutCallback() {
            @Override
            public void onPostLayout() {
                card.animate()
                        .y((swipeRefreshLayout.getHeight() - card.getHeight()) / 2.0f)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(300)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                swipeLayout.listenForChildGestures(card, true);
                            }
                        })
                        .start();
            }
        });
    }

    private void showStartNewGameViews() {
        StartNewGameContainerAnimationHelper.slideIn(startNewGameContainer, swipeRefreshLayout);
    }

    private void hideStartNewGameContainer() {
        StartNewGameContainerAnimationHelper.slideOut(startNewGameContainer, swipeRefreshLayout);
    }

    private void showPlayGameViews() {
        showScoreView();
    }

    private void hidePlayGameViews() {
        hideScoreView();
    }

    private void showScoreView() {
        AlphaAnimationHelper.fadeIn(scoreTextView, getResources().getInteger(R.integer.max_duration_fade_score_ms));
    }

    private void hideScoreView() {
        AlphaAnimationHelper.fadeOut(scoreTextView, getResources().getInteger(R.integer.max_duration_fade_score_ms));
    }

    @OnClick(R.id.start_new_game_button)
    public void startNewGame() {
        if (hasEventsListener()) {
            eventsListener().onClickStartNewGame();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected QuizAndroidView.EventsListener bind(ViewWrapperRepository viewWrapperRepository, String instanceId, Bundle savedState) {
        return viewWrapperRepository.bind(this, instanceId, savedState);
    }

    @Override
    protected void unbind(ViewWrapperRepository viewWrapperRepository, String instanceId, ViewUnbindType unbindType) {
        viewWrapperRepository.unbind(this, instanceId, unbindType);
    }

    @Override
    public Activity activity() {
        return getActivity();
    }
}
