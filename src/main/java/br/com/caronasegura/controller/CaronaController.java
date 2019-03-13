package br.com.caronasegura.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.caronasegura.persist.CaronaRepository;
import br.com.caronasegura.persist.GrupoRepository;
import br.com.caronasegura.persist.LocalRepository;
import br.com.caronasegura.persist.MensagemRepository;
import br.com.caronasegura.persist.UsuarioRepository;
import br.com.caronasegura.persist.modelo.Carona;
import br.com.caronasegura.persist.modelo.Grupo;
import br.com.caronasegura.persist.modelo.Local;
import br.com.caronasegura.persist.modelo.Mensagem;
import br.com.caronasegura.persist.modelo.Usuario;
import br.com.caronasegura.persist.modelo.UsuarioCarona;


@RestController
public class CaronaController {
	
	
	@Autowired
	GrupoRepository grupoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	LocalRepository localRepository;
	
	@Autowired
	CaronaRepository caronaRepository;
	
	@Autowired
	MensagemRepository mensagemRepository;
	
	
	@CrossOrigin
    @RequestMapping(value = "/app/carona/{caronaId}", method = RequestMethod.GET)
    public @ResponseBody Carona getCarona(@PathVariable("caronaId") long caronaId) {
		Carona carona =  caronaRepository.findOne(caronaId);
    	return carona;
    }
	
