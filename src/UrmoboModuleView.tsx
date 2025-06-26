import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { UrmoboModuleViewProps } from './UrmoboModule.types';

const NativeView: React.ComponentType<UrmoboModuleViewProps> =
  requireNativeViewManager('UrmoboModule');

export default function UrmoboModuleView(props: UrmoboModuleViewProps) {
  return <NativeView {...props} />;
}
