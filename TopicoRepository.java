package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TopicoRepository extends JpaRepository<Topico, Long>
{

    // procura na entidade 'Topico', o campo 'Curso',
    // que também é uma entidade, e pesquisa pelo Nome da entidade 'Curso'
    // List<Topico> findByCurso_Nome(String nomeCurso); (se tiver ambiguidade, pode colocar '_'
    Page<Topico> findByCursoNome(String nomeCurso, Pageable pageable);

    // exemplo utilizando query JPQL
    // @Query("SELECT t FROM t WHERE t.curso.nome = :nomeCurso")
    // List<Topico> carregaPorNomeCurso(@Param("nomeCurso") String nomeCurso);


}
