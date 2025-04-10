import * as React from 'react';

import { SmaraNativeViewProps } from './SmaraNative.types';

export default function SmaraNativeView(props: SmaraNativeViewProps) {
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
