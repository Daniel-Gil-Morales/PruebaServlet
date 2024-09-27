package servlets;

import DAO.ProvinceDAO;
import DAOImpl.ProvinceDAOImpl;
import Entity.Province;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


/**
 * Servlet que maneja las operaciones CRUD para la entidad `Province`.
 * Utiliza `ProvinceDAO` para interactuar con la base de datos.
 */
@WebServlet("/Provinces")
public class ProvinceServlet extends HttpServlet {


    // DAO para gestionar las operaciones de las Provincees en la base de datos
    private ProvinceDAO ProvinceDAO;


    @Override
    public void init() throws ServletException {
        try {
            ProvinceDAO = new ProvinceDAOImpl();
        } catch (Exception e) {
            throw new ServletException("Error al inicializar el ProvinceDAO", e);
        }
    }


    /**
     * Maneja las solicitudes GET al servlet. Según el parámetro "action", decide qué método invocar.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws ServletException en caso de errores en el servlet.
     * @throws IOException en caso de errores de E/S.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");


        try {
            // Manejo para evitar el valor null
            if (action == null) {
                action = "list"; // O cualquier acción predeterminada que desees manejar
            }


            switch (action) {
                case "new":
                    showNewForm(request, response);  // Mostrar formulario para nueva región
                    break;
                case "edit":
                    showEditForm(request, response);  // Mostrar formulario para editar región
                    break;
                default:
                    listProvinces(request, response);   // Listar todas las Provincees
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }


    /**
     * Maneja las solicitudes POST al servlet. Según el parámetro "action", decide qué método invocar.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws ServletException en caso de errores en el servlet.
     * @throws IOException en caso de errores de E/S.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "insert":
                    insertProvince(request, response);  // Insertar nueva región
                    break;
                case "update":
                    updateProvince(request, response);  // Actualizar región existente
                    break;
                case "delete":
                    deleteProvince(request, response);  // Eliminar región
                    break;
                default:
                    listProvinces(request, response);   // Listar todas las Provincees
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }


    /**
     * Lista todas las Provincees y las pasa como atributo a la vista `Province.jsp`.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws IOException en caso de error de E/S.
     * @throws ServletException en caso de error en el servlet.
     */
    private void listProvinces(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Province> listProvinces = ProvinceDAO.listAllProvinces(); // Obtener todas las Provincees desde el DAO
        request.setAttribute("listProvinces", listProvinces);      // Pasar la lista de Provincees a la vista
        request.getRequestDispatcher("Province.jsp").forward(request, response); // Redirigir a la página JSP
    }


    /**
     * Muestra el formulario para crear una nueva región.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws ServletException en caso de error en el servlet.
     * @throws IOException en caso de error de E/S.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("Province-form.jsp").forward(request, response); // Redirige a la vista para nueva región
    }


    /**
     * Muestra el formulario para editar una región existente.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws ServletException en caso de error en el servlet.
     * @throws IOException en caso de error de E/S.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Province existingProvince = ProvinceDAO.getProvinceById(id);   // Obtener región por ID desde el DAO
        request.setAttribute("Province", existingProvince);        // Pasar la región a la vista
        request.getRequestDispatcher("Province-form.jsp").forward(request, response); // Redirigir a la vista para editar
    }


    /**
     * Inserta una nueva región en la base de datos después de realizar validaciones.
     * Verifica que el código de la región sea único (ignorando mayúsculas) y que los campos
     * de código y nombre no estén vacíos.
     *
     * @param request  la solicitud HTTP con los datos del formulario.
     * @param response la respuesta HTTP para redirigir o mostrar errores.
     * @throws SQLException      si ocurre un error en la base de datos.
     * @throws IOException       si ocurre un error de entrada/salida.
     * @throws ServletException  si ocurre un error en el procesamiento del servlet.
     */
    private void insertProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String code = request.getParameter("code").trim().toUpperCase(); // Convertir a mayúsculas
        String name = request.getParameter("name").trim();


        // Validaciones básicas
        if (code.isEmpty() || name.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre no pueden estar vacíos.");
            request.getRequestDispatcher("Province-form.jsp").forward(request, response);
            return;
        }


        // Validar si el código ya existe ignorando mayúsculas
        if (ProvinceDAO.existsProvinceByCode(code)) {
            request.setAttribute("errorMessage", "El código de la región ya existe.");
            request.getRequestDispatcher("Province-form.jsp").forward(request, response);
            return;
        }


        Province newProvince = new Province(code, name);
        try {
            ProvinceDAO.insertProvince(newProvince);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Código SQL para unique constraint violation
                request.setAttribute("errorMessage", "El código de la región debe ser único.");
                request.getRequestDispatcher("Province-form.jsp").forward(request, response);
            } else {
                throw e;
            }
        }
        response.sendRedirect("Provinces");
    }


    /**
     * Actualiza una región existente en la base de datos después de realizar validaciones.
     * Verifica que el código de la región sea único para otras Provincees (ignorando mayúsculas)
     * y que los campos de código y nombre no estén vacíos.
     *
     * @param request  la solicitud HTTP con los datos del formulario.
     * @param response la respuesta HTTP para redirigir o mostrar errores.
     * @throws SQLException      si ocurre un error en la base de datos.
     * @throws IOException       si ocurre un error de entrada/salida.
     * @throws ServletException  si ocurre un error en el procesamiento del servlet.
     */
    private void updateProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String code = request.getParameter("code").trim().toUpperCase(); // Convertir a mayúsculas
        String name = request.getParameter("name").trim();


        // Validaciones básicas
        if (code.isEmpty() || name.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre no pueden estar vacíos.");
            request.getRequestDispatcher("Province-form.jsp").forward(request, response);
            return;
        }


        // Validar si el código ya existe para otra región
        if (ProvinceDAO.existsProvinceByCodeAndNotId(code, id)) {
            request.setAttribute("errorMessage", "El código de la región ya existe para otra región.");
            request.getRequestDispatcher("Province-form.jsp").forward(request, response);
            return;
        }


        Province updatedProvince = new Province(id, code, name);
        try {
            ProvinceDAO.updateProvince(updatedProvince);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Código SQL para unique constraint violation
                request.setAttribute("errorMessage", "El código de la región debe ser único.");
                request.getRequestDispatcher("Province-form.jsp").forward(request, response);
            } else {
                throw e;
            }
        }
        response.sendRedirect("Provinces");
    }


    /**
     * Elimina una región de la base de datos según su ID.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws IOException en caso de error de E/S.
     */
    private void deleteProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ProvinceDAO.deleteProvince(id);  // Eliminar región usando el DAO
        response.sendRedirect("Provinces"); // Redirigir al listado de Provincees
    }
}
