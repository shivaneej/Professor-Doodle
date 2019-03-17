package com.mhack.professordoodle;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoomActivity extends AppCompatActivity implements RequestCallback {

    private CardView card1;
    private CardView card2;
    private EditText editText;
    private String dda = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        editText = findViewById(R.id.room_id);
        card1 = findViewById(R.id.card_view);
        card2 = findViewById(R.id.card_view2);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //request
                createRoom(RoomActivity.this);
                showAlert();
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(RoomActivity.this, MainActivity.class);
                    intent.putExtra("roomid", editText.getText().toString());
                    startActivity(intent);
                } else {
                    editText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    void createRoom(final RequestCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL("http://192.168.43.40:2234/room.php");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.setConnectTimeout(15000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.connect();

                    Log.i("response", httpURLConnection.getResponseMessage());

                    InputStream inputStream = httpURLConnection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();

                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    String roomID = builder.toString();
                    dda = roomID;
                    System.out.println(roomID);
//

                    callback.onSuccess(roomID);
//                    showAlert(roomID);
                } catch (Exception ignored) {

                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onSuccess(String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlert();
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(RoomActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        alert.setView(view);
        ((TextView) view.findViewById(R.id.message)).setText("Share the following room id with friends and enjoy!!!\n\n" + dda);
        final AlertDialog dialog = alert.create();

        view.findViewById(R.id.canvas_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomActivity.this, MainActivity.class);
                intent.putExtra("roomid", dda);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Room Id", dda);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(RoomActivity.this, "Room Id Copied to Clipboard", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        //        alert.setTitle("Room Created!");
//        alert.setIcon(R.drawable.ic_whatshot_black_24dp);
//        alert.setMessage("Share the following room id with friends and enjoy!!!\n"+dda);
//        AlertDialog dialog = alert.create();
//        alert.setPositiveButton("Copy Room Id", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("Room Id", dda);
//                clipboard.setPrimaryClip(clip);
//                Toast.makeText(RoomActivity.this, "Room Id Copied to Clipboard", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//        alert.setNegativeButton("Go to Canvas", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(RoomActivity.this, MainActivity.class);
//                intent.putExtra("roomid", dda);
//                startActivity(intent);
//                dialog.dismiss();
//            }
//        });
        dialog.show();
    }


}
