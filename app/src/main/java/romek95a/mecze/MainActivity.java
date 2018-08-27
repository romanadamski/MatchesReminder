package romek95a.mecze;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {


    private TeamsAdapter teamsAdapter;
    ArrayList<String> listOfTeams;
    private ListView teamsListView;
    private EditText team;
    private Button addTeam;
    private String teamToDelete = "";
    private boolean isPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamsListView=findViewById(R.id.list);
        listOfTeams= new ArrayList<>();
        teamsAdapter=new TeamsAdapter(this,listOfTeams);
        teamsListView.setAdapter(teamsAdapter);
        team = findViewById(R.id.team);
        addTeam = findViewById(R.id.addTeam);

        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!team.getText().toString().equals("")){
                    teamsAdapter.add(team.getText().toString());
                    team.setText("");
                }
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> hashOfTeams = new HashSet<>();
        Set<String> temp = new HashSet<>();
        for(String team : listOfTeams){
            hashOfTeams.add(team);
        }
        editor.putStringSet("teams", hashOfTeams);
        editor.commit();
        System.out.println("getStringSet: "+sharedPref.getStringSet("teams", temp));
    }
    @Override
    protected void onResume() {
        super.onResume();
        listOfTeams.clear();
        Set<String> defaultValue = new HashSet<>();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        teamsAdapter.addAll(sharedPref.getStringSet("teams", defaultValue));
    }

    public void showMatchesHandler(View v){
        if(!isOnline()){
            noInternetDialog().show();
        }
        else{
            RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
            Button buttonShow = (Button)vwParentRow.getChildAt(1);
            Context context = getApplicationContext();
            Intent intent = new Intent(context,MatchesViewActivity.class);
            intent.putExtra("teamName", buttonShow.getText());
            startActivity(intent);
        }
    }
    public void deleteTeamHandler(View v){
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        Button buttonShow = (Button)vwParentRow.getChildAt(1);
        teamToDelete = buttonShow.getText().toString();
        deleteDialog().show();
        vwParentRow.refreshDrawableState();
    }

    private Dialog deleteDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Usuwanie drużyny");
        dialogBuilder.setMessage("Czy na pewno chcesz usunąć " + teamToDelete + "?");
        dialogBuilder.setNegativeButton("Nie", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        dialogBuilder.setPositiveButton("Tak", new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                teamsAdapter.remove(teamToDelete);
            }
        });

        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }

    private Dialog noInternetDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Brak połączenia");
        dialogBuilder.setMessage("Brak dostępu do Internetu. Włącz Wi-Fi lub transmisję danych.");
        dialogBuilder.setNegativeButton("OK", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }
    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
