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
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.firebase.firestore.DocumentReference;
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
import java.util.Map;

import android.app.DownloadManager;


public class ResourcesActivity extends AppCompatActivity {

    ListView listView;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    String lvl;
    int nbBooks, nbExams, nbSeries;
    String filename = "file.mt";
    FileOutputStream outputStream;
    DocumentReference docRef;
    List<String> bookNames, bookUrls, examNames, examUrls, serieNames, serieUrls, Subjects, Tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bookNames = new ArrayList<String>();
        bookUrls = new ArrayList<String>();
        examNames = new ArrayList<String>();
        examUrls = new ArrayList<String>();
        serieNames = new ArrayList<String>();
        serieUrls = new ArrayList<String>();
        Subjects = new ArrayList<String>();
        Tests = new ArrayList<String>();
        Tests.add("Contrôle 1");
        Tests.add("Synthèse 1");
        Tests.add("Contrôle 2");
        Tests.add("Synthèse 2");
        Tests.add("Contrôle 3");
        Tests.add("Synthèse 3");

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
                                docRef= db.collection("Books").document(""+lvl);
                                if (lvl.equals("9") || lvl.equals("9")){
                                    Tests.add("Concours");
                                }
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

                //Manuels
                docRef.collection("bk")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                bookNames = new ArrayList<String>();
                                bookUrls = new ArrayList<String>();
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

