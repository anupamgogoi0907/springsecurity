package com.anupam.security;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * If the user is Authenticated, it issues a access_token. The access_token is
 * used to call every REST service.
 *
 * @author brisatc186.gogoi
 */
@Configuration
@EnableAuthorizationServer
public class AuthServer extends AuthorizationServerConfigurerAdapter
{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
    {
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
    {
//        clients.inMemory()
//                .withClient("web")
//                .secret("websecret")
//                .scopes("read", "write")
//                .accessTokenValiditySeconds(60 * 60 * 24)
//                .refreshTokenValiditySeconds(60 * 60 * 24 * 5)
//                .authorizedGrantTypes("password", "refresh_token", "client_credentials", "authorization_code");

//        clients.jdbc(dataSource)
//                .withClient("brisa")
//                .secret("1234")
//                .scopes("read", "write")
//                .accessTokenValiditySeconds(60 * 60 * 24)
//                .refreshTokenValiditySeconds(60 * 60 * 24 * 5)
//                .authorizedGrantTypes("password", "refresh_token", "client_credentials", "authorization_code");
        clients.jdbc(dataSource);
    }

    /**
     * We are providing a custom logic to generate access_token. Otherwise
     * Spring reuses previously generated key and we did not want it.
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore()
    {
        InMemoryTokenStore ts = new InMemoryTokenStore();
        ts.setAuthenticationKeyGenerator(new AuthenticationKeyGenerator()
        {

            @Override
            public String extractKey(OAuth2Authentication authentication)
            {
                String accessToken = UUID.randomUUID().toString();
                return accessToken;
            }
        });
        return ts;

    }
}
