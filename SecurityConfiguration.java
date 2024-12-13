package br.com.alura.forum.config.security;

import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity  // habilita a segurança e bloqueia o acesso a api
@Configuration      // indica que esta classe é de 'configuração' e será acessada pelo springboot na inicializaçao
@Profile(value = {"prod","test"}) // só carrega este controller no ambiente de 'produção' ou 'teste'. no ambiente de desenvolvimento não será carregado
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Bean // indica que o método devolve um autenticationManager para ser injetado em outras opções do sistema
    protected AuthenticationManager authenticationManager() throws Exception
    {
        return super.authenticationManager();
    }

    // configurações de autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {

        /*
        // define o que é público e o que precisa ter controle de acesso
        http.authorizeRequests()
                // define as urls públicas, permitidas a 'todos', sem autenticação
                .antMatchers(HttpMethod.POST,"/auth").permitAll()                   // permite a url de login para 'todos'
                .antMatchers(HttpMethod.GET,"/topicos").permitAll()                 // permite GET para a url /topicos para qualquer pessoa
                .antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()            // em produção deve ficar bloqueado
                .antMatchers(HttpMethod.DELETE, "/topicos/*").hasRole("MODERADOR")  // só pode executar 'delete' na rota '/topicos/*' se for do perfil moderador
                // o restante o usuário precisa estar autenticado
                .anyRequest().authenticated()
                // .and().formLogin(); // habilita o formulário de autenticação do spring > utilizando jwt este código foi comentado pq não iremos mais autenticar e armazenar na 'sessão'
                // apagando esta linha não tem mais a página de login padrão do spring, perde também o Controller do Spring que fazia a página de autenticação
        .and().csrf().disable() // cross site request.. (ataque hacker em aplicações web - em aplicação com autenticação via swt pode ser desabilitado)
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // a aplicação será stateless, não criando sessão, porque será gerado token
        .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class); // antes da autenticação padrão, deve rodar a classe para pegar o token personalizado
         */

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/topicos").permitAll()
                .antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/topicos/*").hasRole("MODERADOR")
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);

    }

    // configurações de autenticação
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {

        // define como será o processo de autenticação do usuário

        // define qual classe é utilizada para 'pesquisa' do usuário
        // define o algoritmo de criptografia da senha
        auth.userDetailsService(autenticacaoService)
                .passwordEncoder(new BCryptPasswordEncoder());

    }

    // configurações de recursos estáticos (requisição js, css, imagens, etc)
    @Override
    public void configure(WebSecurity web) throws Exception
    {

        // ignore requisições do swagger
        web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");

    }

    /*
    public static void main(String[] args)
    {

        // mostra a senha para teste
        // $2a$10$WN2WBIlcrDkaOuTKzoLzXu41N0j7fNqsfuVZvNnlW727fVPG9ly3e
        System.out.println(new BCryptPasswordEncoder().encode("123456"));


    }
    */

}
