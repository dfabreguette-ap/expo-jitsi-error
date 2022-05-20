package com.davonrails.jitsitest.jitsi;

import android.util.Log;
import java.net.URL;
import java.net.MalformedURLException;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.ReadableMap;

@ReactModule(name = "RNJitsiMeetModule")
public class RNJitsiMeetModule extends ReactContextBaseJavaModule {
    public static final String MODULE_NAME = "RNJitsiMeetModule";
    private IRNJitsiMeetViewReference mJitsiMeetViewReference;

    public RNJitsiMeetModule(ReactApplicationContext reactContext, IRNJitsiMeetViewReference jitsiMeetViewReference) {
        super(reactContext);
        mJitsiMeetViewReference = jitsiMeetViewReference;
    }

    @Override
    public String getName() {
        Log.d("RNJ", MODULE_NAME);
        return "RNJitsiMeetModule";
    }

    @ReactMethod
    public void initialize() {
        Log.d("JitsiMeet", "Initialize is deprecated in v2");
    }

    @ReactMethod
    public void retrieveParticipantsInfo(Promise promise) {
        mJitsiMeetViewReference.getJitsiMeetView().retrieveParticipantsInfo();
    }

    @ReactMethod
    public void call(String url, ReadableMap userInfo, ReadableMap meetOptions, ReadableMap meetFeatureFlags) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mJitsiMeetViewReference.getJitsiMeetView() != null) {
                    RNJitsiMeetUserInfo _userInfo = new RNJitsiMeetUserInfo();
                    if (userInfo != null) {
                        if (userInfo.hasKey("displayName")) {
                            _userInfo.setDisplayName(userInfo.getString("displayName"));
                          }
                          if (userInfo.hasKey("email")) {
                            _userInfo.setEmail(userInfo.getString("email"));
                          }
                          if (userInfo.hasKey("avatar")) {
                            String avatarURL = userInfo.getString("avatar");
                            try {
                                _userInfo.setAvatar(new URL(avatarURL));
                            } catch (MalformedURLException e) {
                            }
                          }
                    }
                    RNJitsiMeetConferenceOptions options = new RNJitsiMeetConferenceOptions.Builder()
                            .setRoom(url)
                            .setToken(meetOptions.getString("token"))
                            .setSubject(meetOptions.getString("subject"))
                            .setAudioMuted(meetOptions.getBoolean("audioMuted"))
                            .setAudioOnly(meetOptions.getBoolean("audioOnly"))
                            .setVideoMuted(meetOptions.getBoolean("videoMuted"))
                            .setUserInfo(_userInfo)
                            .setFeatureFlag("invite.enabled", false)
                            .setFeatureFlag("meeting-password.enabled", false)
                            .setFeatureFlag("raise-hand.enabled", false)
                            .setFeatureFlag("chat.enabled", false)
                            .setFeatureFlag("meeting-name.enabled", false)
                            .setFeatureFlag("recording.enabled", false)
                            .setFeatureFlag("security-options.enabled", false)
                            .build();

//                    String[] flagKeys = new String[]{"add-people.enabled", "calendar.enabled", "call-integration.enabled", "chat.enabled", "audio-mute.enabled",
//                            "close-captions.enabled", "invite.enabled", "ios.recording.enabled", "live-streaming.enabled", "meeting-name.enabled", "security-options.enabled",
//                    "meeting-password.enabled", "pip.enabled", "raise-hand.enabled", "recording.enable", "toolbox.alwaysVisible", "welcomepage.enabled"};
//
//                    for (int i = 0; i < flagKeys.length; i++) {
//                        String flagKey = flagKeys[i];
//                        try {
//                            Boolean flagValue = meetFeatureFlags.getBoolean(flagKey);
//                            optionsBuilder = optionsBuilder.setFeatureFlag(flagKey, flagValue);
//                        }
//                        catch (NoSuchKeyException e) { }
//                    }
//
//                    RNJitsiMeetConferenceOptions options = optionsBuilder.build();
                    mJitsiMeetViewReference.getJitsiMeetView().join(options);
                }
            }
        });
    }

    @ReactMethod
    public void audioCall(String url, ReadableMap userInfo) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mJitsiMeetViewReference.getJitsiMeetView() != null) {
                    RNJitsiMeetUserInfo _userInfo = new RNJitsiMeetUserInfo();
                    if (userInfo != null) {
                        if (userInfo.hasKey("displayName")) {
                            _userInfo.setDisplayName(userInfo.getString("displayName"));
                          }
                          if (userInfo.hasKey("email")) {
                            _userInfo.setEmail(userInfo.getString("email"));
                          }
                          if (userInfo.hasKey("avatar")) {
                            String avatarURL = userInfo.getString("avatar");
                            try {
                                _userInfo.setAvatar(new URL(avatarURL));
                            } catch (MalformedURLException e) {
                            }
                          }
                    }
                    RNJitsiMeetConferenceOptions options = new RNJitsiMeetConferenceOptions.Builder()
                            .setRoom(url)
                            .setAudioOnly(true)
                            .setUserInfo(_userInfo)
                            .build();
                    mJitsiMeetViewReference.getJitsiMeetView().join(options);
                }
            }
        });
    }

    @ReactMethod
    public void endCall() {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mJitsiMeetViewReference.getJitsiMeetView() != null) {
                    mJitsiMeetViewReference.getJitsiMeetView().leave();
                }
            }
        });
    }
}
