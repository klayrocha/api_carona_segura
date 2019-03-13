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
@Table(name = "Usuario_Grupo")
@AssociationOverrides({ 
			@AssociationOverride(name = "id.usuario", joinColumns = @JoinColumn(name = "id_usuario")),
			@AssociationOverride(name = "id.grupo", joinColumns = @JoinColumn(name = "id_grupo")) 
		})
public class UsuarioGrupo {

	private UsuarioGrupoId id = new UsuarioGrupoId();

	@Column(name = "indicador_adm")
	private String indicador_adm;

	@EmbeddedId
	private UsuarioGrupoId getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(UsuarioGrupoId id) {
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
	public Grupo getGrupo() {
		return getId().getGrupo();
	}

	public void setGrupo(Grupo grupo) {
		getId().setGrupo(grupo);
	}

	public String getIndicador_adm() {
		return indicador_adm;
	}

	public void setIndicador_adm(String indicador_adm) {
		this.indicador_adm = indicador_adm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((indicador_adm == null) ? 0 : indicador_adm.hashCode());
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
		UsuarioGrupo other = (UsuarioGrupo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (indicador_adm == null) {
			if (other.indicador_adm != null)
				return false;
		} else if (!indicador_adm.equals(other.indicador_adm))
			return false;
		return true;
	}

}