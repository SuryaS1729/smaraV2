import { registerWebModule, NativeModule } from 'expo';

import { ChangeEventPayload } from './SmaraNative.types';

type SmaraNativeModuleEvents = {
  onChange: (params: ChangeEventPayload) => void;
}

class SmaraNativeModule extends NativeModule<SmaraNativeModuleEvents> {
  PI = Math.PI;
  async setValueAsync(value: string): Promise<void> {
    this.emit('onChange', { value });
  }
  hello() {
    return 'Hello world! 👋';
  }
};

export default registerWebModule(SmaraNativeModule);
