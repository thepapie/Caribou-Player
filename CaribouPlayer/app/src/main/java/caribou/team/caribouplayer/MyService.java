package caribou.team.caribouplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.util.ArrayList;
import java.util.Random;

public class MyService extends Service {

    private IBinder monBinder;
    int position = 0;
    ArrayList<String> playlist = new ArrayList<>();
    MediaPlayer mediaPlayer = new MediaPlayer();
    Boolean isRepeat = false;
    Boolean isShuffle = false;
    Random random = new Random();

    @Override
    public void onCreate() {
        super.onCreate();
        monBinder = new MyBinderDActivite();

        Log.i("CARIBOU-SERVICE", "Service Created");

        playlist.add("/storage/emulated/0/Music/Calme/Creep.mp3");
        playlist.add("/storage/emulated/0/Music/Calme/Outro.mp3");
        playlist.add("/storage/emulated/0/Music/Calme/Hurt.mp3");
        playlist.add("/storage/emulated/0/Music/Calme/Redbone.mp3");

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {
                next();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {


        Log.i("CARIBOU-SERVICE", "Service Binded");
        return monBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {

        Log.i("CARIBOU-SERVICE", "Service Unbinded");
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("CARIBOU-SERVICE", "Service Killed");
    }



    public class MyBinderDActivite extends Binder {
        MyService getMyService() {

            Log.i("CARIBOU-SERVICE", "Service Binded");
            return MyService.this;
        }
    }

    /*Setters*/

    public void setRepeat(Boolean repeat) {
        isRepeat = repeat;
    }

    public void setShuffle(Boolean shuffle) {
        isShuffle = shuffle;
    }

    public void setPlaylist(ArrayList<String> playlist) {
        this.playlist = playlist;
    }

    /*Methodes*/

    public void play()
    {
        try
        {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, Uri.parse(playlist.get(position)));
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.i("CARIBOU-SERVICE", "Playing " + playlist.get(position));
        }

        catch (Exception e)
        {
            Log.i("CARIBOU-SERVICE", e.getMessage());
        }
    }

    public void pauseResume()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
            Log.i("CARIBOU-SERVICE", "Music paused");
        }
        else
        {
            mediaPlayer.start();
            Log.i("CARIBOU-SERVICE", "Music resumed");
        }
    }

    public void next()
    {
        if (position + 1 >= playlist.size())
        {
            mediaPlayer.stop();
        }
        else
        {
            if (isShuffle)
            {
                position = random.nextInt(playlist.size() - 1);
            }
            else if (position + 1 < playlist.size())
            {
                ++position;
            }
            else if (isRepeat)
            {
                position = 0;
            }
            play();
        }
    }

    public void previous()
    {
        if (position - 1 < 0)
        {
            mediaPlayer.stop();
        }
        else
        {
            if (isShuffle)
            {
                position = random.nextInt(playlist.size() - 1);
            }
            else if (position - 1 >= 0)
            {
                --position;
            }
            else
            {
                position = playlist.size() - 1;
            }
            play();
        }
    }
}
