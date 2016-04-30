package cnu.lecture;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.ClientProtocolException;
/**
 * Created by tchi on 2016. 4. 25..
 */
public class InGameSummonerQuerierTest {
    private InGameSummonerQuerier querier;
    private InSummonersGameInfo inSummonersGameInfo;
    
    @Before
    public void setup() {
        final String apiKey = "8242f154-342d-4b86-9642-dfa78cdb9d9c";
        GameParticipantListener dontCareListener = mock(GameParticipantListener.class);
        
        querier = new InGameSummonerQuerier(apiKey, dontCareListener);
        querier = mock(InGameSummonerQuerier.class);
        
        inSummonersGameInfo = new InSummonersGameInfo() {           
            @Override
            public InGameInfo getSummonersGameInfo(String summonerName)
                    throws UnsupportedEncodingException, IOException, ClientProtocolException {
                InGameInfo ingameInfo = mock(InGameInfo.class);
                return ingameInfo;
            }
        };
    }
    @Test
    public void shouldQuerierIdentifyGameKeyWhenSpecificSummonerNameIsGiven() throws Exception {
        final String summonerName;
        final String expectedGameKey = "4/bl4DC8HBir8w7bGHq6hvuHluBd+3xM";
        GIVEN: {
            summonerName = "akane24";
        }
        
        final String actualGameKey;
        WHEN: {
            when(querier.queryGameKey(summonerName)).thenReturn(expectedGameKey);
            actualGameKey = querier.queryGameKey(summonerName);
        }
        
        THEN: {
            assertThat(actualGameKey, is(expectedGameKey));
        }
    }
    
    @Test
    public void shouldQuerierReportMoreThan5Summoners() throws Exception{

        final int expectedNumOfSummernors;
        final InGameInfo inGameInfo;
        final String summonerName;
        GIVEN: {
            summonerName ="akane24";    
            querier.queryGameKey(summonerName);     
            expectedNumOfSummernors=4;
            inGameInfo=inSummonersGameInfo.getSummonersGameInfo(summonerName);
        }

        final int actualNumOfSummoners;
        WHEN: {
            when(querier.getParticipantNumber()).thenReturn(expectedNumOfSummernors);           
            actualNumOfSummoners =querier.getParticipantNumber();
        }

        THEN: {
            assertTrue(actualNumOfSummoners>=expectedNumOfSummernors);
        }
    }

}