package app.com.appsamples.frag;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import app.com.appsamples.R;

/**
 * Created by Reena on 8/24/2017.
 */

public class RecordAudio extends Fragment {

    final int REQUEST_MIC = 101;
    @butterknife.BindView(R.id.btnRecordAudio)
    Button btnRecordAudio;
    @butterknife.BindView(R.id.btnAudioPlay)
    Button btnAudioPlay;
    butterknife.Unbinder unbinder;

    int currentNo = 0;

    public static RecordAudio getInstant(Bundle bundle) {
        RecordAudio recordAudio = new RecordAudio();
        recordAudio.setArguments(bundle);
        return recordAudio;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_record_audio, container, false);

        unbinder = butterknife.ButterKnife.bind(this, view);

        if (getArguments() != null) {
            currentNo = getArguments().getInt("no");
            btnAudioPlay.setText("Play Audio " + currentNo);
        }

        if (getAllPermission()) {
            initMedia();
        }

        return view;
    }

    boolean mStartFlag = false;
    MediaRecorder mediaRecorder;
    String lastRecordedFile = "";

    void initMedia() {

        String path = getActivity().getCacheDir() + "/first.3gp";
        lastRecordedFile = path;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        mediaRecorder.setOutputFile(path);
    }

    boolean getAllPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int isRead = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.RECORD_AUDIO);

            ArrayList<String> permiss = new ArrayList<>();

            if (isRead != PackageManager.PERMISSION_GRANTED) {
                permiss.add(Manifest.permission.RECORD_AUDIO);
            }

            if (permiss.size() > 0) {
                String[] per = new String[permiss.size()];
                ActivityCompat.requestPermissions(getActivity(), permiss.toArray(per), REQUEST_MIC);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_MIC:

                boolean isGrandAll = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isGrandAll = false;
                        break;
                    }
                }

                if (!isGrandAll) {

                    Toast.makeText(getActivity(), "you need to grant all permission to Change Profile", Toast.LENGTH_LONG).show();
                } else {
                    initMedia();
                }
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @butterknife.OnClick({R.id.btnRecordAudio, R.id.btnAudioPlay})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btnRecordAudio:

                //((MainActivity) getActivity()).clearBack();

                if (!mStartFlag) {
                    mStartFlag = true;

                    Toast.makeText(getActivity(), lastRecordedFile, Toast.LENGTH_SHORT).show();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mStartFlag = false;
                    mediaRecorder.stop();
                }

                break;
            case R.id.btnAudioPlay:
                if (!mStartFlag) {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(lastRecordedFile);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "onResume:" + currentNo, Toast.LENGTH_SHORT).show();
    }
}
