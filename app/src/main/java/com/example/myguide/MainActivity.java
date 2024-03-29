package com.example.myguide;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapImage;
import com.microsoft.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {

    private static int FIRST_ELEMENT = 0;
    private int mUntitledPushpinCount = 0;
    private MapView mMapView;

    private MapElementLayer mPinLayer;
    private MapImage mPinImage;
    private View mDemoMenu;

    String city = null;
    public LocationListener listener;

    public static final int REQUEST_PERMS = 1;
    public static final int ADDRESSES = 10;

    String type = "restaurants";
    String distance = "250",
            limit = "10";
    TextView distanceValue, limitValue;
    EditText editText;
    Button button, locate, forecast;
    ImageView imageView;
    TextView temptv, time, longitude, latitude, humidity, sunrise, sunset, pressure, wind, country, city_nam, max_temp, min_temp, feels;
    private double lat;
    private double lon;

    private RecyclerView recyclerView;
    private ElementAdapter elementAdapter;
    private ProgressBar progress;
    private ArrayList<Element> fullElemntAdapter;
    private PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button);
        locate = findViewById(R.id.buttonLocation);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        elementAdapter = new ElementAdapter(new ArrayList<>());
        recyclerView.setAdapter(elementAdapter);
        distanceValue = findViewById(R.id.distance_value);
        limitValue = findViewById(R.id.limit_value);


// Récupération des éléments de la vue
        SeekBar distanceSeekBar = findViewById(R.id.distance_seekbar);
        RadioGroup typeRadioGroup = findViewById(R.id.radioGroup);
        RadioButton selectedRadioButton = findViewById(typeRadioGroup.getCheckedRadioButtonId());
// Notez que getCheckedRadioButtonId() renvoie l'ID du RadioButton sélectionné dans le groupe de boutons radio.

// Ajouter un écouteur pour le changement de la valeur de la SeekBar
        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // La valeur a été modifiée, récupérer la nouvelle valeur
                distance = Integer.toString(progress); // conversion de int à String
                distanceValue.setText(distance + "km");
                // Faire quelque chose avec la valeur (par exemple, la stocker dans une variable)
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Ne rien faire
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Ne rien faire
            }
        });

// Ajouter un écouteur pour le changement de la sélection du bouton radio
        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Le bouton radio sélectionné a été modifié, récupérer la nouvelle valeur
                RadioButton selectedRadioButton = findViewById(checkedId);
                type = selectedRadioButton.getText().toString().toLowerCase();
                // Faire quelque chose avec la valeur (par exemple, la stocker dans une variable)
            }
        });


// Récupération des éléments de la vue
        SeekBar limiteSeekBar = findViewById(R.id.limit_seekbar);
// Ajouter un écouteur pour le changement de la valeur de la SeekBar
        limiteSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // La valeur a été modifiée, récupérer la nouvelle valeur
                limit = Integer.toString(progress); // conversion de int à String
                limitValue.setText(limit);
                // Faire quelque chose avec la valeur (par exemple, la stocker dans une variable)
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Ne rien faire
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Ne rien faire
            }
        });


        //   refresh();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(editText.getText())) {

                    editText.setError(getString(R.string.error_msg_city));

                } else {
                    String city0 = editText.getText().toString();
                    //    FindWeather(city);
                    searchCityCoordinates(city0);
                    //  FindNearBy(lat,lon);
                    //lat =42.331082;
                    //     lon = -71.068133;
                    // FindNearBy(42.331082,-71.068133);
                    Toast.makeText(getApplicationContext(), lat + ", " + lon, Toast.LENGTH_LONG).show();
                }
            }
        });

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMS);
                    return;
                }


                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                String provider = locationManager.getBestProvider(new Criteria(), true);
                listener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                    }

                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                };
                if (provider != null) {
                    Location location = locationManager.getLastKnownLocation(provider);

                    locationManager.requestLocationUpdates(provider, 300000, 0, listener);

                    if (location == null) {
                        locationManager.requestSingleUpdate(provider, listener, null);

                        return;
                    }

                    //located(location) affecte à city le nom de la cité via getAdminArea()
                    located(location);
                    //  searchCityCoordinates("tuniss");
                    FindNearBy(lat, lon);
                    // FindNearBy(36.6482673,10.2941294);


                  //  Toast.makeText(getApplicationContext(), city, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),getString(R.string.error_msg_local) , Toast.LENGTH_LONG).show();

                }
            }
        });


        // Récupération de la vue LinearLayout
        LinearLayout optionsLayout = findViewById(R.id.options_layout);

// Récupération du bouton "More options"
        Button moreOptionsButton = findViewById(R.id.show_options_button);

// Récupération du bouton "Hide"
        Button hideButton = findViewById(R.id.hide_button);

// Masquage de la vue LinearLayout au démarrage de l'activité
        optionsLayout.setVisibility(View.GONE);

// Définition de l'action lorsque l'utilisateur appuie sur le bouton "More options"
        moreOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Affichage de la vue LinearLayout
                optionsLayout.setVisibility(View.VISIBLE);
                // Affichage du bouton "Hide"
                hideButton.setVisibility(View.VISIBLE);
                moreOptionsButton.setVisibility(View.GONE);
            }
        });

