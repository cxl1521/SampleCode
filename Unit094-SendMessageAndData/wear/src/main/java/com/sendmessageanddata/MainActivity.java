package com.sendmessageanddata;

import android.app.Activity;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends Activity {

    private static final String WEARABLE_PATH_MESSAGE = "/message";
    private static final String WEARABLE_PATH_SEND_DATA = "/send-data";
    private static final String DATA_KEY = "data key";

    private GoogleApiClient mGoogleApiClient;

    private TextView mTextView;
    private View mViewRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mViewRoot = (View) stub.findViewById(R.id.viewRoot);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(gooApiClientConnCallback)
                .addOnConnectionFailedListener(gooApiClientOnConnFail)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Wearable.MessageApi.removeListener(mGoogleApiClient, wearableMsgListener);
        Wearable.DataApi.removeListener(mGoogleApiClient, wearableDataListener);
        mGoogleApiClient.disconnect();
    }

    private GoogleApiClient.ConnectionCallbacks gooApiClientConnCallback =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    // Google API 連線成功時會執行這個方法。
                    Wearable.MessageApi.addListener(mGoogleApiClient, wearableMsgListener);
                    Wearable.DataApi.addListener(mGoogleApiClient, wearableDataListener);
                }

                @Override
                public void onConnectionSuspended(int cause) {
                    // Google API 無故斷線時，才會執行這個方法。
                    // 程式呼叫disconnect()時不會執行這個方法。
                    switch (cause) {
                        case CAUSE_SERVICE_DISCONNECTED:
                            Toast.makeText(MainActivity.this, "Google API 斷線，無法使用",
                                    Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(MainActivity.this, "Google API 異常，無法使用",
                                    Toast.LENGTH_LONG).show();
                    }
                }
            };

    private GoogleApiClient.OnConnectionFailedListener gooApiClientOnConnFail =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    // 和 Google API 連線失敗時會執行這個方法。
                    Toast.makeText(MainActivity.this, "Google API 連線失敗",
                            Toast.LENGTH_LONG).show();
                }
            };

    private MessageApi.MessageListener wearableMsgListener = new
            MessageApi.MessageListener() {
                @Override
                public void onMessageReceived(final MessageEvent messageEvent) {
                    // 呼叫messageEvent.getData()取得message中附帶的位元組陣列。
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String s = null;
                            try {
                                s = new String(messageEvent.getData(), "UTF-8");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG)
                                    .show();

                            new AsyncTaskReplyMessage().execute(messageEvent.getSourceNodeId());
                        }
                    });
                }
            };

    private class AsyncTaskReplyMessage extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            byte[] payload = "收到Message".getBytes();
            // Send the message and get the result.
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, strings[0], WEARABLE_PATH_MESSAGE,
                    payload).await();
            return null;
        }
    }

    private DataApi.DataListener wearableDataListener = new DataApi.DataListener() {
        @Override
        public void onDataChanged(DataEventBuffer dataEvents) {
            // 取得目前收到的所有資料項目。
            List<DataEvent> listDataEvents = FreezableUtils.freezeIterable(dataEvents);
            dataEvents.close();

            // 比對每一筆資料項目，找出我們需要的資料。
            for (DataEvent event : listDataEvents) {
                String path = event.getDataItem().getUri().getPath();
                if (path.equals(WEARABLE_PATH_SEND_DATA)) {
                    final DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Asset asset = dataMapItem.getDataMap()
                                    .getAsset(DATA_KEY);
                            InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                                    mGoogleApiClient, asset).await().getInputStream();
                            final Bitmap bitmap =  BitmapFactory.decodeStream(assetInputStream);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mViewRoot.setBackground(new BitmapDrawable(getResources(), bitmap));
                                }
                            });
                        }
                    }).start();
                }
            }
        }
    };
}
