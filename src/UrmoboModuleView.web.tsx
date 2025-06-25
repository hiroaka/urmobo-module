import * as React from 'react';

import { UrmoboModuleViewProps } from './UrmoboModule.types';

export default function UrmoboModuleView(props: UrmoboModuleViewProps) {
  return (
    <div>
      <iframe
        style={{ flex: 1 }}
        src={props.url}
        onLoad={() => props.onLoad({ nativeEvent: { url: props.url } })}
      />
    </div>
  );
}
