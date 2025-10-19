package kookparty.kookpang.dao;

import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.ProductDTO;

public interface ProductDAO {
	List<ProductDTO> selectAll() throws SQLException;
	
	List<ProductDTO> selectByOptions(String word, String category, String order) throws SQLException;
	
	List<String> selectCategory() throws SQLException;
	
	ProductDTO selectByProductId(long productId) throws SQLException;
	
	int insertProduct(ProductDTO productDTO) throws SQLException;
	
	int updateProduct(ProductDTO productDTO) throws SQLException;
	
	int deleteProduct(long productId) throws SQLException;
}
