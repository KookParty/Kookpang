package kookparty.kookpang.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	public List<ProductDTO> selectByOptions(String word, String category, String order) throws SQLException{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ProductDTO> productList = new ArrayList<ProductDTO>();
		String preparedSql = null;
		String sql = null;
		try {
			con = DbUtil.getConnection();
			String orderBy = "desc".equals(order) ? "desc" : "asc";
			if(category == null || category.isEmpty()) {
				preparedSql = proFile.getProperty("product.selectByOptions2");
				sql = preparedSql + " " + orderBy;
				ps = con.prepareStatement(sql);
				ps.setString(1, "%"+word+"%");
			}else {
				preparedSql = proFile.getProperty("product.selectByOptions1");
				sql = preparedSql + " " + orderBy;
				ps = con.prepareStatement(sql);
				ps.setString(1, category);
				ps.setString(2, "%"+word+"%");	
			}
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
	public List<String> selectCategory() throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> categoryList = new ArrayList<String>();
		String sql = proFile.getProperty("product.selectCategory");
		
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				categoryList.add(rs.getString("category"));
			}
		} finally {
			DbUtil.dbClose(con, ps, rs);
		} 
		
		return categoryList;
	}
	
	@Override
	public int insertProduct(ProductDTO productDTO) throws SQLException{
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("product.insertProduct");
		
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, productDTO.getName());
			ps.setInt(2, productDTO.getPrice());
			ps.setString(3, productDTO.getDescription());
			ps.setString(4, productDTO.getCategory());
			ps.setString(5, productDTO.getBrand());
			ps.setString(6, productDTO.getImageUrl());
			result = ps.executeUpdate();
		} finally {
			DbUtil.dbClose(con, ps);
		}
		return result;
	}

	@Override
	public int updateProduct(ProductDTO productDTO) throws SQLException{
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("product.updateProduct");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, productDTO.getName());
			ps.setInt(2, productDTO.getPrice());
			ps.setString(3, productDTO.getDescription());
			ps.setString(4, productDTO.getCategory());
			ps.setString(5, productDTO.getBrand());
			ps.setString(6, productDTO.getImageUrl());
			ps.setLong(7, productDTO.getProductId());
			result = ps.executeUpdate();
		} finally {
			DbUtil.dbClose(con, ps);
		}
		return result;
	}

	@Override
	public int deleteProduct(long productId) throws SQLException{
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("product.deleteProduct");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setLong(1, productId);
			result = ps.executeUpdate();
		} finally {
			DbUtil.dbClose(con, ps);
		}
		return result;
	}

	

}
