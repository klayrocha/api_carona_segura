package br.com.caronasegura.persist.modelo;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class UsuarioCaronaId implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	private Usuario usuario;
	private Carona carona;

	@ManyToOne
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne
	public Carona getCarona() {
		return carona;
	}

	public void setCarona(Carona carona) {
		this.carona = carona;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((carona == null) ? 0 : carona.hashCode());
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
		UsuarioCaronaId other = (UsuarioCaronaId) obj;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (carona == null) {
			if (other.carona != null)
				return false;
		} else if (!carona.equals(other.carona))
			return false;
		return true;
	}

}
