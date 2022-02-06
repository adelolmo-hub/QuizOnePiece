package com.example.quizonepiece;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final Map<Integer, Integer> resourceMap = new HashMap<>();

    static {
        resourceMap.put(R.drawable.luffy, R.raw.luffy);
        resourceMap.put(R.drawable.zoro, R.raw.zoro);
        resourceMap.put(R.drawable.usopp, R.raw.usopp);
        resourceMap.put(R.drawable.nami, R.raw.nami);
        resourceMap.put(R.drawable.chopper, R.raw.chopper);
        resourceMap.put(R.drawable.robin, R.raw.robin);
        resourceMap.put(R.drawable.franky, R.raw.franky);
        resourceMap.put(R.drawable.brook, R.raw.brook);
    }
    private List<Integer> imageList;

    private ImageView imageLeft, imageCenter, imageRight;
    private TextView tvResultado, tvRetry;
    private Button audioButton, btCheck;
    private MediaPlayer mPlayerAudio, mPlayerResult;

    private int imagenCorrecta;
    private int imagenSeleccionada = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResultado = findViewById(R.id.tvResultado);
        tvRetry = findViewById(R.id.tvRetry);

        imageLeft = findViewById(R.id.imageLeft);
        imageCenter = findViewById(R.id.imageCenter);
        imageRight = findViewById(R.id.imageRight);

        audioButton = findViewById(R.id.audioButton);
        btCheck = findViewById(R.id.btCheck);

        randImage();
    }


    public void randImage() {
        imageList = new ArrayList<>(resourceMap.keySet());
        Collections.shuffle(imageList);
        imageLeft.setImageResource(imageList.get(0));
        imageCenter.setImageResource(imageList.get(1));
        imageRight.setImageResource(imageList.get(2));

        imagenCorrecta = imageList.get((int) (Math.random() * 3));
    }

    public void playSound(View view) {
        if(mPlayerAudio == null) {
            mPlayerAudio = MediaPlayer.create(MainActivity.this, resourceMap.get(imagenCorrecta));
        }
        mPlayerAudio.start();

        audioButton.setText("Pause");
        audioButton.setOnClickListener(this::pauseSound);

        mPlayerAudio.setOnCompletionListener(mediaPlayer -> {
            audioButton.setText("Play");
            audioButton.setOnClickListener(this::playSound);
        });

    }

    public void pauseSound(View view) {
        if (mPlayerAudio !=null) {
            mPlayerAudio.pause();
            audioButton.setText("Play");
            audioButton.setOnClickListener(this::playSound);
        }
    }

    public void imagenSeleccionada(View view) {
        switch (view.getId()) {
            case R.id.imageLeft:
                clearBackground();
                imageLeft.setBackgroundResource(R.drawable.highlight);
                imagenSeleccionada = imageList.get(0);
                break;
            case R.id.imageRight:
                clearBackground();
                imageRight.setBackgroundResource(R.drawable.highlight);
                imagenSeleccionada = imageList.get(2);
                break;
            case R.id.imageCenter:
                clearBackground();
                imageCenter.setBackgroundResource(R.drawable.highlight);
                imagenSeleccionada = imageList.get(1);
                break;
            default:
                break;
        }
    }

    private void clearBackground() {
        imageLeft.setBackground(null);
        imageRight.setBackground(null);
        imageCenter.setBackground(null);
    }

    public void comprobarRespuesta(View view){
        if(imagenSeleccionada==0){
            Toast.makeText(this, "Selecciona una imagen", Toast.LENGTH_LONG).show();
            return;
        }
        if(mPlayerAudio.isPlaying()){
            mPlayerAudio.stop();
        }
        if(imagenSeleccionada == imagenCorrecta){
            mPlayerResult = MediaPlayer.create(MainActivity.this, R.raw.correcto);
            tvResultado.setText("Correcto");
        }else{
            mPlayerResult = MediaPlayer.create(MainActivity.this, R.raw.incorrecto);
            tvResultado.setText("Incorrecto");
        }
        mPlayerResult.start();
        btCheck.setText("No");
        btCheck.setOnClickListener(v -> System.exit(0));
        audioButton.setText("Si");
        audioButton.setOnClickListener(this::resetAplication);
        tvRetry.setVisibility(View.VISIBLE);
    }

    private void resetAplication(View view) {
        randImage();
        btCheck.setText("Check");
        mPlayerAudio = null;
        btCheck.setOnClickListener(this::comprobarRespuesta);
        audioButton.setText("Play");
        audioButton.setOnClickListener(this::playSound);
        tvRetry.setVisibility(View.INVISIBLE);
        tvResultado.setText("");
        clearBackground();
        imagenSeleccionada = 0;
    }
}