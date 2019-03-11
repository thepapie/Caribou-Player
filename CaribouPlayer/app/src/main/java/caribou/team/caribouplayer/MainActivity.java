
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

public class MainActivity extends AppCompatActivity {

    private MyService myService;
    private ServiceConnection monServiceConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent bindIntent = new Intent(this, MyService.class);
        setServiceConnection();
        bindService(bindIntent, monServiceConnection, Context.BIND_AUTO_CREATE);
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
}
