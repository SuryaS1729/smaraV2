import { requireNativeView } from 'expo';
import * as React from 'react';

import { SmaraNativeViewProps } from './SmaraNative.types';

const NativeView: React.ComponentType<SmaraNativeViewProps> =
  requireNativeView('SmaraNative');

export default function SmaraNativeView(props: SmaraNativeViewProps) {
  return <NativeView {...props} />;
}
