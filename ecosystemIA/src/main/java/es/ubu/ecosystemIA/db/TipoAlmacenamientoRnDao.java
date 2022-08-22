package es.ubu.ecosystemIA.db;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import es.ubu.ecosystemIA.modelo.TipoAlmacenamiento;

@Transactional
@Repository(value = "TiposAlmacenamientoRnDao")
public interface TipoAlmacenamientoRnDao {
	
	public List<TipoAlmacenamiento> getTiposAlmacenamientoList();
	
	public TipoAlmacenamiento getTipoAlmacenamiento(Integer idTipoAlmacenamiento);
	
    public void nuevoTipoAlmacenamiento(TipoAlmacenamiento tiposAlmacenamiento);
	
    public void editarTipoAlmacenamiento(TipoAlmacenamiento tiposAlmacenamiento);
    
    public void borrarTipoAlmacenamiento(TipoAlmacenamiento tiposAlmacenamiento);
    
}
