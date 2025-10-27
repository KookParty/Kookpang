package kookparty.kookpang.dto;

import java.util.List;

public class ChartDataDTO {
	private List<String> chartLabels;
	private List<Integer> chartDatas;

	public List<String> getChartLabels() {
		return chartLabels;
	}

	public void setChartLabels(List<String> chartLabels) {
		this.chartLabels = chartLabels;
	}

	public List<Integer> getChartDatas() {
		return chartDatas;
	}

	public void setChartDatas(List<Integer> chartDatas) {
		this.chartDatas = chartDatas;
	}

	@Override
	public String toString() {
		return "ChartDataDTO [chartLabels=" + chartLabels + ", chartDatas=" + chartDatas + "]";
	}

}
