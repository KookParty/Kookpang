// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily =
  '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = "#292b2c";

// Bar Chart Example
var ctx2 = document.getElementById("myBarChart");

const getBestItems = async () => {
  try {
    const response = await fetch(path + "/ajax?key=admin&methodName=getBestItems", {
      method: "GET",
    });
    const result = await response.json();

    const labels = result.chartLabels;
    const data = result.chartDatas;

    await new Chart(ctx2, {
      type: "bar",
      data: {
        labels: labels,
        datasets: [
          {
            label: "Revenue",
            backgroundColor: "rgba(2,117,216,1)",
            borderColor: "rgba(2,117,216,1)",
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
                display: true,
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
    console.error("Error fetching best items:", error);
  }
};

getBestItems();