// Définition de l'action lorsque l'utilisateur appuie sur le bouton "Hide"
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Masquage de la vue LinearLayout
                optionsLayout.setVisibility(View.GONE);
                // Masquage du bouton "Hide"
                hideButton.setVisibility(View.GONE);
                moreOptionsButton.setVisibility(View.VISIBLE);
            }
        });



        EditText searchEt = findViewById(R.id.searchEt);
        ImageView filtreBtn = findViewById(R.id.filtreBtn);


        // Ajouter le TextWatcher
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Mettre à jour les éléments de RecyclerView en fonction de la recherche
                //recyclerViewAdapter.getFilter().filter(s.toString());
                if (elementAdapter != null&&fullElemntAdapter!=null) {
                    elementAdapter.filter(s.toString(),fullElemntAdapter);
                }
                //  Toast.makeText(getContext(),s.toString(),Toast.LENGTH_SHORT).show();
                //  Log.d("EditText :     ",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        popupMenu = new PopupMenu(MainActivity.this, filtreBtn);
        popupMenu.getMenu().add(1,1,1,R.string.none);
        popupMenu.getMenu().add(1,1,2,R.string.name);
        popupMenu.getMenu().add(1,1,3,R.string.rating);

        filtreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                popupMenu.show();
            }
        });
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String item = menuItem.getTitle().toString();
                int order = menuItem.getOrder();
                switch (order) {
                    case 1:
                        // effectuer l'action pour supprimer le filtre
                        if (elementAdapter != null&&fullElemntAdapter!=null) {
                            elementAdapter.sortData("none",fullElemntAdapter);
                            searchEt.setText("");
                        }
                        break;
                    case 2:
                        // effectuer l'action pour filtrer par nom
                        if (elementAdapter != null&&fullElemntAdapter!=null) {
                            elementAdapter.sortData("name",fullElemntAdapter);
                        }
                        break;
                    case 3:
                        // effectuer l'action pour filtrer par note
                        if (elementAdapter != null&&fullElemntAdapter!=null) {
                            elementAdapter.sortData("rating",fullElemntAdapter);
                        }
                        break;

                }
                return true;
            }
        });


    }



    public void FindNearBy(double latitude, double longitude) {

        if (lat == 0 && lon == 0) {

        } else {
            // Vider la liste d'éléments de l'adaptateur
            elementAdapter.getElementList().clear();

            progress = (ProgressBar) findViewById(R.id.progress);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progress.setVisibility(View.VISIBLE);
                progress.setProgress(0, true);


                //elementAdapter.setElementList(null);
            } else progress.setProgress(0);


            OkHttpClient client = new OkHttpClient();


            String url = "https://travel-advisor.p.rapidapi.com/" + type + "/list-by-latlng?latitude=" + latitude + "&longitude=" + longitude + "&limit=" + limit + "&distance=" + distance + "&lunit=km";

            Log.d("url : ", url);
        /*Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "9a8a6b8942msh7862e75a384b437p113bfajsn9368eb03cb21")
                .build();*/

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Code pour traiter la réponse JSON
                            try {
                                //find temperature
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray data = jsonObject.getJSONArray("data");

                                // Créer une liste de restaurants
                                List<Element> elementList = new ArrayList<>();
                                progress.setVisibility(View.GONE);
                                int len = data.length();
//                            Toast.makeText(getApplicationContext(), data.getJSONObject(0).getString("name").toString(), Toast.LENGTH_LONG).show();
                                for (int i = 0; i < len; i++) {
                                    JSONObject elementSeeked = data.getJSONObject(i);

                                    if (elementSeeked.has("name")) {
                                        // Le JSON contient une clé "name"
                                        String rating = "No rating";

                                        if (elementSeeked.has("rating")) {
                                            rating = elementSeeked.getString("rating");
                                        }
                                        String urlImage = "";
                                        if (elementSeeked.has("photo")) {
                                            JSONObject jsonPhoto = elementSeeked.getJSONObject("photo");
                                            JSONObject jsonImages = jsonPhoto.getJSONObject("images");
                                            JSONObject jsonLarge = jsonImages.getJSONObject("large");
                                            urlImage = jsonLarge.getString("url");

                                        }
                                        String address = "";
                                        if (elementSeeked.has("address")) {
                                            address = elementSeeked.getString("address");
                                        }


                                        String name = elementSeeked.getString("name");
                                        double lt = elementSeeked.getDouble("latitude");
                                        double lg = elementSeeked.getDouble("longitude");

                                        Element element = new Element(name, lt, lg, rating, address, urlImage);

                                /*String address = restaurant.getString("address");
                                String cuisine = restaurant.getString("cuisine");
                                String phone = restaurant.getString("phone");
                                String website = restaurant.getString("website");*/
                                        // Ajoutez le code pour stocker les détails des restaurants dans une liste ou une autre structure de données appropriée.

                                        elementList.add(element);
                                    } else {
                                        // Le JSON ne contient pas de clé "name"
                                    }
                                }

                                int s = elementList.size();
                                Toast.makeText(getApplicationContext(), "  " + s, Toast.LENGTH_LONG).show();

                                // Mettre à jour l'adaptateur du RecyclerView
                                elementAdapter.setElementList(elementList);
                                fullElemntAdapter = new ArrayList<>(elementAdapter.getElementList());
                                int a = elementAdapter.getItemCount();
                                Toast.makeText(getApplicationContext(), "    :" + a, Toast.LENGTH_LONG).show();
                                elementAdapter.notifyDataSetChanged();

                                elementAdapter.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(Element element) {
                                      //  Toast.makeText(getApplicationContext(), "youe have clicked !!!!!" + element.getLongitude(), Toast.LENGTH_LONG).show();


                                        // Handle positive button click here
                                        ParcelableGeopoint elementLocation = new ParcelableGeopoint(element.getLatitude(), element.getLongitude());
                                        ParcelableGeopoint myLocation = new ParcelableGeopoint(lat, lon);

                                        MapDialogFragment mapDialog = new MapDialogFragment();
                                        Bundle args = new Bundle();
                                        args.putParcelable("elementLocation", elementLocation);
                                        args.putParcelable("myLocation", myLocation);
                                        mapDialog.setArguments(args);

                                        mapDialog.show(getSupportFragmentManager(), "map_dialog");



                                    }
                                });

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Code pour gérer les erreurs de requête

                            if (!isNetworkAvailable(getApplicationContext())) {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_msg_cnx), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_msg_tap), Toast.LENGTH_LONG).show();
                            }
                            // Toast.makeText(MainActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com");
                    // headers.put("X-RapidAPI-Key", "9a8a6b8942msh7862e75a384b437p113bfajsn9368eb03cb21");
                    //headers.put("X-RapidAPI-Key", "016fa5cfbfmsh57370e6c5df1f30p1cdd22jsn969c05ef7a43");
                  //  headers.put("X-RapidAPI-Key", "54da830830msh3b6cacf87fd75d3p196435jsn050cd5ee7973")
                    // ajad api key
                    headers.put("X-RapidAPI-Key", "c3784205d8mshf89f1c53e38548ep161f68jsn59abcce9d967");
                    return headers;
                }
            };

