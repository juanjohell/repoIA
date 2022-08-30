package es.ubu.ecosystemIA.db;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import es.ubu.ecosystemIA.modelo.TipoSalida;

@Transactional
@Repository(value = "TiposSalidaRnDao")
public interface TipoSalidaRnDao {
	
	public List<TipoSalida> getTiposSalidaList();
	public TipoSalida getTipoSalida(Integer idTipoSalida);
    public void nuevoTipoSalida(TipoSalida tiposSalida);
    public void editarTipoSalida(TipoSalida tiposSalida);
    public void borrarTipoSalida(TipoSalida tiposSalida);
    
}
