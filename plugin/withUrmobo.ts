// // withUrmoboModule.ts
// import {
//   ConfigPlugin,
//   withAndroidManifest,
//   // AndroidConfig,
//   withDangerousMod,
// } from "expo/config-plugins";
// import * as fs from "fs";
// import * as path from "path";
// import { deviceInfoReceiverTemplate } from "./deviceReceiverTemplate";

// const { getMainApplicationOrThrow } = AndroidConfig.Manifest;

const withUrmoboModule = (config) => {
  // Modify Android manifest
  // config = withAndroidManifest(config, async (config) => {
  //   // const androidManifest = config.modResults;

  //   // // Add the BroadcastReceiver to the manifest
  //   // addBroadcastReceiver(androidManifest);

  //   return config;
  // });

  // // Create the DeviceInfoReceiver.java file
  // config = withDangerousMod(config, [
  //   "android",
  //   async (config) => {
  //     // const mainApplication = getMainApplicationOrThrow(config.modResults);
  //     // const packageName = mainApplication.$["android:name"]
  //     //   .split(".")
  //     //   .slice(0, -1)
  //     //   .join(".");

  //     // const filePath = path.join(
  //     //   config.modRequest.projectRoot,
  //     //   "android",
  //     //   "app",
  //     //   "src",
  //     //   "main",
  //     //   "java",
  //     //   ...packageName.split("."),
  //     //   "DeviceInfoReceiver.java"
  //     // );

  //     // // Ensure directory exists
  //     // const dir = path.dirname(filePath);
  //     // if (!fs.existsSync(dir)) {
  //     //   fs.mkdirSync(dir, { recursive: true });
  //     // }

  //     // // Write the file if it doesn't exist
  //     // if (!fs.existsSync(filePath)) {
  //     //   fs.writeFileSync(filePath, deviceInfoReceiverTemplate);
  //     // }

  //     return config;
  //   },
  // ]);

  return config;
};

// function addBroadcastReceiver(androidManifest: any) {
//   // Same implementation as before
//   // ...
// }

export default withUrmoboModule;
