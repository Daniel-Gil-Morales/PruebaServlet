package DAO;

import Entity.Province;
import java.sql.SQLException;
import java.util.List;

public interface ProvinceDAO {


    List<Province> listAllProvinces() throws SQLException;
    void insertProvince(Province province) throws SQLException;
    void updateProvince(Province province) throws SQLException;
    void deleteProvince(int id) throws SQLException;
    Province getProvinceById(int id) throws SQLException;
    boolean existsProvinceByCode(String code) throws SQLException;
    boolean existsProvinceByCodeAndNotId(String code, int id) throws SQLException;
}
