package kookparty.kookpang.dao;

import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.ProductDTO;

public interface ProductDAO {
	List<ProductDTO> selectAll() throws SQLException;
	
	List<ProductDTO> selectByOptions(String word, String category, String order);
	
	int insertProduct(ProductDTO productDTO);
	
	int updateProduct(ProductDTO productDTO);
	
	int deleteProduct(int productId);
}
