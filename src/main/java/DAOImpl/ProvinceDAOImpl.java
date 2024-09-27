package DAOImpl;

import Config.DatabaseConnectionManager;
import DAO.ProvinceDAO;
import Entity.Province;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class ProvinceDAOImpl implements ProvinceDAO {


    /**
     * Lista todas las Provincees de la base de datos.
     * @return Lista de Provincees
     * @throws SQLException
     */
    public List<Province> listAllProvinces() throws SQLException {
        List<Province> Provinces = new ArrayList<>();
        String query = "SELECT * FROM Provinces";


        // Obtener una nueva conexión para cada operación
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {


            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                Provinces.add(new Province(id, code, name));
            }
        }
        return Provinces;
    }

    public void insertProvince(Province Province) throws SQLException {
        String query = "INSERT INTO Provinces (code, name) VALUES (?, ?)";


        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            preparedStatement.setString(1, Province.getCode());
            preparedStatement.setString(2, Province.getName());
            preparedStatement.executeUpdate();
        }
    }


    /**
     * Actualiza una región existente en la base de datos.
     * @param Province Región a actualizar
     * @throws SQLException
     */
    public void updateProvince(Province Province) throws SQLException {
        String query = "UPDATE Provinces SET code = ?, name = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            preparedStatement.setString(1, Province.getCode());
            preparedStatement.setString(2, Province.getName());
            preparedStatement.setInt(3, Province.getId());
            preparedStatement.executeUpdate();
        }
    }


    /**
     * Elimina una región de la base de datos.
     * @param id ID de la región a eliminar
     * @throws SQLException
     */
    public void deleteProvince(int id) throws SQLException {
        String query = "DELETE FROM Provinces WHERE id = ?";
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
    public Province getProvinceById(int id) throws SQLException {
        String query = "SELECT * FROM Provinces WHERE id = ?";
        Province Province = null;


        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                Province = new Province(id, code, name);
            }
        }
        return Province;
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
    public boolean existsProvinceByCode(String code) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Provinces WHERE UPPER(code) = ?";
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
    public boolean existsProvinceByCodeAndNotId(String code, int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Provinces WHERE UPPER(code) = ? AND id != ?";
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

