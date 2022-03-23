package com.autofill.droiday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class PdfActivity extends AppCompatActivity implements OnPageChangeListener,OnLoadCompleteListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    public static  String SAMPLE_FILE = "dos_spo.pdf";
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    String filename = "file.mt";
    String line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView titlepdf = (TextView) findViewById(R.id.tv_header);
        try {
            FileInputStream in = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                if ((line = bufferedReader.readLine()) != null) {
                    SAMPLE_FILE = line;
                } else {
                    SAMPLE_FILE = "null";
                }
            } catch (IOException e) {
                SAMPLE_FILE = "null";
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            SAMPLE_FILE = "null";
            e.printStackTrace();
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), SAMPLE_FILE);
        pdfView= (PDFView)findViewById(R.id.pdfView);
        titlepdf.setText(SAMPLE_FILE);
        ImageView home_but = (ImageView) findViewById(R.id.home_but3);
        ImageView cal_but = (ImageView) findViewById(R.id.cal_but3);
        ImageView games_but = (ImageView) findViewById(R.id.games_but3);
        if(!SAMPLE_FILE.equals("null"))displayFromAsset(SAMPLE_FILE,file);
        cal_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PdfActivity.this, CalenderActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
            }
        });

        home_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PdfActivity.this, HomePage.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
            }
        });

        games_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PdfActivity.this, ResourcesActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
            }
        });
    }
        private void displayFromAsset(String assetFileName,File file) {
            pdfFileName = assetFileName;

            pdfView.fromFile(file)
                    .defaultPage(pageNumber)
                    .enableSwipe(true)

                    .swipeHorizontal(false)
                    .onPageChange(this)
                    .enableAnnotationRendering(true)
                    .onLoad(this)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
        }


        @Override
        public void onPageChanged(int page, int pageCount) {
            pageNumber = page;
            setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
        }


        @Override
        public void loadComplete(int nbPages) {
            PdfDocument.Meta meta = pdfView.getDocumentMeta();
            printBookmarksTree(pdfView.getTableOfContents(), "-");

        }

        public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
            for (PdfDocument.Bookmark b : tree) {

                Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

                if (b.hasChildren()) {
                    printBookmarksTree(b.getChildren(), sep + "-");
                }
            }
        }

    }