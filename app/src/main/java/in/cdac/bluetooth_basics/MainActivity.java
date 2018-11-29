package in.cdac.bluetooth_basics;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import in.cdac.bluetooth_basics.file_browser.FileBrowserActivity;

public class MainActivity extends AppCompatActivity implements BluetoothDeviceRecyclerViewAdapter.OnListFragmentInteractionListener, PairedBluetoothDeviceAdapter.OnPairedListFragmentInteractionListener, PairedBluetoothDeviceAdapter.OnPairedListItemLongPressListener {

    Switch enable_disable_bluetooth;
    Button pairing_start_stop, send_data;

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

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        arrayListPairedBluetoothDevices = new ArrayList<BluetoothDevice>();
        arrayListBluetoothDevices = new ArrayList<BluetoothDevice>();

        pairedBluetoothDeviceAdapter = new PairedBluetoothDeviceAdapter(arrayListPairedBluetoothDevices, this, this);
        bluetoothDeviceRecyclerViewAdapter = new BluetoothDeviceRecyclerViewAdapter(arrayListBluetoothDevices, this);

        bluetoothReceiver = new BluetoothReceiver(arrayListBluetoothDevices, bluetoothDeviceRecyclerViewAdapter);

        // Get the default adapter
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.e("Bluetooth not supported", "Bluetooth not supoorted on this device");
        }

        //Paired devices list view
        RecyclerView recyclerView_paired_devices = (RecyclerView) findViewById(R.id.rv_paired_devices);
        recyclerView_paired_devices.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_paired_devices.setAdapter(pairedBluetoothDeviceAdapter);

        RecyclerView recyclerView_new_devices = (RecyclerView) findViewById(R.id.rv_new_devices);
        recyclerView_new_devices.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_new_devices.setAdapter(bluetoothDeviceRecyclerViewAdapter);

        //Switch to enable or disable bluetooth
        enable_disable_bluetooth = (Switch) findViewById(R.id.bluetooth_switch);
        if (bluetoothAdapter.isEnabled()) {
            enable_disable_bluetooth.setChecked(true);
            makeDiscoverable();
        } else {
            enable_disable_bluetooth.setChecked(false);
        }
        enable_disable_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (!isChecked) {
                    //turn off bluetooth
                    offBluetooth();
                } else {
                    //turn on bluetooth
                    onBluetooth();
                    //make bluetooth discoverable
                    makeDiscoverable();
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

        send_data = (Button) findViewById(R.id.send_data);
        send_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FileBrowserActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //getpaired devices
        getPairedDevices();
    }

    @Override
    public void onListFragmentInteraction(BluetoothDevice item, int position) {
        Log.e("Bluetooth device", "item clicked");
        bdDevice = arrayListBluetoothDevices.get(position);
        //bdClass = arrayListBluetoothDevices.get(position);
        Log.i("Log", "The dvice : " + bdDevice.toString());
        /*
         * here below we can do pairing without calling the callthread(), we can directly call the
         * connect(). but for the safer side we must usethe threading object.
         */
        Boolean isBonded = false;
        try {
            isBonded = createBond(bdDevice);
            if (isBonded) {
                getPairedDevices();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Log", "The bond is created: " + isBonded);
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
        arrayListPairedBluetoothDevices.clear();

        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

        if (pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                arrayListPairedBluetoothDevices.add(device);
            }
        }
        pairedBluetoothDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPairListInteraction(BluetoothDevice item, int position) {
        Log.e("Bluetooth device", "paired item clicked");

        /*Request to send data to the clicked paired device*/
        //startActivity(new Intent(getApplicationContext(), FileBrowserActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


    }

    public boolean removeBond(BluetoothDevice btDevice)
            throws Exception {
        Class btClass = Class.forName("android.bluetooth.BluetoothDevice");
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }


    public boolean createBond(BluetoothDevice btDevice)
            throws Exception {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    @Override
    public void onPairedLsitItemLongPressListener(BluetoothDevice item, int position) {
        /*
         *Code to remove Bluetooth paired device*/
        bdDevice = arrayListPairedBluetoothDevices.get(position);
        try {
            Boolean removeBonding = removeBond(bdDevice);
            if (removeBonding) {
                arrayListPairedBluetoothDevices.remove(position);
                pairedBluetoothDeviceAdapter.notifyItemRemoved(position);
            }
            Log.i("Log", "Removed" + removeBonding);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter.isEnabled())
            bluetoothAdapter.disable();
    }
}