package br.com.caronasegura.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.caronasegura.persist.UsuarioRepository;
import br.com.caronasegura.persist.modelo.Usuario;
import br.com.caronasegura.persist.modelo.UsuarioGrupo;
import br.com.caronasegura.security.CustomPasswordEncoder;
import br.com.caronasegura.util.EnviarEmail;
import br.com.caronasegura.util.Mensagem;

@RestController
public class UsuarioController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	EnviarEmail enviarEmail;
	
	Logger looger = Logger.getLogger(UsuarioController.class.getName());
	
	@CrossOrigin
	@RequestMapping(value = "/app/usuario/cadastro", method = RequestMethod.POST)
    public @ResponseBody Usuario saveUsuario(@RequestBody Usuario usuario) throws Exception {
		try {
			if(!isTelefone(usuario.getTelefone().toString())){
				throw new Exception("Esse telefone não é Celular !");
			}
			usuario.setDataCadastro(new Date());
			CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();
			usuario.setSenha(customPasswordEncoder.encode(usuario.getSenha()));
			usuarioRepository.save(usuario);
		}catch(DataIntegrityViolationException dvex){
			throw new Exception("Email já cadastrado !");
		}catch(ConstraintViolationException cve){
			throw new Exception("Email já cadastrado !");
		}catch(Exception ex){
			throw new Exception(ex.toString());
		}
		return usuario;
	}
	
	public static boolean isTelefone(String numeroTelefone) {
		if(numeroTelefone.length() == 10){
			if(numeroTelefone.substring(2,3).equals("2") ||
			   numeroTelefone.substring(2,3).equals("3") ||
			   numeroTelefone.substring(2,3).equals("4") ||
			   numeroTelefone.substring(2,3).equals("5")){
			   return false;
			}
		} else if(numeroTelefone.length() == 11){
			if(numeroTelefone.substring(3,4).equals("2") ||
			   numeroTelefone.substring(3,4).equals("3") ||
			   numeroTelefone.substring(3,4).equals("4") ||
			   numeroTelefone.substring(3,4).equals("5")){
			   return false;
			}
		}
		return true;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/app/usuario/alterar", method = RequestMethod.POST)
    public @ResponseBody Usuario updateUsuario(@RequestBody Usuario usuario) throws Exception {
		try {
			Usuario usuarioBD = usuarioRepository.findOne(usuario.getId());
			usuarioBD.setNome(usuario.getNome());
			usuarioBD.setEmail(usuario.getEmail());
			usuarioBD.setTelefone(usuario.getTelefone());
			usuarioBD.setFoto(usuario.getFoto());
			usuarioRepository.save(usuarioBD);
		}catch(DataIntegrityViolationException dvex){
			throw new Exception("Email já cadastrado !");
		}catch(ConstraintViolationException cve){
			throw new Exception("Email já cadastrado !");
		}catch(Exception ex){
			throw new Exception("Erro no Cadastro do Usuário, favor entrar em contato com o  administrador do sistema !");
		}
		return usuario;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/app/usuario/login", method = RequestMethod.POST)
    public @ResponseBody Usuario loginTreinador(@RequestBody Usuario usuario) throws Exception {
		try {
			CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();
			usuario.setSenha(customPasswordEncoder.encode(usuario.getSenha()));
			usuario = usuarioRepository.findByEmailAndSenha(usuario.getEmail(),usuario.getSenha());
		}catch(Exception ex){
			throw new Exception("Erro no Cadastro do Atleta, favor entrar em contato com o  administrador do sistema !");
		}
		if(usuario == null || usuario.getDataCadastro() == null){
			throw new Exception("Email ou senha inválido !");
		}
		return usuario;
	}
	
	@CrossOrigin
    @RequestMapping(value = "/app/usuario/{usuId}", method = RequestMethod.GET)
    public @ResponseBody Usuario getUsuario(@PathVariable("usuId") long usuId) {
		Usuario usuario =  usuarioRepository.findOne(usuId);
    	return usuario;
    }
	
	
	@SuppressWarnings("rawtypes")
	@CrossOrigin
    @RequestMapping(value = "/app/usuarios/{grupoId}",method = RequestMethod.GET)
    public @ResponseBody Iterable<Usuario> mapeamentosList(@PathVariable("grupoId") long grupoId) {
		Iterable<Usuario> usuarios = usuarioRepository.findByGruposIdGrupoId(grupoId);
		for (Iterator iterator = usuarios.iterator(); iterator.hasNext();) {
			Usuario usuario = (Usuario) iterator.next();
			usuario.setSenha(null);
			usuario.setAdm(getAdm(grupoId, usuario.getGrupos()));
		}
		return usuarios;
    }
	
	public String getAdm(long idGrupo, List<UsuarioGrupo> grupos){
		if(grupos != null && grupos.size() > 0){
			for (UsuarioGrupo usuGrupo : grupos) {
				if(usuGrupo.getGrupo().getId().longValue() ==  idGrupo){
					return usuGrupo.getIndicador_adm();
				}
			}
		}
		return "N";
	}
	
	
	@SuppressWarnings("rawtypes")
	@CrossOrigin
	@RequestMapping(value = "/app/paginado/usuarios/{grupoId}/{pagina}", method = RequestMethod.GET)
    public @ResponseBody List<Usuario> pesquisaUsuarioPaginado(@PathVariable("grupoId")long grupoId , 
    		@PathVariable("pagina") int pageNumber) {
		List<Usuario> listaRetorno = new ArrayList<Usuario>();
    	PageRequest pageable = new PageRequest(pageNumber - 1,10);
    	Page<Usuario> usuarios = usuarioRepository.findByGruposIdGrupoIdOrderByNome(grupoId,pageable);
		for (Iterator iterator = usuarios.iterator(); iterator.hasNext();) {
			Usuario usuario = (Usuario) iterator.next();
			usuario.setSenha(null);
			usuario.setAdm(getAdm(grupoId, usuario.getGrupos()));
			listaRetorno.add(usuario);
		}
		return listaRetorno;
    	
    }
	
	@CrossOrigin
    @RequestMapping(value = "/app/usuario/verificaradm/{grupoId}/{userId}",method = RequestMethod.GET)
    public @ResponseBody String verificaADM(@PathVariable("grupoId") long grupoId,@PathVariable("userId") long userId) {
		Usuario usuario = usuarioRepository.findOne(userId);
		return getAdm(grupoId, usuario.getGrupos());
    }
	
	
	@CrossOrigin
    @RequestMapping(value = "/app/usuario/trocarsenha/{userId}/{senhaAtual}/{novaSenha}",method = RequestMethod.POST)
    public @ResponseBody String trocarSenha(@PathVariable("userId") long userId, @PathVariable("senhaAtual") String senhaAtual, 
    										@PathVariable("novaSenha") String novaSenha) throws Exception {
		try {
			Usuario usuario = usuarioRepository.findOne(userId);
			CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();
			String senhaAtualVerifica = customPasswordEncoder.encode(senhaAtual);
			if(!usuario.getSenha().equals(senhaAtualVerifica)){
				throw new Exception("Senha atual não confere!");
			}
			usuario.setSenha(customPasswordEncoder.encode(novaSenha));
			usuarioRepository.save(usuario);
		}catch(Exception ex){
			throw new Exception(ex.toString());
		}
		return "OK";
    }
	
	@CrossOrigin
    @RequestMapping(value = "/app/usuario/recuperarsenha/{email}",method = RequestMethod.POST)
    public @ResponseBody String gerarNovaSenha(@PathVariable("email") String email) {
		looger.info("##########  Email no gerna novasenha --> "+ email);
		Usuario usuario = usuarioRepository.findByEmail(email);
		CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();
		String novaSenha = criarNovaSenha();
		usuario.setSenha(customPasswordEncoder.encode(novaSenha.trim()));
		usuarioRepository.save(usuario);
		
		Mensagem mensagem= new Mensagem("Carona Segura <caronasegura2018@gmail.com>",
									Arrays.asList(usuario.getNome()+" <"+email+">"),
									"Nova senha Caroana Segura","Olá ! \n\n Segue abaixo a sua nova senha ! \n\n senha : "+novaSenha);
		
		looger.info("#######  vai chamar a thred --> "+ mensagem.getAssunto());
		
		enviarEmail.mensagem = mensagem;
		Thread t = new Thread(enviarEmail);
		t.start();	
		return "OK";
    }
	
	private String criarNovaSenha(){
		char[] chart ={'0','1','2','3','4','5','6','7','8','9','a','b','c','d',
					   'e','f','g','h','i','j','k','l','m','n','o','p','q','r',
					   's','t','u','v','w','x','y','z','A','B','C','D','E','F',
					   'G','H','I','J','K','L','M','N','O','P','Q','R','S','T',
					   'U','V','W','X','Y','Z'};
		char[] senha= new char[10];
		int chartLenght = chart.length;
		Random rdm = new Random();
		for (int x=0; x<8; x++)
		senha[x] = chart[rdm.nextInt(chartLenght)];
		return new String(senha);
	}
}
