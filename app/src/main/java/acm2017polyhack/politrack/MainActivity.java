package acm2017polyhack.politrack;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

    ImageButton btnShowLocation;
    Button btnZipSearch;
    EditText textBoxZip;

    // GPSTracker class
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowLocation = (ImageButton) findViewById(R.id.locButton);
        btnZipSearch = (Button) findViewById(R.id.searchBut) ;
        textBoxZip = (EditText) findViewById(R.id.editText);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    //TO DO
                    //get the latitude and longitude and call the API

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
        btnZipSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

            String zipCode = textBoxZip.getText().toString();
                //TO DO
                //Get zipcode and put it into API call
            }
        });
    }

}