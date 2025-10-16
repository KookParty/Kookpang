package kookparty.kookpang.service;

import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.ProductDTO;

public interface ProductService {
	/**
	 * 모든 식재료 검색
	 * @return
	 * @throws SQLException 
	 */
	List<ProductDTO> selectAll() throws SQLException;
	
	List<ProductDTO> selectAllLimit(int limit, int offset);
	
	/**
	 * 키워드, 카테고리 검색 및 순서
	 * @param word
	 * @param category
	 * @param order
	 * @return
	 */
	List<ProductDTO> selectByOptions(String word, String category, String order);
	
	/**
	 * 식재료 등록(관리자용)
	 * @param productDTO
	 * @return
	 */
	int insertProduct(ProductDTO productDTO);
	
	/**
	 * 식재료 정보 수정(관리자용)
	 * @param productDTO
	 * @return
	 */
	int updateProduct(ProductDTO productDTO);
	
	/**
	 * 식재료 삭제(관리자용)
	 * @param productId
	 * @return
	 */
	int deleteProduct(int productId);
	
}
