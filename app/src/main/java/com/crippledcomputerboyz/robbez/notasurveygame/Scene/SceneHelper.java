package com.crippledcomputerboyz.robbez.notasurveygame.Scene;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.crippledcomputerboyz.robbez.notasurveygame.GameLogic.GameDecision;
import com.crippledcomputerboyz.robbez.notasurveygame.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robbez on 5/23/16.
 */
public class SceneHelper {
    private VideoView[] mVideoViews;
    private Button mChoice1, mChoice2;
    private ProgressDialog mProgressDialog;
    private Boolean[] mPreparedFlags;
    private String mPackageName;
    private ArrayList<GameDecision> mDecisions;


    private static final Map<Integer, Scene> mVideoResources;
    static{
        Map<Integer, Scene> temp = new HashMap<Integer, Scene>();
        temp = new HashMap<Integer, Scene>();
        temp.put(R.raw.main_final, new Scene("Main", R.raw.main_final, R.raw.left_main, R.raw.right_main));
        temp.put(R.raw.left_main, new Scene("Go Left", R.raw.left_main, R.raw.left_a, R.raw.left_b));
        temp.put(R.raw.left_a, new Scene("Go Down", R.raw.left_a, -1, -1));
        temp.put(R.raw.left_b, new Scene("Go Up", R.raw.left_b, -1, -1));
        temp.put(R.raw.right_main, new Scene("Go Right", R.raw.right_main, R.raw.right_a, R.raw.right_b));
        temp.put(R.raw.right_a, new Scene("Sword", R.raw.right_a, -1, -1));
        temp.put(R.raw.right_b, new Scene("Box", R.raw.right_b, -1, -1));
        mVideoResources = Collections.unmodifiableMap(temp);
    }

    private int mCurrentSceneIndex;
    private int mCurrentInUseVideoViewIndex;

    private OnGameFinishedListener mGameFinishedCallback;

    public interface OnGameFinishedListener {
        void onGameFinished(ArrayList<GameDecision> decisions);
    }

    public SceneHelper(VideoView[] videoViews, Button choice1, Button choice2, ProgressDialog progressDialog, String packageName, OnGameFinishedListener gameCallback) {
        this.mVideoViews = videoViews;
        this.mChoice1 = choice1;
        this.mChoice2 = choice2;
        this.mProgressDialog = progressDialog;
        this.mPreparedFlags = new Boolean[videoViews.length];
        this.mPackageName = packageName;
        this.mCurrentSceneIndex = R.raw.main_final;
        this.mGameFinishedCallback = gameCallback;
        this.mDecisions = new ArrayList<>(3);
        this.mCurrentInUseVideoViewIndex = 0;
        initializePreparedFlags();
        setupVideoViews();
        initializeChoiceButtons();
    }

    private void initializeChoiceButtons() {
        this.mChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoViews[mCurrentInUseVideoViewIndex].setVisibility(View.INVISIBLE);
                mDecisions.add(new GameDecision(mCurrentSceneIndex, mChoice1.getText().toString()));
                mCurrentSceneIndex = mVideoResources.get(mCurrentSceneIndex).getLeftChoice();
                mCurrentInUseVideoViewIndex = (mCurrentInUseVideoViewIndex + 1) % mVideoViews.length;
                playScene(mCurrentInUseVideoViewIndex, mCurrentSceneIndex);
            }
        });

        this.mChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoViews[mCurrentInUseVideoViewIndex].setVisibility(View.INVISIBLE);
                mDecisions.add(new GameDecision(mCurrentSceneIndex, mChoice2.getText().toString()));
                mCurrentSceneIndex = mVideoResources.get(mCurrentSceneIndex).getRightChoice();
                mCurrentInUseVideoViewIndex = (mCurrentInUseVideoViewIndex + 2) % mVideoViews.length;
                playScene(mCurrentInUseVideoViewIndex, mCurrentSceneIndex);
            }
        });
    }

    public void startFirstScene() {
        prepareAndShowProgressDialog();
        playScene(mCurrentInUseVideoViewIndex, mCurrentSceneIndex);
    }

    private void playScene(int videoViewIndex, int sceneIndex) {
        final boolean hasNext = mVideoResources.get(sceneIndex).getLeftChoice() != -1;
        setVideoResource(videoViewIndex, sceneIndex);
        this.mVideoViews[videoViewIndex].requestFocus();
        this.mVideoViews[videoViewIndex].setVisibility(View.VISIBLE);
        if(hasNext) {
            prepareNextScenes(videoViewIndex, sceneIndex);
        }

        if(sceneIndex != R.raw.main_final) {
            this.mVideoViews[videoViewIndex].start();
        }

    }

    private void prepareNextScenes(int videoViewIndex, int sceneIndex) {
        setVideoResource((videoViewIndex + 1) % mVideoViews.length, mVideoResources.get(sceneIndex).getLeftChoice());
        setVideoResource((videoViewIndex + 2) % mVideoViews.length, mVideoResources.get(sceneIndex).getLeftChoice());
    }

    private void setVideoResource(int videoViewIndex, int sceneIndex) {
        this.mVideoViews[videoViewIndex].setVideoURI(Uri.parse("android.resource://" + this.mPackageName + "/" + mVideoResources.get(sceneIndex).getSceneResource()));
    }


    private void prepareAndShowProgressDialog() {
        this.mProgressDialog.setTitle("Best Game Ever");
        this.mProgressDialog.setMessage("Loading...");
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.show();
    }

    private void initializePreparedFlags() {
        for (int i = 0; i < this.mPreparedFlags.length; i++)
        {
            this.mPreparedFlags[i] = false;
        }
    }

    private void setupVideoViews() {
        for (int i = 0; i < this.mVideoViews.length; i++)
        {
            final VideoView currView = this.mVideoViews[i];
            final Boolean[] currFlag = {this.mPreparedFlags[i]};


            currView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    currFlag[0] = true;
                    mChoice1.setVisibility(View.INVISIBLE);
                    mChoice2.setVisibility(View.INVISIBLE);

                    final boolean isFirst = mCurrentSceneIndex == R.raw.main_final;
                    if(isFirst) {
                        mProgressDialog.dismiss();
                        currView.start();
                    }
                }
            });

            currView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currFlag[0] = false;
                    final boolean hasNext = mVideoResources.get(mCurrentSceneIndex).getLeftChoice() != -1;

                    final String choice1Title, choice2Title;

                    if(hasNext) {
                        int left = mVideoResources.get(mCurrentSceneIndex).getLeftChoice();
                        int right = mVideoResources.get(mCurrentSceneIndex).getRightChoice();

                        choice1Title = mVideoResources.get(left).getDecisionTitle();
                        choice2Title = mVideoResources.get(right).getDecisionTitle();
                    } else {
                        choice1Title = "";
                        choice2Title = "";
                    }

                    if(hasNext && !choice1Title.isEmpty() && !choice2Title.isEmpty()) {
                        mChoice1.setText(choice1Title);
                        mChoice2.setText(choice2Title);
                        mChoice1.setVisibility(View.VISIBLE);
                        mChoice2.setVisibility(View.VISIBLE);
                    } else {
                        mGameFinishedCallback.onGameFinished(mDecisions);
                    }
                }
            });
        }
    }


}
