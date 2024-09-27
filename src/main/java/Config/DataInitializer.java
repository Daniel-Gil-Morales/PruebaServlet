package Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataInitializer {


    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);


    public static void loadDataFromSQL(InputStream sqlFileStream) throws SQLException, IOException {
        logger.info("Entrando en el método loadDataFromSQL");


        if (sqlFileStream == null) {
            logger.error("El archivo SQL no se ha proporcionado o es nulo");
            throw new IOException("El archivo SQL es nulo o no se ha encontrado");
        }


        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            logger.info("Conexión a la base de datos establecida");


            // Leer el archivo y dividir las sentencias por ';'
            String sql;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(sqlFileStream))) {
                sql = reader.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                logger.error("Error al leer el archivo SQL: {}", e.getMessage(), e);
                throw new IOException("Error al leer el archivo SQL", e);
            }


            String[] statements = sql.split(";"); // Dividir las sentencias por ';'


            // Manejar transacciones
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                // Ejecutar cada instrucción SQL por separado
                for (String sqlStatement : statements) {
                    if (!sqlStatement.trim().isEmpty()) {
                        logger.info("Ejecutando la sentencia SQL: {}", sqlStatement);
                        statement.execute(sqlStatement.trim());
                    }
                }
                connection.commit();  // Confirmar los cambios
                logger.info("Datos cargados exitosamente desde el archivo SQL");
            } catch (SQLException e) {
                connection.rollback();  // Revertir los cambios si algo falla
                logger.error("Error al ejecutar el archivo SQL, se ha revertido la transacción: {}", e.getMessage(), e);
                throw new SQLException("Error al ejecutar el archivo SQL, transacción revertida", e);
            }
        } catch (SQLException e) {
            logger.error("Error durante la conexión a la base de datos o la ejecución SQL: {}", e.getMessage(), e);
            throw e;
        }


        logger.info("Saliendo del método loadDataFromSQL");
    }
}

