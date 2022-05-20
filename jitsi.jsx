import React, { useEffect } from "react";
import { View, Button } from "react-native";
import JitsiMeet, { JitsiMeetView } from "./jitsi-module";

const VideoCall = () => {
  const onConferenceTerminated = (nativeEvent) => {
    /* Conference terminated event */
    console.log("---videoConferenceTerminated");

    JitsiMeet.endCall();
  };

  const onConferenceJoined = (nativeEvent) => {
    /* Conference joined event */
    console.log("---videoConferenceJoined");
  };

  const onConferenceWillJoin = (nativeEvent) => {
    /* Conference will join event */
    console.log("---videoConferenceWillJoin");
  };

  const launchCall = () => {
    setTimeout(() => {
      const options = {
        token: "",
        audioMuted: false,
        audioOnly: false,
        videoMuted: false,
        subject: "Super Duper Conference",
      };
      const meetFeatureFlags = {
        // FIXME: for some reasons, flags aren't considered in the Android plugin
        // For now, they are applied directly by the plugin
        // inside : https://gitlab.com/dfabreguette-ap/react-native-jitsi-meet-with-options/-/blob/master/android/src/main/java/com/reactnativejitsimeet/RNJitsiMeetModule.java#L56
        // "meeting-name.enabled": false,
        // "add-people.enabled": false,
        // "security-options.enabled": false,
        // // callIntegrationEnabled: true,
        // "meeting-password.enabled": false,
        // "chat.enabled": false,
      };
      const url = `https://meet.jit.si/deneme`; // can also be only room name and will connect to jitsi meet servers
      const userInfo = {
        displayName: "David",
      };
      console.log("launching call ...");
      JitsiMeet.call(url, userInfo, options, meetFeatureFlags);
    }, 1000);
  };

  // Event is triggered only on android native module
  const onParticipantsInfoRetrieved = (e) => {
    const { participantInfoList } = e.nativeEvent;
    console.log("onParticipantsInfoRetrieved", e);
  };

  // has android.permission.CALL_PHONE.

  return (
    <View
      style={{
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "red",
        width: "100%",
        height: "100%",
        paddingTop: 50
      }}
    >
      {/* {callStarted && <Text>Call already started !</Text>}*/}
      <Button onPress={launchCall} title="Call" />
      <JitsiMeetView
        onConferenceTerminated={onConferenceTerminated}
        onConferenceJoined={onConferenceJoined}
        onConferenceWillJoin={onConferenceWillJoin}
        onParticipantsInfoRetrieved={onParticipantsInfoRetrieved}
        style={{
          flex: 1,
          width: "100%",
          height: "100%",
          backgroundColor: "blue",
        }}
      />
    </View>
  );
};

export default VideoCall;
