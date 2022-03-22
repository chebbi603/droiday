package com.autofill.droiday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.DownloadManager;


public class ResourcesActivity extends AppCompatActivity {

    ListView listView;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    String lvl;
    int nbBooks;
    String filename = "file.mt";
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        List<String> bookNames = new ArrayList<String>();
        List<String> bookUrls = new ArrayList<String>();

        //FireBase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        //Initializing Views
        ImageView indic  = (ImageView) findViewById(R.id.indic);
        ImageView home_but = (ImageView) findViewById(R.id.home_but);
        ImageView cal_but = (ImageView) findViewById(R.id.cal_but);
        ImageView games_but = (ImageView) findViewById(R.id.games_but);
        TextView title = (TextView) findViewById(R.id.title_res);
        Button manuels = (Button) findViewById(R.id.books_btn);
        Button exam = (Button) findViewById(R.id.exams_btn);
        Button series = (Button) findViewById(R.id.series_btn);
        Button return_btn = (Button) findViewById(R.id.return_btn);
        listView = (ListView) findViewById(R.id.listview);

        title.setText("");
        manuels.setVisibility(View.VISIBLE);
        exam.setVisibility(View.VISIBLE);
        series.setVisibility(View.VISIBLE);
        return_btn.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);

        db.collection("users")
                .document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                lvl = document.getData().get("level").toString();
                                db.collection("Books")
                                        .document(""+lvl)
                                        .collection("bk")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        bookNames.add(document.getData().get("name").toString());
                                                        bookUrls.add(document.getData().get("link").toString());

                                                    }
                                                }
                                                nbBooks = bookNames.size();
                                                int[] bookstatus = new int[nbBooks];
                                                Log.d("BOOKS", "onComplete: "+ Arrays.toString(bookNames.toArray()));
                                                for(int i = 0 ; i < nbBooks ; i++){
                                                    String url = bookUrls.get(i);
                                                    String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
                                                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                                                    if(!file.exists()) bookstatus[i] = 0;
                                                    else bookstatus[i] = 1;
                                                }
                                                MyAdapter adapter = new MyAdapter(ResourcesActivity.this,bookNames,bookUrls,bookstatus);
                                                listView.setAdapter(adapter);


                                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                        try {

                                                            String url = bookUrls.get(i);
                                                            DownloadManager.Request dmr = new DownloadManager.Request(Uri.parse(url));
                                                            String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
                                                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                                                            if(!file.exists()) {
                                                                dmr.setTitle(fileName);
                                                                dmr.setDescription("Some descrition about file"); //optional
                                                                dmr.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                                                                dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                                                                dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                                                DownloadManager manager = (DownloadManager) ResourcesActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                                                                manager.enqueue(dmr);
                                                                BroadcastReceiver onComplete=new BroadcastReceiver() {
                                                                    public void onReceive(Context ctxt, Intent intent) {
                                                                        Intent intente = new Intent(ResourcesActivity.this, PdfActivity.class);
                                                                        startActivity(intente);
                                                                    }
                                                                };
                                                                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                                                String value = fileName;
                                                                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                                                outputStream.write(value.getBytes());
                                                                outputStream.close();
                                                            }
                                                           else {
                                                                Intent intent = new Intent(ResourcesActivity.this, PdfActivity.class);
                                                                startActivity(intent);
                                                            }

                                                        } catch (FileNotFoundException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }


                                                    }
                                                });
                                            }
                                        });
                            }
                        }
                    }
                });


        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("");
                manuels.setVisibility(View.VISIBLE);
                exam.setVisibility(View.VISIBLE);
                series.setVisibility(View.VISIBLE);
                return_btn.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);
            }
        });

        manuels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Manuels :");
                manuels.setVisibility(View.INVISIBLE);
                exam.setVisibility(View.INVISIBLE);
                series.setVisibility(View.INVISIBLE);
                return_btn.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
            }
        });

        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Examens et épreuves :");
                manuels.setVisibility(View.INVISIBLE);
                exam.setVisibility(View.INVISIBLE);
                series.setVisibility(View.INVISIBLE);
                return_btn.setVisibility(View.VISIBLE);
            }
        });

        series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Series d'exercices :");
                manuels.setVisibility(View.INVISIBLE);
                exam.setVisibility(View.INVISIBLE);
                series.setVisibility(View.INVISIBLE);
                return_btn.setVisibility(View.VISIBLE);
                Intent intent = new Intent(ResourcesActivity.this, PdfActivity.class);
                startActivity(intent);
            }
        });

        cal_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX((cal_but.getX()+cal_but.getWidth())/2);
                Intent intent = new Intent(ResourcesActivity.this, CalenderActivity.class);
                startActivity(intent);
            }
        });

        home_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX((home_but.getX()+home_but.getWidth())/2);
            }
        });

        games_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indic.setX((games_but.getX()+games_but.getWidth())/2);
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> rTitle;
        List<String> rUrl;
        int rStatus[];

        MyAdapter(Context c, List<String> title, List<String> url, int[] status) {
            super(c, R.layout.row, R.id.title, title);
            this.context = c;
            this.rTitle = title;
            this.rUrl = url;
            this.rStatus = status;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView myBack = row.findViewById(R.id.backimg);
            TextView myTitle = row.findViewById(R.id.title);
            Typeface latobold = ResourcesCompat.getFont(context, R.font.lato_bold);
            myTitle.setText(rTitle.get(position));
            if(rStatus[position] == 1) myBack.setBackground(getDrawable(R.drawable.blue_but_done));
            else myBack.setBackground(getDrawable(R.drawable.grey_but_download));
            myTitle.setTypeface(latobold);
            myTitle.setX(myBack.getX() + 8);
            return row;
        }


        }



}