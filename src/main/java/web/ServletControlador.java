package web;

import datos.*;
import dominio.ClienteVO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "editar":
                    this.editarCliente(request, response);
                    break;
                case "eliminar":
                    this.eliminarCliente(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "insertar":
                    this.insertarCliente(request, response);
                    break;
                case "modificar":
                    this.modificarCliente(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    private double calcularSaldoTotal(List<ClienteVO> clientes) {
        double saldoTotal = 0;
        for (ClienteVO cliente : clientes) {
            saldoTotal += cliente.getSaldo();
        }
        return saldoTotal;
    }

    private void insertarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //recuperamos los valores del formulario agregarCliente
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        if (saldoString != null && !"".equals(saldoString)) {
            saldo = Double.parseDouble(saldoString);
        }
        ClienteDao clienteDao = new ClienteDaoJDBC();
        // creamos el objeto de cliente
        ClienteVO cliente = new ClienteVO(nombre, apellido, email, telefono, saldo);

        //insertamos el nuevo objeto en la base de datos
        int registrosModificados = clienteDao.insertar(cliente);
        System.out.println("registrosModificados = " + registrosModificados);

        //redirigimos hacia accion por default
        this.accionDefault(request, response);

    }

    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClienteDao cliente = new ClienteDaoJDBC();
        List<ClienteVO> clientes = cliente.listar();
        HttpSession sesion = request.getSession();
        sesion.setAttribute("clientes", clientes);
        sesion.setAttribute("totalClientes", clientes.size());
        sesion.setAttribute("saldoTotal", this.calcularSaldoTotal(clientes));
        //request.getRequestDispatcher("clientes.jsp").forward(request, response);
        response.sendRedirect("clientes.jsp");
    }
    
    private void editarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //recuperar el idCliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        ClienteDao clienteDao = new ClienteDaoJDBC();
        ClienteVO cliente = clienteDao.encontrar(new ClienteVO(idCliente));
        request.setAttribute("cliente", cliente);
        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        request.getRequestDispatcher(jspEditar).forward(request, response);
    }
    
    private void modificarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //recuperamos los valores del formulario editarCliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        if (saldoString != null && !"".equals(saldoString)) {
            saldo = Double.parseDouble(saldoString);
        }
        ClienteDao clienteDao = new ClienteDaoJDBC();
        // creamos el objeto de cliente
        ClienteVO cliente = new ClienteVO(idCliente, nombre, apellido, email, telefono, saldo);

        //modificiar el objeto en la base de datos
        int registrosModificados = clienteDao.actualizar(cliente);
        System.out.println("registrosModificados = " + registrosModificados);

        //redirigimos hacia accion por default
        this.accionDefault(request, response);

    }
    
    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //recuperamos los valores del formulario editarCliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
       
        ClienteDao clienteDao = new ClienteDaoJDBC();
        // creamos el objeto de cliente
        ClienteVO cliente = new ClienteVO(idCliente);

        //eliminamos el objeto en la base de datos
        int registrosModificados = clienteDao.eliminar(cliente);
        System.out.println("registrosModificados = " + registrosModificados);

        //redirigimos hacia accion por default
        this.accionDefault(request, response);

    }

}
