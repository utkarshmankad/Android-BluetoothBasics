package in.cdac.bluetooth_basics;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PairedBluetoothDeviceAdapter extends RecyclerView.Adapter<PairedBluetoothDeviceAdapter.ViewHolder> {

    private List<BluetoothDevice> mValues = new ArrayList<BluetoothDevice>();
    private OnPairedListFragmentInteractionListener mListener;

    public PairedBluetoothDeviceAdapter(ArrayList<BluetoothDevice> mValues, OnPairedListFragmentInteractionListener mListener) {
        this.mValues = mValues;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public PairedBluetoothDeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_fragment_bluetoothdevice, viewGroup, false);

        return new PairedBluetoothDeviceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PairedBluetoothDeviceAdapter.ViewHolder holder, final int i) {

        holder.bluetoothDevice = mValues.get(i);
        holder.mIdView.setText((i+1)+"");
        holder.mContentView.setText(holder.bluetoothDevice.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPairListInteraction(holder.bluetoothDevice,i);
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

    public interface OnPairedListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPairListInteraction(BluetoothDevice item, int position);
    }
}
