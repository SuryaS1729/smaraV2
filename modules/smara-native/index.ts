// Reexport the native module. On web, it will be resolved to SmaraNativeModule.web.ts
// and on native platforms to SmaraNativeModule.ts
export { default } from './src/SmaraNativeModule';
export { default as SmaraNativeView } from './src/SmaraNativeView';
export * from  './src/SmaraNative.types';
