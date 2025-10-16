package kookparty.kookpang.service;

import java.sql.SQLException;
import java.util.List;

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
	public List<ProductDTO> selectByOptions(String word, String category, String order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertProduct(ProductDTO productDTO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateProduct(ProductDTO productDTO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteProduct(int productId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
