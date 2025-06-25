// Modificar src/UrmoboModule.ts
import { NativeModule, requireNativeModule } from "expo";

import { DeviceInfo, UrmoboModuleEvents } from "./UrmoboModule.types";

declare class UrmoboModule extends NativeModule<UrmoboModuleEvents> {
  PI: number;
  hello(): string;
  setValueAsync(value: string): Promise<void>;
  getDeviceInfo(): Promise<DeviceInfo>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<UrmoboModule>("UrmoboModule");
