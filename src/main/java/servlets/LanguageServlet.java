package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.Locale;


@WebServlet("/changeLanguage")
public class LanguageServlet extends HttpServlet {


    // Logger para registrar entradas, salidas y errores
    private static final Logger logger = LoggerFactory.getLogger(LanguageServlet.class);


    /**
     * Método doGet que se ejecuta cuando se realiza una petición GET a la URL mapeada "/changeLanguage".
     * Su propósito es cambiar el idioma de la sesión del usuario y redirigirlo a la página anterior.
     *
     * @param request  La solicitud HTTP que contiene los parámetros enviados por el cliente
     * @param response La respuesta HTTP que se devolverá al cliente
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        // Log de entrada al método doGet
        logger.info("Entrando en el método doGet del servlet LanguageServlet");


        try {
            // Obtener el parámetro 'lang' de la solicitud, que contiene el idioma seleccionado
            String language = request.getParameter("lang");


            // Verificar si el parámetro no es nulo
            if (language != null) {
                Locale locale;


                // Comprobar si el idioma seleccionado es español ("es")
                if (language.equals("es")) {
                    locale = new Locale("es");
                    logger.info("Idioma seleccionado: Español (es)");
                } else {
                    // Si no es español, se establece por defecto el inglés
                    locale = new Locale("en");
                    logger.info("Idioma seleccionado: Inglés (en)");
                }


                // Almacenar el locale seleccionado en la sesión del usuario
                request.getSession().setAttribute("locale", locale);
                logger.info("El locale '{}' ha sido guardado en la sesión", locale);
            } else {
                // Si no se recibe el parámetro 'lang', registrar un mensaje de advertencia
                logger.warn("No se ha recibido ningún parámetro 'lang'");
            }


            // Redirigir al usuario a la página desde la que llegó (cabecera "Referer")
            String referer = request.getHeader("Referer");
            if (referer != null) {
                logger.info("Redirigiendo al usuario a la página anterior: {}", referer);
                response.sendRedirect(referer);
            } else {
                // Si no hay cabecera Referer, redirigir a una página por defecto (home, por ejemplo)
                logger.warn("No se encontró la cabecera Referer, redirigiendo a la página principal.");
                response.sendRedirect(request.getContextPath() + "/");
            }


        } catch (Exception e) {
            // Registrar cualquier excepción que ocurra durante la ejecución
            logger.error("Se ha producido un error en el método doGet de LanguageServlet: ", e);


            // Manejar la excepción de forma apropiada, aquí enviamos un error 500 al cliente
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cambiar el idioma");
        }


        // Log de salida del método doGet
        logger.info("Saliendo del método doGet del servlet LanguageServlet");
    }
}

