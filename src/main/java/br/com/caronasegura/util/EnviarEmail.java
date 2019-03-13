package br.com.caronasegura.util;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EnviarEmail implements Runnable {
	
	@Autowired
	private JavaMailSender javaMailSender;
	public Mensagem  mensagem;
	Logger looger = Logger.getLogger(EnviarEmail.class.getName());
	

	public void enviar(){
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(this.mensagem.getRemetente());
		simpleMailMessage.setTo(this.mensagem.getDestinatarios()
				.toArray(new String[this.mensagem.getDestinatarios().size()]));
		simpleMailMessage.setSubject(this.mensagem.getAssunto());
		simpleMailMessage.setText(this.mensagem.getCorpo());
		
		looger.info("##########  Chegou no enviar "+ this.mensagem.getAssunto());
		
		javaMailSender.send(simpleMailMessage);
		
		looger.info("############  Enviou !!!!! "+ this.mensagem.getAssunto());
	}

	@Override
	public void run() {
		enviar();
	}
	
}