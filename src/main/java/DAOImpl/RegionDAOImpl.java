package DAOImpl;

import Config.DatabaseConnectionManager;
import DAO.RegionDAO;
import Entity.Region;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class RegionDAOImpl implements RegionDAO {


    /**
     * Lista todas las regiones de la base de datos.
     * @return Lista de regiones
     * @throws SQLException
     */
    public List<Region> listAllRegions() throws SQLException {
        List<Region> regions = new ArrayList<>();
        String query = "SELECT * FROM regions";


        // Obtener una nueva conexión para cada operación
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {


            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                regions.add(new Region(id, code, name));
            }
        }
        return regions;
    }

    public void insertRegion(Region region) throws SQLException {
        String query = "INSERT INTO regions (code, name) VALUES (?, ?)";


        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            preparedStatement.setString(1, region.getCode());
            preparedStatement.setString(2, region.getName());
            preparedStatement.executeUpdate();
        }
    }


    /**
     * Actualiza una región existente en la base de datos.
     * @param region Región a actualizar
     * @throws SQLException
     */
    public void updateRegion(Region region) throws SQLException {
        String query = "UPDATE regions SET code = ?, name = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            preparedStatement.setString(1, region.getCode());
            preparedStatement.setString(2, region.getName());
            preparedStatement.setInt(3, region.getId());
            preparedStatement.executeUpdate();
        }
    }


    /**
     * Elimina una región de la base de datos.
     * @param id ID de la región a eliminar
     * @throws SQLException
     */
    public void deleteRegion(int id) throws SQLException {
        String query = "DELETE FROM regions WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }


    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * ignorando mayúsculas.
     *
     * @param code el código de la región a verificar.
     * @return true si una región con el código ya existe, false de lo contrario.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    public Region getRegionById(int id) throws SQLException {
        String query = "SELECT * FROM regions WHERE id = ?";
        Region region = null;


        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                region = new Region(id, code, name);
            }
        }
        return region;
    }


    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * ignorando mayúsculas.
     *
     * @param code el código de la región a verificar.
     * @return true si una región con el código ya existe, false de lo contrario.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    @Override
    public boolean existsRegionByCode(String code) throws SQLException {
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, code.toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }


    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * ignorando mayúsculas, pero excluyendo una región con un ID específico.
     *
     * @param code el código de la región a verificar.
     * @param id   el ID de la región a excluir de la verificación.
     * @return true si una región con el código ya existe (y no es la región con el ID dado),
     *         false de lo contrario.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    @Override
    public boolean existsRegionByCodeAndNotId(String code, int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ? AND id != ?";
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, code.toUpperCase());
            statement.setInt(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }


}

