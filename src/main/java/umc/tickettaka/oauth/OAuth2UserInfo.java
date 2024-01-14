package umc.tickettaka.oauth;

import java.util.Map;

/**
 * this is the class for the response data from oauth2 such as email or something.
 */
public class OAuth2UserInfo {
    private Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

}
