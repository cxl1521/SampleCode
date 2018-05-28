package com.sendmessageanddata;

import android.content.IntentSender;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String WEARABLE_PATH_MESSAGE = "/message";
    private static final String WEARABLE_PATH_SEND_DATA = "/send-data";
    private static final String DATA_KEY = "data key";
    private static final String DATA_TIME = "time";

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
        btnSendMsg.setOnClickListener(btnSendMsgOnClick);

        Button btnSendData = (Button) findViewById(R.id.btnSendData);
        btnSendData.setOnClickListener(btnSendDataOnClick);

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
                        }
                    });
                }
            };

    private class AsyncTaskSendMessageToOtherDevices extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            // 取得所有連線的Android裝置。
            NodeApi.GetConnectedNodesResult connectedWearableDevices =
                    Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

            // 傳送message給每一個連線的Android裝置。
            for (Node node : connectedWearableDevices.getNodes()) {
                // 把Message要附帶的資料儲存在一個位元組陣列中。
                byte[] payload = "手機App傳來的message。".getBytes();

                // 傳送message。
                Wearable.MessageApi.sendMessage(
                        mGoogleApiClient, node.getId(), WEARABLE_PATH_MESSAGE,
                        payload);
            }

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
                    Toast.makeText(getApplicationContext(), "onDataChanged().", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    };

    private View.OnClickListener btnSendMsgOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new AsyncTaskSendMessageToOtherDevices().execute();
        }
    };

    private View.OnClickListener btnSendDataOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 將App專案中的影像檔包裝成Asset物件。
            InputStream inStream  = getResources().openRawResource(R.raw.image);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int numByte;
            byte[] data = new byte[16384];

            try {
                while ((numByte = inStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, numByte);
                }

                buffer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Asset assetImage = Asset.createFromBytes(buffer.toByteArray());

            // 建立傳送資料的相關物件。
            PutDataMapRequest dataMapRequest = PutDataMapRequest.create(WEARABLE_PATH_SEND_DATA);
            dataMapRequest.getDataMap().putAsset(DATA_KEY, assetImage);

            // 一定要加時間，否則下次傳送資料時就不會更新（對方不會收到資料）。
            dataMapRequest.getDataMap().putLong(DATA_TIME, new Date().getTime());

            PutDataRequest request = dataMapRequest.asPutDataRequest();
            Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                    .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                        @Override
                        public void onResult(DataApi.DataItemResult dataItemResult) {
                            Toast.makeText(getApplicationContext(),
                                    "資料傳送成功。",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

        }
    };
}
