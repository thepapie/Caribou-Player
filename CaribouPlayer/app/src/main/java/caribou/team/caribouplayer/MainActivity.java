
package caribou.team.caribouplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyService myService;
    private ServiceConnection monServiceConnection;
    private ArrayList<String> playlist;
    private TextView text;
    private Switch loop;
    private Switch repeat;
    private Switch shuffle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent bindIntent = new Intent(this, MyService.class);
        setServiceConnection();
        bindService(bindIntent, monServiceConnection, Context.BIND_AUTO_CREATE);

        text = findViewById(R.id.text);

        playlist = new ArrayList<>();
        playlist.add("/storage/emulated/0/Music/Calme/Creep.mp3");
        playlist.add("/storage/emulated/0/Music/Calme/Outro.mp3");
        playlist.add("/storage/emulated/0/Music/Calme/Hurt.mp3");
        playlist.add("/storage/emulated/0/Music/Calme/Redbone.mp3");

        loop = findViewById(R.id.loop);
        repeat = findViewById(R.id.repeat);
        shuffle = findViewById(R.id.shuffle);
    }

    private void setServiceConnection() {
        monServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                myService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myService = ((MyService.MyBinderDActivite) service).getMyService();
            }
        };
    }

    public void demarrerService(View view) {
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
        Log.i("CARIBOU", "Service Started");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5678);
        }
    }

    public void arreterService(View view) {
        Intent serviceIntent = new Intent(this, MyService.class);
        stopService(serviceIntent);
        Log.i("CARIBOU", "Service Stoped");
    }

    public void play(View view) {
        myService.setPlaylist(playlist);
        text.setText(playlist.toString());
        myService.play();
        Log.i("CARIBOU", "Play");
    }

    public void pause(View view) {
        myService.pauseResume();
        Log.i("CARIBOU", "Pause");
    }

    public void next(View view) {
        myService.next();
        Log.i("CARIBOU", "Next");
    }

    public void previous(View view) {
        myService.previous();
        Log.i("CARIBOU", "Service Démarré");
    }

    public void loop(View view) {
        myService.setLoop(loop.isChecked());
    }

    public void repeat(View view) {
        myService.setRepeat(repeat.isChecked());
        myService.repeat();
    }

    public void shuffle(View view) {
        myService.shuffle();
        myService.setShuffle(shuffle.isChecked());
    }

    public void addEnd()
    {
        playlist.add("/storage/emulated/0/Music/Calme/Parade.mp3");
        myService.addEnd("/storage/emulated/0/Music/Calme/Parade.mp3");
        text.setText(playlist.toString());
    }

    public void addNext()
    {
        playlist.add(myService.getPosition() + 1, "/storage/emulated/0/Music/Calme/Parade.mp3");
        myService.addNext("/storage/emulated/0/Music/Calme/Parade.mp3");
        text.setText(playlist.toString());
    }
}
