package com.davonrails.jitsitest.jitsi;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.module.annotations.ReactModule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jitsi.meet.sdk.BroadcastAction;
import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.BroadcastReceiver;
import org.jitsi.meet.sdk.JitsiMeetViewListener;
import org.jitsi.meet.sdk.ParticipantInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ReactModule(name = RNJitsiMeetViewManager.REACT_CLASS)
public class RNJitsiMeetViewManager extends SimpleViewManager<RNJitsiMeetView> implements JitsiMeetViewListener {
    public static final String REACT_CLASS = "RNJitsiMeetView";
    private IRNJitsiMeetViewReference mJitsiMeetViewReference;
    private ReactApplicationContext mReactContext;

    private static final String REQUEST_ID = "requestId";
    private static final String TAG = "RNJitsiMeetViewManager";

    private final BroadcastReceiver broadcastReceiver;

    public RNJitsiMeetViewManager(ReactApplicationContext reactContext, IRNJitsiMeetViewReference jitsiMeetViewReference) {
        mJitsiMeetViewReference = jitsiMeetViewReference;
        mReactContext = reactContext;


        broadcastReceiver = new BroadcastReceiver(mReactContext) {
            @Override
            public void onReceive(Context context, Intent intent) {
                onBroadcastReceived(intent);
            }
        };
    }

    private void onBroadcastReceived(Intent intent) {
        //Log.d("JitsiBT", "youpi");
        //Log.d("JitsiBT", intent.getAction());


        BroadcastEvent event = new BroadcastEvent(intent);

        try {
            Type listType = new TypeToken<ArrayList<ParticipantInfo>>(){}.getType();

            List<ParticipantInfo> participantInfoList = new Gson().fromJson(
                    event.getData().get("participantsInfo").toString(),
                    listType);

            onParticipantsInfoRetrieved(participantInfoList);

        } catch (Exception e) {
            Log.w(TAG, "error parsing participantsList", e);
        }

    }

    public void onParticipantsInfoRetrieved(List<ParticipantInfo> participantInfoList) {
//        WritableMap event = Arguments.createMap();
////        event.putArray("participantInfoList", participantInfoList.toArray());
//        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
//                mJitsiMeetViewReference.getJitsiMeetView().getId(),
//                "onParticipantsInfoRetrieved",
//                event);
        WritableMap event = Arguments.createMap();
        WritableArray pList = new WritableNativeArray();
        for (ParticipantInfo p : participantInfoList) {
            WritableMap b = new WritableNativeMap();
            b.putString("participantId", p.id);
            b.putString("displayName", p.displayName);
            b.putString("avatarUrl", p.avatarUrl);
            b.putString("email", p.email);
            b.putString("name", p.name);
            b.putBoolean("isLocal", p.isLocal);
            b.putString("role", p.role);
            pList.pushMap(b);
        }

//        event.putString("message", "MyMessage");
        event.putArray("participantInfoList", pList);
        ReactContext reactContext = mReactContext;
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                mJitsiMeetViewReference.getJitsiMeetView().getId(),
                "onParticipantsInfoRetrieved",
                event);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public RNJitsiMeetView createViewInstance(ThemedReactContext context) {
        if (mJitsiMeetViewReference.getJitsiMeetView() == null) {
            RNJitsiMeetView view = new RNJitsiMeetView(context.getCurrentActivity());
            view.setListener(this);
            mJitsiMeetViewReference.setJitsiMeetView(view);
        }
        return mJitsiMeetViewReference.getJitsiMeetView();
    }

    public void onConferenceJoined(Map<String, Object> data) {
        WritableMap event = Arguments.createMap();
        event.putString("url", (String) data.get("url"));
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                mJitsiMeetViewReference.getJitsiMeetView().getId(),
                "conferenceJoined",
                event);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastEvent.Type.PARTICIPANTS_INFO_RETRIEVED.getAction());
        LocalBroadcastManager.getInstance(this.mReactContext).registerReceiver(broadcastReceiver, intentFilter);

        Intent participantsBroadcastIntent = new Intent("org.jitsi.meet.RETRIEVE_PARTICIPANTS_INFO");
        participantsBroadcastIntent.addFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION);
        participantsBroadcastIntent.putExtra("requestId", REQUEST_ID);
        LocalBroadcastManager.getInstance(this.mReactContext).sendBroadcast(participantsBroadcastIntent);
    }

    public void onConferenceTerminated(Map<String, Object> data) {
        WritableMap event = Arguments.createMap();
        event.putString("url", (String) data.get("url"));
        event.putString("error", (String) data.get("error"));
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                mJitsiMeetViewReference.getJitsiMeetView().getId(),
                "conferenceTerminated",
                event);
    }


    public void onConferenceWillJoin(Map<String, Object> data) {
        WritableMap event = Arguments.createMap();
        event.putString("url", (String) data.get("url"));
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                mJitsiMeetViewReference.getJitsiMeetView().getId(),
                "conferenceWillJoin",
                event);
    }


    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put("conferenceJoined", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onConferenceJoined")))
                .put("conferenceTerminated", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onConferenceTerminated")))
                .put("conferenceWillJoin", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onConferenceWillJoin")))
                .put("onParticipantsInfoRetrieved", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onParticipantsInfoRetrieved")))
                .build();
    }
}