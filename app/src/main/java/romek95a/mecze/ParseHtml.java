package romek95a.mecze;

import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ParseHtml {
    ParseHtml(String teamName){
        this.teamName = teamName;
    }

    static String teamName;
    public ElementsBefore parse(){
        AsyncParse asyncParse = new AsyncParse();
        try {
            return asyncParse.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    static class AsyncParse extends AsyncTask<String, String, ElementsBefore> {
        @Override
        protected ElementsBefore doInBackground(String... strings) {
            try {
                String link = "http://www.google.com/search?q=" + teamName;
                String classFirstMatch = "div.abhAW";
                String classNextMatches = "table.ml-bs-u";
                Document doc = Jsoup.connect(link).get();

                Elements firstMatch = doc.select(classFirstMatch);
                Elements nextMatches = doc.select(classNextMatches);
                return new ElementsBefore(firstMatch, nextMatches);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public Map<String, String> firstMatchFinalInfo(Elements firstMatch){
        Map<String, String> firstMatchesMap = new HashMap<>();

        String classFirstLeagueTimeEnd= "div.imso_mh__stts-l";
        String classFirstTeamsScore = "div.imso_mh__tm-a-sts";

        String classFirstLeagueTime1 = "div.imso-hide-overflow";
        String classFirstLeagueTime2 = "span.imso-hide-overflow";
        String classFirstDuring = "span.vVvqnf";
        String classFirstTeamHomeName = "div.imso_mh__first-tn-ed";
        String classFirstTeamAwayName = "div.imso_mh__second-tn-ed";
        String classFirstTeamHomeScore = "div.imso_mh__l-tm-sc";
        String classFirstTeamAwayScore = "div.imso_mh__r-tm-sc";
        String classFirstMatchScorers = "div.imso_gs__gs-cont-ed";
        String classFirstMatchTeamHomeScorers = "div.imso_gs__left-team";
        String classFirstMatchTeamAwayScorers = "div.imso_gs__right-team";

        String firstMatchLeagueTime1 = firstMatch.select(classFirstLeagueTimeEnd).select(classFirstLeagueTime1).text();
        String firstMatchLeagueTime2 = firstMatch.select(classFirstLeagueTimeEnd).select(classFirstLeagueTime2).text();
        String firstMatchDuring = firstMatch.select(classFirstLeagueTimeEnd).select(classFirstDuring).text();
        String firstMatchTeamHomeName = firstMatch.select(classFirstTeamsScore).select(classFirstTeamHomeName).text();
        firstMatchTeamHomeName = firstMatchTeamHomeName.replace("-", "");
        String firstMatchTeamAwayName = firstMatch.select(classFirstTeamsScore).select(classFirstTeamAwayName).text();
        firstMatchTeamAwayName = firstMatchTeamAwayName.replace("-", "");
        String firstMatchTeamHomeScore = firstMatch.select(classFirstTeamsScore).select(classFirstTeamHomeScore).text();
        String firstMatchTeamAwayScore = firstMatch.select(classFirstTeamsScore).select(classFirstTeamAwayScore).text();

        String firstMatchTeamHomeScorers = firstMatch.select(classFirstMatchScorers).select(classFirstMatchTeamHomeScorers).text();
        String firstMatchTeamAwayScorers = firstMatch.select(classFirstMatchScorers).select(classFirstMatchTeamAwayScorers).text();
        firstMatchTeamHomeScorers = firstMatchTeamHomeScorers.replace(", ","\n");
        firstMatchTeamHomeScorers = firstMatchTeamHomeScorers.replace("+'","");
        firstMatchTeamAwayScorers = firstMatchTeamAwayScorers.replace(", ","\n");
        firstMatchTeamAwayScorers = firstMatchTeamAwayScorers.replace("+'","");

        if(!firstMatchLeagueTime1.equals(""))
            firstMatchesMap.put("leagueTime", firstMatchLeagueTime1);
        if(!firstMatchLeagueTime2.equals(""))
            firstMatchesMap.put("leagueTime", firstMatchLeagueTime2);
        firstMatchesMap.put("teamHomeName", firstMatchTeamHomeName);
        firstMatchesMap.put("teamAwayName", firstMatchTeamAwayName);
        firstMatchesMap.put("teamHomeScore", firstMatchTeamHomeScore);
        firstMatchesMap.put("teamAwayScore", firstMatchTeamAwayScore);
        firstMatchesMap.put("during", firstMatchDuring);
        firstMatchesMap.put("homeTeamScorers", firstMatchTeamHomeScorers);
        firstMatchesMap.put("awayTeamScorers", firstMatchTeamAwayScorers);

        return firstMatchesMap;
    }
    public List<String> nextMatchesFinalInfo(Elements elements){
        List<String>  singleMatchesList = new ArrayList<>();
        String classSingleCell = "td.ZhlJRc";
        String classSingleMatchScore = "div.imspo_mt__tt-w";
        String classDateNextMatches = "div.imspo_mt__ns-pm-s";
        String classDatePreviousMatches = "div.imspo_mt__cmd";
        String classLeague = "div.imspo_mt__lg-st-co";
        Elements singleCells = elements.select(classSingleCell);
        for(Element el : singleCells){
            String singleScore = el.select(classSingleMatchScore).text();
            String singleLeague = el.select(classLeague).text();
            if(!singleLeague.equals("")){
                singleLeague = singleLeague + "\n";
            }
            String singleDate = el.select(classDateNextMatches).text();
            if(singleDate.equals("")){
                singleDate = el.select(classDatePreviousMatches).text();
            }
            if((Character.isDigit(singleScore.charAt(0)))){
                singleScore = singleScore.replace("---", Character.toString(singleScore.charAt(0)) + " -");
                singleScore = singleScore.substring(2,singleScore.length()-4);
            }
            else{
                singleScore = singleScore.replace("---", " - ");
                singleScore = singleScore.substring(0,singleScore.length()-2);
            }
            String singleFinalInfo = singleLeague + singleDate + "\n" + singleScore;
            singleMatchesList.add(singleFinalInfo);
        }

        return singleMatchesList;
    }
}
