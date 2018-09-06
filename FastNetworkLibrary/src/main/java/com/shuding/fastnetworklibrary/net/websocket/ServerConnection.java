package com.shuding.fastnetworklibrary.net.websocket;

import android.os.Handler;
import android.os.Message;

import com.shuding.fastnetworklibrary.utils.LogUtils;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by tangpeng on 2018/7/11.
 */

public class ServerConnection {

    public enum ConnectionStatus {
        DISCONNECTED,
        CONNECTED,
        CLOSED,
        CLOSING,
        CONNECTING,
    }
    public ServerConnection(String url) {
        mClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        mServerUrl = url;
    }

    public interface ServerListener {
        void onNewMessage(String message);
        void onStatusChange(ConnectionStatus status);
    }

    private WebSocket mWebSocket;
    private OkHttpClient mClient;
    private String mServerUrl;
    private Handler mMessageHandler;
    private Handler mStatusHandler;
    private Handler handler;
    private ServerListener mListener;



    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Message m = mStatusHandler.obtainMessage(0, ConnectionStatus.CONNECTED);
            mStatusHandler.sendMessage(m);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Message m = mMessageHandler.obtainMessage(0, text);
            mMessageHandler.sendMessage(m);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            LogUtils.e("WebSocket链接报错onClosed,原因："+reason);
            Message m = mStatusHandler.obtainMessage(0, ConnectionStatus.CLOSED);
            mStatusHandler.sendMessage(m);
            disconnect();
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            LogUtils.e("WebSocket链接报错onFailure,原因："+t.getMessage());
            Message m = mStatusHandler.obtainMessage(0, ConnectionStatus.DISCONNECTED);
            mStatusHandler.sendMessage(m);
            Message m1 = mStatusHandler.obtainMessage(0, ConnectionStatus.CONNECTING);
            handler.sendMessageDelayed(m1,5000);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            LogUtils.e("WebSocket链接报错onCloseing,原因："+reason);
            Message m = mStatusHandler.obtainMessage(0, ConnectionStatus.CLOSING);
            mStatusHandler.sendMessage(m);
        }
    }


    public void connect(ServerListener listener) {
        Request request = new Request.Builder()
                .url(mServerUrl)
                .build();
        mWebSocket = mClient.newWebSocket(request, new SocketListener());
        mListener = listener;
        mMessageHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (mListener!=null){
                    mListener.onNewMessage((String) msg.obj);
                }
                return true;
            }
        });
        mStatusHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (mListener!=null){
                    mListener.onStatusChange((ConnectionStatus) msg.obj);
                }
                return true;
            }
        });
        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                LogUtils.e("websocket进行重连");
                if (mListener!=null){
                    connect(mListener);
                }
                return true;
            }
        });
    }

    public boolean isConnect(){
        if (mListener==null){
            return false;
        }else {
            return true;
        }
    }

    public void disconnect() {
        mWebSocket.cancel();
        mListener = null;
        mMessageHandler.removeCallbacksAndMessages(null);
        mStatusHandler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
    }

    public void sendMessage(String message) {
        mWebSocket.send(message);
    }

    public void sendMessage(ByteString bytes){
        mWebSocket.send(bytes);
    }
}
