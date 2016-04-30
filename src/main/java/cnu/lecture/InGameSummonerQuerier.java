package cnu.lecture;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by tchi on 2016. 4. 25..
 */
public class InGameSummonerQuerier extends Querier {
   

    public InGameSummonerQuerier(String apiKey, GameParticipantListener listener) {
       super(apiKey,listener);
    }

    public String queryGameKey(String summonerName) throws IOException {
        InGameInfo gameInfo = getSummonersGameInfo(summonerName);
        
        if(hasParticipant(gameInfo)){
			Arrays.asList(gameInfo.getParticipants()).forEach((InGameInfo.Participant participant) -> {
				listener.player(participant.getSummonerName());
			});
			return getEncryptionKey(gameInfo);
		}
		return null;		
	}

	protected String getEncryptionKey(InGameInfo gameInfo) {
		return gameInfo.getObservers().getEncryptionKey();
	}

	protected boolean hasParticipant(InGameInfo gameInfo) {
		return gameInfo.getParticipants() !=null;
	}


}
