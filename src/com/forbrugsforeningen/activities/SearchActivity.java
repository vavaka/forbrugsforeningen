package com.forbrugsforeningen.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.forbrugsforeningen.R;
import com.forbrugsforeningen.data.Coordinates;
import com.forbrugsforeningen.data.LocationInfo;
import com.forbrugsforeningen.data.StoreInfo;
import com.forbrugsforeningen.providers.CoordinatesProvider;
import com.forbrugsforeningen.providers.LocationInfoProvider;
import com.forbrugsforeningen.providers.StoresInfoProvider;

import java.util.List;

public class SearchActivity extends Activity {
    public static List<StoreInfo> storeInfoList;
    public static String currentQueryString;
    public static String currentLocation;
    public static Coordinates currentCoordinates;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_search);

        ImageButton btnSearch = (ImageButton) findViewById(R.id.button_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText txtQueryString = (EditText) findViewById(R.id.edit_search);
                currentQueryString = txtQueryString.getText().toString();

                EditText txtLocation = (EditText) findViewById(R.id.edit_location);
                currentLocation = txtLocation.getText().toString();

                final ProgressDialog progressDialog = showProgressDialog();

                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        progressDialog.dismiss();

                        if (msg.what == 0) {
                            showNothingFoundDialog();
                        } else {
                            startActivity(new Intent(SearchActivity.this, ResultsActivity.class));
                        }
                    }
                };

                Thread searchThread = new Thread(new Runnable() {
                    public void run() {
                        storeInfoList = StoresInfoProvider.GetStoresInfoByPostCodeAndQueryString(currentLocation, currentQueryString);

                        if (storeInfoList != null && storeInfoList.size() > 0) {
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    }
                });

                searchThread.start();
            }
        });

        ImageButton btnResfresh = (ImageButton) findViewById(R.id.button_location);
        btnResfresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                updateLocationControl();
            }
        });

        EditText txtQueryString = (EditText) findViewById(R.id.edit_search);
        txtQueryString.setText(currentQueryString);

        EditText txtLocation = (EditText) findViewById(R.id.edit_location);
        txtLocation.setText(currentLocation);

        currentCoordinates = CoordinatesProvider.GetCoordinatesFromLocationManager((LocationManager) getSystemService(LOCATION_SERVICE));
    }

    private ProgressDialog showProgressDialog() {
        return ProgressDialog.show(this, "", getResources().getString(R.string.loading), true);
    }

    private void showNothingFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.nothing_found))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //SearchActivity.this.finish();
                    }
                });
        builder.create().show();
    }

    private void showLocationErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.location_error))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //SearchActivity.this.finish();
                    }
                });
        builder.create().show();
    }

    private void showLocationInfoErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Post info not found for location: latitude=" + currentCoordinates.latitude + ", longitude=" + currentCoordinates.longitude)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //SearchActivity.this.finish();
                    }
                });
        builder.create().show();
    }

    private void updateLocationControl() {
        final LocationInfo[] locationInfo = new LocationInfo[1];
        final ProgressDialog progressDialog = showProgressDialog();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();

                if (msg.what == 1) {
                    showLocationErrorDialog();
                } else {
                    if (locationInfo[0] == null) {
                        showLocationInfoErrorDialog();
                    } else {
                        EditText txtLocation = (EditText) findViewById(R.id.edit_location);
                        txtLocation.setText(locationInfo[0].postCode + " " + locationInfo[0].postName);
                    }
                }
            }
        };

        Thread updateLocationThread = new Thread(new Runnable() {
            public void run() {
                currentCoordinates = CoordinatesProvider.GetCoordinatesFromLocationManager((LocationManager) getSystemService(LOCATION_SERVICE));
                if (currentCoordinates == null) {
                    handler.sendEmptyMessage(1);
                } else {
                    locationInfo[0] = LocationInfoProvider.GetLocationInfoByCoordinate(currentCoordinates);
                    handler.sendEmptyMessage(0);
                }
            }
        });

        updateLocationThread.start();
    }
}
