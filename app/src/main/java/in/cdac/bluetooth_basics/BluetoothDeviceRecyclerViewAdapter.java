package in.cdac.bluetooth_basics;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BluetoothDeviceRecyclerViewAdapter extends RecyclerView.Adapter<BluetoothDeviceRecyclerViewAdapter.ViewHolder> {

    private List<BluetoothDevice> mValues = new ArrayList<BluetoothDevice>();
    private OnListFragmentInteractionListener mListener;
    BluetoothDeviceRecyclerViewAdapter bluetoothDeviceRecyclerViewAdapter;

    public BluetoothDeviceRecyclerViewAdapter(List<BluetoothDevice> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        bluetoothDeviceRecyclerViewAdapter = this;
    }

    public BluetoothDeviceRecyclerViewAdapter getInstance(){
        return  bluetoothDeviceRecyclerViewAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_fragment_bluetoothdevice, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.bluetoothDevice = mValues.get(position);
        holder.mIdView.setText((position+1)+"");
        holder.mContentView.setText(holder.bluetoothDevice.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.bluetoothDevice);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        public final TextView mIdView;
        public final TextView mContentView;

        public BluetoothDevice bluetoothDevice;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(BluetoothDevice item);
    }
}
