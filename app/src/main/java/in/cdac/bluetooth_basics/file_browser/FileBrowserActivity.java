package in.cdac.bluetooth_basics.file_browser;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.cdac.bluetooth_basics.R;

public class FileBrowserActivity extends AppCompatActivity implements FileBrowserAdapter.OnFileClickListener {

    RecyclerView rv_file_contents;
    FileBrowserAdapter fileBrowserAdapter;
    private String root = Environment.getExternalStorageDirectory().getPath();

    ArrayList<String> items = new ArrayList<String>();
    ArrayList<String> paths = new ArrayList<String>();
    ArrayList<String> files = new ArrayList<String>();
    ArrayList<String> filesPath = new ArrayList<String>();

    static final int CUSTOM_DIALOG_ID = 0;
    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;
    BluetoothAdapter btAdatper = BluetoothAdapter.getDefaultAdapter();
    File dataPath = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        rv_file_contents = (RecyclerView) findViewById(R.id.rv_files);
        fileBrowserAdapter = new FileBrowserAdapter(paths, items,true,this);
        rv_file_contents.setLayoutManager(new LinearLayoutManager(getApplicationContext() ,LinearLayoutManager.VERTICAL, false));
        rv_file_contents.setAdapter(fileBrowserAdapter);
        getDirFromRoot(root);
        fileBrowserAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFileClick(int position) {
        File m_isFile = new File(paths.get(position));

        if (m_isFile.isDirectory()) {
            getDirFromRoot(m_isFile.toString());
            fileBrowserAdapter.notifyDataSetChanged();
        } else {
            //Toast.makeText(FileBrowserActivity.this, "This is File", Toast.LENGTH_SHORT).show();
            dataPath = m_isFile;
            sendViaBluetooth(dataPath);
        }

    }

    public void getDirFromRoot(String p_rootPath) {


        Boolean m_isRoot = true;

        //clear items before populating
        paths.clear();
        items.clear();

        File m_file = new File(p_rootPath);
        File[] m_filesArray = m_file.listFiles();
        if (!p_rootPath.equals(root)) {
            items.add("../");
            paths.add(m_file.getParent());
            m_isRoot = false;
        }

        //m_curDir = p_rootPath;

        //sorting file list in alphabetical order
        Arrays.sort(m_filesArray);
        for (int i = 0; i < m_filesArray.length; i++) {
            File file = m_filesArray[i];
            if (file.isDirectory()) {
                items.add(file.getName());
                paths.add(file.getPath());
            } else {
                files.add(file.getName());
                filesPath.add(file.getPath());
            }
        }
        for (String m_AddFile : files) {
            items.add(m_AddFile);
        }
        for (String m_AddPath : filesPath) {
            paths.add(m_AddPath);
        }

    }

    //Method for send file via bluetooth------------------------------------------------------------
    public void sendViaBluetooth(File dataPath) {
        if(!dataPath.equals(null)){
            if (btAdatper == null) {
                Toast.makeText(this, "Device not support bluetooth", Toast.LENGTH_LONG).show();
            } else {
                enableBluetooth();
            }
        }else{
            Toast.makeText(this,"Please select a file.",Toast.LENGTH_LONG).show();
        }
    }

    public void enableBluetooth() {
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);
        startActivityForResult(discoveryIntent, REQUEST_BLU);
    }

    //Override method for sending data via bluetooth availability--------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == DISCOVER_DURATION && requestCode == REQUEST_BLU) {

            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.setType("*/*");

            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(dataPath));

            PackageManager pm = getPackageManager();

            List<ResolveInfo> list = pm.queryIntentActivities(i, 0);

            if (list.size() > 0) {
                String packageName = null;
                String className = null;
                boolean found = false;

                for (ResolveInfo info : list) {
                    packageName = info.activityInfo.packageName;
                    if (packageName.equals("com.android.bluetooth")) {
                        className = info.activityInfo.name;
                        found = true;
                        break;
                    }
                }
                //CHECK BLUETOOTH available or not------------------------------------------------
                if (!found) {
                    Toast.makeText(this, "Bluetooth not been found", Toast.LENGTH_LONG).show();
                } else {
                    i.setClassName(packageName, className);
                    startActivity(i);
                }
            }
        } else {
            Toast.makeText(this, "Bluetooth is cancelled", Toast.LENGTH_LONG).show();
        }
    }

    //Method to delete selected files
    /*void deleteFile() {
        for (int m_delItem : m_listAdapter.m_selectedItem) {
            File m_delFile = new File(m_path.get(m_delItem));
            Log.d("file", m_path.get(m_delItem));
            boolean m_isDelete = m_delFile.delete();
            Toast.makeText(RootListActivity.this, "File(s) Deledted", Toast.LENGTH_SHORT).show();
            getDirFromRoot(m_curDir);

        }
    }

    void createNewFolder(final int p_opt) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        // Set up the input
        final EditText m_edtinput = new EditText(this);
        // Specify the type of input expected;
        m_edtinput.setInputType(InputType.TYPE_CLASS_TEXT);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_text = m_edtinput.getText().toString();
                if (p_opt == 1) {
                    File m_newPath = new File(m_curDir, m_text);
                    Log.d("cur dir", m_curDir);
                    if (!m_newPath.exists()) {
                        m_newPath.mkdirs();
                    }
                } else {
                    try {
                        FileOutputStream m_Output = new FileOutputStream((m_curDir + File.separator + m_text), false);
                        m_Output.close();
                        //  <!--<intent-filter>
                        //  <action android:name="android.intent.action.SEARCH" />
                        //  </intent-filter>
                        //  <meta-data android:name="android.app.searchable"
                        //  android:resource="@xml/searchable"/>-->

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getDirFromRoot(m_curDir);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setView(m_edtinput);
        builder.show();
    }*/

}
