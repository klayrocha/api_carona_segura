package br.com.caronasegura.controller;

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

import br.com.caronasegura.persist.GrupoRepository;
import br.com.caronasegura.persist.LocalRepository;
import br.com.caronasegura.persist.UsuarioRepository;
import br.com.caronasegura.persist.modelo.Grupo;
import br.com.caronasegura.persist.modelo.Local;
import br.com.caronasegura.persist.modelo.Usuario;
import br.com.caronasegura.persist.modelo.UsuarioGrupo;

@RestController
public class GrupoController {
	
	@Autowired
	GrupoRepository grupoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	LocalRepository localRepository;
	
	@CrossOrigin
	@RequestMapping(value = "/app/grupo/cadastro", method = RequestMethod.POST)
    public @ResponseBody Grupo saveGrupo(@RequestBody Grupo grupo) throws Exception {
		try {
			Usuario usuario = usuarioRepository.findOne(grupo.getIdUsuario());
			
			UsuarioGrupo usuGrup = new UsuarioGrupo();
			usuGrup.setUsuario(usuario);
			usuGrup.setGrupo(grupo);
			usuGrup.setIndicador_adm("S");
			grupo.getUsuarios().add(usuGrup);
			grupoRepository.save(grupo);
		}catch(DataIntegrityViolationException dvex){
			throw new Exception("Grupo já cadastrado !");
		}catch(ConstraintViolationException cve){
			throw new Exception("Grupo já cadastrado !");
		}catch(Exception ex){
			throw new Exception("Erro no Cadastro do Grupo, favor entrar em contato com o  administrador do sistema !");
		}
		return grupo;
	}
	
	
	
	@CrossOrigin
    @RequestMapping(value = "/app/grupo/{grupoId}", method = RequestMethod.GET)
    public @ResponseBody Grupo getGrupo(@PathVariable("grupoId") long grupoId) {
		Grupo grupo =  grupoRepository.findOne(grupoId);
    	return grupo;
    }
	
	
	@CrossOrigin
    @RequestMapping(value = "/app/grupos/{userId}",method = RequestMethod.GET)
    public @ResponseBody Iterable<Grupo> mapeamentosList(@PathVariable("userId") long userId) {
    	return grupoRepository.findByUsuariosIdUsuarioId(userId);
    }


	@CrossOrigin
	@RequestMapping(value = "/app/grupo/cadastro/usuario/{grupoId}/{telUsuario}", method = RequestMethod.POST)
    public @ResponseBody Grupo addUuarioGrupo(@PathVariable("grupoId") long grupoId, @PathVariable("telUsuario") String telUsuario) throws Exception {
		Grupo grupo = null;
		try {
			grupo = grupoRepository.findOne(grupoId);
			if(grupo == null){
				throw new Exception("Esse Grupo não foi cadastrado !");
			}
			String telefoneTratado = tratarNumeroTelefone(telUsuario,grupo);
			long telLong = Long.parseLong(telefoneTratado);
			
			Usuario usuario = usuarioRepository.findByTelefone(telLong);
			
			if(usuario == null){
				throw new Exception("Esse telefone não foi cadastrado !");
			}
			UsuarioGrupo usuGrup = new UsuarioGrupo();
			usuGrup.setUsuario(usuario);
			usuGrup.setGrupo(grupo);
			usuGrup.setIndicador_adm("N");
			grupo.getUsuarios().add(usuGrup);
			grupoRepository.save(grupo);
		}catch(DataIntegrityViolationException dvex){
			throw new Exception("Grupo já cadastrado !");
		}catch(ConstraintViolationException cve){
			throw new Exception("Grupo já cadastrado !");
		}catch(Exception ex){
			throw new Exception(ex.toString());
		}
		return grupo;
	}
	
