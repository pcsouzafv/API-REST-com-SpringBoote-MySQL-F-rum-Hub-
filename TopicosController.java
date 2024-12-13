package br.com.alura.forum.controller;

import br.com.alura.forum.controller.form.AtualizaTopicoForm;
import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController
{

    // utilizar cache em tabelas que são pouco atualizadas
    private final String CACHE_LISTA_TOPICOS = "ListaDeTopicos";

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository  cursoRepository;

    @GetMapping
    @Cacheable(value = CACHE_LISTA_TOPICOS) // habilita o cache para este método
    // http://localhost:8080/topicos?nomeCurso=Spring Boot
    // http://localhost:8080/topicos?page=0&size=5&sort=id,asc
    // http://localhost:8080/topicos?page=0&size=5&sort=mensagem,asc&sort=dataCriacao,desc
    public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
                                 // @RequestParam int pagina,
                                 // @RequestParam int qtd,
                                 // @RequestParam String ordenacao
                                 @PageableDefault(sort="id", direction = Sort.Direction.DESC, page=0, size=10) Pageable paginacao) // se não definir o parametro, ordena o id decrescente
    {

        // Pageable paginacao = PageRequest.of(pagina, qtd, Sort.Direction.ASC, ordenacao);
        // http://localhost:8080/topicos?pagina=0&qtd=10&ordenacao=dataCriacao

        if (nomeCurso == null)
        {
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDto.converter(topicos);
        }
        else
        {
            Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
            return TopicoDto.converter(topicos);
        }

    }

    @PostMapping
    @CacheEvict(value = CACHE_LISTA_TOPICOS, allEntries = true) // quando incluir, limpa o cache para que seja atualizado
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder)
    {

        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        // resposta '201'
        // devolve ao cliente a localização do recurso e o próprio recurso no body da resposta
        return ResponseEntity.created(uri).body(new TopicoDto(topico));

    }

    @GetMapping("/{id}")
    // http://localhost:8080/topicos/2
    public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id)
    {

        Optional<Topico> topico = topicoRepository.findById(id);

        if (topico.isPresent())
        {
            return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
        }
        else
        {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/{id}")
    @CacheEvict(value = CACHE_LISTA_TOPICOS, allEntries = true) // quando atualizar, limpa o cache para que seja atualizado
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id,
                                               @RequestBody @Valid AtualizaTopicoForm form)
    {

        Optional<Topico> optional = topicoRepository.findById(id);

        if (optional.isPresent())
        {

           Topico topico = form.atualizar(id, topicoRepository);

           topicoRepository.save(topico);

           return ResponseEntity.ok(new TopicoDto(topico));

        }
        else
        {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = CACHE_LISTA_TOPICOS, allEntries = true) // quando excluir, limpa o cache para que seja atualizado
    public ResponseEntity<Void> remover(@PathVariable Long id)
    {

        Optional<Topico> optional = topicoRepository.findById(id);

        if (optional.isPresent())
        {

            topicoRepository.deleteById(id);

            return ResponseEntity.ok().build();

        }
        else
        {
            return ResponseEntity.notFound().build();
        }

    }

}
