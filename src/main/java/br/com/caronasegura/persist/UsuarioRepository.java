package br.com.caronasegura.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.caronasegura.persist.modelo.Usuario;


public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long>{
	
	Usuario findByEmailAndSenha(String email, String senha);
	Usuario findByEmail(String email);
	Usuario findByTelefone(long telefone);
	Iterable<Usuario> findByGruposIdGrupoId(Long idGrupo);
	Page<Usuario> findByGruposIdGrupoIdOrderByNome(Long idGrupo, Pageable pageable);
}
