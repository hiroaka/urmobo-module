import { NativeModule, requireNativeModule } from 'expo';

import { UrmoboModuleEvents } from './UrmoboModule.types';

declare class UrmoboModule extends NativeModule<UrmoboModuleEvents> {
  PI: number;
  hello(): string;
  setValueAsync(value: string): Promise<void>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<UrmoboModule>('UrmoboModule');
