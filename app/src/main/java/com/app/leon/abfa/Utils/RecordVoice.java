package com.app.leon.abfa.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import com.app.leon.abfa.Models.Enums.DialogType;
import com.app.leon.abfa.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Leon on 12/18/2017.
 */

public final class RecordVoice {
    public static String FileName = null;
    private static Context context;
    public MediaPlayer Player = null;
    private MediaRecorder Recorder = null;

    public RecordVoice(Context context) {
        RecordVoice.context = context;
        FileName = context.getExternalCacheDir().getAbsolutePath() + "/AbfaAudioRecord" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".amr";
    }

    public static MultipartBody.Part prepareVoiceToSend(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse(("multipart/form-data")), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("voice", file.getName(), requestFile);
        return body;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public File getFile() {
        File mediaStorageDir = new File(FileName);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return mediaStorageDir;
    }

    public void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        Player = new MediaPlayer();
        try {
            Player.setDataSource(FileName);
            Player.prepare();
            Player.start();
        } catch (IOException e) {
            new CustomDialog(DialogType.Yellow, context,
                    context.getString(R.string.error_in_play_voice),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.error),
                    context.getString(R.string.accepted));
        }
    }

    private void stopPlaying() {
        Player.pause();
//        Player.release();
//        Player = null;
    }

    private void startRecording() {
        Recorder = new MediaRecorder();
        Recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        Recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        Recorder.setOutputFile(FileName);
        Recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            Recorder.prepare();
        } catch (IOException e) {
            new CustomDialog(DialogType.Yellow, context,
                    context.getString(R.string.error_in_record_voice),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.error),
                    context.getString(R.string.accepted));
        }
        Recorder.start();
    }

    private void stopRecording() {
        try {
            Recorder.stop();
            Recorder.release();
            Recorder = null;
        } catch (Exception e) {
        }
    }

    public MediaPlayer getPlayer() {
        return Player;
    }

    public void setPlayer(MediaPlayer player) {
        Player = player;
    }

    public MediaRecorder getRecorder() {
        return Recorder;
    }

    public void setRecorder(MediaRecorder recorder) {
        Recorder = recorder;
    }

}
