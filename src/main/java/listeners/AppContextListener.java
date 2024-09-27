package listeners;

import Config.DataInitializer;
import Config.DatabaseConnectionManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.io.InputStream;


/**
 * Listener que se ejecuta al iniciar y cerrar la aplicación.
 * Se usa para iniciar y cerrar la conexión a la base de datos.
 */
@WebListener
public class AppContextListener implements ServletContextListener {


    // Logger para trazar eventos usando SLF4J
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Inicializando la aplicación y conectando a la base de datos...");


        // Iniciamos la conexión a la base de datos al arrancar la aplicación
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            // Obtener el archivo data.sql desde el classpath
            InputStream sqlFileStream = sce.getServletContext().getResourceAsStream("/WEB-INF/classes/data.sql");


            if (sqlFileStream == null) {
                logger.error("No se pudo encontrar el archivo data.sql en /WEB-INF/classes/");
                return;
            }


            // Cargar los datos desde el archivo SQL
            DataInitializer.loadDataFromSQL(sqlFileStream);
            logger.info("Carga de datos finalizada.");


        } catch (Exception e) {
            logger.error("Error al inicializar la aplicación y cargar los datos: {}", e.getMessage(), e);
        }
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cerramos la conexión a la base de datos cuando se destruye el contexto
        logger.info("Cerrando la conexión a la base de datos al apagar la aplicación...");
        DatabaseConnectionManager.closeConnection();
    }
}

