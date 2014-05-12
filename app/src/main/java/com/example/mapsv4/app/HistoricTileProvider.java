package com.example.mapsv4.app;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.BitSet;

public class HistoricTileProvider implements TileProvider {
    private static final int TILE_WIDTH = 256;
    private static final int TILE_HEIGHT = 256;
    private static final int BUFFER_SIZE =  TILE_HEIGHT * TILE_WIDTH * 4 * 2;
    private static final String SD_PATH = Environment.getExternalStorageDirectory()+"/HistoricalMaps/maps/";
    private static final String TAG = "Maps::TileProvider";

        @Override
        public Tile getTile(int x, int y, int zoom) {
            Log.i(TAG, String.format("Tile request! X: %d, Y: %d, Zoom: %d", x, y, zoom));
            byte[] image = readTileImage(x, y, zoom);
            return image == null ? NO_TILE : new Tile(TILE_WIDTH, TILE_HEIGHT, image);
        }

        private byte[] readTileImage(int x, int y, int zoom) {
            FileInputStream in = null;
            ByteArrayOutputStream buffer = null;


            try {
                String tileFilename = getTileFilename(x, y, zoom);
                Log.i(TAG, "tile: "+tileFilename);

                in = new FileInputStream(tileFilename);
                buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] data = new byte[BUFFER_SIZE];

                while ((nRead = in.read(data, 0, BUFFER_SIZE)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();


                // Start tile position debugging part
                Bitmap bmp = textAsBitmap(x, y, zoom, 40, Color.BLACK);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                buffer = stream;
                // End tile position debugging part

                return buffer.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            } finally {
                if (in != null) try { in.close(); } catch (Exception ignored) {}
                if (buffer != null) try { buffer.close(); } catch (Exception ignored) {}
            }
        }

        private String getTileFilename(int x, int y, int zoom) {
            return SD_PATH + "map.png";
            //return SD_PATH + zoom + '/' + x + '_' + y + ".png";
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
