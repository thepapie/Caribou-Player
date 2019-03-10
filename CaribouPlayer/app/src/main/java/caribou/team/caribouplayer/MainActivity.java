package caribou.team.caribouplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MyService myService;
    private ServiceConnection myServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent bindIntent = new Intent(this, MyService.class);
        setServiceConnection();
        bindService(bindIntent, myServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setServiceConnection() {

        myServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                myService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myService = ((MyService.MyBinderDActivite) service).getMonService();
            }
        };
    }

}
