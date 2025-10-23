package kookparty.kookpang.util;

public class PageCnt {
	private int pageCnt; // 총페이지 수Math.ceil(전체레코드수 /pagesize)
	private int pageNo;// 현재 페이지 넘버
	private int blockcount;//페이지네이션 프론트에 한번에 보여줄 숫자 개수 ex) (<< 2 3 4 5 6 >>) <-5
	
	private int limit;
	private int offset;
	
	
	/**
	 * 페이지 번호를 눌러서 넘기는 형태의 페이지네이션시 생성자
	 * @param totalCount
	 * @param pageSize
	 * @param pageNo
	 * @param blockCount
	 */
	public PageCnt(int totalCount, int pageSize, int pageNo, int blockCount) {
		this.pageCnt = (int)Math.ceil((double)totalCount / pageSize);
		this.pageNo = pageNo;
		this.blockcount = blockCount;
		this.limit = pageSize;
		this.offset = (pageNo - 1) * pageSize;
	}
	
	/**
	 * 스크롤 내려서 넘기는 형태의 페이지네이션시 생성자(blockCount가 필요 없음)
	 * @param totalCount
	 * @param pageSize
	 * @param pageNo
	 */
	public PageCnt(int totalCount, int pageSize, int pageNo) {
		this.pageCnt = (int)Math.ceil((double)totalCount / pageSize);
		this.pageNo = pageNo;
		this.limit = pageSize;
		this.offset = (pageNo - 1) * pageSize;
	}
	
	public int getPageCnt() {
		return pageCnt;
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getBlockcount() {
		return blockcount;
	}

	public int getLimit() {
		return limit;
	}
	public int getOffset() {
		return offset;
	}

	

}
