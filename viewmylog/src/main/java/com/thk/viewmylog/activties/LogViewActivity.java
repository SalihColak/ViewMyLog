package com.thk.viewmylog.activties;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thk.viewmylog.R;
import com.thk.viewmylog.adapter.ActivityLogAdapter;
import com.thk.viewmylog.data.LogReader;
import com.thk.viewmylog.entities.Log;
import com.thk.viewmylog.helper.LogParser;
import com.thk.viewmylog.helper.MainThread;
import com.thk.viewmylog.interfaces.LogListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LogViewActivity extends AppCompatActivity {

    private List<Log> logList;
    private LogReader logReader;
    private MainThread mainThread;

    private RecyclerView recyclerView;
    private ActivityLogAdapter activityLogAdapter;

    private Button btnScroll;
    private androidx.appcompat.widget.SearchView searchView;
    private boolean endReached = true;
    private boolean read = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logview);
        init();
        readLogs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logactivtiymenu, menu);
        searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.filter).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                activityLogAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                activityLogAdapter.filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save){
            saveToFile();
        }
        else if(item.getItemId() == R.id.stopContinue){
            if(read){
                logReader.stopReading();
                item.setIcon(R.drawable.ic_baseline_play_white_24);
                item.setTitle(R.string.continueRead);
                read = !read;
            }else{
                logReader.continueReading();
                item.setIcon(R.drawable.ic_baseline_pause_white_24);
                item.setTitle(R.string.stopRead);
                read = !read;
            }
        }
        else if(item.getItemId() == R.id.filter){

        }
        else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void saveToFile(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");
            startActivityForResult(intent, 2);
        }
        else{
            if(!externalStorageWritePermission()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Dateiname:");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String filename = input.getText().toString().toLowerCase();
                        if (filename.equals("")) {
                            Toast.makeText(getApplicationContext(), "Sie müssen mindestens ein Zeichen eintragen.", Toast.LENGTH_LONG).show();
                        }
                        else{
                            try {
                                filename +=".txt";
                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(getLogData().getBytes());
                                fos.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                try{
                    ParcelFileDescriptor pfd = this.getContentResolver() .openFileDescriptor(uri, "w");
                    FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());
                    fos.write(getLogData().getBytes());
                    fos.close();
                    pfd.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }

    private String getLogData(){
        try{
            int pid = android.os.Process.myPid();
            String command = "logcat --pid="+pid+" -b main -d";
            Process process = Runtime.getRuntime().exec(command);

            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder fileContentBuilder = new StringBuilder();
            while ((line = br.readLine()) != null){
                fileContentBuilder.append(line).append("\n");
            }
            return fileContentBuilder.toString();

        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

    private boolean externalStorageWritePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveToFile();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void init() {

        btnScroll = findViewById(R.id.btnScrollActivity);

        logList = new ArrayList<>();

        recyclerView = findViewById(R.id.rvActivity);
        activityLogAdapter = new ActivityLogAdapter(logList,this);
        recyclerView.setAdapter(activityLogAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert linearLayoutManager != null;
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastVisibleItemPosition();
                endReached = lastVisible +1 >= totalItemCount;
                if(!endReached){
                    btnScroll.setVisibility(View.VISIBLE);
                }else {
                    btnScroll.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(activityLogAdapter.getItemCount()-1);
                endReached = true;
                btnScroll.setVisibility(View.INVISIBLE);
            }
        });
        btnScroll.setVisibility(View.INVISIBLE);

        logReader = new LogReader();
        mainThread = new MainThread();
    }

    private void readLogs() {
        logReader.setLogListener(new LogListener() {
            @Override
            public void onLogRead(final String msg) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        Log log = LogParser.getLogFromMessage(msg);
                        if(log != null){
                            activityLogAdapter.addItem(log);
                            if(endReached){
                                recyclerView.scrollToPosition(activityLogAdapter.getItemCount()-1);
                            }
                        }
                    }
                });
            }
        });
        if (logReader.getState().equals(Thread.State.NEW)) {
            logReader.start();
        }
    }

}
