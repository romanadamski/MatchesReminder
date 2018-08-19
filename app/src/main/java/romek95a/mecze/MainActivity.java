package romek95a.mecze;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {


    private TeamsAdapter teamsAdapter;
    ArrayList<String> listOfTeams;
    private ListView teamsListView;
    private EditText team;
    private Button addTeam;

    private int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamsListView=(ListView) findViewById(R.id.list);
        listOfTeams= new ArrayList<String>();
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
        /*teamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context,MatchesViewActivity.class);
                intent.putExtra("teamName", listOfTeams.get(position));
                startActivity(intent);
            }
        });*/
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> hashOfTeams = new HashSet<>();
        Set<String> temp = new HashSet<>();
        temp.add("lipa");
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
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        Button buttonShow = (Button)vwParentRow.getChildAt(1);
        Context context = getApplicationContext();
        Intent intent = new Intent(context,MatchesViewActivity.class);
        intent.putExtra("teamName", buttonShow.getText());
        startActivity(intent);
    }
    public void deleteTeamHandler(View v){
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        Button buttonShow = (Button)vwParentRow.getChildAt(1);
        teamsAdapter.remove(buttonShow.getText().toString());
        vwParentRow.refreshDrawableState();
    }
}
