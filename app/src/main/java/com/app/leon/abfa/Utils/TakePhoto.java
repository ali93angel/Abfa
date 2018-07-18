package com.app.leon.abfa.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Leon on 12/18/2017.
 */

public class TakePhoto extends Activity {

    public static Uri file;
    static Context context;
    Button button;

    public TakePhoto(Button button) {
        this.button = button;
    }

    public static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "AbfaCamera");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    public Uri getFile() {
        return file;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                button.setEnabled(true);
            }
        }
    }

    public void takePicture(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        activity.startActivityForResult(intent, 100);
    }

    public File downsizeImage(File file, int newScaleInPercent) {
        try {
            final int REQUIRED_SIZE = newScaleInPercent;
            final int QUALITY = 100;
            final int BITMAP_OPTION_SIZE = 6;
            BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
            bitmapOption.inJustDecodeBounds = true;
            bitmapOption.inSampleSize = BITMAP_OPTION_SIZE;
            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, bitmapOption);
            inputStream.close();
            int scale = calculateProperScale(REQUIRED_SIZE, bitmapOption.outWidth, bitmapOption.outHeight);
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream);
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public int calculateProperScale(int requierdScale, int bitmapWidth, int bitmapHight) {
        int scale = 1;
        while (bitmapWidth / scale / 2 >= requierdScale &&
                bitmapHight / scale / 2 >= requierdScale) {
            scale *= 2;
        }
        return scale;
    }

    public void setImageSrc(Display display, ImageView imageSrc, Uri uri, double width, double height) {
        try {
            Bitmap myBitmap = BitmapFactory.decodeFile(uri.getPath());
            Point size = new Point();
            display.getSize(size);
            Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, (int) (width * size.x), (int) (height * size.y), true);
            imageSrc.setImageBitmap(scaled);
        } catch (Exception e) {
            Log.e(e.toString(), e.getMessage());
        }
    }

    public MultipartBody.Part preparePicToSend(Uri uri) {
        File file = new File(uri.getPath());
        file = downsizeImage(file, 75);
        RequestBody requestFile = RequestBody.create(MediaType.parse(("multipart/form-data")), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        return body;
    }
}
