package br.com.caronasegura.persist;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.caronasegura.persist.modelo.Mensagem;

public interface MensagemRepository extends PagingAndSortingRepository<Mensagem, Long>{

	
}