	private static String tratarNumeroTelefone(String numeroTelefone, Grupo grupo) throws Exception {
		if(numeroTelefone.contains("+")){
			if(numeroTelefone.substring(0,3).equals("+55")){
				String numeroSem55 = tirarCaracter(numeroTelefone.substring(3,numeroTelefone.length()).trim());
				if(numeroSem55.length() == 10 || numeroSem55.length() == 11){
					return numeroSem55; 
				} else if(numeroSem55.length() == 9){
					for (UsuarioGrupo usuarioGrupo : grupo.getUsuarios()) {
						if(usuarioGrupo.getIndicador_adm().equals("S") && usuarioGrupo.getUsuario().getTelefone() != null){
							return usuarioGrupo.getUsuario().getTelefone().toString().substring(0,2)+numeroSem55;
						}
					}
				} else if(numeroSem55.length() == 13){
					return numeroSem55.substring(3,numeroSem55.length());
				} else {
					throw new Exception("Número inválido ");
				}
			}
		} else if(numeroTelefone.contains("(")){
			String ddd = numeroTelefone.substring(numeroTelefone.indexOf("(")+1,numeroTelefone.indexOf(")"));
			if(ddd.length() > 3){
				ddd = ddd.substring(ddd.length()-2,ddd.length());
			}
			String numero = numeroTelefone.substring(numeroTelefone.indexOf(")")+1,numeroTelefone.length());
			String numeroTratado = tirarCaracter(ddd.substring(1) + numero);
			if(numeroTratado.length() == 10 || numeroTratado.length() == 11){
				return numeroTratado;
			} else if(numeroTratado.length() == 9){
				for (UsuarioGrupo usuarioGrupo : grupo.getUsuarios()) {
					if(usuarioGrupo.getIndicador_adm().equals("S") && usuarioGrupo.getUsuario().getTelefone() != null){
						return usuarioGrupo.getUsuario().getTelefone().toString().substring(0,2)+numeroTratado;
					}
				}
			} else if(numeroTratado.length() == 13){
				return numeroTratado.substring(3,numeroTratado.length());
			} else {
				throw new Exception("Número inválido ");
			}
		} else {
			String numeroTratado = tirarCaracter(numeroTelefone.trim());
			if(numeroTratado.length() == 10 || numeroTratado.length() == 11){
				return numeroTratado;
			} else if(numeroTratado.length() == 9){
				for (UsuarioGrupo usuarioGrupo : grupo.getUsuarios()) {
					if(usuarioGrupo.getIndicador_adm().equals("S") && usuarioGrupo.getUsuario().getTelefone() != null){
						return usuarioGrupo.getUsuario().getTelefone().toString().substring(0,2)+numeroTratado;
					}
				}
			} else if(numeroTratado.length() == 13){
				return numeroTratado.substring(3,numeroTratado.length());
			} else if(numeroTratado.length() == 12){
				return numeroTratado.substring(1,numeroTratado.length());
			} else {
				throw new Exception("Número inválido ");
			}
		}
		return "";
	}
	
	private static String tirarCaracter(String numeroTelefone){
		return numeroTelefone
			   .replaceAll("\\(","")
			   .replaceAll("\\)","")
			   .replaceAll("-","")
			   .replaceAll(" ","");
	}
	
