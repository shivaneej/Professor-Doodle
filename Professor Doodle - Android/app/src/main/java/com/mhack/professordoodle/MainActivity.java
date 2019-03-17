package com.mhack.professordoodle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawingView dv;
    private Paint mPaint;
    private ImageView addImageButton;
    private ImageView brushSizeButton;
    private int i = 0;
    private String roomId;
    private ImageView colorButton;
    private AlertDialog colorAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomId = getIntent().getStringExtra("roomid");

        dv = new DrawingView(this);

        setContentView(R.layout.activity_main);
        RelativeLayout canvasLayout = findViewById(R.id.canvas_layout);
        canvasLayout.addView(dv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        addImageButton = findViewById(R.id.add_image);
        brushSizeButton = findViewById(R.id.brush_size);
        colorButton = findViewById(R.id.color);
        brushSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openbrushsizedialog();
            }
        });
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dv.convertCanvasToImage();
//                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1000);
            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialogue();
            }
        });

    }

    private void openbrushsizedialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.seekbarlayout, null);
        alert.setView(view);
        final SeekBar seekBar = view.findViewById(R.id.seek);
        seekBar.setProgress((int) mPaint.getStrokeWidth());
        alert.create().show();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPaint.setStrokeWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void opendialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.color_dialog, null);
        builder.setView(view);
        view.findViewById(R.id.red).setOnClickListener(this);
        view.findViewById(R.id.yellow).setOnClickListener(this);
        view.findViewById(R.id.blue).setOnClickListener(this);
        view.findViewById(R.id.green).setOnClickListener(this);
        view.findViewById(R.id.white).setOnClickListener(this);
        view.findViewById(R.id.black).setOnClickListener(this);
        colorAlert = builder.create();
        colorAlert.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1000 && data.getData() != null) {
            String uri = getRealPathFromURI(data.getData().toString());
            mCanvas = new Canvas(BitmapFactory.decodeFile(uri).copy(Bitmap.Config.ARGB_8888, true));
        }
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String t = cursor.getString(index);
            cursor.close();
            return t;
        }
    }

    private Canvas mCanvas;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.red:
                mPaint.setColor(ContextCompat.getColor(MainActivity.this, R.color.red));
                colorAlert.dismiss();
                break;
            case R.id.blue:
                mPaint.setColor(ContextCompat.getColor(MainActivity.this, R.color.blue));
                colorAlert.dismiss();
                break;
            case R.id.yellow:
                mPaint.setColor(ContextCompat.getColor(MainActivity.this, R.color.yellow));
                colorAlert.dismiss();
                break;
            case R.id.green:
                mPaint.setColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                colorAlert.dismiss();
                break;
            case R.id.white:
                mPaint.setColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                colorAlert.dismiss();
                break;
            case R.id.black:
                mPaint.setColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                colorAlert.dismiss();
                break;

        }

    }

    public class DrawingView extends View implements RequestCallback {

        public int width;
        public int height;
        private Bitmap mBitmap;
        private Path mPath;
        private Paint mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;
        boolean first = true;
        private String objectid;

        public DrawingView(Context c) {
            super(c);
            context = c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);

            getUpdatedImage(DrawingView.this);
        }

        int mw, mh;

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawARGB(255, 255, 255, 255);
            uploadCanvas();
            mw = w;
            mh = h;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
            canvas.drawPath(circlePath, circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
            uploadCanvas();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            HashMap<String, Float> map = new HashMap<>();
            map.put("x", x);
            map.put("y", y);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    map.put("break", 1.0f);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    map.put("break", 0f);
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    map.put("break", 0f);
                    break;
            }
//            reference.child("drawing0000").child(String.valueOf(i++)).setValue(map);
            return true;
        }

        private void uploadCanvas() {
//            convertCanvasToImage();
            new Thread(new Runnable() {
                private HttpURLConnection httpURLConnection = null;

                @Override
                public void run() {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    String base64 = "data:image/png;base64," + Base64.encodeToString(bytes, Base64.DEFAULT).replace("\n", "|");
                    try {
                        URL url = new URL("http://192.168.43.40:2234/drawing.php");
                        httpURLConnection = (HttpURLConnection) url.openConnection();
//                        httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        httpURLConnection.setReadTimeout(10000);
                        httpURLConnection.setConnectTimeout(15000);
                        httpURLConnection.setRequestMethod("POST");
//                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setDoOutput(true);

/*
                        Uri.Builder uriBuilder = new Uri.Builder()
                                .appendQueryParameter("image", base64)
                                .appendQueryParameter("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
*/
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("image", base64);
//                        jsonObject.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

//                        if (first) {
//                            jsonObject.put("first", "1");
//                        } else {
//                            jsonObject.put("first", "0");
                        jsonObject.put("id", roomId);
//                        }
//                        String query = uriBuilder.build().getEncodedQuery();

                        System.out.println(jsonObject.toString());
                        DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
                        os.writeBytes(jsonObject.toString());
                        os.flush();
                        os.close();

                        httpURLConnection.connect();

//
//                           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        InputStream inputStream = httpURLConnection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder builder = new StringBuilder();

                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                  /*      if (first) {
                            objectid = builder.toString();
                            System.out.println(objectid);
                        }
                        first = false;*/
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    }

                }
            }).start();
        }

        void convertCanvasToImage() {
            FileOutputStream fileOutputStream = null;
           String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/doodle" + System.currentTimeMillis() / 1000 + ".jpeg";
            try {
                fileOutputStream = new FileOutputStream(filename);
            } catch (Exception e) {
            }
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
            Toast.makeText(MainActivity.this,"Image downloaded as "+ filename, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(String data) {
            data = data.substring(22);
            Log.i("data received ", data);
            byte[] decodedString = Base64.decode(data, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            mCanvas = new Canvas(Bitmap.createBitmap(mw,mh, Bitmap.Config.ARGB_8888));
//            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, mw, mh);
            mCanvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, mw, mh, false), 0, 0, null);
            invalidate();
        }
    }

    private void getUpdatedImage(final RequestCallback requestCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                while (true) {
                    try {
                        URL url = new URL("http://192.168.43.40:2234/drawingforandroid.php?id=" + roomId);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setReadTimeout(10000);
                        httpURLConnection.setConnectTimeout(15000);

                        httpURLConnection.connect();

                        InputStream inputStream = httpURLConnection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder builder = new StringBuilder();

                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }

                        requestCallback.onSuccess(builder.toString()
                                .replace("|", "\n")
                                .replace("\n\n", ""));

                    } catch (Exception ignored) {

                    } finally {
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}