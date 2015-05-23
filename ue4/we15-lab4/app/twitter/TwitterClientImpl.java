package twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Author: Lukas
 * Date: 23.05.2015
 */
public class TwitterClientImpl implements ITwitterClient{

    private String consumerKeyStr = "GZ6tiy1XyB9W0P4xEJudQ";
    private String consumerSecretStr = "gaJDlW0vf7en46JwHAOkZsTHvtAiZ3QUd2mD1x26J9w";
    private String accessTokenStr = "1366513208-MutXEbBMAVOwrbFmZtj1r4Ih2vcoHGHE2207002";
    private String accessTokenSecretStr = "RMPWOePlus3xtURWRVnv1TgrjTyK7Zk33evp4KKyA";

    @Override
    public void publishUuid(TwitterStatusMessage message) throws Exception {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);

        AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecretStr);
        twitter.setOAuthAccessToken(accessToken);

        twitter.updateStatus(message.getTwitterPublicationString());
    }
}
