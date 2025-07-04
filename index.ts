import {
  NativeModulesProxy,
  EventEmitter,
  Subscription,
} from "expo-modules-core";

// Import the native module. On web, it will be resolved to UrmoboModule.web.ts
// and on native platforms to UrmoboModule.ts
import UrmoboModule from "./src/UrmoboModule";
import UrmoboModuleView from "./src/UrmoboModuleView";
import {
  ChangeEventPayload,
  UrmoboModuleViewProps,
  IDeviceInfo,
} from "./src/UrmoboModule.types";

// Get the native constant value.
export const PI = UrmoboModule.PI;

export function hello(): string {
  return UrmoboModule.hello();
}

//manda a intent para a urmobo, e ai começa a esperar a resposta de volta
export function sendIntent(): string {
  return UrmoboModule.sendIntent();
}
//espera de retorno a intent de resposta da urmobo
export function getDeviceInfo(): IDeviceInfo {
  return UrmoboModule.getDeviceInfo();
}

export async function setValueAsync(value: string) {
  return await UrmoboModule.setValueAsync(value);
}

const emitter = new EventEmitter(
  UrmoboModule ?? NativeModulesProxy.UrmoboModule,
);

export function addChangeListener(
  listener: (event: ChangeEventPayload) => void,
): Subscription {
  return emitter.addListener<ChangeEventPayload>("onChange", listener);
}

export { UrmoboModuleView, UrmoboModuleViewProps, ChangeEventPayload };
