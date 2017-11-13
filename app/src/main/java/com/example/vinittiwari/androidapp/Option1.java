package com.example.vinittiwari.androidapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Calculate the position of the sticky header view according to the
 * position of the first item in the ListView. When the first item is already
 * reached to top, you don't need to position the sticky header view.
 *
 * @author Nilanchala
 */
public class Option1 extends ActionBarActivity {

    private TextView stickyView;
    private ListView listView;
    private View heroImageView;
    private ImageView img;

    private View stickyViewSpacer;

    private int MAX_ROWS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getIntent();
        String message = intent.getStringExtra("clickedItem").replace("\""," ");
        Log.e("--------->ClickedItem",message);
        //List<String> nums1 = Arrays.asList("wallpaper","wallpaper2","wallpaper3","wallpaper4");
        //Log.e("--------->ClickedItem",""+Integer.parseInt(message));

        //Log.e("--------->ImagePath",""+nums1.get(Integer.parseInt(message)));
        //String picName =nums1.get(Integer.parseInt(message));

        //Resources r = getResources();
        //int picId = r.getIdentifier(picName, "drawable", "com.example.vinittiwari.androidapp");

        /* Initialise list view, hero image, and sticky view */
        listView = (ListView) findViewById(R.id.listView);
        img = (ImageView) findViewById(R.id.heroImageView);
        heroImageView = findViewById(R.id.heroImageView);
        //heroImageView.setBackgroundResource(picId);
        Picasso.with(this).load(message.trim()).into(img);

        stickyView = (TextView) findViewById(R.id.stickyView);


        /* Inflate list header layout */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.list_header, null);
        stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);

        /* Add list view header */
        listView.addHeaderView(listHeader);

        /* Handle list View scroll events */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                /* Check if the first item is already reached to top.*/
                if (listView.getFirstVisiblePosition() == 0) {
                    View firstChild = listView.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }

                    int heroTopY = stickyViewSpacer.getTop();
                    stickyView.setY(Math.max(0, heroTopY + topY));

                    /* Set the image to scroll half of the amount that of ListView */
                    heroImageView.setY(topY * 0.5f);
                }
            }
        });


        /* Populate the ListView with sample data */
        final List<String> modelList = new ArrayList<>();
        for (int i = 0; i < MAX_ROWS; i++) {
            modelList.add("List item " + i);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_row,R.id.text1, modelList){
                @Override
                public View getView(int position,
                View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.text1);
                TextView text2 = (TextView) view.findViewById(R.id.text2);
                text1.setText(modelList.get(position));
                text2.setText( modelList.get(position));
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

}