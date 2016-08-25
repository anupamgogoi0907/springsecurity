package com.anupam;


import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class ClientApplication
{


    public static void main(String[] args)
    {
        OAuth2AccessToken token = restTemplate().getAccessToken();
        System.out.println(token.getValue());
    }


    public static OAuth2ProtectedResourceDetails resourceDetails()
    {
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();

        List<String> scopes = new ArrayList<String>();
        scopes.add("write");
        scopes.add("read");
        resource.setAccessTokenUri("http://localhost:8080/oauth/token");
        resource.setClientId("web");
        resource.setClientSecret("1234");
        resource.setGrantType("password");
        resource.setScope(scopes);

        resource.setUsername("admin");
        resource.setPassword("1234");
        return resource;
    }


    public static OAuth2RestOperations restTemplate()
    {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails(), new DefaultOAuth2ClientContext(atr));
        oAuth2RestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return oAuth2RestTemplate;
    }

}
