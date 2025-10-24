package kookparty.kookpang.service;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.Part;
import kookparty.kookpang.dto.ProductDTO;

public interface ProductService {
	/**
	 * 모든 식재료 검색 & paging
	 * @return
	 * @throws SQLException 
	 */
	List<ProductDTO> selectAll() throws SQLException;

	
	/**
	 * 키워드, 카테고리, 순서로 검색 & paging
	 * @param word
	 * @param category
	 * @param order
	 * @return
	 * @throws SQLException 
	 */
	List<ProductDTO> selectByOptions(String word, String category, String order, int PageNo) throws SQLException;
	
	/**
	 * 등록된 식재료의 카테고리를 검색
	 * @return
	 * @throws SQLException 
	 */
	List<String> selectCategory() throws SQLException;
	
	/**
	 * 식재료 등록(관리자용)
	 * @param productDTO
	 * @return
	 * @throws SQLException 
	 */
	int insertProduct(ProductDTO productDTO) throws SQLException;
	
	/**
	 * 식재료 정보 수정(관리자용)
	 * @param productDTO
	 * @return
	 * @throws SQLException 
	 */
	int updateProduct(ProductDTO productDTO) throws SQLException;
	
	/**
	 * 식재료 삭제(관리자용)
	 * @param productId
	 * @return
	 * @throws SQLException 
	 */
	int deleteProduct(long productId) throws SQLException;
	
	
	
}
