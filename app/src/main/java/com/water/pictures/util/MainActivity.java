package com.water.pictures.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends CheckPermissionsActivity implements View.OnClickListener {

    private ImageView photoTv1;
    private final static int TAKEPHOTOCODE = 110;
    private String sdState;
    private ImageView openPhotoIv;
    // /storage/emulated/0/pic
    public final static String SAVED_IMAGE_PATH1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pic";//+"/pic";
    // /storage/emulated/0/Pictures
    public final static String SAVED_IMAGE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();//.getAbsolutePath()+"/pic";//+"/pic";
    String photoPath;
    Bitmap bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取SD卡安装状态
        sdState = Environment.getExternalStorageState();
        init();
    }

    private void init() {
        findViewById();
        setListener();
    }

    private void findViewById() {
        photoTv1 = findViewById(R.id.take_photos_1);
    }

    private void setListener() {
        photoTv1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photos_1:
                if (sdState.equals(Environment.MEDIA_MOUNTED)) {
                    //设置图片保存路径
                    photoPath = SAVED_IMAGE_PATH + "/" + System.currentTimeMillis() + ".png";

                    File imageDir = new File(photoPath);
                    if (!imageDir.exists()) {

                        //根据一个 文件地址生成一个新的文件用来存照片
                        try {
                            imageDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        takePhotos();
                    }

                } else {
                    Toast.makeText(this, "SD卡未插入", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void takePhotos() {
        //考虑到安全机制的问题，Android的SDK25以后对相机调用有了新的限定，在这里要做一下处理
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        //根据路径实例化图片文件
        File photoFile = new File(photoPath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (currentapiVersion < 24) {

            //设置拍照后图片保存到文件中
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            //启动拍照activity并获取返回数据
            startActivityForResult(intent, TAKEPHOTOCODE);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, photoFile.getAbsolutePath());
            Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, TAKEPHOTOCODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKEPHOTOCODE && resultCode == Activity.RESULT_OK) {
            File photoFile = new File(photoPath);
            if (photoFile.exists()) {
                //通过图片地址将图片加载到bitmap里面
                Bitmap bm = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                //添加日期水印
                bp = ImageUtils.addTimeFlag(bm);
                //将拍摄的照片显示到界面上
                photoTv1.setImageBitmap(bp);
            } else {
                Toast.makeText(this, "没有图片数据", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