	@CrossOrigin
	@RequestMapping(value = "/app/carona/cadastro", method = RequestMethod.POST)
    public @ResponseBody Carona saveCarona(@RequestBody Carona carona) throws Exception {
		try {
			Local localOrigem = localRepository.findOne(carona.getOrigem().getId());
			Local localDestino = localRepository.findOne(carona.getDestino().getId());
			Grupo grupo = grupoRepository.findOne(carona.getGrupo().getId());
			carona.setOrigem(localOrigem);
			carona.setDestino(localDestino);
			carona.setGrupo(grupo);
			caronaRepository.save(carona);
			
			Usuario usuario = usuarioRepository.findOne(Long.parseLong(carona.getIdUsuarioCriador()));
			UsuarioCarona usuCar = new UsuarioCarona();
			usuCar.setUsuario(usuario);
			usuCar.setCarona(carona);
			usuCar.setIndicador_criador("S");
			carona.getUsuarios().add(usuCar);
			caronaRepository.save(carona);
			
		}catch(DataIntegrityViolationException dvex){
			throw new Exception("Carona já cadastrado !");
		}catch(ConstraintViolationException cve){
			throw new Exception("Carona já cadastrado !");
		}catch(Exception ex){
			throw new Exception("Erro no Cadastro da Carona, favor entrar em contato com o  administrador do sistema !");
		}
		return carona;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/app/carona/alterar", method = RequestMethod.POST)
    public @ResponseBody Carona alterarCarona(@RequestBody Carona carona) throws Exception {
		try {
			Carona caronaBD = caronaRepository.findOne(carona.getId());
			
			Local localOrigem = localRepository.findOne(carona.getOrigem().getId());
			Local localDestino = localRepository.findOne(carona.getDestino().getId());
			Grupo grupo = grupoRepository.findOne(carona.getGrupo().getId());
			caronaBD.setOrigem(localOrigem);
			caronaBD.setDestino(localDestino);
			caronaBD.setGrupo(grupo);
			caronaBD.setData(carona.getData());
			caronaBD.setHoraSaida(carona.getHoraSaida());
			caronaBD.setQuantidadeVagas(carona.getQuantidadeVagas());
			caronaBD.setObservacao(carona.getObservacao());
			caronaBD.setFormaPagamento(carona.getFormaPagamento());
			caronaRepository.save(caronaBD);
			
		}catch(DataIntegrityViolationException dvex){
			throw new Exception("Carona já cadastrado !");
		}catch(ConstraintViolationException cve){
			throw new Exception("Carona já cadastrado !");
		}catch(Exception ex){
			throw new Exception("Erro no Cadastro da Carona, favor entrar em contato com o  administrador do sistema !");
		}
		return carona;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/app/carona/excluir/{caronaId}", method = RequestMethod.POST)
    public @ResponseBody String excluirCarona(@PathVariable("caronaId") long caronaId) throws Exception {
		String retorno = null;
		try {
			Carona carona = caronaRepository.findOne(caronaId);
			if(carona.getUsuarios() != null && carona.getUsuarios().size() > 0){
				for (int i =0; i < carona.getUsuarios().size(); i++) {
					carona.getUsuarios().remove(i);
				}
			}
			caronaRepository.save(carona);
			Carona caronaExcluir = caronaRepository.findOne(caronaId);
			caronaRepository.delete(caronaExcluir);
			retorno = "OK";
		}catch(Exception ex){
			retorno = "Erro geral ao excluir"+ex.toString();
		}
		return retorno;
	}
	
	
	@SuppressWarnings("rawtypes")
	@CrossOrigin
    @RequestMapping(value = "/app/caronas/{grupoId}",method = RequestMethod.GET)
    public @ResponseBody List<Carona> todasCaronasGrupo(@PathVariable("grupoId") long grupoId) {
		Iterable<Carona> caronas =  (Iterable<Carona>) caronaRepository.findByGrupoId(grupoId);
		List<Carona> caronasRetorno = new ArrayList<>();
		for (Iterator iterator = caronas.iterator(); iterator.hasNext();) {
				Object object[] = (Object[]) iterator.next();
				Carona carona = new Carona();
				carona.setId(Long.parseLong(object[0].toString()));
				carona.setOrigem((Local)object[1]);
				carona.setDestino((Local)object[2]);
				carona.setGrupo((Grupo)object[3]);
				carona.setData((Date)object[4]);
				carona.setHoraSaida (object[5].toString());
				carona.setQuantidadeVagas(Integer.parseInt(object[6].toString()));
				carona.setObservacao(object[7].toString());
				carona.setFormaPagamento(object[8].toString());
				
				caronasRetorno.add(carona);
		}	
		return caronasRetorno;
    }
	
	@CrossOrigin
    @RequestMapping(value = "/app/caronas/usuario/{usuarioId}",method = RequestMethod.GET)
    public @ResponseBody Iterable<Carona> todasCaronasUsuario(@PathVariable("usuarioId") long usuarioId) {
		return caronaRepository.findByUsuariosIdUsuarioIdOrderByDataDesc(usuarioId);
	}
	
	
	@CrossOrigin
    @RequestMapping(value = "/app/caronas/parametros/{grupoId}/{origemId}/{destinoId}/{data}",method = RequestMethod.GET)
    public @ResponseBody Iterable<Carona> buscaCaronasPorParametros(@PathVariable("grupoId") long grupoId, @PathVariable("origemId") long origemId,
    		@PathVariable("destinoId") long destinoId,@PathVariable("data") String data)  throws Exception {
		
		Iterable<Carona> listaRetorno = null;
		
		try{	
			Date dataPesquisar = null;
			
			if(data.equals("vazio")){
				data = null;
			}
			if(data != null){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				dataPesquisar = format.parse(data);
			}
			
			if(grupoId != 0){
				if(origemId != 0 &&	destinoId == 0 && data == null){
					listaRetorno = caronaRepository.findByGrupoIdAndOrigemId(grupoId,origemId);
				} else if(origemId != 0 && destinoId != 0 &&  data == null){
					listaRetorno = caronaRepository.findByGrupoIdAndOrigemIdAndDestinoId(grupoId,origemId,destinoId);
				} else if(origemId != 0 && destinoId == 0 && data != null){
					listaRetorno = caronaRepository.findByGrupoIdAndOrigemIdAndData(grupoId,origemId,dataPesquisar);
				} else if(destinoId != 0 && origemId == 0 && data == null){
					listaRetorno = caronaRepository.findByGrupoIdAndDestinoId(grupoId, destinoId);
				} else if(destinoId != 0 && origemId == 0 && data != null){
					listaRetorno = caronaRepository.findByGrupoIdAndDestinoIdAndData(grupoId,destinoId,dataPesquisar);
				} else if(destinoId == 0 && origemId == 0 && data != null){
					listaRetorno = caronaRepository.findByGrupoIdAndData(grupoId,dataPesquisar);
				} else if(destinoId != 0 && origemId != 0 && data != null){
					listaRetorno = caronaRepository.findByGrupoIdAndOrigemIdAndDestinoIdAndData(grupoId,origemId,destinoId,dataPesquisar);
				}
			}
		}catch(Exception ex){
			throw new Exception("Erro no Cadastro da Carona, favor entrar em contato com o  administrador do sistema !");
		}
    	return listaRetorno;
    }
	
	
	
	@CrossOrigin
	@RequestMapping(value = "/app/caronas/cadastro/usuario/{caronaId}/{usuarioId}", method = RequestMethod.POST)
    public @ResponseBody Carona addUuarioCarona(@PathVariable("caronaId") long caronaId, @PathVariable("usuarioId") long usuarioId) throws Exception {
		Carona carona = null;
		try {
			Usuario usuario = usuarioRepository.findOne(usuarioId);
			carona = caronaRepository.findOne(caronaId);
			UsuarioCarona usuCar = new UsuarioCarona();
			usuCar.setUsuario(usuario);
			usuCar.setCarona(carona);
			usuCar.setIndicador_criador("N");
			carona.getUsuarios().add(usuCar);
			caronaRepository.save(carona);
		}catch(DataIntegrityViolationException dvex){
			throw new Exception("Erro ao reservar Carona");
		}catch(ConstraintViolationException cve){
			throw new Exception("Erro ao reservar Carona");
		}catch(Exception ex){
			throw new Exception("Erro ao reservar Carona"+ex.toString());
		}
		return carona;
	}
	
	
	@CrossOrigin
	@RequestMapping(value = "/app/caronas/excluir/usuario/{caronaId}/{usuarioId}", method = RequestMethod.POST)
    public @ResponseBody String removeUsuarioCarona(@PathVariable("caronaId") long caronaId, @PathVariable("usuarioId") long usuarioId) throws Exception {
		String retorno = null;
		try {
			Carona carona = caronaRepository.findOne(caronaId);
			if(carona.getUsuarios() != null && carona.getUsuarios().size() > 0){
				for (UsuarioCarona usuarioCarona : carona.getUsuarios()) {
					if(usuarioCarona.getUsuario().getId() == usuarioId){
						carona.getUsuarios().remove(usuarioCarona);
						break;
					}
				}
			}
			caronaRepository.save(carona);
		}catch(Exception ex){
			retorno = "Erro geral ao excluir"+ex.toString();
		}
		return retorno;
	}
	
	
	@SuppressWarnings("rawtypes")
	@CrossOrigin
    @RequestMapping(value = "/app/caronas/usuarios/{caronaId}",method = RequestMethod.GET)
    public @ResponseBody List<Usuario> consultaUsuariosCarona(@PathVariable("caronaId") long caronaId) {
		List<Usuario> usuariosRet = new ArrayList<Usuario>();
		Carona carona = caronaRepository.findOne(caronaId);
		for (Iterator iterator = carona.getUsuarios().iterator(); iterator.hasNext();) {
			UsuarioCarona usuarioCarona = (UsuarioCarona) iterator.next();
			usuarioCarona.getUsuario().setSenha(null);
			usuariosRet.add(usuarioCarona.getUsuario());
		}
		return usuariosRet;
    }
	
	
	@CrossOrigin
	@RequestMapping(value = "/app/carona/cadastro/mensagem/", method = RequestMethod.POST)
    public @ResponseBody Mensagem saveMensagem(@RequestBody Mensagem mensagem) throws Exception {
		try {
			Carona carona = caronaRepository.findOne(mensagem.getCarona().getId());
			Usuario usuario = usuarioRepository.findOne(mensagem.getUsuario().getId());
			mensagem.setCarona(carona);
			mensagem.setUsuario(usuario);
			mensagemRepository.save(mensagem);
		}catch(DataIntegrityViolationException dvex){
			throw new Exception("Carona já cadastrado !");
		}catch(ConstraintViolationException cve){
			throw new Exception("Carona já cadastrado !");
		}catch(Exception ex){
			throw new Exception("Erro no Cadastro da Carona, favor entrar em contato com o  administrador do sistema !");
		}
		return mensagem;
	}

}
