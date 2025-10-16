package kookparty.kookpang.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kookparty.kookpang.dto.ProductDTO;
import kookparty.kookpang.util.DbUtil;

public class ProductDAOImpl implements ProductDAO {
	private Properties proFile = new Properties();
	
	public ProductDAOImpl() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
			proFile.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ProductDTO> selectAll() throws SQLException{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ProductDTO> productList = new ArrayList<ProductDTO>();
		String sql = proFile.getProperty("product.selectAll");
		
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				ProductDTO productDTO = new ProductDTO(rs.getInt("product_id"),
						rs.getString("name"),
						rs.getInt("price"),
						rs.getString("description"),
						rs.getString("category"),
						rs.getString("brand"),
						rs.getString("image_url"),
						rs.getString("created_at"));
				productList.add(productDTO);
			}
			
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		
		return productList;
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
