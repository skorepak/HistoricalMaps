package com.example.mapsv4.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;

/**
 * Created by Skorepa on 16.květen.2014.
 */
public class InfoTileProvider implements TileProvider {
    private static final int TILE_WIDTH = 256;
    private static final int TILE_HEIGHT = 256;
    private static final String TAG = "Maps::InfoTileProvider";

    @Override
    public Tile getTile(int x, int y, int zoom) {
        Log.i(TAG, String.format("Tile request! X: %d, Y: %d, Zoom: %d", x, y, zoom));
        byte[] image = readTileImage(x, y, zoom);
        return image == null ? NO_TILE : new Tile(TILE_WIDTH, TILE_HEIGHT, image);
    }

    private byte[] readTileImage(int x, int y, int zoom) {
        Bitmap bmp = textAsBitmap(x, y, zoom, 40, Color.BLACK);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    public Bitmap textAsBitmap(int x, int y, int zoom, float textSize, int textColor) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        int width = TILE_WIDTH;
        int height = TILE_HEIGHT;

        float baseline = (int) (-paint.ascent() + 0.5f); // ascent() is negative

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText("Zoom: " + zoom, 10, baseline + 10, paint);
        canvas.drawText("X: " + x, 10, baseline + 60, paint);
        canvas.drawText("Y: " + y, 10, baseline + 110, paint);

        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(0, 0, width, height, paint);
        return image;
    }
}

