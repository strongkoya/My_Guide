package com.example.myguide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapImage;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapView;

import java.util.Locale;

public class MapDialogFragment extends DialogFragment {

    private MapView mMapView;
    private FloatingActionButton mFab;
    private MapElementLayer mPinLayer;
    private MapImage mPinImage;
    private int mUntitledPushpinCount = 0;
    private static final String MY_API_KEY = "AlwLTKgevIemLkhFY8wA2oDQwpxY8SBBAR8a5dXymXDFKTmfGWKkXnJGQkGzXUMM";

    public MapDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the map view
        mMapView = new MapView(requireContext(), MapRenderMode.VECTOR);
        mMapView.setCredentialsKey(MY_API_KEY);
        mMapView.onCreate(savedInstanceState);

        // Set the map scene to show both locations
        Bundle args = getArguments();
        ParcelableGeopoint parcelableElementLocation = args.getParcelable("elementLocation");
        double elementLatitude = parcelableElementLocation.getLatitude();
        double elementLongitude = parcelableElementLocation.getLongitude();
        Geopoint elementLocation = new Geopoint(elementLatitude, elementLongitude);

        ParcelableGeopoint parcelableMyLocation = args.getParcelable("myLocation");
        double myLatitude = parcelableMyLocation.getLatitude();
        double myLongitude = parcelableMyLocation.getLongitude();
        Geopoint myLocation = new Geopoint(myLatitude, myLongitude);
        mMapView.setScene(
                MapScene.createFromLocationAndZoomLevel(elementLocation, 15),
                MapAnimationKind.NONE);

        // Add pins for both locations
        mPinLayer = new MapElementLayer();
        mMapView.getLayers().add(mPinLayer);
        mPinImage = getPinImage();
        addPin(elementLocation, "Restaurant");
        addPin(myLocation, "My location");


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*// Create a dialog with the map view
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mMapView);
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        builder.setTitle(null);
        return builder.create();*/

        // Créer une instance de la vue du fragment
        View fragmentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_map_dialog, null);
        mMapView = fragmentView.findViewById(R.id.map_view);
        mMapView.setCredentialsKey(MY_API_KEY);
        mMapView.onCreate(savedInstanceState);
        // Set the map scene to show both locations
        Bundle args = getArguments();
        ParcelableGeopoint parcelableElementLocation = args.getParcelable("elementLocation");
        double elementLatitude = parcelableElementLocation.getLatitude();
        double elementLongitude = parcelableElementLocation.getLongitude();
        Geopoint elementLocation = new Geopoint(elementLatitude, elementLongitude);

        ParcelableGeopoint parcelableMyLocation = args.getParcelable("myLocation");
        double myLatitude = parcelableMyLocation.getLatitude();
        double myLongitude = parcelableMyLocation.getLongitude();
        Geopoint myLocation = new Geopoint(myLatitude, myLongitude);
        mMapView.setScene(
                MapScene.createFromLocationAndZoomLevel(elementLocation, 15),
                MapAnimationKind.NONE);

        // Add pins for both locations
        mPinLayer = new MapElementLayer();
        mMapView.getLayers().add(mPinLayer);
        mPinImage = getPinImage();
        addPin(elementLocation, getString(R.string.destination));
        addPin(myLocation, getString(R.string.location));
       // FloatingActionButton fabDirections = fragmentView.findViewById(R.id.fab_directions);
        ImageButton btnDirections = fragmentView.findViewById(R.id.btn_directions);
        btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Google Maps with directions from current location to element location

                Bundle args = getArguments();
                ParcelableGeopoint parcelableElementLocation = args.getParcelable("elementLocation");
                double elementLatitude = parcelableElementLocation.getLatitude();
                //Toast.makeText(getContext(),elementLatitude+" lat",Toast.LENGTH_LONG).show();
                double elementLongitude = parcelableElementLocation.getLongitude();
                ParcelableGeopoint parcelableMyLocation = args.getParcelable("myLocation");
                double myLatitude = parcelableMyLocation.getLatitude();
                double myLongitude = parcelableMyLocation.getLongitude();
                /*Uri gmmIntentUri = Uri.parse("google.navigation:q=" + elementLatitude + "," + elementLongitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }*/



                String uri = "http://maps.google.com/maps?saddr=" + myLatitude + "," + myLongitude + "&daddr=" + elementLatitude + "," + elementLongitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);

            }
        });
        // Créer une nouvelle AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(fragmentView);
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        builder.setTitle(null);
        return builder.create();
    }

    private MapImage getPinImage() {
        // Create a pin image from a drawable resource
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pin, null);

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return new MapImage(bitmap);
    }

    private void addPin(Geopoint location, String title) {
        // Add a pin to the map at the given location
        MapIcon pushpin = new MapIcon();
        pushpin.setLocation(location);
        pushpin.setTitle(title);
        pushpin.setImage(mPinImage);

        pushpin.setNormalizedAnchorPoint(new PointF(0.5f, 1f));
        if (title.isEmpty()) {
            pushpin.setContentDescription(String.format(
                    Locale.ROOT,
                    "Untitled pushpin %d",
                    ++mUntitledPushpinCount));
        }
        mPinLayer.getElements().add(pushpin);
    }

}