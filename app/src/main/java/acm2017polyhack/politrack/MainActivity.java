package acm2017polyhack.politrack;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageButton btnShowLocation;
    Button btnZipSearch;
    EditText textBoxZip;
    TextView feedback;
    TextView site;
    TextView phoneNum;
    TextView name;
    ArrayList<Person> person = new ArrayList<>();

    // GPSTracker class
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowLocation = (ImageButton) findViewById(R.id.locButton);
        btnZipSearch = (Button) findViewById(R.id.searchBut);
        textBoxZip = (EditText) findViewById(R.id.editText);





        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {
                    person.clear();
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    //TO DO
                    //get the latitude and longitude and call the API

                    new getSenatorWGPS().execute("https://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + latitude + "&longitude=" + longitude);


                    // \n is for new line
                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
        btnZipSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                String zipCode = textBoxZip.getText().toString();
                // create class object
                    person.clear();
                    new getSenatorWGPS().execute("https://congress.api.sunlightfoundation.com/legislators/locate?zip=" + zipCode);


                }
        });
    }

    public class getSenatorWGPS extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection toConnect = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                toConnect = (HttpURLConnection) url.openConnection();
                toConnect.connect();

                InputStream stream = toConnect.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJason = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJason);
                JSONArray parentArray = parentObject.getJSONArray("results");

                for (int a = 0; a < parentArray.length(); a++) {
                    JSONObject finalObject = parentArray.getJSONObject(a);
                    String fName = finalObject.getString("first_name");
                    String lName = finalObject.getString("last_name");
                    String email = finalObject.getString("oc_email");
                    String party = finalObject.getString("party");
                    String phone = finalObject.getString("phone");
                    String website = finalObject.getString("website");
                    person.add(new Person(fName, lName, email, party, phone, website));
                }


                return "" + person.size();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (toConnect != null) {
                    toConnect.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            //textview1.setText(result);
            runTables();

        }

    }
    public void runTables(){
        TableLayout table = (TableLayout)MainActivity.this.findViewById(R.id.activity_main);
        cleanTable(table);

        for(int i = 0; i < person.size(); i++) {
            // Inflate your row "template" and fill out the fields.
            TableRow row = (TableRow) LayoutInflater.from(MainActivity.this).inflate(R.layout.tablerows, null);

            feedback = (TextView) row.findViewById(R.id.link);
            phoneNum = (TextView) row.findViewById(R.id.phoneNum);
            name = (TextView) row.findViewById(R.id.nameR);
            site = (TextView) row.findViewById(R.id.website);

            name.setText(person.get(i).getfName()+" "+person.get(i).getlName());
            feedback.setText(Html.fromHtml("<a href=\"mailto:"+person.get(i).getEmail()+"\">Send Email</a>"));
            feedback.setMovementMethod(LinkMovementMethod.getInstance());
            site.setText(Html.fromHtml("<a href=\""+person.get(i).getWebsite()+"\">Website</a>"));
            phoneNum.setText(person.get(i).getPhone());
            site.setMovementMethod(LinkMovementMethod.getInstance());

            table.addView(row);
        }
        table.requestLayout();



    }
    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }

}