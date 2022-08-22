package es.ubu.ecosystemIA.db;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import es.ubu.ecosystemIA.modelo.TipoFichero;

@Transactional
@Repository(value = "TipoFicheroRnDao")
public interface TipoFicheroRnDao {
	
	public List<TipoFichero> getTipoFicheroList();
	
	public TipoFichero getTipoFichero(Integer idTipoFichero);
	
    public void nuevoTipoFichero(TipoFichero tipoFichero);
	
    public void editarTipoFichero(TipoFichero tipoFichero);
    
    public void borrarTipoFichero(TipoFichero tipoFichero);
    
}
