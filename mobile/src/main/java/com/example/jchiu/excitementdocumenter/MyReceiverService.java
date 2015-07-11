package com.example.jchiu.excitementdocumenter;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

public class MyReceiverService extends WearableListenerService {

    private static final String PHOTO_MESSAGE_PATH = "/take_photo";
    private int notification_id = 1;
    private final String NOTIFICATION_ID = "notification_id";
    private NotificationCompat.Builder notification_builder;
    private NotificationManagerCompat notification_manager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(MyReceiverService.class.getSimpleName(), "WEAR create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(MyReceiverService.class.getSimpleName(), "WEAR destroy");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        Log.i(MyReceiverService.class.getSimpleName(), "WEAR Data changed " );
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);
        Log.i(MyReceiverService.class.getSimpleName(), "WEAR Connected ");
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        super.onPeerDisconnected(peer);
        Log.i(MyReceiverService.class.getSimpleName(), "WEAR Disconnected");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i(MyReceiverService.class.getSimpleName(), "message received");
        if (messageEvent.getPath().equals(PHOTO_MESSAGE_PATH)) {
            Intent startIntent = new Intent(this, ActivatedActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.putExtra("PHOTO_DATA", messageEvent.getData());
            startActivity(startIntent);
        }
        //Intent open_activity_intent = new Intent(this, ActivatedActivity.class);

//        open_activity_intent.putExtra(NOTIFICATION_ID, notification_id);
//        PendingIntent pending_intent = PendingIntent.getActivity(this, 0, open_activity_intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        notification_builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentText(getString(R.string.notification_text))
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setAutoCancel(true)
//                .setContentIntent(pending_intent);
//        notification_manager = NotificationManagerCompat.from(this);
//        notification_manager.notify(notification_id, notification_builder.build());
    }
}
