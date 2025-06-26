import * as React from 'react';

import { UrmoboModuleViewProps } from './UrmoboModule.types';

export default function UrmoboModuleView(props: UrmoboModuleViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
