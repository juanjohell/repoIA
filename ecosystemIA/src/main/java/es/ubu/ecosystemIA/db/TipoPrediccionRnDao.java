package es.ubu.ecosystemIA.db;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import es.ubu.ecosystemIA.modelo.TipoPrediccion;

@Transactional
@Repository(value = "TiposPrediccionRnDao")
public interface TipoPrediccionRnDao {
	
	public List<TipoPrediccion> getTiposPrediccionList();
	public TipoPrediccion getTipoPrediccion(Integer idTipoPrediccion);
    public void nuevoTipoPrediccion(TipoPrediccion tiposPrediccion);
    public void editarTipoPrediccion(TipoPrediccion tiposPrediccion);
    public void borrarTipoPrediccion(TipoPrediccion tiposPrediccion);
    
}
