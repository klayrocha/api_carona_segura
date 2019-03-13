package br.com.caronasegura.persist.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "Carona")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Carona implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_local_origem")
	private Local origem = new Local();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_local_destino")
	private Local destino = new Local();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_grupo")
	private Grupo grupo = new Grupo();

	@Column(name = "data", nullable = false)
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="PST")
	private Date data;

	@Column(name = "hora_saida", nullable = false)
	private String horaSaida;

	@Column(name = "quantidade_vagas", nullable = false)
	private Integer quantidadeVagas;

	@Column(name = "observacao", nullable = false)
	private String observacao;

	@Column(name = "formaPagamento", nullable = false)
	private String formaPagamento;
	
	
	@SuppressWarnings("deprecation")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "id.carona",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	private List<UsuarioCarona> usuarios;
	
	
	@SuppressWarnings("deprecation")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "carona",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	private List<Mensagem> mensagens;
	
	
	@Transient
	public String idUsuarioCriador;
	
	
	@Transient
	public Long getIdCriadorCarona(){
		if(this.usuarios != null && this.usuarios.size() > 0){
			for (UsuarioCarona usuarioCarona : this.usuarios) {
				if(usuarioCarona.getIndicador_criador().equals("S")){
					return usuarioCarona.getUsuario().getId();
				}
			}
		}
		return null;
	}

	public Long getId() {
		return id;
	}

	public Local getOrigem() {
		return origem;
	}

	public Local getDestino() {
		return destino;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public Date getData() {
		return data;
	}

	public String getHoraSaida() {
		return horaSaida;
	}

	public Integer getQuantidadeVagas() {
		return quantidadeVagas;
	}

	public String getObservacao() {
		return observacao;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrigem(Local origem) {
		this.origem = origem;
	}

	public void setDestino(Local destino) {
		this.destino = destino;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setHoraSaida(String horaSaida) {
		this.horaSaida = horaSaida;
	}

	public void setQuantidadeVagas(Integer quantidadeVagas) {
		this.quantidadeVagas = quantidadeVagas;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	
	public List<UsuarioCarona> getUsuarios() {
		if(usuarios == null){
			usuarios = new ArrayList<UsuarioCarona>();
		}
		return usuarios;
	}

	public void setUsuarios(List<UsuarioCarona> usuarios) {
		this.usuarios = usuarios;
	}

	public String getIdUsuarioCriador() {
		return idUsuarioCriador;
	}

	public void setIdUsuarioCriador(String idUsuarioCriador) {
		this.idUsuarioCriador = idUsuarioCriador;
	}

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((destino == null) ? 0 : destino.hashCode());
		result = prime * result
				+ ((formaPagamento == null) ? 0 : formaPagamento.hashCode());
		result = prime * result + ((grupo == null) ? 0 : grupo.hashCode());
		result = prime * result
				+ ((horaSaida == null) ? 0 : horaSaida.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((observacao == null) ? 0 : observacao.hashCode());
		result = prime * result + ((origem == null) ? 0 : origem.hashCode());
		result = prime * result
				+ ((quantidadeVagas == null) ? 0 : quantidadeVagas.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Carona other = (Carona) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (destino == null) {
			if (other.destino != null)
				return false;
		} else if (!destino.equals(other.destino))
			return false;
		if (formaPagamento == null) {
			if (other.formaPagamento != null)
				return false;
		} else if (!formaPagamento.equals(other.formaPagamento))
			return false;
		if (grupo == null) {
			if (other.grupo != null)
				return false;
		} else if (!grupo.equals(other.grupo))
			return false;
		if (horaSaida == null) {
			if (other.horaSaida != null)
				return false;
		} else if (!horaSaida.equals(other.horaSaida))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (observacao == null) {
			if (other.observacao != null)
				return false;
		} else if (!observacao.equals(other.observacao))
			return false;
		if (origem == null) {
			if (other.origem != null)
				return false;
		} else if (!origem.equals(other.origem))
			return false;
		if (quantidadeVagas == null) {
			if (other.quantidadeVagas != null)
				return false;
		} else if (!quantidadeVagas.equals(other.quantidadeVagas))
			return false;
		return true;
	}
}
