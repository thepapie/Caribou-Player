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
    private int position;
    private int shuffleIterator;
    private ArrayList<String> playlist;
    private ArrayList<Integer> shuffleOrder;
    private MediaPlayer mediaPlayer;
    private Boolean isRepeat;
    private Boolean isLoop;
    private Boolean isShuffle;
    private Random random;

    @Override
    public void onCreate() {
        super.onCreate();
        monBinder = new MyBinderDActivite();

        Log.i("CARIBOU-SERVICE", "Service Created");

        playlist = new ArrayList<>();
        shuffleOrder = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        random = new Random();
        isLoop = false;
        isRepeat = false;
        isShuffle = false;
        position = 0;
        shuffleIterator = 0;

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

    public void setLoop(Boolean loop) {
        isLoop = loop;
    }

    public void setRepeat(Boolean repeat) {
        isLoop = repeat;
    }

    public void setShuffle(Boolean shuffle) {
        isShuffle = shuffle;
    }

    public void setPlaylist(ArrayList<String> playlist) {
        this.playlist = playlist;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
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
        if (isShuffle && shuffleIterator + 1 < shuffleOrder.size())
        {
            ++shuffleIterator;
            position = shuffleOrder.get(shuffleIterator);
            play();
        }
        else if (!isShuffle && position + 1 < playlist.size())
        {
            ++position;
            play();
        }
        else if (isLoop)
        {
            if (isShuffle)
            {
                shuffleIterator = 0;
                position = shuffleOrder.get(0);
            }
            else
            {
                position = 0;
            }
            play();
        }
        else
        {
            mediaPlayer.stop();
            Log.i("CARIBOU-SERVICE", "Music stoped");
        }
    }

    public void previous()
    {
        if (isShuffle && shuffleIterator - 1 >= 0)
        {
            --shuffleIterator;
            position = shuffleOrder.get(shuffleIterator);
            play();
        }
        else if (!isShuffle && position - 1 >= 0)
        {
            --position;
            play();
        }
        else if (isLoop)
        {
            if (isShuffle)
            {
                shuffleIterator = playlist.size() - 1;
                position = shuffleOrder.get(shuffleIterator);
            }
            else
            {
                position = playlist.size() - 1;
            }
            play();
        }
        else
        {
            mediaPlayer.stop();
            Log.i("CARIBOU-SERVICE", "Music stoped");
        }
    }

    public void addEnd(String path)
    {
        playlist.add(path);
    }

    public void addNext(String path)
    {
        playlist.add(position + 1, path);
    }

    public void shuffle()
    {
        shuffleIterator = 0;
        shuffleOrder = new ArrayList<>();
        shuffleOrder.add(0);
        for (int i = 1; i < playlist.size(); i++)
        {
            shuffleOrder.add(random.nextInt(shuffleOrder.size()) + 1, i);
        }
    }

    public void repeat()
    {
        mediaPlayer.setLooping(isRepeat);
    }
}
