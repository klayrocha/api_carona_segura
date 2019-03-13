package br.com.caronasegura.persist;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.caronasegura.persist.modelo.Grupo;

public interface GrupoRepository extends PagingAndSortingRepository<Grupo, Long>{
	
	
	public Iterable<Grupo> findByUsuariosIdUsuarioId(Long idUser);
	
}
