package datos;

import dominio.ClienteVO;
import java.util.List;

public interface ClienteDao {
    
    public List<ClienteVO> listar();
    
    public ClienteVO encontrar(ClienteVO cliente);
    
    public int insertar(ClienteVO cliente);
    
    public int actualizar(ClienteVO cliente);
    
    public int eliminar(ClienteVO cliente);
    
}
