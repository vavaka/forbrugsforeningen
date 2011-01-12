package com.forbrugsforeningen.overlays;

import android.graphics.*;
import com.forbrugsforeningen.data.MapLocationInfo;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import java.util.Iterator;
import java.util.List;

/**
 * User: vavaka
 * Date: 12/27/10 8:41 PM
 */
public class MapLocationOverlay extends Overlay {
    //  Store these as global instances so we don't keep reloading every time
    private Bitmap bubbleIcon, shadowIcon;
    private Paint innerPaint, borderPaint, textPaint;

    //  The currently selected Map Location...if any is selected.  This tracks whether an information
    //  window should be displayed & where...i.e. whether a user 'clicked' on a known map location
    private List<MapLocationInfo> mapLocationInfoList;
    private MapLocationInfo selectedMapLocation;

    public MapLocationOverlay(List<MapLocationInfo> mapLocationInfoList, Bitmap bubbleIcon, Bitmap shadowIcon) {
        this.mapLocationInfoList = mapLocationInfoList;
        this.bubbleIcon = bubbleIcon;
        this.shadowIcon = shadowIcon;
    }

    @Override
    public boolean onTap(GeoPoint p, MapView mapView) {

        //  Store whether prior popup was displayed so we can call invalidate() & remove it if necessary.
        boolean isRemovePriorPopup = selectedMapLocation != null;

        //  Next test whether a new popup should be displayed
        selectedMapLocation = getHitMapLocation(mapView, p);
        if (isRemovePriorPopup || selectedMapLocation != null) {
            mapView.invalidate();
        }

        //  Lastly return true if we handled this onTap()
        return selectedMapLocation != null;
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {

        drawMapLocations(canvas, mapView, shadow);
        drawInfoWindow(canvas, mapView, shadow);
    }

    /**
     * Test whether an information balloon should be displayed or a prior balloon hidden.
     */
    private MapLocationInfo getHitMapLocation(MapView mapView, GeoPoint tapPoint) {

        //  Track which MapLocation was hit...if any
        MapLocationInfo hitMapLocation = null;

        RectF hitTestRecr = new RectF();
        Point screenCoords = new Point();
        Iterator<MapLocationInfo> iterator = mapLocationInfoList.iterator();
        while (iterator.hasNext()) {
            MapLocationInfo testLocation = iterator.next();

            //  Translate the MapLocation's lat/long coordinates to screen coordinates
            mapView.getProjection().toPixels(new GeoPoint((int) (testLocation.latitude * 1e6), (int) (testLocation.longitude * 1e6)), screenCoords);

            // Create a 'hit' testing Rectangle w/size and coordinates of our icon
            // Set the 'hit' testing Rectangle with the size and coordinates of our on screen icon
            hitTestRecr.set(-bubbleIcon.getWidth() / 2, -bubbleIcon.getHeight(), bubbleIcon.getWidth() / 2, 0);
            hitTestRecr.offset(screenCoords.x, screenCoords.y);

            //  Finally test for a match between our 'hit' Rectangle and the location clicked by the user
            mapView.getProjection().toPixels(tapPoint, screenCoords);
            if (hitTestRecr.contains(screenCoords.x, screenCoords.y)) {
                hitMapLocation = testLocation;
                break;
            }
        }

        //  Lastly clear the newMouseSelection as it has now been processed
        tapPoint = null;

        return hitMapLocation;
    }

    private void drawMapLocations(Canvas canvas, MapView mapView, boolean shadow) {

        Iterator<MapLocationInfo> iterator = mapLocationInfoList.iterator();
        Point screenCoords = new Point();
        while (iterator.hasNext()) {
            MapLocationInfo location = iterator.next();
            mapView.getProjection().toPixels(new GeoPoint((int) (location.latitude * 1e6), (int) (location.longitude * 1e6)), screenCoords);

            if (shadow) {
                //  Only offset the shadow in the y-axis as the shadow is angled so the base is at x=0;
                canvas.drawBitmap(shadowIcon, screenCoords.x, screenCoords.y - shadowIcon.getHeight(), null);
            } else {
                canvas.drawBitmap(bubbleIcon, screenCoords.x - bubbleIcon.getWidth() / 2, screenCoords.y - bubbleIcon.getHeight(), null);
            }
        }
    }

    private void drawInfoWindow(Canvas canvas, MapView mapView, boolean shadow) {

        if (selectedMapLocation != null) {
            if (shadow) {
                //  Skip painting a shadow in this tutorial
            } else {
                //  First determine the screen coordinates of the selected MapLocation
                Point selDestinationOffset = new Point();
                mapView.getProjection().toPixels(new GeoPoint((int) (selectedMapLocation.latitude * 1e6), (int) (selectedMapLocation.longitude * 1e6)), selDestinationOffset);

                //  Setup the info window with the right size & location
                int INFO_WINDOW_WIDTH = 125;
                int INFO_WINDOW_HEIGHT = 25;
                RectF infoWindowRect = new RectF(0, 0, INFO_WINDOW_WIDTH, INFO_WINDOW_HEIGHT);
                int infoWindowOffsetX = selDestinationOffset.x - INFO_WINDOW_WIDTH / 2;
                int infoWindowOffsetY = selDestinationOffset.y - INFO_WINDOW_HEIGHT - bubbleIcon.getHeight();
                infoWindowRect.offset(infoWindowOffsetX, infoWindowOffsetY);

                //  Draw inner info window
                canvas.drawRoundRect(infoWindowRect, 5, 5, getInnerPaint());

                //  Draw border for info window
                canvas.drawRoundRect(infoWindowRect, 5, 5, getBorderPaint());

                //  Draw the MapLocation's name
                int TEXT_OFFSET_X = 10;
                int TEXT_OFFSET_Y = 15;
                canvas.drawText(selectedMapLocation.storeInfo.name, infoWindowOffsetX + TEXT_OFFSET_X, infoWindowOffsetY + TEXT_OFFSET_Y, getTextPaint());
            }
        }
    }

    public Paint getInnerPaint() {
        if (innerPaint == null) {
            innerPaint = new Paint();
            innerPaint.setARGB(225, 75, 75, 75); //gray
            innerPaint.setAntiAlias(true);
        }
        return innerPaint;
    }

    public Paint getBorderPaint() {
        if (borderPaint == null) {
            borderPaint = new Paint();
            borderPaint.setARGB(255, 255, 255, 255);
            borderPaint.setAntiAlias(true);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(2);
        }
        return borderPaint;
    }

    public Paint getTextPaint() {
        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setARGB(255, 255, 255, 255);
            textPaint.setAntiAlias(true);
        }
        return textPaint;
    }
}
