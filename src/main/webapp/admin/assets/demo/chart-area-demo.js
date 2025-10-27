// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily =
  '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = "#292b2c";

// Area Chart Example
var ctx = document.getElementById("myAreaChart");

const getDailySales = async () => {
  try {
    const response = await fetch(path + "/ajax?key=admin&methodName=getDailySales", {
      method: "GET",
    });
    const result = await response.json();
    const labels = result.chartLabels;
    const data = result.chartDatas;

    console.log("Daily Sales Labels:", labels);
    console.log("Daily Sales Data:", data);

    new Chart(ctx, {
      type: "line",
      data: {
        labels: labels,
        datasets: [
          {
            label: "Sessions",
            lineTension: 0.3,
            backgroundColor: "rgba(2,117,216,0.2)",
            borderColor: "rgba(2,117,216,1)",
            pointRadius: 5,
            pointBackgroundColor: "rgba(2,117,216,1)",
            pointBorderColor: "rgba(255,255,255,0.8)",
            pointHoverRadius: 5,
            pointHoverBackgroundColor: "rgba(2,117,216,1)",
            pointHitRadius: 50,
            pointBorderWidth: 2,
            data: data,
          },
        ],
      },
      options: {
        scales: {
          xAxes: [
            {
              gridLines: {
                display: false,
              },
              ticks: {
                autoSkip: false,
              },
            },
          ],
          yAxes: [
            {
              ticks: {
                min: 0,
                max: Math.max(...data) + 1,
                maxTicksLimit: 5,
              },
              gridLines: {
                color: "rgba(0, 0, 0, .125)",
              },
            },
          ],
        },
        legend: {
          display: false,
        },
      },
    });
  } catch (error) {
    console.error("Error fetching daily sales:", error);
  }
};
getDailySales();
