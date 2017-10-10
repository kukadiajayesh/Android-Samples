package app.com.analystictest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnSecondScreen)
    Button btnSecondScreen;
    @BindView(R.id.btnSendEvent)
    Button btnSendEvent;
    @BindView(R.id.btnException)
    Button btnException;
    @BindView(R.id.btnAppCrash)
    Button btnAppCrash;
    @BindView(R.id.btnLoadFragment)
    Button btnLoadFragment;
    @BindView(R.id.frame_container)
    FrameLayout frameContainer;

    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        myApplication = MyApplication.getInstance();
        myApplication.getDefaultTracker().set("uid","72628");
        //myApplication.trackScreenView("MainActivitysss");
    }

    @OnClick({R.id.btnSecondScreen, R.id.btnSendEvent, R.id.btnException,
            R.id.btnAppCrash, R.id.btnLoadFragment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSecondScreen:
                Intent intent = new Intent(MainActivity.this, Second.class);
                startActivity(intent);
                Toast.makeText(this, "sdfs", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnSendEvent:
                myApplication.trackEvent("Book", "Download", "Track book download");
                break;
            case R.id.btnException:
                try {
                    String name = null;
                    if (name.equals("abc")) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    myApplication.trackException(e);
                }
                break;
            case R.id.btnAppCrash:
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        int answer = 12 / 0;
                    }
                };

                Handler h = new Handler();
                h.postDelayed(r, 1500);
                break;
            case R.id.btnLoadFragment:
                break;
        }
    }
}
