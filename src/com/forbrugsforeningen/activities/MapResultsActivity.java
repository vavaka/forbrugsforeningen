package com.forbrugsforeningen.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.forbrugsforeningen.R;
import com.forbrugsforeningen.data.MapLocationInfo;
import com.forbrugsforeningen.data.StoreInfo;
import com.forbrugsforeningen.overlays.MapLocationOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import java.util.List;

public class MapResultsActivity extends MapActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        ImageButton btnBack = (ImageButton) findViewById(R.id.button_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });


        MapView mapView = (MapView) findViewById(R.id.mapview);

        mapView.setSatellite(true);

        LinearLayout zoomLayout = (LinearLayout) findViewById(R.id.layout_zoom);
        View zoomView = mapView.getZoomControls();
        zoomLayout.addView(zoomView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mapView.displayZoomControls(true);
        mapView.getController().setZoom(15);

        Bitmap bubbleIcon = BitmapFactory.decodeResource(getResources(), R.drawable.bubble);
        Bitmap shadowIcon = BitmapFactory.decodeResource(getResources(), R.drawable.shadow);

        List<MapLocationInfo> mapLocationInfoListForOverlay = ResultsActivity.mapLocationInfoList;
        if(SearchActivity.currentCoordinates != null){
            MapLocationInfo myMapLocationInfo = new MapLocationInfo();
            myMapLocationInfo.storeInfo = new StoreInfo();
            myMapLocationInfo.storeInfo.name = getResources().getString(R.string.my_location);
            myMapLocationInfo.latitude = SearchActivity.currentCoordinates.latitude;
            myMapLocationInfo.longitude = SearchActivity.currentCoordinates.longitude;
            mapLocationInfoListForOverlay.add(myMapLocationInfo);
        }

        MapLocationOverlay mapLocationOverlay = new MapLocationOverlay(mapLocationInfoListForOverlay, bubbleIcon, shadowIcon);
        mapView.getOverlays().add(mapLocationOverlay);

        if (SearchActivity.currentCoordinates != null) {
            setCenterOnMap(SearchActivity.currentCoordinates.latitude, SearchActivity.currentCoordinates.longitude);
        } else if (ResultsActivity.mapLocationInfoList.size() > 0) {
            setCenterOnMap(ResultsActivity.mapLocationInfoList.get(0).latitude, ResultsActivity.mapLocationInfoList.get(0).longitude);
        }
    }

    private void setCenterOnMap(double latitude, double longitude) {
        MapView mapView = (MapView) findViewById(R.id.mapview);
        GeoPoint geoPoint = new GeoPoint((int) (latitude * 1e6), (int) (longitude * 1e6));
        mapView.getController().setCenter(geoPoint);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        showDialog(0);
        return false;
    }

    private void showYourLocationNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.your_location_not_found))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), null);
        builder.create().show();
    }

    private void showStoreLocationNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.store_location_not_found))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), null);
        builder.create().show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final String[] menuItems = new String[SearchActivity.storeInfoList.size() + 1];
        menuItems[0] = getResources().getString(R.string.my_location);

        for (int i = 0; i < SearchActivity.storeInfoList.size(); i++) {
            StoreInfo storeInfo = SearchActivity.storeInfoList.get(i);
            menuItems[i + 1] = storeInfo.name + "\n" + storeInfo.address;
        }

        return new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.go_to_location))
                .setItems(menuItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (SearchActivity.currentCoordinates == null) {
                                showYourLocationNotFoundDialog();
                            } else {
                                setCenterOnMap(SearchActivity.currentCoordinates.latitude, SearchActivity.currentCoordinates.longitude);
                            }
                        } else {
                            String selectedMenuItem = menuItems[which];
                            for (MapLocationInfo mapLocationInfo : ResultsActivity.mapLocationInfoList) {
                                if (selectedMenuItem.equals(mapLocationInfo.storeInfo.name + "\n" + mapLocationInfo.storeInfo.address)) {
                                    setCenterOnMap(mapLocationInfo.latitude, mapLocationInfo.longitude);
                                    return;
                                }
                            }

                            showStoreLocationNotFoundDialog();
                        }
                    }
                })
                .create();
    }
}
