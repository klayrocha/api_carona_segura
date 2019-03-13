package br.com.caronasegura.persist.modelo;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Usuario_Carona")
@AssociationOverrides({ 
			@AssociationOverride(name = "id.usuario", joinColumns = @JoinColumn(name = "id_usuario")),
			@AssociationOverride(name = "id.carona", joinColumns = @JoinColumn(name = "id_carona")) 
		})
public class UsuarioCarona {

	private UsuarioCaronaId id = new UsuarioCaronaId();

	@Column(name = "indicador_criador")
	private String indicador_criador;

	@EmbeddedId
	private UsuarioCaronaId getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(UsuarioCaronaId id) {
		this.id = id;
	}

	@Transient
	public Usuario getUsuario() {
		return getId().getUsuario();
	}

	public void setUsuario(Usuario usuario) {
		getId().setUsuario(usuario);
	}

	@Transient
	public Carona getCarona() {
		return getId().getCarona();
	}

	public void setCarona(Carona carona) {
		getId().setCarona(carona);
	}

	public String getIndicador_criador() {
		return indicador_criador;
	}

	public void setIndicador_criador(String indicador_criador) {
		this.indicador_criador = indicador_criador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((indicador_criador == null) ? 0 : indicador_criador.hashCode());
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
		UsuarioCarona other = (UsuarioCarona) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (indicador_criador == null) {
			if (other.indicador_criador != null)
				return false;
		} else if (!indicador_criador.equals(other.indicador_criador))
			return false;
		return true;
	}
}
