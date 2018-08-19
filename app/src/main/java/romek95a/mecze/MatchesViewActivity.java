package romek95a.mecze;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MatchesViewActivity extends Activity {

    private String teamName = "";
    private TextView teamNameTV;
    private TextView afterParse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_view);
        teamNameTV = (TextView)findViewById(R.id.teamName);
        afterParse = (TextView)findViewById(R.id.afterParse);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teamName = extras.getString("teamName");
        }
        teamNameTV.setText(teamName);
        ParseHtml parseHtml = new ParseHtml(teamName);
        parseHtml.parse();
        afterParse.setText(MatchesHandler.nextTeams.toString());
    }
}
