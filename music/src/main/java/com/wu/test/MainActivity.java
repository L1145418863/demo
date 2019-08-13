package com.wu.test;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kr.co.namee.permissiongen.PermissionGen;

public class MainActivity extends Activity {
    private static final String TAG="MainActivity";
    private ListView musicListView;
    private List<Music> musicList=new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private int currentId ;
    private int flag ;
    private int PlayMode = 0;   //播放模式，0顺序模式，1随机模式，2单曲循环
    private boolean isChange = false;
    private Thread thread;

    private final String MUSIC_NOTIFICATION_ACTION_PLAY = "musicnotificaion.To.PLAY";
    private final String MUSIC_NOTIFICATION_ACTION_NEXT = "musicnotificaion.To.NEXT";
    private final String MUSIC_NOTIFICATION_ACTION_PRE = "musicnotificaion.To.Pre";

    private MusicBroadCast musicBroadCast = null;
    private MusicTypeBroadcast typeBroadcast = null;
    private MusicNotification musicNotification = null;
    private MediaSessionManager mediaSessionManager = null;

    private ImageButton music_pro;
    private ImageButton music_play;
    private ImageButton music_next;
    private ImageButton play_mode;

    private SeekBar seekBar;
    private TextView time_play;
    private TextView time_max;
    private TextView new_name;
    private TextView new_singer;
    private TextView empty;
    MusicService musicService;
//   private Thread mThread;
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case 100 :
//                    int currentPosition = (Integer) msg.obj;
//                    time_play.setText(MusicUtil.formatTime(currentPosition));
//                    seekBar.setProgress(currentPosition);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getAuthority();    //安卓6.0后要在代码中动态申请权限
        mediaSessionManager = new MediaSessionManager(musicService);
        sharedPreferences = getSharedPreferences("music", MODE_PRIVATE);
        //clear(this);   //清除shreadPreferences里的数据
        // 加载currentposition的初始数据
        currentId = sharedPreferences.getInt("currenposition", 0);
        PlayMode = sharedPreferences.getInt("playmode",0);
        Toast.makeText(this,currentId+"",Toast.LENGTH_SHORT).show();
        Log.i(TAG,sharedPreferences.getAll()+"");
        init();                                    //初始化控件
        //time_play.setText(sharedPreferences.getString("music_playtime","0:00"));
        //seekBar.setProgress(sharedPreferences.getInt("seekBarProgress",0));
        bindToMusicService();                      //绑定服务
        MusicUtil.getMusicList(MainActivity.this, musicList);                  //得到本地音乐列表
        musicAdapter mAdapter = new musicAdapter(musicList, this);
        musicListView.setAdapter(mAdapter);
        musicListView.setEmptyView(empty);
        if(musicList .size()>0) {
            new_name.setText(musicList.get(currentId).getMusic_name());
            new_singer.setText(musicList.get(currentId).getArtist());
            time_max.setText(MusicUtil.formatTime(musicList.get(currentId).getDuration()));
        }
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    currentId = position;
                    playMusic(currentId);
                    music_play.setImageResource(R.drawable.music_play);
                    musicNotification.changePlay();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        musicNotification= MusicNotification.getMusicNotification(getApplicationContext());
        musicNotification.setContext(getBaseContext());
        musicNotification
                .setManager((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        musicNotification.onCreateMusicNotifi();
        if(musicList.size()>0) {
            musicNotification.onUpdataMusicNotifi(musicList.get(currentId), false);
        }
        musicBroadCast = new MusicBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MUSIC_NOTIFICATION_ACTION_PLAY);
        filter.addAction(MUSIC_NOTIFICATION_ACTION_NEXT);
        filter.addAction(MUSIC_NOTIFICATION_ACTION_PRE);
        registerReceiver(musicBroadCast, filter);
        typeBroadcast = new MusicTypeBroadcast();

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("wu.com.PlayType");
        registerReceiver(typeBroadcast,filter1);

        //mPlayer = new MediaPlayer();
    }

