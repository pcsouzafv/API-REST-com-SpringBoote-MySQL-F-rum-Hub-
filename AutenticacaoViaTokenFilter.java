package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// filtro do spring que é chamado uma única vez a cada requisição
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter
{

    private TokenService tokenService;

    private UsuarioRepository repository;

    public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository repository)
    {
        this.tokenService = tokenService;
        this.repository   = repository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException
    {

        String token = recuperarToken(request);

        // System.out.println(token);
        boolean valido = tokenService.isTokenValido(token);

        System.out.println("token ok > " + valido);

        // verifica se o token é válido
        if (valido)
        {
            autenticarCliente(token);
        }

        // após realizar o filtro e tiver autenticado
        // continua o processamento
        filterChain.doFilter(request, response);

    }

    private String recuperarToken(HttpServletRequest request)
    {

        String token = request.getHeader("Authorization");

        // valida o 'Authorization'
        if (token == null || token.isEmpty() || !token.startsWith("Bearer" ))
        {
            return null;
        }

        return token.substring(7, token.length());

    }

    private void autenticarCliente(String token)
    {

        // busca o id do usuário pelo token enviado
        Long idUsuario = tokenService.getIdUsuario(token);

        // busca o usuário cadastrado na base de dados
        Usuario usuario = repository.findById(idUsuario).get();

        // cria o objeto para autenticação
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

        // avisa para o spring que o usuário está autenticado
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }


}
