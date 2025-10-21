package kookparty.kookpang.service;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.Part;
import kookparty.kookpang.dao.ProductDAO;
import kookparty.kookpang.dao.ProductDAOImpl;
import kookparty.kookpang.dto.ProductDTO;

public class ProductServiceImpl implements ProductService {
	ProductDAO productDAO = new ProductDAOImpl();
	@Override
	public List<ProductDTO> selectAll() throws SQLException{
		List<ProductDTO> productList = productDAO.selectAll();
		return productList;
	}

	@Override
	public List<ProductDTO> selectAllLimit(int limit, int offset) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<ProductDTO> selectByOptions(String word, String category, String order) throws SQLException {
		List<ProductDTO> productList = productDAO.selectByOptions(word, category, order);
		return productList;
	}
	
	@Override
	public List<String> selectCategory() throws SQLException{
		List<String> categoryList = productDAO.selectCategory();
		return categoryList;
	}

	@Override
	public int insertProduct(ProductDTO productDTO) throws SQLException{
		
		int result = productDAO.insertProduct(productDTO);
		return result;
	}

	@Override
	public int updateProduct(ProductDTO productDTO) throws SQLException{
		int result = productDAO.updateProduct(productDTO);
		return result;
	}

	@Override
	public int deleteProduct(long productId) throws SQLException{
		int result = productDAO.deleteProduct(productId);
		return result;
	}

	
	
	
}
