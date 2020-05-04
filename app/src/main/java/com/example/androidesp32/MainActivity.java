package com.example.androidesp32;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private TextView output;
    private OkHttpClient client;

    private final class EchoWebSocket extends WebSocketListener implements com.example.androidesp32.EchoWebSocket {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response){
            webSocket.send("Helo!!!");
            webSocket.send("Yo Bro");
            webSocket.send(ByteString.decodeHex("COBA-COBA"));
            webSocket.close(NORMAL_CLOSURE_STATUS, "BYE-BYE");
        }
        @Override
        public void onMessage(WebSocket webSocket, String text){
            output("Terima = " +text);
        }

        @Override
        public void onByte(WebSocket webSocket, ByteString bytes){
            output("Receiving bytes = " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason){
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing = " + code + " /" +reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response){
            output("Error = " + t.getMessage());
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);
        client = new OkHttpClient();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

    }
    private void start(){
        Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        EchoWebSocket Listener = new EchoWebSocket();
        WebSocket ws = client.newWebSocket(request, Listener);

        client.dispatcher().executorService().shutdown();
    }
    private void output(final String txt){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString()+"\n\n"+txt);
            }
        });
    }
}
