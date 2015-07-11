package com.example.jchiu.excitementdocumenter;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class OpenCameraService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private static final String PHOTO_CAPABILITY_NAME = "take_photo";
    private String photoNodeId = null;
    public static final String PHOTO_MESSAGE_PATH = "/take_photo";

    public OpenCameraService() {

    }

    @Override
    public void onCreate() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnected(Bundle bundle) {
        setUpTakePhoto();
    }

    private void setUpTakePhoto() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CapabilityApi.GetCapabilityResult result =
                        Wearable.CapabilityApi.getCapability(
                                mGoogleApiClient, PHOTO_CAPABILITY_NAME,
                                CapabilityApi.FILTER_REACHABLE).await();

                Collection<String> nodes = getNodes();
                Log.i(OpenCameraService.class.getSimpleName(), String.valueOf(nodes.size()));

                for (String node : nodes) {
                    photoNodeId = node;
                    Log.i(OpenCameraService.class.getSimpleName(), String.valueOf(photoNodeId));

                }

                Wearable.MessageApi.sendMessage(mGoogleApiClient, photoNodeId,
                        PHOTO_MESSAGE_PATH, "null".getBytes()).setResultCallback(
                        new ResultCallback<MessageApi.SendMessageResult>() {
                            @Override
                            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                if (!sendMessageResult.getStatus().isSuccess()) {
                                    Log.i(OpenCameraService.class.getSimpleName(), "message failed");
                                }
                            }
                        });
            }
        }).start();
    }

    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }
        return results;
    }

    private void updatePhotoCapability(CapabilityInfo capabilityInfo) {
        Set<Node> connectedNodes = capabilityInfo.getNodes();

        photoNodeId = pickBestNodeId(connectedNodes);
        sendMessage();
    }


    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }

    private void sendMessage() {
        Wearable.MessageApi.sendMessage(mGoogleApiClient, photoNodeId,
                PHOTO_MESSAGE_PATH, "hello".getBytes()).setResultCallback(
                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.i(OpenCameraService.class.getSimpleName(), "message failed");
                        }
                    }
                }
        );
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
