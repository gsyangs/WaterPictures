package com.water.pictures.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author:create by ys
 * 时间:2019/7/19 09
 * 邮箱 894417048@qq.com
 */
public class ImageUtils {
    /**
     * 添加时间水印
     * @param
     */
    public static Bitmap addTimeFlag(Bitmap src){
        // 获取原始图片与水印图片的宽与高
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(newBitmap);
        // 往位图中开始画入src原始图片
        mCanvas.drawBitmap(src, 0, 0, null);
        //添加文字
        Paint textPaint = new Paint();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = sdf.format(new Date(System.currentTimeMillis()));
        textPaint.setColor(Color.RED) ;
        textPaint.setTextSize(32);
        String familyName = "宋体";
//        Typeface typeface = Typeface.create(familyName,
//                Typeface.BOLD_ITALIC);
//        textPaint.setTypeface(typeface);
//        textPaint.setTextAlign(Align.CENTER);

        mCanvas.drawText(time, (float)(w*1)/7, (float)(h*14)/15, textPaint);
        mCanvas.save();
        mCanvas.restore();
        return newBitmap ;
    }
}
