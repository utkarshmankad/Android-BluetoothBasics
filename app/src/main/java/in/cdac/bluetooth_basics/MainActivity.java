package in.cdac.bluetooth_basics;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements BluetoothDeviceRecyclerViewAdapter.OnListFragmentInteractionListener, PairedBluetoothDeviceAdapter.OnPairedListFragmentInteractionListener {

    Switch enable_disable_bluetooth;
    Button pairing_start_stop;

    ListView listViewPaired;
    ListView listViewDetected;
    ArrayList<String> arrayListpaired;
    Button buttonSearch, buttonOn, buttonDesc, buttonOff;
    ArrayAdapter<String> adapter, detectedAdapter;
    HandleDeviceSearch handleSeacrh;
    BluetoothDevice bdDevice;
    BluetoothClass bdClass;
    ArrayList<BluetoothDevice> arrayListPairedBluetoothDevices;
    BluetoothAdapter bluetoothAdapter = null;
    ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;

    public BluetoothDeviceRecyclerViewAdapter bluetoothDeviceRecyclerViewAdapter;

    public PairedBluetoothDeviceAdapter pairedBluetoothDeviceAdapter;

    BluetoothReceiver bluetoothReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayListpaired = new ArrayList<String>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handleSeacrh = new HandleDeviceSearch();
        arrayListPairedBluetoothDevices = new ArrayList<BluetoothDevice>();
        arrayListBluetoothDevices = new ArrayList<BluetoothDevice>();

        bluetoothReceiver = new BluetoothReceiver(arrayListBluetoothDevices,bluetoothDeviceRecyclerViewAdapter);

        pairedBluetoothDeviceAdapter = new PairedBluetoothDeviceAdapter(arrayListPairedBluetoothDevices,this);
        bluetoothDeviceRecyclerViewAdapter = new BluetoothDeviceRecyclerViewAdapter(arrayListBluetoothDevices, this);

        // Get the default adapter
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.e("Bluetooth not supported", "Bluetooth not supoorted on this device");
        }

        //Paired devices list view
        RecyclerView recyclerView_paired_devices = (RecyclerView)findViewById(R.id.rv_paired_devices);
        recyclerView_paired_devices.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_paired_devices.setAdapter(pairedBluetoothDeviceAdapter);

        RecyclerView recyclerView_new_devices = (RecyclerView)findViewById(R.id.rv_new_devices);
        recyclerView_new_devices.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_new_devices.setAdapter(bluetoothDeviceRecyclerViewAdapter);

        //Switch to enable or disable bluetooth
        enable_disable_bluetooth = (Switch) findViewById(R.id.bluetooth_switch);
        enable_disable_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    //turn on bluetooth
                    onBluetooth();
                    //make bluetooth discoverable
                    makeDiscoverable();
                } else {
                    //turn off bluetooth
                    offBluetooth();
                }
            }
        });

        //Button to start pairing of device
        pairing_start_stop = (Button) findViewById(R.id.button);
        pairing_start_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayListBluetoothDevices.clear();
                startSearching();
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        //getpaired devices
        getPairedDevices();
    }

    @Override
    public void onListFragmentInteraction(BluetoothDevice item) {
        Log.e("Bluetooth device", "item clicked");
    }


    private void startSearching() {
        Log.i("Log", "in the start searching method");
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        MainActivity.this.registerReceiver(bluetoothReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();
    }

    private void onBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
            Log.i("Log", "Bluetooth is Enabled");
        }
    }

    private void offBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    private void makeDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        Log.i("Log", "Discoverable ");
    }

    private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

        if (pairedDevice.size() > 0) {

            for (BluetoothDevice device : pairedDevice) {

                arrayListpaired.add(device.getName() + "\n" + device.getAddress());
                arrayListPairedBluetoothDevices.add(device);
            }
        }
        pairedBluetoothDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPairListInteraction(BluetoothDevice item) {
        Log.e("Bluetooth device", "paired item clicked");
    }
}