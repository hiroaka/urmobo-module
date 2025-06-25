// Modificar src/UrmoboModule.types.ts
import type { StyleProp, ViewStyle } from "react-native";

export type OnLoadEventPayload = {
  url: string;
};

export type UrmoboModuleEvents = {
  onChange: (params: ChangeEventPayload) => void;
  onDeviceInfo: (params: DeviceInfoEventPayload) => void;
};

export type ChangeEventPayload = {
  value: string;
};

export type DeviceInfoEventPayload = {
  imei: string;
  serialNumber: string;
  deviceId: string;
};

export type DeviceInfo = {
  imei: string;
  serialNumber: string;
  deviceId: string;
};

export type UrmoboModuleViewProps = {
  url: string;
  onLoad: (event: { nativeEvent: OnLoadEventPayload }) => void;
  style?: StyleProp<ViewStyle>;
};