	@CrossOrigin
	@RequestMapping(value = "/app/grupo/cadastro/usuario/adm/{grupoId}/{userId}", method = RequestMethod.POST)
    public @ResponseBody Grupo addUuarioADMGrupo(@PathVariable("grupoId") long grupoId, @PathVariable("userId") long userId) throws Exception {
		Grupo grupo = null;
		try {
			Usuario usuario = usuarioRepository.findOne(userId);
			grupo = grupoRepository.findOne(grupoId);
			UsuarioGrupo usuGrup = new UsuarioGrupo();
			usuGrup.setUsuario(usuario);
			usuGrup.setGrupo(grupo);
			usuGrup.setIndicador_adm("S");
			grupo.getUsuarios().add(usuGrup);
			grupoRepository.save(grupo);
		}catch(DataIntegrityViolationException dvex){
			throw new Exception("Usuário já é administrador !");
		}catch(ConstraintViolationException cve){
			throw new Exception("Usuário já é administrador !");
		}catch(Exception ex){
			throw new Exception(ex.toString());
		}
		return grupo;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/app/localidade/cadastro", method = RequestMethod.POST)
    public @ResponseBody Local saveLocalidade(@RequestBody Local local) throws Exception {
		try {
			Grupo grupo = grupoRepository.findOne(local.getGrupo().getId());
			local.setGrupo(grupo);
			localRepository.save(local);
		}catch(DataIntegrityViolationException dvex){
			throw new Exception("Localidade já cadastrado !");
		}catch(ConstraintViolationException cve){
			throw new Exception("Localidade já cadastrado !");
		}catch(Exception ex){
			throw new Exception("Erro no Cadastro da Localidade, favor entrar em contato com o  administrador do sistema !");
		}
		return local;
	}
	
	
	
	@CrossOrigin
    @RequestMapping(value = "/app/locais/{grupoId}",method = RequestMethod.GET)
    public @ResponseBody Iterable<Local> recuperaLocais(@PathVariable("grupoId") long grupoId) {
    	return localRepository.findByGrupoId(grupoId);
    }
	
	
	@CrossOrigin
	@RequestMapping(value = "/app/grupo/excluir/usuario/{grupoId}/{usuarioId}", method = RequestMethod.POST)
    public @ResponseBody String removeUsuarioGrupo(@PathVariable("grupoId") long grupoId, @PathVariable("usuarioId") long usuarioId) throws Exception {
		String retorno = null;
		try {
			Grupo grupo = grupoRepository.findOne(grupoId);
			if(grupo.getUsuarios() != null && grupo.getUsuarios().size() > 0){
				for (UsuarioGrupo usuarioGrupo : grupo.getUsuarios()) {
					if(usuarioGrupo.getUsuario().getId() == usuarioId){
						grupo.getUsuarios().remove(usuarioGrupo);
						break;
					}
				}
			}
			grupoRepository.save(grupo);
		}catch(Exception ex){
			retorno = "Erro geral ao excluir"+ex.toString();
		}
		return retorno;
	}
	
	
	@CrossOrigin
	@RequestMapping(value = "/app/grupo/sair/{grupoId}/{usuarioId}", method = RequestMethod.POST)
    public @ResponseBody String sairGrupo(@PathVariable("grupoId") long grupoId, @PathVariable("usuarioId") long usuarioId) throws Exception {
		String retorno = null;
		try {
			Grupo grupo = grupoRepository.findOne(grupoId);
			if(grupo.getUsuarios() != null && grupo.getUsuarios().size() > 0){
				for (UsuarioGrupo usuarioGrupo : grupo.getUsuarios()) {
					if(usuarioGrupo.getUsuario().getId() == usuarioId){
						grupo.getUsuarios().remove(usuarioGrupo);
						break;
					}
				}
			}
			grupoRepository.save(grupo);
			
			if(grupo.getUsuarios() != null && grupo.getUsuarios().size() > 0){
				for (UsuarioGrupo usuarioGrupo : grupo.getUsuarios()) {
					usuarioGrupo.setIndicador_adm("S");
					break;
				}
			}
			grupoRepository.save(grupo);
			retorno = "OK";
		}catch(Exception ex){
			retorno = "Erro geral ao excluir"+ex.toString();
		}
		return retorno;
	}
	
	
	@CrossOrigin
	@RequestMapping(value = "/app/grupo/cadastro/usuarios/{grupoId}/{telefonesArroba}", method = RequestMethod.POST)
    public @ResponseBody String addUuariosGrupo(@PathVariable("grupoId") long grupoId, @PathVariable("telefonesArroba") String telefonesArroba) throws Exception {
		String retorno = "";
		try {
			Grupo grupo = grupoRepository.findOne(grupoId);
			if(grupo == null){
				throw new Exception("Esse Grupo não foi cadastrado !");
			}
			String telefones [] = telefonesArroba.split("@");
			for (int i = 0; i < telefones.length; i++) {
				if(telefones[i] != null && !telefones[i].trim().equals("")){
					Usuario usuario = usuarioRepository.findByTelefone(Long.parseLong(tratarNumeroTelefone(tirarPercentVinte(telefones[i]),grupo)));
					if(usuario == null){
						retorno += ""+telefones[i]+";";
					} else {
						UsuarioGrupo usuGrup = new UsuarioGrupo();
						usuGrup.setUsuario(usuario);
						usuGrup.setGrupo(grupo);
						usuGrup.setIndicador_adm("N");
						grupo.getUsuarios().add(usuGrup);
						grupoRepository.save(grupo);
					}
				}
			}
		}catch(DataIntegrityViolationException dvex){
			throw new Exception("Grupo já cadastrado !");
		}catch(ConstraintViolationException cve){
			throw new Exception("Grupo já cadastrado !");
		}catch(Exception ex){
			throw new Exception(ex.toString());
		}
		if(retorno.trim().equals("")){
			retorno = "OK";
		}
		return retorno;
	}
	
	private static String tirarPercentVinte(String telefone){
		return telefone.replaceAll("%20","");
	}
	
}
