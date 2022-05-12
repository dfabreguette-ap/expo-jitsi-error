import { StatusBar } from "expo-status-bar";
import { StyleSheet, Text, View, Button } from "react-native";
import BLJitsiShow from "./jitsi";
import React from "react";

export default function App() {
  const [showJitsi, setShowJitsi] = React.useState(false);

  return (
    <View style={styles.container}>
      {showJitsi ? (
        <BLJitsiShow></BLJitsiShow>
      ) : (
        <Button title="JITSI" onPress={() => setShowJitsi(true)}></Button>
      )}
      <StatusBar style="auto" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: "100%",
    height: "100%",
    backgroundColor: "#eee",
    alignItems: "center",
    justifyContent: "center",
  },
});
