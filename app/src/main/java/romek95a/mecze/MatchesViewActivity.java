package romek95a.mecze;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

public class MatchesViewActivity extends Activity {

    private String teamName = "";
    private TextView teamNameTV;
    private TextView firstLeagueTime;
    private TextView firstTeamHomeName;
    private TextView firstTeamAwayName;
    private TextView firstTeamHomeScore;
    private TextView firstTeamAwayScore;
    private TextView firstDuring;
    private TextView separator;
    Map<String, String> firstMatchMap;
    Map<String, String> nexMatchesMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_view);
        teamNameTV = findViewById(R.id.teamName);
        firstLeagueTime = findViewById(R.id.firstLeagueTime);
        firstTeamHomeName = findViewById(R.id.firstTeamHomeName);
        firstTeamAwayName = findViewById(R.id.firstTeamAwayName);
        firstTeamHomeScore = findViewById(R.id.firstTeamHomeScore);
        firstTeamAwayScore = findViewById(R.id.firstTeamAwayScore);
        firstDuring = findViewById(R.id.firstDuring);
        separator = findViewById(R.id.separator);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teamName = extras.getString("teamName");
        }
        teamNameTV.setText(teamName);
        ParseHtml parseHtml = new ParseHtml(teamName);
        ElementsBefore elementsBefore = parseHtml.parse();
        firstMatchMap = parseHtml.firstMatchFinalInfo(elementsBefore.firstMatch);
        getFirstMatchInfo(firstMatchMap);

    }
    public void getFirstMatchInfo(Map<String, String> firstMatchMap){
        firstLeagueTime.setText(firstMatchMap.get("leagueTime"));
        if(!firstLeagueTime.getText().equals("")){
            firstTeamHomeName.setText(firstMatchMap.get("teamHomeName"));
            firstTeamAwayName.setText(firstMatchMap.get("teamAwayName"));
            firstTeamHomeScore.setText(firstMatchMap.get("teamHomeScore"));
            firstTeamAwayScore.setText(firstMatchMap.get("teamAwayScore"));
            firstTeamAwayScore.setText(firstMatchMap.get("teamAwayScore"));
            firstDuring.setText(firstMatchMap.get("during"));
            separator.setText("-");
        }
        else{
            firstTeamHomeName.setVisibility(View.GONE);
            firstTeamAwayName.setVisibility(View.GONE);
            firstTeamHomeScore.setVisibility(View.GONE);
            firstTeamAwayScore.setVisibility(View.GONE);
            firstTeamAwayScore.setVisibility(View.GONE);
            firstDuring.setVisibility(View.GONE);
            separator.setVisibility(View.GONE);
        }
    }
}
