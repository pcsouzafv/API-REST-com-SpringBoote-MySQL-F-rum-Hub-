package br.com.alura.forum.controller;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controller.dto.TokenDto;
import br.com.alura.forum.controller.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Profile(value={"prod","test"}) // só carrega este controller no ambiente de 'produção' ou 'teste'. no ambiente de desenvolvimento não será carregado
public class AutenticacaoController
{

    @Autowired
    private AuthenticationManager authManager; // esta classe não é criada automaticamente pelo spring, então terá que ser criada na classe SecurityConfigurations

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form)
    {

        // System.out.println(form.getEmail());
        // System.out.println(form.getSenha());

        UsernamePasswordAuthenticationToken dadosLogin = form.converter();

        try
        {
            Authentication authentication = authManager.authenticate(dadosLogin);

            String token = tokenService.gerarToken(authentication);

            System.out.println(token);

            return ResponseEntity.ok(new TokenDto(token, "Bearer"));

        }
        catch (AuthenticationException e)
        {
            return ResponseEntity.badRequest().build();
        }

    }






}