// Ajouter la requête à la file d'attente de Volley


            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);

        }
    }


    private void located(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();

        if (!Geocoder.isPresent()) {
            Toast.makeText(getApplicationContext(), "Les coordonnées sont indisponibles", Toast.LENGTH_LONG).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lon, ADDRESSES);
            //  String cityName = addressList.get(0).getAddressLine(0);
            //   String stateName = addressList.get(0).getAddressLine(1);
            // String countryName = addressList.get(0).getAddressLine(2);
            // Toast.makeText(getApplicationContext(), String.valueOf(addressList.get(0).getAdminArea()),Toast.LENGTH_LONG).show();
            //  Toast.makeText(getApplicationContext(), addressList.get(0).getAdminArea(),Toast.LENGTH_LONG).show();

            if (addressList == null) {
                Toast.makeText(getApplicationContext(), "Les coordonnées sont indisponibles", Toast.LENGTH_LONG).show();
                return;
            }


            city = addressList.get(0).getAdminArea();
            // Toast.makeText(getApplicationContext(), city,Toast.LENGTH_LONG).show();


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void searchCityCoordinates(String cityName) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + cityName + "&format=json";
        // String url = "https://api.openweathermap.org/data/2.5/weather?q="+ cityName +"&appid=462f445106adc1d21494341838c10019&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.length() > 0) {

                                JSONArray responseArray = new JSONArray(response);

                                if (responseArray.length() != 0) {
                                    JSONObject firstEntry = responseArray.getJSONObject(1);
                                    lat = Double.parseDouble(firstEntry.getString("lat"));
                                    lon = Double.parseDouble(firstEntry.getString("lon"));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Essayez une autre ville !!!", Toast.LENGTH_LONG).show();
                                    lat = 0;
                                    lon = 0;
                                }



                                /*JSONObject jsonObject = new JSONObject(response);

                                JSONObject object = jsonObject.getJSONObject("coord");
                                double lat = object.getDouble("lat");
                                double lon = object.getDouble("lon");*/
                                FindNearBy(lat, lon);
                                Log.d("Latitude: " + lat, "Longitude: " + lon);
                                Toast.makeText(getApplicationContext(), "lat de la ville rech :" + lat, Toast.LENGTH_LONG).show();


                                //  Toast.makeText(getApplicationContext(), "lat de la ville rech :"+ lat, Toast.LENGTH_LONG).show();


                                // Utilisez les coordonnées lat et lon pour effectuer d'autres opérations
                            } else {
                                if (!isNetworkAvailable(getApplicationContext())) {
                                    Toast.makeText(getApplicationContext(), "Il n'y a pas de connexion, veuillez l'activer!!!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Essayez une autre ville !!!", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "Il n'y a pas de connexion, veuillez l'activer!!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Essayez une autre ville !!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }



    // Vérifie si le terminal est connecté à Internet
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }



}