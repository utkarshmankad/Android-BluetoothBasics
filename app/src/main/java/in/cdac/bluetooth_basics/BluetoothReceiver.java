package in.cdac.bluetooth_basics;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class BluetoothReceiver extends BroadcastReceiver {

    List<BluetoothDevice> arrayListBluetoothDevices;

    BluetoothReceiver(List<BluetoothDevice> arrayListBluetoothDevices) {
        this.arrayListBluetoothDevices = arrayListBluetoothDevices;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Message msg = Message.obtain();
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            Toast.makeText(context, "ACTION_FOUND", Toast.LENGTH_SHORT).show();

            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            try {
                //device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(device, true);
                //device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);
            } catch (Exception e) {
                Log.i("Log", "Inside the exception: ");
                e.printStackTrace();
            }

            if (arrayListBluetoothDevices.size() < 1) // this checks if the size of bluetooth device is 0,then add the
            {                                           // device to the arraylist.
                //detectedAdapter.add(device.getName() + "\n" + device.getAddress());
                arrayListBluetoothDevices.add(device);
                //detectedAdapter.notifyDataSetChanged();
            } else {
                boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
                for (int i = 0; i < arrayListBluetoothDevices.size(); i++) {
                    if (device.getAddress().equals(arrayListBluetoothDevices.get(i).getAddress())) {
                        flag = false;
                    }
                }
                if (flag == true) {
                    //detectedAdapter.add(device.getName() + "\n" + device.getAddress());
                    arrayListBluetoothDevices.add(device);
                    //detectedAdapter.notifyDataSetChanged();
                }
            }
        }

    }
}
