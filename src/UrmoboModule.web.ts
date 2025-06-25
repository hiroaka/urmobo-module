import { registerWebModule, NativeModule } from 'expo';

import { UrmoboModuleEvents } from './UrmoboModule.types';

class UrmoboModule extends NativeModule<UrmoboModuleEvents> {
  PI = Math.PI;
  async setValueAsync(value: string): Promise<void> {
    this.emit('onChange', { value });
  }
  hello() {
    return 'Hello world! 👋';
  }
}

export default registerWebModule(UrmoboModule, 'UrmoboModule');
