package app.com.appsamples.frag;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import app.com.appsamples.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Reena on 8/27/2017.
 */

public class RingerMode extends Fragment {

    @BindView(R.id.btnRinger)
    Button btnRinger;
    @BindView(R.id.btnVibrate)
    Button btnVibrate;
    @BindView(R.id.btnSilent)
    Button btnSilent;
    @BindView(R.id.tvMode)
    TextView tvMode;
    Unbinder unbinder;

    private AudioManager mAudioManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_ringer_mode, container, false);

        unbinder = ButterKnife.bind(this, view);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        getStatus();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnRinger, R.id.btnVibrate, R.id.btnSilent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnRinger:
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            case R.id.btnVibrate:
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
            case R.id.btnSilent:
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
        }

        getStatus();

    }

    void getStatus() {
        int mod = mAudioManager.getRingerMode();
        if (mod == AudioManager.RINGER_MODE_NORMAL) {
            tvMode.setText("Current Status: Ring");
        } else if (mod == AudioManager.RINGER_MODE_SILENT) {
            tvMode.setText("Current Status: Silent");
        } else if (mod == AudioManager.RINGER_MODE_VIBRATE) {
            tvMode.setText("Current Status: Vibrate");
        }

    }
}
