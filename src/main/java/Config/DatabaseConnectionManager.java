package Config;


import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DatabaseConnectionManager {


    // Instancia única de la conexión
    private static Connection connection = null;


    // Logger para trazar eventos usando SLF4J
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionManager.class);


    // Cargamos el archivo .env usando dotenv
    private static Dotenv dotenv = Dotenv.load();


    // Constructor privado para evitar la creación de instancias
    private DatabaseConnectionManager() {}


    /**
     * Método para obtener la instancia de la conexión. Si no existe, se inicializa.
     * Utiliza el patrón Singleton.
     *
     * @return La conexión a la base de datos
     */
    public static Connection getConnection() {
        try {
            // Si la conexión es null o está cerrada, intentamos abrir una nueva
            if (connection == null || connection.isClosed()) {
                logger.info("Iniciando la conexión a la base de datos MariaDB...");


                // Obtenemos las variables del archivo .env
                String dbUrl = dotenv.get("DB_URL");
                String dbUser = dotenv.get("DB_USER");
                String dbPassword = dotenv.get("DB_PASSWORD");


                // Intentamos establecer la conexión
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                logger.info("Conexión a la base de datos establecida exitosamente.");
            }
        } catch (SQLException e) {
            logger.error("Error al conectar con la base de datos: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo conectar a la base de datos.", e);
        }
        return connection;
    }


    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                // Trazamos el proceso de cierre de la conexión
                logger.info("Cerrando la conexión a la base de datos...");
                connection.close();
                logger.info("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                // Trazamos cualquier error durante el cierre
                logger.error("Error al cerrar la conexión a la base de datos: {}", e.getMessage(), e);
            }
        }
    }
}