                            //Manuels
                            MyAdapter adapter = new MyAdapter(ResourcesActivity.this,bookNames,bookUrls,bookstatus);
                            listView.setAdapter(adapter);
                            listView.setVisibility(View.VISIBLE);

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
                                            // dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                                            dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                            DownloadManager manager = (DownloadManager) ResourcesActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                                            manager.enqueue(dmr);
                                            bookstatus[i] = 1;
                                            BroadcastReceiver onComplete=new BroadcastReceiver() {
                                                public void onReceive(Context ctxt, Intent intent) {
                                                    MyAdapter adaptera = new MyAdapter(ResourcesActivity.this,bookNames,bookUrls,bookstatus);
                                                    listView.setAdapter(adaptera);
                                                    Intent intente = new Intent(ResourcesActivity.this, PdfActivity.class);
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                    startActivity(intente);
                                                }
                                            };
                                            //
                                            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                            String value = fileName;
                                            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                            outputStream.write(value.getBytes());
                                            outputStream.close();
                                        }
                                        else {
                                            String value = fileName;
                                            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                            outputStream.write(value.getBytes());
                                            outputStream.close();
                                            Intent intent = new Intent(ResourcesActivity.this, PdfActivity.class);
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
        });

        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Examens et épreuves :");
                manuels.setVisibility(View.INVISIBLE);
                exam.setVisibility(View.INVISIBLE);
                series.setVisibility(View.INVISIBLE);
                return_btn.setVisibility(View.VISIBLE);

                //Series
                docRef.collection("Devoirs")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Subjects = new ArrayList<String>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Subjects.add(document.getId());
                                    }
                                }
                                FolderAdapter adapter = new FolderAdapter(ResourcesActivity.this,Subjects);
                                listView.setAdapter(adapter);
                                listView.setVisibility(View.VISIBLE);

                                //On Click On Subject
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String subject = Subjects.get(i);
                                        title.setText("Examens de :"+ subject);
                                        listView.setVisibility(View.INVISIBLE);
                                        FolderAdapter adapter = new FolderAdapter(ResourcesActivity.this,Tests);
                                        listView.setAdapter(adapter);
                                        listView.setVisibility(View.VISIBLE);
                                        //On Click On Test
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String test = Tests.get(i);
                                                //title.setText("Examens " + test +" de :"+ subject);
                                                listView.setVisibility(View.INVISIBLE);
                                                docRef.collection("Devoirs")
                                                    .document(subject)
                                                    .collection(test)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    examNames = new ArrayList<String>();
                                                                    examUrls = new ArrayList<String>();
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        examNames.add(document.getData().get("name").toString());
                                                                        examUrls.add(document.getData().get("link").toString());

                                                                    }
                                                                }
                                                                nbExams = examNames.size();
                                                                int[] examstatus = new int[nbExams];
                                                                Log.d("BOOKS", "onComplete: "+ Arrays.toString(examNames.toArray()));
                                                                for(int i = 0 ; i < nbExams ; i++){
                                                                    String url = examUrls.get(i);
                                                                    String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
                                                                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                                                                    if(!file.exists()) examstatus[i] = 0;
                                                                    else examstatus[i] = 1;
                                                                }


                                                                MyAdapter adapter = new MyAdapter(ResourcesActivity.this,examNames,examUrls,examstatus);
                                                                listView.setAdapter(adapter);
                                                                listView.setVisibility(View.VISIBLE);

                                                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                    @Override
                                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                                        try {

                                                                            String url = examUrls.get(i);
                                                                            DownloadManager.Request dmr = new DownloadManager.Request(Uri.parse(url));
                                                                            String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
                                                                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                                                                            if(!file.exists()) {
                                                                                dmr.setTitle(fileName);
                                                                                dmr.setDescription("Some descrition about file"); //optional
                                                                                dmr.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                                                                                // dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                                                                                dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                                                                DownloadManager manager = (DownloadManager) ResourcesActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                                                                                manager.enqueue(dmr);
                                                                                BroadcastReceiver onComplete=new BroadcastReceiver() {
                                                                                    public void onReceive(Context ctxt, Intent intent) {
                                                                                        Intent intente = new Intent(ResourcesActivity.this, PdfActivity.class);
                                                                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                                                        startActivity(intente);
                                                                                    }
                                                                                };
                                                                                //
                                                                                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                                                                String value = fileName;
                                                                                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                                                                outputStream.write(value.getBytes());
                                                                                outputStream.close();
                                                                            }
                                                                            else {
                                                                                String value = fileName;
                                                                                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                                                                outputStream.write(value.getBytes());
                                                                                outputStream.close();
                                                                                Intent intent = new Intent(ResourcesActivity.this, PdfActivity.class);
                                                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
                                        });
                                    }
                                });
                            }
                        });
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
                docRef.collection("Series")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Subjects = new ArrayList<String>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Subjects.add(document.getId());
                                    }
                                }
                                FolderAdapter adapter = new FolderAdapter(ResourcesActivity.this,Subjects);
                                listView.setAdapter(adapter);
                                listView.setVisibility(View.VISIBLE);

                                //On Click On Subject
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String subject = Subjects.get(i);
                                        title.setText("Series de :"+ subject);
                                        listView.setVisibility(View.INVISIBLE);
                                        FolderAdapter adapter = new FolderAdapter(ResourcesActivity.this,Tests);
                                        listView.setAdapter(adapter);

                                        docRef.collection("Series")
                                            .document(subject)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            serieNames = new ArrayList<String>();
                                                            serieUrls = new ArrayList<String>();
                                                            Map<String,Object> Series = document.getData();
                                                            for (String name : Series.keySet()) {
                                                                serieNames.add(name);
                                                                serieUrls.add(Series.get(name).toString());
                                                            }

                                                            nbSeries = serieNames.size();
                                                            int[] examstatus = new int[nbSeries];
                                                            Log.d("BOOKS", "onComplete: "+ Arrays.toString(serieNames.toArray()));
                                                            for(int i = 0 ; i < nbSeries ; i++){
                                                                String url = serieUrls.get(i);
                                                                String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
                                                                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                                                                if(!file.exists()) examstatus[i] = 0;
                                                                else examstatus[i] = 1;
                                                            }


                                                            MyAdapter adapter = new MyAdapter(ResourcesActivity.this,serieNames,serieUrls,examstatus);
                                                            listView.setAdapter(adapter);
                                                            listView.setVisibility(View.VISIBLE);

                                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                                    try {
                                                                        String url = serieUrls.get(i);
                                                                        DownloadManager.Request dmr = new DownloadManager.Request(Uri.parse(url));
                                                                        String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
                                                                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                                                                        if(!file.exists()) {
                                                                            dmr.setTitle(fileName);
                                                                            dmr.setDescription("Some descrition about file"); //optional
                                                                            dmr.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                                                                            // dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                                                                            dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                                                            DownloadManager manager = (DownloadManager) ResourcesActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                                                                            manager.enqueue(dmr);
                                                                            examstatus[i] = 1;
                                                                            BroadcastReceiver onComplete=new BroadcastReceiver() {
                                                                                public void onReceive(Context ctxt, Intent intent) {
                                                                                    MyAdapter adapter = new MyAdapter(ResourcesActivity.this,serieNames,serieUrls,examstatus);
                                                                                    listView.setAdapter(adapter);
                                                                                    Intent intente = new Intent(ResourcesActivity.this, PdfActivity.class);
                                                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                                                    startActivity(intente);
                                                                                }
                                                                            };
                                                                            //
                                                                            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                                                            String value = fileName;
                                                                            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                                                            outputStream.write(value.getBytes());
                                                                            outputStream.close();
                                                                        }
                                                                        else {
                                                                            String value = fileName;
                                                                            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                                                            outputStream.write(value.getBytes());
                                                                            outputStream.close();
                                                                            Intent intent = new Intent(ResourcesActivity.this, PdfActivity.class);
                                                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
                                                    }
                                                }
                                            });
                                    }
                                });
                            }
                        });
            }
        });

        cal_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResourcesActivity.this, CalenderActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
            }
        });

        home_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResourcesActivity.this, HomePage.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
            }
        });

        games_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            if(rStatus[position] == 1) {
                myBack.setBackground(getDrawable(R.drawable.blue_but_done));
                myTitle.setTextColor(Color.WHITE);
            }
            else {
                myBack.setBackground(getDrawable(R.drawable.grey_but_download));
                myTitle.setTextColor(Color.rgb(15,53,103));
            }
            myTitle.setTypeface(latobold);
            myTitle.setX(myBack.getX() + 8);
            return row;
        }
    }

    class FolderAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> rTitle;

        FolderAdapter(Context c, List<String> title) {
            super(c, R.layout.row, R.id.title, title);
            this.context = c;
            this.rTitle = title;
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
            myBack.setBackground(getDrawable(R.drawable.blue_but_done));
            myTitle.setTypeface(latobold);
            myTitle.setX(myBack.getX() + 8);
            return row;
        }
    }

}