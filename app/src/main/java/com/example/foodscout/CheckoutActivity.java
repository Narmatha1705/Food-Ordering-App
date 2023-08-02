package com.example.foodscout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CheckoutActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private List<AutocompletePrediction> listPrediction;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private PlacesClient placesClient;
    private MaterialSearchBar materialSearchBar;
    private SearchView searchView;
    private View mapView;
    private Button findPlaces;
    private final float DEFAULT_ZOOM = 18;
    private FirebaseFirestore fStoreRef;
    private FirebaseAuth user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //materialSearchBar=findViewById(R.id.searchBar);
       // searchView = (SearchView) findViewById(R.id.search_location);
        findPlaces = findViewById(R.id.btn_search_place);
        fStoreRef = (FirebaseFirestore) FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // mapFragment.getMapAsync(this);


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(CheckoutActivity.this);
        Places.initialize(CheckoutActivity.this, "AIzaSyC7s-p35lpYZnUl7CXODFXIC2BVxbZRy_k");
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

/*
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                //checkFunc(location);
                locationGet(location);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
*/
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(CheckoutActivity.this, Locale.getDefault());
                    try {
                        addressList = geocoder.getFromLocationName("Kathmandu", 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    if(addressList.size()>0) {
                        double latitude = addressList.get(0).getLatitude();
                        double longitude = addressList.get(0).getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        //LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng));
                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                //.title(location));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),DEFAULT_ZOOM));
                        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                        Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        //Checking gps enabled or not
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(CheckoutActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(CheckoutActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(CheckoutActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(CheckoutActivity.this, 55);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        searchView=(SearchView) findViewById(R.id.search_location);
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                    //checkFunc(location);
                try {
                    locationGet(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        findPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationSet();
            }
        });


        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 55) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }

                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                            Double Longitude = mLastKnownLocation.getLongitude();
                            Double Latitude = mLastKnownLocation.getLatitude();


                            try {
                                locationNowSet(Longitude, Latitude);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(CheckoutActivity.this, "Longitude:" + mLastKnownLocation.getLongitude() + " Latitude:" + mLastKnownLocation.getLatitude(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(CheckoutActivity.this, "Unable to find last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void locationGet(String location) throws IOException {
       List<Address> addresses;
       // if (location != null || !location.equals("")) {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
          //  try {
                addresses = gcd.getFromLocationName(location, 10);

             /*   while (addresses.size()==0) {
                    addresses = gcd.getFromLocationName(location, 1);
                }
*/
              //  if(addresses.size()>0){
                    Address address = addresses.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM));
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                    String locality = addresses.get(0).getLocality();
                    String city = addresses.get(0).getSubLocality();
                    String Area = address.getAdminArea();
                    String AddressLine1 = addresses.get(0).getAddressLine(0);
                    String AddressLine2 = addresses.get(0).getAddressLine(1);
                    String Premises= addresses.get(0).getPremises();
                    //Locale locale=address.getLocale();
                    //mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                    Toast.makeText(CheckoutActivity.this, "Latitude" + address.getLatitude() + " " + "Longitude:" + address.getLongitude() + " " + "Location:" + locality + " " + "Sub-Location:" + city + " " + "Area:" + Area, Toast.LENGTH_LONG).show();
                    fStoreRef = (FirebaseFirestore) FirebaseFirestore.getInstance();
                    user = FirebaseAuth.getInstance();

                    String uid = user.getUid();
                    CollectionReference locationInfoCollection = fStoreRef.collection("Users").document(uid).collection("User_Delivery_Address");
                    Map<String, Object> locationInfo = new HashMap<>();
                    locationInfo.put("Premises",Premises);
                    locationInfo.put("Latitude", address.getLatitude());
                    locationInfo.put("Longitude", address.getLongitude());
                    locationInfo.put("Location", locality);
                    locationInfo.put("Sub-Location", city);
                    locationInfo.put("Area", Area);
                    locationInfo.put("AddressLine1", AddressLine1);
                    locationInfo.put("AddressLine2", AddressLine2);
                    //locationInfo.put("Locale",locale);
                    locationInfoCollection.document("Delivery_Address").set(locationInfo);

              //  }

           // } catch (IOException e) {
           //     e.printStackTrace();
           // }

      // }
    }


        private void locationSet(){
        Intent intent = new Intent(CheckoutActivity.this, UserCheckoutPayProcess.class );
        startActivity(intent);
    }

   private void locationNowSet(Double Longitude,Double Latitude) throws IOException{
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
            final List<Address> addresses = gcd.getFromLocation(Longitude,Latitude, 1);
            //Address address=addresses.get(0);

            //LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

       if(addresses!=null && addresses.size() > 0) {
           String locality = addresses.get(0).getLocality();
           String city = addresses.get(0).getSubLocality();
           String Area = addresses.get(0).getAdminArea();
           //Locale locale= addresses.get(0).getLocale();

           fStoreRef = (FirebaseFirestore) FirebaseFirestore.getInstance();
           user = FirebaseAuth.getInstance();
           String uid = user.getUid();
           CollectionReference locationInfoCollection1 = fStoreRef.collection("Users").document(uid).collection("User_Delivery_Address");
           Map<String, Object> locationInfo = new HashMap<>();
           locationInfo.put("Latitude", Latitude);
           locationInfo.put("Longitude", Longitude);
           locationInfo.put("Location", locality);
           locationInfo.put("Sub-Location", city);
           locationInfo.put("Area", Area);
           //locationInfo.put("Locale",locale);
           locationInfoCollection1.document("Delivery_Address").set(locationInfo);
       }
    }

    private void checkFunc(String location)  {
        if (!location.equals("")) {
            try {
            int data=50/0;
            if (location.equals("Kathmandu")) {
                Intent intent = new Intent(CheckoutActivity.this, CategoryFoodActivity.class);
                startActivity(intent);
            } } catch (ArithmeticException e) {
                e.printStackTrace();
            }
        }
    }
}