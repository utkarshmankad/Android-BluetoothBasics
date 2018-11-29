package in.cdac.bluetooth_basics.file_browser;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.cdac.bluetooth_basics.R;

public class FileBrowserAdapter extends RecyclerView.Adapter<FileBrowserAdapter.ViewHolder> {

    public List<String> path = new ArrayList<String>();
    public List<String> item = new ArrayList<String>();
    public ArrayList<Integer> m_selectedItem = new ArrayList<Integer>();
    private boolean isRoot = false;
    private OnFileClickListener mListener;


    public FileBrowserAdapter(ArrayList<String> path, ArrayList<String> item, boolean isRoot, OnFileClickListener mListener) {
        this.path = path;
        this.item = item;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public FileBrowserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_file_browser, viewGroup, false);

        return new FileBrowserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FileBrowserAdapter.ViewHolder holder, final int i) {

        //if (!isRoot && i == 0) {
          //  holder.m_cbCheck.setVisibility(View.INVISIBLE);
        //}

        holder.m_tvFileName.setText(item.get(i));
        holder.m_ivIcon.setImageResource(setFileImageType(new File(path.get(i))));
        //holder.m_tvDate.setText(getLastDate(i));
        /*holder.m_cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    m_selectedItem.add(i);
                } else {
                    m_selectedItem.remove(m_selectedItem.indexOf(i));
                }
            }
        });*/


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFileClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public CheckBox m_cbCheck;
        public ImageView m_ivIcon;
        public TextView m_tvFileName;
        public TextView m_tvDate;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            m_tvFileName = (TextView) view.findViewById(R.id.lr_tvFileName);
            //m_tvDate = (TextView) view.findViewById(R.id.lr_tvdate);
            m_ivIcon = (ImageView) view.findViewById(R.id.lr_ivFileIcon);
            //m_cbCheck = (CheckBox) view.findViewById(R.id.lr_cbCheck);
        }

    }

    public interface OnFileClickListener {
        // TODO: Update argument type and name
        void onFileClick(int position);
    }

    private String getLastDate(int p_pos) {
        File m_file = new File(path.get(p_pos));
        SimpleDateFormat m_dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return m_dateFormat.format(m_file.lastModified());
    }

    private int setFileImageType(File m_file) {
        int m_lastIndex = m_file.getAbsolutePath().lastIndexOf(".");
        String m_filepath = m_file.getAbsolutePath();
        if (m_file.isDirectory())
            return R.drawable.ic_folder_black_24dp;
        else {
            if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".png")) {
                return R.drawable.ic_image_black_24dp;
            } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".jpg")) {
                return R.drawable.ic_image_black_24dp;
            } else {
                return R.drawable.ic_insert_drive_file_black_24dp;
            }
        }
    }
}
