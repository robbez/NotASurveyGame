package com.crippledcomputerboyz.robbez.notasurveygame;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import com.crippledcomputerboyz.robbez.notasurveygame.ExternalCommunication.Logger;
import com.crippledcomputerboyz.robbez.notasurveygame.GameLogic.GameDecision;
import com.crippledcomputerboyz.robbez.notasurveygame.Scene.SceneHelper;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements SceneHelper.OnGameFinishedListener {

    private static final int VIDEO_VIEW_COUNT = 3;

    private VideoView[] mVideoViews;
    private SceneHelper mSceneHelper;
    private Button mChoice1, mChoice2;
    private ProgressDialog mProgressDialog;
    private Logger mLogger;

    private String mUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.mVideoViews = new VideoView[]{
                (VideoView) findViewById(R.id.videoView1),
                (VideoView) findViewById(R.id.videoView2),
                (VideoView) findViewById(R.id.videoView3)
        };

        mChoice1 = (Button) findViewById(R.id.choice1btn);
        mChoice2 = (Button) findViewById(R.id.choice2btn);
        mProgressDialog = new ProgressDialog(this);

        this.mUserName = getIntent().getStringExtra("username");
        this.mLogger = new Logger(this);
        this.mSceneHelper = new SceneHelper(mVideoViews, mChoice1, mChoice2, mProgressDialog, getPackageName(), this);
        this.mSceneHelper.startFirstScene();
    }

    @Override
    public void onGameFinished(ArrayList<GameDecision> decisions) {
        this.mLogger.logDecisionSet(decisions, mUserName);
    }
}
