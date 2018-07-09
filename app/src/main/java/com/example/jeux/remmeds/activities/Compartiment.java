package com.example.jeux.remmeds.activities;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.example.jeux.remmeds.R;
import com.example.jeux.remmeds.fragments.FragmentAccueil;


import java.text.DecimalFormat;
import java.util.Calendar;

public class Compartiment extends AppCompatActivity implements View.OnClickListener {

    private Switch swiheureperso;
    private Switch swifrequenceperso;
    private ToggleButton toglundi;
    private ToggleButton togmardi;
    private ToggleButton togmercredi;
    private ToggleButton togjeudi;
    private ToggleButton togvendredi;
    private ToggleButton togsamedi;
    private ToggleButton togdimanche;
    private Button enregistrer;
    private TextView texnommedic;
    private EditText edinbrheure;
    private EditText nbrduree;
    private EditText texnotes;
    private Switch swibreakfast;
    private Switch swidejeuner;
    private Switch swidiner;
    private Switch swicoucher;
    private Spinner typeduree;

    private String dayperso;
    private String listpref;
    private String durationtext;
    private String durationnumber;
    private String drugname;
    private String notes;
    private String compid;
    private String heureperso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartiment);
        swibreakfast = findViewById(R.id.ptitdej_switch_layout_compartiment);
        swidejeuner = findViewById(R.id.dejeuner_switch_layout_compartiment);
        swidiner = findViewById(R.id.diner_switch_layout_compartiment);
        swicoucher = findViewById(R.id.coucher_switch_layout_compartiment);
        swiheureperso = findViewById(R.id.heureperso_switch_layout_compartiment);
        swifrequenceperso = findViewById(R.id.frequenceperso_switch_layout_compartiment);

        enregistrer = findViewById(R.id.enregistrer_button_layout_compartiment);

        typeduree = findViewById(R.id.typenombre_spinner_layout_compartiment);

        texnotes = findViewById(R.id.note_editText_layout_compartiment);
        nbrduree = findViewById(R.id.nombre_editText_layout_compartiment);
        texnommedic = findViewById(R.id.nom_editText_layout_compartiment);
        edinbrheure = findViewById(R.id.heureperso_editText_layout_compartiment);

        toglundi = findViewById(R.id.lundi_toggleButton_layout_compartiment);
        togmardi = findViewById(R.id.mardi_toggleButton_layout_compartiment);
        togmercredi = findViewById(R.id.mercredi_toggleButton_layout_compartiment);
        togjeudi = findViewById(R.id.jeudi_toggleButton_layout_compartiment);
        togvendredi = findViewById(R.id.vendredi_toggleButton_layout_compartiment);
        togsamedi = findViewById(R.id.samedi_toggleButton_layout_compartiment);
        togdimanche = findViewById(R.id.dimanche_toggleButton_layout_compartiment);

        getFormattedHour(edinbrheure);

        swibreakfast.setOnClickListener(this);
        swidejeuner.setOnClickListener(this);
        swidiner.setOnClickListener(this);
        swicoucher.setOnClickListener(this);
        swiheureperso.setOnClickListener(this);
        swifrequenceperso.setOnClickListener(this);
        enregistrer.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            compid = getIntent().getExtras().getString("compartment_id");
            if (getIntent().getExtras().getString("days") != null) {
                dayperso = getIntent().getExtras().getString("days");
                setUpDaysPref(dayperso);
            }
            if (getIntent().getExtras().getString("list_pref") != null) {
                listpref = getIntent().getExtras().getString("list_pref");
                setUpTimePref(listpref);
            }
            durationtext = getIntent().getExtras().getString("duration_text");
            durationnumber = getIntent().getExtras().getString("duration_number");
            drugname = getIntent().getExtras().getString("drug_name");
            notes = getIntent().getExtras().getString("note");
            heureperso = getIntent().getExtras().getString("perso_hour");
            setUpDureePref(durationtext);
            nbrduree.setText(durationnumber);
            texnommedic.setText(drugname);
            texnotes.setText(notes);
            if(!heureperso.equals("")){
                swiheureperso.setChecked(true);
                optionsheureperso();
                edinbrheure.setText(heureperso);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ptitdej_switch_layout_compartiment:
                break;
            case R.id.dejeuner_switch_layout_compartiment:
                break;
            case R.id.diner_switch_layout_compartiment:
                break;
            case R.id.coucher_switch_layout_compartiment:
                break;
            case R.id.heureperso_switch_layout_compartiment:
                optionsheureperso();
                break;
            case R.id.frequenceperso_switch_layout_compartiment:
                optionsfrequenceperso();
                break;
            case R.id.enregistrer_button_layout_compartiment:
                saveChanges();
                FragmentAccueil.destroyRecyclerAccueil();
                onBackPressed();
            default:
                break;
        }
    }

    private void saveChanges() {
        drugname = texnommedic.getText().toString();
        notes = texnotes.getText().toString();
        durationnumber = nbrduree.getText().toString();
        durationtext = typeduree.getSelectedItem().toString();
        dayperso = saveDayPerso();
        listpref = saveListPref();
        heureperso = edinbrheure.getText().toString();
        MainActivity.postDoInBackground("http://212.73.217.202:15020/compartment/update_com/" + compid + "&" + drugname + "&" + notes + "&" + durationnumber + "&" + durationtext + "&0&"+ heureperso +"&0&" + dayperso + "&" + listpref);
    }

    private String saveListPref() {
        String urlpref = "";
        if (swibreakfast.isChecked()) {
            urlpref += "Breakfast,";
        }
        if (swidejeuner.isChecked()) {
            urlpref += "Lunch,";
        }
        if (swidiner.isChecked()) {
            urlpref += "Dinner,";
        }
        if (swicoucher.isChecked()) {
            urlpref += "Bedtime,";
        }
        urlpref = urlpref.substring(0, urlpref.length() - 1);
        return urlpref;
    }

    private String saveDayPerso() {
        String urldays = "";
        if (toglundi.isChecked()) {
            urldays += "Lundi,";
        }
        if (togmardi.isChecked()) {
            urldays += "Mardi,";
        }
        if (togmercredi.isChecked()) {
            urldays += "Mercredi,";
        }
        if (togjeudi.isChecked()) {
            urldays += "Jeudi,";
        }
        if (togvendredi.isChecked()) {
            urldays += "Vendredi,";
        }
        if (togsamedi.isChecked()) {
            urldays += "Samedi,";
        }
        if (togdimanche.isChecked()) {
            urldays += "Dimanche,";
        }
        urldays = urldays.substring(0, urldays.length() - 1);
        return urldays;
    }

    private void setUpDureePref(String dureePref) {
        String[] items = dureePref.split(",");
        for (String item : items) {
            switch (item) {
                case "Jours":
                    typeduree.setSelection(0);
                    break;
                case "Semaines":
                    typeduree.setSelection(1);
                    break;
                case "Mois":
                    typeduree.setSelection(2);
                    break;
                case "Années":
                    typeduree.setSelection(3);
                    break;
                default:
                    break;
            }
        }
    }

    private void setUpTimePref(String timePref) {
        String[] items = timePref.split(",");
        for (String item : items) {
            switch (item) {
                case "Breakfast":
                    swibreakfast.setChecked(true);
                    break;
                case "Lunch":
                    swidejeuner.setChecked(true);
                    break;
                case "Dinner":
                    swidiner.setChecked(true);
                    break;
                case "Bedtime":
                    swicoucher.setChecked(true);
                    break;
                default:
                    break;
            }
        }
    }

    private void setUpDaysPref(String daysPref) {
        String[] items = daysPref.split(",");
        swifrequenceperso.setChecked(true);
        optionsfrequenceperso();
        for (String item : items) {
            switch (item) {
                case "Lundi":
                    toglundi.setChecked(true);
                    break;
                case "Mardi":
                    togmardi.setChecked(true);
                    break;
                case "Merecredi":
                    togmercredi.setChecked(true);
                    break;
                case "Jeudi":
                    togjeudi.setChecked(true);
                    break;
                case "Vendredi":
                    togvendredi.setChecked(true);
                    break;
                case "Samedi":
                    togsamedi.setChecked(true);
                    break;
                case "Dimanche":
                    togdimanche.setChecked(true);
                    break;
            }
        }
    }

    private void getFormattedHour(final EditText whichField) {
        //Launch a time picker instead of keyboard on clock field
        whichField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar myCurrentTime = Calendar.getInstance();
                int hour = myCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = myCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Compartiment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        DecimalFormat addZero = new DecimalFormat("00");
                        whichField.setText(addZero.format(selectedHour) + ":" + addZero.format(selectedMinute)); //NOSONAR
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Sélectionner heure");
                mTimePicker.show();

            }
        });
    }

    private void optionsfrequenceperso() {
        if (swifrequenceperso.isChecked()) {
            toglundi.setVisibility(View.VISIBLE);
            togmardi.setVisibility(View.VISIBLE);
            togmercredi.setVisibility(View.VISIBLE);
            togjeudi.setVisibility(View.VISIBLE);
            togvendredi.setVisibility(View.VISIBLE);
            togsamedi.setVisibility(View.VISIBLE);
            togdimanche.setVisibility(View.VISIBLE);
        } else {
            toglundi.setVisibility(View.GONE);
            togmardi.setVisibility(View.GONE);
            togmercredi.setVisibility(View.GONE);
            togjeudi.setVisibility(View.GONE);
            togvendredi.setVisibility(View.GONE);
            togsamedi.setVisibility(View.GONE);
            togdimanche.setVisibility(View.GONE);
        }
    }

    private void optionsheureperso() {
        if (swiheureperso.isChecked()) {
            edinbrheure.setVisibility(View.VISIBLE);
        } else {
            edinbrheure.setVisibility(View.GONE);
        }
    }
}