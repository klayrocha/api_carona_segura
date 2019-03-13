package br.com.caronasegura.persist;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import br.com.caronasegura.persist.modelo.Carona;

public interface CaronaRepository extends PagingAndSortingRepository<Carona, Long>{
	
	@Query(" SELECT c.id, c.origem , c.destino, c.grupo, c.data,"
		  +" c.horaSaida , c.quantidadeVagas, c.observacao,"
		  +" c.formaPagamento"
		  +" FROM Carona c where c.grupo.id = :idGrupo "
		  +" AND c.data >= CURDATE() order by c.data desc ")
	public Iterable<Carona> findByGrupoId(@Param("idGrupo") long idGrupo);
	public Iterable<Carona> findByGrupoIdAndOrigemId(Long idGrupo, Long idOrigem);
	public Iterable<Carona> findByGrupoIdAndOrigemIdAndDestinoId(Long idGrupo,Long idOrigem, Long idDestino);
	public Iterable<Carona> findByGrupoIdAndOrigemIdAndData(Long idGrupo, Long idOrigem,Date data);
	public Iterable<Carona> findByGrupoIdAndDestinoId(Long idGrupo, Long idDestino);
	public Iterable<Carona> findByGrupoIdAndDestinoIdAndData(Long idGrupo, Long idDestino,Date data);
	public Iterable<Carona> findByGrupoIdAndData(Long idGrupo,Date data);
	public Iterable<Carona> findByGrupoIdAndOrigemIdAndDestinoIdAndData(Long idGrupo,Long idOrigem, Long idDestino,Date data);
	public Iterable<Carona> findByUsuariosIdUsuarioIdOrderByDataDesc(Long idUser);
	
}
