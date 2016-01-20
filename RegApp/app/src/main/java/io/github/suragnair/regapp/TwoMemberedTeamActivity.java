package io.github.suragnair.regapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.service.wallpaper.WallpaperService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TwoMemberedTeamActivity extends AppCompatActivity {

    private static final String ServerURL = "http://agni.iitd.ernet.in/cop290/assign0/register/";

    public static final String KEY_TEAMNAME = "teamname";
    public static final String KEY_ENTRY1 = "entry1";
    public static final String KEY_NAME1 = "name1";
    public static final String KEY_ENTRY2 = "entry2";
    public static final String KEY_NAME2 = "name2";

    private EditText teamnameField;
    private EditText name1Field;
    private EditText entry1Field;
    private EditText name2Field;
    private EditText entry2Field;

    private ListView name1SuggestionsListView;
    private ListView name2SuggestionsListView;
    private ArrayAdapter name1SuggestionsListAdapter;
    private ArrayAdapter name2SuggestionsListAdapter;
    private List<String> name1SuggestionsList = new ArrayList<String>();
    private List<String> name2SuggestionsList = new ArrayList<String>();

    private List<String> StudentNameList = new ArrayList<String>();
    private List<String> StudentEntrynoList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_membered_team);

        // Linking EditText
        teamnameField = (EditText) findViewById(R.id.teamnameField);
        name1Field = (EditText) findViewById(R.id.name1Field);
        entry1Field = (EditText) findViewById(R.id.entry1Field);
        name2Field = (EditText) findViewById(R.id.name2Field);
        entry2Field = (EditText) findViewById(R.id.entry2Field);

        //Setting live background
        ImageView ivLoader = (ImageView) findViewById(R.id.IVloadinganimation);
        ivLoader.setBackgroundResource(R.layout.live_bg);

        AnimationDrawable frameAnimation = (AnimationDrawable) ivLoader.getBackground();
        frameAnimation.start();

        //Initialising List View
        name1SuggestionsListAdapter = new ArrayAdapter(this, R.layout.activity_listview, name1SuggestionsList);
        name1SuggestionsListView = (ListView) findViewById(R.id.name1SuggestionsList);
        name1SuggestionsListView.setAdapter(name1SuggestionsListAdapter);
        name2SuggestionsListAdapter = new ArrayAdapter(this, R.layout.activity_listview, name2SuggestionsList);
        name2SuggestionsListView = (ListView) findViewById(R.id.name2SuggestionsList);
        name2SuggestionsListView.setAdapter(name2SuggestionsListAdapter);

        //EditText On Focus Listener
        name1Field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    name1SuggestionsListView.setVisibility(ListView.VISIBLE);
                } else {
                    name1SuggestionsListView.setVisibility(ListView.GONE);
                }
            }
        });
        name2Field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    name2SuggestionsListView.setVisibility(ListView.VISIBLE);
                } else {
                    name2SuggestionsListView.setVisibility(ListView.GONE);
                }
            }
        });

        //TextChange Listener for name1Field
        name1Field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name1SuggestionsList.clear();
                if(s.length()>2) {
                    name1SuggestionsList.addAll(suggestStudents(s.toString()));
                    while (name1SuggestionsList.size() > 3)
                        name1SuggestionsList.remove(name1SuggestionsList.size() - 1);
                }
                name1SuggestionsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name2Field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name2SuggestionsList.clear();
                if(s.length()>2){
                    name2SuggestionsList.addAll(suggestStudents(s.toString()));
                    while (name2SuggestionsList.size() > 3)
                        name2SuggestionsList.remove(name2SuggestionsList.size() - 1);
                }
                name2SuggestionsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Load EntryNO Database
        loadEntryNoDataFromTextFile();
    }

    public void submit_button(View view)
    {
        // Extracting Text From EditText
        final String teamname = teamnameField.getText().toString();
        final String name1 = name1Field.getText().toString();
        final String entry1 = entry1Field.getText().toString();
        final String name2 = name2Field.getText().toString();
        final String entry2 = entry2Field.getText().toString();

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

        if (ERROR_FLAG) {
            return;
        }

        //SEND THE REQUEST TO SERVER
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseReceived(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_TEAMNAME,teamname);
                params.put(KEY_ENTRY1,entry1);
                params.put(KEY_NAME1,name1);
                params.put(KEY_ENTRY2,entry2);
                params.put(KEY_NAME2,name2);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void responseReceived(String response)
    {
        Toast.makeText(TwoMemberedTeamActivity.this, response, Toast.LENGTH_SHORT).show();
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
        } catch (NumberFormatException nfe) {
            return false;
        }

        if ((year>2015)||(year<2000))
            return false;

        Set<String> allDepts = new HashSet<String>();
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

        if(!allDepts.contains(dept))
            return false;

        if ((serialNo<1)||(serialNo>9999))
            return false;

        return true;
    }

    private void loadEntryNoDataFromTextFile() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getBaseContext().getResources().openRawResource(R.raw.entrydata)));
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0)
                    continue;
                StudentEntrynoList.add(line.substring(0, 11));
                StudentNameList.add(line.substring(11, line.length()).trim());
            }
        } catch (IOException e) {

        }
    }

    private List<String> suggestStudents (String partString)
    {
        List<String> suggestions = new ArrayList<String>();
        for (String name : StudentNameList){
            if(name.contains(partString))
                suggestions.add(name);
        }
        return suggestions;
    }


}

