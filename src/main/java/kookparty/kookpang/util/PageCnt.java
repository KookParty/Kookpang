package kookparty.kookpang.util;

public class PageCnt {
	private int pageCnt; // 총페이지 수Math.ceil(전체레코드수 /pagesize)
	public int pagesize;// 한 페이지 당 5개 출력물
	public int pageNo;// 현재 페이지 넘버
	public int blockcount;//페이지네이션 프론트에 한번에 보여줄 숫자 개수 ex) (<< 2 3 4 5 6 >>) <-5
	public PageCnt() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PageCnt(int pageCnt, int pagesize, int pageNo, int blockcount) {
		super();
		this.pageCnt = pageCnt;
		this.pagesize = pagesize;
		this.pageNo = pageNo;
		this.blockcount = blockcount;
	}
	public int getPageCnt() {
		return pageCnt;
	}
	public void setPageCnt(int pageCnt) {
		this.pageCnt = pageCnt;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getBlockcount() {
		return blockcount;
	}
	public void setBlockcount(int blockcount) {
		this.blockcount = blockcount;
	}
	
	

	

}
