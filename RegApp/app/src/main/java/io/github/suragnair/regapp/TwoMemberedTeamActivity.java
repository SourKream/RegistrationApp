package io.github.suragnair.regapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.service.wallpaper.WallpaperService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class TwoMemberedTeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_membered_team);

        //Setting live background

        ImageView ivLoader = (ImageView) findViewById(R.id.IVloadinganimation);
        ivLoader.setBackgroundResource(R.layout.live_bg);

        AnimationDrawable frameAnimation = (AnimationDrawable) ivLoader.getBackground();
        frameAnimation.start();


    }
    public void submit_button(View view)
    {
        EditText editText_teamName = (EditText) findViewById(R.id.teamName);
        EditText editText_member1Name = (EditText) findViewById(R.id.member1Name);
        EditText editText_member1EntryNumber = (EditText) findViewById(R.id.member1EntryNumber);
        EditText editText_member2Name = (EditText) findViewById(R.id.member2Name);
        EditText editText_member2EntryNumber = (EditText) findViewById(R.id.member2EntryNumber);
        String teamName = editText_teamName.getText().toString();
        String member1Name = editText_member1Name.getText().toString();
        String member1EntryNumber = editText_member1EntryNumber.getText().toString();
        String member2Name = editText_member2Name.getText().toString();
        String member2EntryNumber = editText_member2EntryNumber.getText().toString();

        //Check for Empty Text-Field
        if(teamName.matches(""))
        {
            editText_teamName.setError("Enter Team Name");
            return;
        }
        if(member1Name.matches(""))
        {
            editText_member1Name.setError("Enter Name");
            return;
        }
        if(member1EntryNumber.matches(""))
        {
            editText_member1EntryNumber.setError("Enter Entry Number");
            return;
        }
        if(member2Name.matches(""))
        {
            editText_member2Name.setError("Enter Name");
            return;
        }
        if(member2EntryNumber.matches(""))
        {
            editText_member2EntryNumber.setError("Enter Entry Number");
            return;
        }

        //Checks for valid entry number

        int Entry1_Length = member1EntryNumber.length();
        int Entry2_Length = member2EntryNumber.length();

        //Checking Length of entry number

        if((Entry1_Length!=11))
        {
            editText_member1EntryNumber.setError("Invalid Entry Number");
            return;
        }

        if((Entry2_Length!=11))
        {
            editText_member2EntryNumber.setError("Invalid Entry Number");
            return;
        }

        String Year_EntryNumber1 = member1EntryNumber.substring(0,4);
        String Year_EntryNumber2 = member2EntryNumber.substring(0,4);
        String BranchCode_EntryNumber1 = member1EntryNumber.substring(4,7);
        String BranchCode_EntryNumber2 = member2EntryNumber.substring(4,7);
        String Number_EntryNumber1 = member1EntryNumber.substring(7,11);
        String Number_EntryNumber2 = member2EntryNumber.substring(7,11);

        String[] Year = {"2010","2011","2012","2013","2014"};

        BranchCode_EntryNumber1 = BranchCode_EntryNumber1.toUpperCase();
        BranchCode_EntryNumber2 = BranchCode_EntryNumber2.toUpperCase();

        String[] BranchCodes= {"CH1","CS1","CE1","EE1","EE2","EE3","ME1","ME2","PH1","TT1","BE5","CH7",
                "CS5","EE5","MT5","AMX","CEX","CYS","MAS","PHS","SMF","SMT","SMN","JDS","AME",
                "AMD","CHE","CYM","CEG","CEU","CES","CEW","CET","CEC","CEV","CEP","MCS","EEE",
                "EET","EEA","EEN","EEP","EES","MED","MEE","MEP","MET","PHA","PHM","TTF","TTE",
                "CRF","AST","JCA","JES","JEN","JIT","JID","JOP","JPT","JTM","JVL","AMY","BSY",
                "BEY","CHY","CEY","CSY","EEY","MEY","SIY"};

        int counter1 = 0;
        int counter2 = 0;

        //Check for valid year

        for(int i = 0;i<5;i++)
        {
            if(Year_EntryNumber1.matches(Year[i]))
            {
                counter1++;
            }
            if(Year_EntryNumber2.matches(Year[i]))
            {
                counter2++;
            }
        }

        if(counter1==0)
        {
            editText_member1EntryNumber.setError("Invalid Entry Number");
            return;
        }

        if(counter2==0)
        {
            editText_member2EntryNumber.setError("Invalid Entry Number");
            return;
        }

        counter1 = 0;
        counter2 = 0;

        //Check for valid branch code

        for(int i = 0; i< BranchCodes.length; i++)
        {
            if(BranchCode_EntryNumber1.matches(BranchCodes[i]))
            {
                counter1++;
                if(counter2 == 1)
                    break;
            }
            if(BranchCode_EntryNumber2.matches(BranchCodes[i]))
            {
                counter2++;
                if(counter1 == 1)
                    break;
            }
        }

        if(counter1==0)
        {
            editText_member1EntryNumber.setError("Invalid Entry Number");
            return;
        }

        if(counter2==0)
        {
            editText_member2EntryNumber.setError("Invalid Entry Number");
            return;
        }

        //Check if Serial Number is integer or not

        for(int i = 0;i < 4;i++)
        {
            char ch1 = Number_EntryNumber1.charAt(i);
            char ch2 = Number_EntryNumber2.charAt(i);
            if(ch1 > '9' || ch1 < '0')
            {
                editText_member1EntryNumber.setError("Invalid Entry Number");
                return;
            }
            if(ch2 > '9' || ch2 < '0')
            {
                editText_member2EntryNumber.setError("Invalid Entry Number");
                return;
            }
        }

        //Message Display if there is no error

        String message = "Nice Team" + " " + teamName;

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        //TO DO : SEND THE REQUEST TO SERVER


        //After Successful Registration Going back to home screen

        Intent intent = new Intent(TwoMemberedTeamActivity.this, MainActivity.class);
        startActivity(intent);


    }


}

