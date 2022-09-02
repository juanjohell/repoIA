package es.ubu.ecosystemIA.db;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import es.ubu.ecosystemIA.modelo.Ficheros;

@Transactional
@Repository(value = "FicherosRnDao")
public interface FicherosRnDao {
	public Ficheros getFichero(Integer idModelo);
    public void nuevoFichero(Ficheros fichero);
    public void editarFichero(Ficheros fichero);
    public void borrarFichero(Ficheros fichero);
    
}
