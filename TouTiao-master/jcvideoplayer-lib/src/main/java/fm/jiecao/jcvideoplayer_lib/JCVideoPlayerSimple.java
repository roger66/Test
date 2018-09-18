package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Manage UI
 * Created by Nathen
 * On 2016/04/10 15:45
 */
public class JCVideoPlayerSimple extends JCVideoPlayer {

    public JCVideoPlayerSimple(Context context) {
        super(context);
    }

    public JCVideoPlayerSimple(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jc_layout_base;
    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        updateFullscreenButton();
        fullscreenButton.setVisibility(View.GONE);
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        updateStartImage();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        updateStartImage();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        updateStartImage();
    }

    private void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setImageResource(R.drawable.jc_pause_small);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setImageResource(R.drawable.jc_play_small);
        } else {
            startButton.setImageResource(R.drawable.jc_play_small);
        }
    }

    public void pause() {
        if (currentState == CURRENT_STATE_PLAYING) {
            onEvent(JCUserAction.ON_CLICK_PAUSE);
            JCMediaManager.instance().mediaPlayer.pause();
            onStatePause();
        }
    }

    public void updateFullscreenButton() {
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            fullscreenButton.setImageResource(R.drawable.jc_shrink);
        } else {
            fullscreenButton.setImageResource(R.drawable.jc_enlarge);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fullscreen && currentState == CURRENT_STATE_NORMAL) {
            Toast.makeText(getContext(), "Play video first", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onClick(v);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (currentState == CURRENT_STATE_NORMAL) {
                Toast.makeText(getContext(), "Play video first", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        super.onProgressChanged(seekBar, progress, fromUser);
    }
}
