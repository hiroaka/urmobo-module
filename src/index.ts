// Reexport the native module. On web, it will be resolved to UrmoboModule.web.ts
// and on native platforms to UrmoboModule.ts
export { default } from './UrmoboModule';
export { default as UrmoboModuleView } from './UrmoboModuleView';
export * from  './UrmoboModule.types';
