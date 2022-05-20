/**
 * @providesModule JitsiMeet
 */
import { NativeModules, requireNativeComponent } from "react-native";
export const JitsiMeetView = requireNativeComponent("RNJitsiMeetView");
export const JitsiMeetModule = NativeModules.RNJitsiMeetModule;

const call = JitsiMeetModule.call;
const audioCall = JitsiMeetModule.audioCall;
const getParticipantsInfo = JitsiMeetModule.retrieveParticipantsInfo;
JitsiMeetModule.getParticipantsInfo = () => {
  return getParticipantsInfo();
};
JitsiMeetModule.call = (url, userInfo, meetOptions, meetFeatureFlags) => {
  userInfo = userInfo || {};
  call(url, userInfo, meetOptions, meetFeatureFlags);
};
JitsiMeetModule.audioCall = (url, userInfo) => {
  userInfo = userInfo || {};
  audioCall(url, userInfo);
};
export default JitsiMeetModule;
