package com.forbrugsforeningen.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import com.forbrugsforeningen.R;
import com.forbrugsforeningen.data.MapLocationInfo;
import com.forbrugsforeningen.data.StoreInfo;
import com.forbrugsforeningen.data.StoreInfoAdapter;
import com.forbrugsforeningen.providers.MapLocationInfoProvider;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends Activity {
    public static List<MapLocationInfo> mapLocationInfoList = new ArrayList<MapLocationInfo>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_results);

        ImageButton btnBack = (ImageButton) findViewById(R.id.button_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton btnMap = (ImageButton) findViewById(R.id.button_map);
        btnMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final ProgressDialog progressDialog = showProgressDialog();

                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        progressDialog.dismiss();

                        if (msg.what == 0) {
                            showMapLocationsNotFoundDialog();
                        } else {
                            startActivity(new Intent(ResultsActivity.this, MapResultsActivity.class));
                        }
                    }
                };

                Thread searchThread = new Thread(new Runnable() {
                    public void run() {
                        mapLocationInfoList.clear();
                        for (StoreInfo storeInfo : SearchActivity.storeInfoList) {
                            MapLocationInfo mapLocationInfo = MapLocationInfoProvider.GetMapLocationInfoByStoreInfo(storeInfo);
                            if(mapLocationInfo != null){
                                mapLocationInfoList.add(mapLocationInfo);
                            }
                        }

                        if (mapLocationInfoList.size() > 0) {
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    }
                });

                searchThread.start();
            }
        });

        StoreInfoAdapter listViewAdapter = new StoreInfoAdapter(this, R.layout.row_result, SearchActivity.storeInfoList);

        ListView listView = (ListView) findViewById(R.id.listview_results);
        listView.setAdapter(listViewAdapter);
    }

    private ProgressDialog showProgressDialog() {
        return ProgressDialog.show(this, "", getResources().getString(R.string.loading), true);
    }

    private void showMapLocationsNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.map_locations_not_found))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //SearchActivity.this.finish();
                    }
                });
        builder.create().show();
    }
}