    //通知栏发送广播的接收器 实现通知栏按钮的实现
    public class MusicBroadCast extends BroadcastReceiver {
        private final String MUSIC_NOTIFICATION_ACTION_PLAY = "musicnotificaion.To.PLAY";
        private final String MUSIC_NOTIFICATION_ACTION_NEXT = "musicnotificaion.To.NEXT";
        private final String MUSIC_NOTIFICATION_ACTION_PRE = "musicnotificaion.To.PRE";
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case MUSIC_NOTIFICATION_ACTION_PLAY :
                        musicService.playORpause();
                        if(musicService.mPlayer == null){
                            try {
                                playMusic(currentId);
                                musicNotification.changePlay();
                                music_play.setImageResource(R.drawable.music_play);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                            if (!musicService.isPlaying()) {
                                musicNotification.changeStop();
                                music_play.setImageResource(R.drawable.music_pause);
                            } else {
                                musicNotification.changePlay();
                                music_play.setImageResource(R.drawable.music_play);
                            }
                            musicNotification.onUpdataMusicNotifi(musicList.get(currentId),musicService.isPlaying());
                        }
                    break;
                case MUSIC_NOTIFICATION_ACTION_NEXT:
                    playNext();
                    musicNotification.onUpdataMusicNotifi(musicList.get(currentId),musicService.isPlaying());
                    break;
                case MUSIC_NOTIFICATION_ACTION_PRE:
                    playRre();
                    musicNotification.onUpdataMusicNotifi(musicList.get(currentId),musicService.isPlaying());
                    break;
            }

        }
    }

    public class MusicTypeBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
           switch (PlayMode) {
               case 0:
                   playNext();
                   break;
               case 1:
                   try {
                       playRandom();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   break;
               case 2:
                   try {
                       playRepeat();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   break;
               default:
                   break;
           }
            Log.i(TAG,"接受广播");
        }

    }
    //绑定服务时的ServiceConnection参数
    private ServiceConnection conn = new ServiceConnection() {

        //绑定成功后该方法回调，并获得服务端IBinder的引用
        public void onServiceConnected(ComponentName name, IBinder service) {
            //通过获得的IBinder获取PlayMusicService的引用
            musicService = ((MusicService.MyBinder) service).getService();
            //Toast.makeText(MainActivity.this, "onServiceConnected", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }

    };

    private void bindToMusicService() {
        bindService(new Intent(this,MusicService.class),conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                    //加载菜单栏
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.play_mode :

        }
        return super.onOptionsItemSelected(item);
    }

    //通过获得的PlayMusicService引用调用播放音乐的方法，方法传进去的参数为音乐url
    protected void playMusic(int position) throws IOException {
        if (musicService != null) {
           // musicService.stop();
            musicService.play((String)musicList.get(position).getPath());
            if(flag == 0) {
                musicService.getType();
                Log.i(TAG,flag+"");
            }

            initSeekBar();
            flag =1;
            new_name.setText(musicList.get(currentId).getMusic_name());
            new_singer.setText(musicList.get(currentId).getArtist());
            musicNotification.onUpdataMusicNotifi(musicList.get(position),musicService.isPlaying());
            mediaSessionManager.updatePlaybackState(musicService.isPlaying());
            mediaSessionManager.updateLocMsg(currentId);
            thread = new Thread(new seekBarThread());
            thread.start();
        }
    }
    private void init() {
        musicListView= (ListView) findViewById(R.id.musicListView);

        music_pro= (ImageButton) findViewById(R.id.music_pre);
        music_play= (ImageButton) findViewById(R.id.music_play);
        music_next= (ImageButton) findViewById(R.id.music_next);
        play_mode = (ImageButton) findViewById(R.id.play_mode);
        seekBar= (SeekBar) findViewById(R.id.seekBar);
        time_play= (TextView) findViewById(R.id.time_play);
        time_max= (TextView) findViewById(R.id.time_max);
        new_name = (TextView) findViewById(R.id.new_name);
        new_singer = (TextView) findViewById(R.id.new_singer);
        empty = findViewById(R.id.tv_list_empty);

        music_play.setOnClickListener(listener);
        music_next.setOnClickListener(listener);
        music_pro.setOnClickListener(listener);
        play_mode.setOnClickListener(listener);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {             //监听进度条的拖动
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                time_play.setText(MusicUtil.formatTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isChange = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isChange = false;
                musicService.setCurrentProgress(seekBar.getProgress());
                thread = new Thread(new seekBarThread());
                thread.start();
            }
        });
    }

    private void initSeekBar(){
        int size = musicService.getMusicLength();
        seekBar.setMax(size);
        time_max.setText(MusicUtil.formatTime(size));
    }
    //监听进度条的变化
