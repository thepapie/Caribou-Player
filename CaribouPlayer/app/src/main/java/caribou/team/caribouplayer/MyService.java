package caribou.team.caribouplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class MyService extends Service {
    public MyService() {
    }

    private IBinder myBinder;
    int position = 0;
    ArrayList<String> playlist = new ArrayList<>();
    MediaPlayer mediaPlayer = new MediaPlayer();
    Boolean isRepeat;
    Boolean isShuffle;
    Random random = new Random();

    @Override
    public IBinder onBind(Intent intent) {

        return myBinder;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {
                next();
            }
        });
    }

    public void setRepeat(Boolean repeat) {
        isRepeat = repeat;
    }

    public void setShuffle(Boolean shuffle) {
        isShuffle = shuffle;
    }

    public void play()
    {
        try
        {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(playlist.get(position));
            mediaPlayer.prepare();
            mediaPlayer.start();
        }

        catch (Exception e)
        {
            Log.i("DIM-DEBUG", e.getMessage());
        }
    }

    public void pauseResume()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
        else
        {
            mediaPlayer.start();
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
