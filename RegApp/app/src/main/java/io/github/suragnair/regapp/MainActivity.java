package io.github.suragnair.regapp;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private static final String ServerURL = "http://agni.iitd.ernet.in/cop290/assign0/register/";

    public static final String KEY_TEAMNAME = "teamname";
    public static final String KEY_ENTRY1 = "entry1";
    public static final String KEY_NAME1 = "name1";
    public static final String KEY_ENTRY2 = "entry2";
    public static final String KEY_NAME2 = "name2";
    public static final String KEY_ENTRY3 = "entry3";
    public static final String KEY_NAME3 = "name3";

    private RequestQueue requestQueue;

    private EditText teamnameField;
    private EditText name1Field;
    private EditText entry1Field;
    private EditText name2Field;
    private EditText entry2Field;
    private EditText name3Field;
    private EditText entry3Field;

    private List<String> name1SuggestionsList = new ArrayList<>();
    private List<String> name2SuggestionsList = new ArrayList<>();
    private List<String> name3SuggestionsList = new ArrayList<>();

    private List<String> StudentNameList = new ArrayList<>();
    private List<String> StudentEntrynoList = new ArrayList<>();

    private ProgressBar spinner;
    private Button submitButton;
    private Button addMemberButton;
    private Button removeMemberButton;
    private int noOfMembers = 2;
    private Set<String> allDepts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Linking EditText
        teamnameField = (EditText) findViewById(R.id.teamnameField);
        name1Field = (EditText) findViewById(R.id.name1Field);
        entry1Field = (EditText) findViewById(R.id.entry1Field);
        name2Field = (EditText) findViewById(R.id.name2Field);
        entry2Field = (EditText) findViewById(R.id.entry2Field);
        name3Field = (EditText) findViewById(R.id.name3Field);
        entry3Field = (EditText) findViewById(R.id.entry3Field);

        //Linking Buttons
        submitButton = (Button) findViewById(R.id.submitButton);
        addMemberButton = (Button) findViewById(R.id.addThirdMemberButton);
        removeMemberButton = (Button) findViewById(R.id.removeThirdMemberButton);
        spinner = (ProgressBar)findViewById(R.id.loadingIcon);

        //Initialising ListViews
        ListView name1SuggestionsListView = (ListView) findViewById(R.id.name1SuggestionsList);
        ListView name2SuggestionsListView = (ListView) findViewById(R.id.name2SuggestionsList);
        ListView name3SuggestionsListView = (ListView) findViewById(R.id.name3SuggestionsList);

        //Initialising Array Adapters
        ArrayAdapter name1SuggestionsListAdapter = new ArrayAdapter(this, R.layout.activity_listview, name1SuggestionsList);
        ArrayAdapter name2SuggestionsListAdapter = new ArrayAdapter(this, R.layout.activity_listview, name2SuggestionsList);
        ArrayAdapter name3SuggestionsListAdapter = new ArrayAdapter(this, R.layout.activity_listview, name3SuggestionsList);

        // Set Listeners to EditTexts and ListViews
        initialiseNameSuggestionsList(name1Field, entry1Field, name1SuggestionsListView, name1SuggestionsList, name1SuggestionsListAdapter);
        initialiseNameSuggestionsList(name2Field, entry2Field, name2SuggestionsListView, name2SuggestionsList, name2SuggestionsListAdapter);
        initialiseNameSuggestionsList(name3Field, entry3Field, name3SuggestionsListView, name3SuggestionsList, name3SuggestionsListAdapter);

        //Initialising Request Queue
        requestQueue = Volley.newRequestQueue(this);

        //Load EntryNO Database
        loadEntryNoDataFromTextFile();
        loadDeptsList();
    }

    public void submitButtonClicked(View view)
    {
        spinner.setVisibility(View.VISIBLE);
        submitButton.setClickable(false);

        // Extracting Text From EditText
        final String teamname = teamnameField.getText().toString();
        final String name1 = name1Field.getText().toString();
        final String entry1 = entry1Field.getText().toString();
        final String name2 = name2Field.getText().toString();
        final String entry2 = entry2Field.getText().toString();
        final String name3 = name3Field.getText().toString();
        final String entry3 = entry3Field.getText().toString();

        boolean ERROR_FLAG = false;

        if(teamname.matches(""))
        {
            teamnameField.setError("Enter Team Name");
            ERROR_FLAG = true;
        }
        if(name1.matches(""))
        {
            name1Field.setError("Enter Name");
            ERROR_FLAG = true;
        }
        if(name2.matches(""))
        {
            name2Field.setError("Enter Name");
            ERROR_FLAG = true;
        }
        if(!isValidEntryNo(entry1))
        {
            entry1Field.setError("Invalid Entry No");
            ERROR_FLAG = true;
        }
        if(!isValidEntryNo(entry2))
        {
            entry2Field.setError("Invalid Entry No");
            ERROR_FLAG = true;
        }
        if((!entry2.equals(""))&&(entry2.equals(entry1)))
        {
            entry2Field.setError("Repeated Entry No");
            ERROR_FLAG = true;
        }

        if (noOfMembers==3)
        {
            if(name3.matches(""))
            {
                name3Field.setError("Enter Name");
                ERROR_FLAG = true;
            }
            if(!isValidEntryNo(entry3))
            {
                entry3Field.setError("Invalid Entry No");
                ERROR_FLAG = true;
            }
            if((!entry3.equals(""))&&((entry3.equals(entry1))||(entry3.equals(entry2))))
            {
                entry3Field.setError("Repeated Entry No");
                ERROR_FLAG = true;
            }
        }

        if (ERROR_FLAG) {
            spinner.setVisibility(View.GONE);
            submitButton.setClickable(true);
            return;
        }

        //SEND THE REQUEST TO SERVER
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        spinner.setVisibility(View.GONE);
                        submitButton.setClickable(true);
                        responseReceived(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.GONE);
                        submitButton.setClickable(true);
                        alertMessageBox("Error Response From Server", error.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_TEAMNAME,teamname);
                params.put(KEY_ENTRY1,entry1);
                params.put(KEY_NAME1,name1);
                params.put(KEY_ENTRY2,entry2);
                params.put(KEY_NAME2,name2);
                params.put(KEY_ENTRY3,entry3);
                params.put(KEY_NAME3,name3);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void responseReceived(String response)
    {
        boolean REGISTRATION_SUCCESS = false;

        try {
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.getString("RESPONSE_MESSAGE").equals("Data not posted!"))
                alertMessageBox("Oops!","Something Went Wrong. Data not posted.");
            else if (jsonResponse.getString("RESPONSE_MESSAGE").equals("Registration completed")) {
                alertMessageBox("Congratulations","Welcome to the course. Your team has been registered.");
                REGISTRATION_SUCCESS = true;
            }
            else if (jsonResponse.getString("RESPONSE_MESSAGE").equals("User already registered"))
                alertMessageBox("Well That's Embarrassing!","One or more member(s) of your team is already registered.");
            else
                alertMessageBox("Unaccounted Response", response);
        } catch (JSONException e) {
            Log.d("JSON Exception", e.getMessage());
            alertMessageBox("Unexpected Response From Server", "JSON Exception : " + e.getMessage());
        }

        if (REGISTRATION_SUCCESS)
            clearButtonClicked(null);
    }

    private boolean isValidEntryNo (String entryNo)
    {
        //CHECKS FOR VALID ENTRY NUMBER
        if (entryNo.length()!=11)
            return false;

        int year, serialNo;
        String dept = entryNo.substring(4, 7).toUpperCase();

        try {
            year = Integer.parseInt(entryNo.substring(0, 4));
            serialNo = Integer.parseInt((entryNo.substring(7, 11)));
        } catch (NumberFormatException e) {
            return false;
        }

        return ((year>=2000)&&(year<=2015)) && allDepts.contains(dept) && ((serialNo>0)&&(serialNo<10000));
    }

    private void loadEntryNoDataFromTextFile() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getBaseContext().getResources().openRawResource(R.raw.entrydata)));
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0)
                    continue;
                StudentEntrynoList.add(line.substring(0, 11));
                StudentNameList.add(line.substring(11, line.length()).trim());
            }
        } catch (IOException e) {
            Log.d("IOException", "loadEntryNoDataFromTextFile: " + e.getMessage());
        }
    }

    private List<String> suggestStudents (String partString)
    {
        List<String> suggestions = new ArrayList<>();
        for (String name : StudentNameList){
            if(name.toLowerCase().contains(partString.toLowerCase()))
                suggestions.add(name);
            if(suggestions.size()==3)
                break;
        }
        return suggestions;
    }

    public void addMemberClicked(View view) {
        name3Field.setVisibility(View.VISIBLE);
        entry3Field.setVisibility(View.VISIBLE);
        removeMemberButton.setVisibility(View.VISIBLE);
        addMemberButton.setVisibility(View.GONE);
        noOfMembers = 3;
    }

    public void removeMemberClicked(View view) {
        name3Field.setVisibility(View.GONE);
        entry3Field.setVisibility(View.GONE);
        removeMemberButton.setVisibility(View.GONE);
        addMemberButton.setVisibility(View.VISIBLE);
        noOfMembers = 2;
        name3Field.setText("");
        entry3Field.setText("");
        name3Field.setError(null);
        entry3Field.setError(null);
    }

    public void clearButtonClicked(View view) {
        teamnameField.setText("");
        name1Field.setText("");
        entry1Field.setText("");
        name2Field.setText("");
        entry2Field.setText("");
        name3Field.setText("");
        entry3Field.setText("");
        teamnameField.setError(null);
        name1Field.setError(null);
        entry1Field.setError(null);
        name2Field.setError(null);
        entry2Field.setError(null);
        name3Field.setError(null);
        entry3Field.setError(null);
    }

    private void initialiseNameSuggestionsList (final EditText nameField,
                                                final EditText entryField,
                                                final ListView nameSuggestionsListView,
                                                final List<String> nameSuggestionsList,
                                                final ArrayAdapter nameSuggestionsListAdapter) {

        // Assign Adapter to ListView
        nameSuggestionsListView.setAdapter(nameSuggestionsListAdapter);

        // Set OnFocusChangeListener to Name EditText to toggle ListView visibility
        nameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nameSuggestionsListView.setVisibility(View.VISIBLE);
                } else {
                    nameSuggestionsListView.setVisibility(View.GONE);
                }
            }
        });

        // Set TextChangeListener to EditText to repopulate ListView
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameSuggestionsList.clear();
                if (s.length() > 2)
                    nameSuggestionsList.addAll(suggestStudents(s.toString()));
                nameSuggestionsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Set OnItemClickListener to ListView
        nameSuggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nameField.clearFocus();
                entryField.setText(StudentEntrynoList.get(StudentNameList.indexOf(nameSuggestionsList.get(position))));
                nameField.setText(nameSuggestionsList.get(position));
                entryField.setError(null);
            }
        });

        // Set OnFocusChangeListener to Entry EditText to autofill Name
        entryField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String entryNo = entryField.getText().toString();
                    if (isValidEntryNo(entryNo) && StudentEntrynoList.contains(entryNo.toLowerCase()) && nameField.getText().toString().equals("")) {
                        nameField.setText(StudentNameList.get(StudentEntrynoList.indexOf(entryNo.toLowerCase())));
                    }
                }
            }
        });
    }

    private void alertMessageBox (String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("OKAY", null);
        builder.show();
    }

    private void loadDeptsList(){
        allDepts = new HashSet<>();
        allDepts.add("BB1");
        allDepts.add("BB5");
        allDepts.add("CH1");
        allDepts.add("CH7");
        allDepts.add("CS1");
        allDepts.add("CS5");
        allDepts.add("CE1");
        allDepts.add("EE1");
        allDepts.add("EE3");
        allDepts.add("ME1");
        allDepts.add("ME2");
        allDepts.add("PH1");
        allDepts.add("MT1");
        allDepts.add("MT5");
        allDepts.add("MT6");
        allDepts.add("TT1");
        allDepts.add("CSZ");
        allDepts.add("MCS");
    }
}