//    private void updateProgress(){
//        mThread = new Thread(){
//            @Override
//            public void run() {
//                while(!interrupted()){
//                    int currentPosition = musicService.getCurrentProgress();
//                    Message message = Message.obtain();
//                    message.obj = currentPosition;
//                    message.what = 100;
//                    handler.sendMessage(message);
//                }
//            }
//        };
//        mThread.start();
//    }

    class seekBarThread implements Runnable{

        @Override
        public void run() {
            while (!isChange){
                seekBar.setProgress(musicService.getCurrentProgress());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("music_name",musicList.get(currentId).getMusic_name());
                editor.putString("music_singer",musicList.get(currentId).getArtist());
                //editor.putInt("music_duration",musicList.get(currentId).getDuration());
                editor.putInt("currenposition",currentId);
                editor.putInt("playmode",PlayMode);
                //editor.putString("music_playtime",MusicUtil.formatTime(musicService.getCurrentProgress()));
                //editor.putInt("seekBarProgress",musicService.getCurrentProgress());
                editor.commit();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
//        if(mThread != null &!mThread.isInterrupted()){
//            mThread.interrupt();
//        }
        unbindService(conn);
        unregisterReceiver(musicBroadCast);
        unregisterReceiver(typeBroadcast);
        musicNotification.onCancelMusicNotifi();
        Log.i(TAG,"界面被销毁了");

        super.onDestroy();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.music_play:
                        musicService.playORpause();
                    if(musicService.mPlayer == null){
                        try {
                            playMusic(currentId);
                            music_play.setImageResource(R.drawable.music_play);
                            musicNotification.changePlay();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        if(!musicService.isPlaying()) {
                            musicNotification.changeStop();
                            music_play.setImageResource(R.drawable.music_pause);
                        }else {
                            musicNotification.changePlay();
                            music_play.setImageResource(R.drawable.music_play);
                        }
                    }
                    break;
                case R.id.music_next:
                    playNext();
                    break;
                case R.id.music_pre:
                    playRre();
                    break;
                case R.id.play_mode:
                    PlayMode++;
                    if(PlayMode >2){
                        PlayMode = 0;
                    }
                    if(PlayMode ==0) {
                        play_mode.setImageResource(R.drawable.ordered);
                    }else if(PlayMode == 1){
                        play_mode.setImageResource(R.drawable.random);
                    }else if(PlayMode == 2){
                        play_mode.setImageResource(R.drawable.one);
                    }
                default:
                    break;
            }
        }
    };
    //播放下一首
   public void playNext(){
       currentId++;
       if(currentId > musicList.size()-1){
           currentId = 0;
       }
       try {
           playMusic(currentId);
           musicNotification.changePlay();
           music_play.setImageResource(R.drawable.music_play);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   //播放上一首
   public void playRre(){
       currentId--;
       if(currentId <0){
           currentId = musicList.size()-1;
       }
       try {
           playMusic(currentId);
           musicNotification.changePlay();
           music_play.setImageResource(R.drawable.music_play);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   //播放随机
   public void playRandom() throws IOException {
       Random random = new Random();
      currentId = random.nextInt(musicList.size());
      playMusic(currentId);
   }
   //单曲
   public void playRepeat() throws IOException {
      playMusic(currentId);
   }

    public static void clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("music", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        editor.commit();
    }

    private void getAuthority(){               //简单动态申请权限
        //适配6.0以上机型请求权限
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .request();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (keyCode == KeyEvent.KEYCODE_BACK) {//按下退出按键
           AlertDialog dialog = new AlertDialog.Builder(this).create();
           dialog.setTitle("系统提示");
           dialog.setMessage("是否退出");
           dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   finish();
               }
           });
           dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.cancel();
               }
           });
           dialog.show();
       }
        return super.onKeyDown(keyCode, event);
    }
}
