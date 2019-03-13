package br.com.caronasegura.util;

import java.util.List;

public class Mensagem {

	private String remetente;
	private List<String> destinatarios;
	private String assunto;
	private String corpo;

	public Mensagem(String remetente, List<String> destinatarios,
			String assunto, String corpo) {
		super();
		this.remetente = remetente;
		this.destinatarios = destinatarios;
		this.assunto = assunto;
		this.corpo = corpo;
	}

	public String getRemetente() {
		return remetente;
	}

	public List<String> getDestinatarios() {
		return destinatarios;
	}

	public String getAssunto() {
		return assunto;
	}

	public String getCorpo() {
		return corpo;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

	public void setDestinatarios(List<String> destinatarios) {
		this.destinatarios = destinatarios;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}

}
