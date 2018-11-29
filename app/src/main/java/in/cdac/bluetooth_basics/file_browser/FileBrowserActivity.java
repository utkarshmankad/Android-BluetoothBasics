package in.cdac.bluetooth_basics.file_browser;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import in.cdac.bluetooth_basics.R;

public class FileBrowserActivity extends AppCompatActivity implements FileBrowserAdapter.OnFileClickListener {

    RecyclerView rv_file_contents;
    FileBrowserAdapter fileBrowserAdapter;
    private String root = Environment.getExternalStorageDirectory().getPath();

    ArrayList<String> items = new ArrayList<String>();
    ArrayList<String> paths = new ArrayList<String>();
    ArrayList<String> files = new ArrayList<String>();
    ArrayList<String> filesPath = new ArrayList<String>();


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
            Toast.makeText(FileBrowserActivity.this, "This is File", Toast.LENGTH_SHORT).show();
        }

    }

    public void getDirFromRoot(String p_rootPath) {


        Boolean m_isRoot = true;
        /*items = new ArrayList<String>();
        paths = new ArrayList<String>();
        files = new ArrayList<String>();
        filesPath = new ArrayList<String>();*/

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
