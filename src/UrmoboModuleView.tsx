import { requireNativeView } from 'expo';
import * as React from 'react';

import { UrmoboModuleViewProps } from './UrmoboModule.types';

const NativeView: React.ComponentType<UrmoboModuleViewProps> =
  requireNativeView('UrmoboModule');

export default function UrmoboModuleView(props: UrmoboModuleViewProps) {
  return <NativeView {...props} />;
}
