export const deviceInfoReceiverTemplate = `package com.megaappgnos.urmobo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceInfoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // This method will be called when the receiver receives a broadcast
        // You can leave it empty as we're using the dynamic receiver in UrmoboModule
    }
}
`;
