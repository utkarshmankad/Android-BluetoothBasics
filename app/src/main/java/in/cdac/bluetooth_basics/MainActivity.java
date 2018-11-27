package in.cdac.bluetooth_basics;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import in.cdac.bluetooth_basics.lists.BluetoothDeviceFragment;
import in.cdac.bluetooth_basics.lists.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements BluetoothDeviceFragment.OnListFragmentInteractionListener {

    Switch enable_disable_bluetooth;
    Button pairing_start_stop;

    BluetoothHeadset mBluetoothHeadset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the default adapter
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.e("Bluetooth not supported","Bluetooth not supoorted on this device");
        }

        //Switch to enable or disable bluetooth
        enable_disable_bluetooth = (Switch)findViewById(R.id.bluetooth_switch);
        enable_disable_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    if(!mBluetoothAdapter.isEnabled()){
                        mBluetoothAdapter.enable();
                    }
                    else{
                        Log.e("Bluetooth enabled","Bluetooth enabled");
                    }
                }else{
                    if(mBluetoothAdapter.isEnabled()){
                        mBluetoothAdapter.disable();
                    }
                }
            }
        });

        //Button to start pairing of device
        pairing_start_stop = (Button)findViewById(R.id.button);
        pairing_start_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
