package br.com.alura.forum.config.security;




import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableWebSecurity  // habilita a segurança e bloqueia o acesso a api
@Configuration      // indica que esta classe é de 'configuração' e será acessada pelo springboot na inicializaçao
@Profile("dev")     // esta classe será carregada se o profile ativo for 'dev'
public class DevSecurityConfiguration extends WebSecurityConfigurerAdapter
{

    // no ambiente de 'desenvolvimento', todas as urls estão liberadas
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {

        // define o que é público e o que precisa ter controle de acesso
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .and().csrf().disable();

    }

}
