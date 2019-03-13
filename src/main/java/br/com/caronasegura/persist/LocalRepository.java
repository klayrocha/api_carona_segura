package br.com.caronasegura.persist;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.caronasegura.persist.modelo.Local;

public interface LocalRepository extends PagingAndSortingRepository<Local, Long>{

	public Iterable<Local> findByGrupoId(Long idGrupo);
}
