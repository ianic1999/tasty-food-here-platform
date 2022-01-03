import { Component, OnInit } from '@angular/core';
import {ActuatorService} from "../../service/actuator.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MetricModel} from "../../dto/metric.model";
import {StatisticsService} from "../../service/statistics.service";
import {StatisticsModel} from "../../dto/statistics.model";
import {ApexChart, ApexDataLabels, ApexNonAxisChartSeries, ApexResponsive, ApexStroke, ApexXAxis} from 'ng-apexcharts';
import {ItemsPerCategoryStatisticsModel} from "../../dto/items-per-category-statistics.model";
import {BookingsPerDayModel} from "../../dto/bookings-per-day.model";
import {BookingsPerWeekModel} from "../../dto/bookings-per-week.model";
import {BookingsPerMonthModel} from "../../dto/bookings-per-month.model";

export type PieChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  responsive: ApexResponsive[];
  labels: any;
};

export type LineChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  responsive: ApexResponsive[];
  dataLabels: ApexDataLabels;
  stroke: ApexStroke,
  xaxis: ApexXAxis
}

@Component({
  selector: 'app-admin-diagnostics',
  templateUrl: './admin-diagnostics.component.html',
  styleUrls: ['./admin-diagnostics.component.css']
})
export class AdminDiagnosticsComponent implements OnInit {

  constructor(private actuatorService: ActuatorService,
              private statisticsService: StatisticsService,
              private snackBar: MatSnackBar) { }

  cpuMetric: MetricModel = new MetricModel();
  uptimeMetric: MetricModel = new MetricModel();
  requestsMetric: MetricModel = new MetricModel();
  maxMemoryMetric: MetricModel = new MetricModel();
  usedMemoryMetric: MetricModel = new MetricModel();
  loadedClassesMetric: MetricModel = new MetricModel();
  unloadedClassesMetric: MetricModel = new MetricModel();
  statistics: StatisticsModel = new StatisticsModel();

  itemsPerCategoriesChartOptions: Partial<PieChartOptions> | any = {
    series: [],
    chart: {type: 'pie'},
    labels: null,
    responsive: []
  }

  bookingsPerDayChartOptions: Partial<LineChartOptions> | any = {
    series: [],
    chart: {type: 'line'},
    labels: null,
    responsive: [],
    dataLabels: {},
    stroke: {},
    xaxis: {}
  }

  bookingsPerWeekChartOptions: Partial<LineChartOptions> | any = {
    series: [],
    chart: {type: 'line'},
    labels: null,
    responsive: [],
    dataLabels: {},
    stroke: {},
    xaxis: {}
  }

  bookingsPerMonthChartOptions: Partial<LineChartOptions> | any = {
    series: [],
    chart: {type: 'line'},
    labels: null,
    responsive: [],
    dataLabels: {},
    stroke: {},
    xaxis: {}
  }

  ngOnInit(): void {
    this.actuatorService.getCpuCount()
      .toPromise()
      .then(response => this.cpuMetric = response!)
      .catch(_ => this.snackBar.open('CPU metric not loaded.', 'OK', {duration: 1000}));

    this.actuatorService.getUptime()
      .toPromise()
      .then(response => this.uptimeMetric = response!)
      .catch(_ => this.snackBar.open('Uptime metric not loaded.', 'OK', {duration: 1000}));

    this.actuatorService.getServerRequestsCount()
      .toPromise()
      .then(response => this.requestsMetric = response!)
      .catch(_ => this.snackBar.open('Requests metric not loaded.', 'OK', {duration: 1000}));

    this.actuatorService.getMaxMemory()
      .toPromise()
      .then(response => this.maxMemoryMetric = response!)
      .catch(_ => this.snackBar.open('Max memory metric not loaded.', 'OK', {duration: 1000}));

    this.actuatorService.getUsedMemory()
      .toPromise()
      .then(response => this.usedMemoryMetric = response!)
      .catch(_ => this.snackBar.open('Used memory metric not loaded.', 'OK', {duration: 1000}));

    this.actuatorService.getLoadedClassesCount()
      .toPromise()
      .then(response => this.loadedClassesMetric = response!)
      .catch(_ => this.snackBar.open('Loaded classes metric not loaded.', 'OK', {duration: 1000}));

    this.actuatorService.getNotLoadedClassesCount()
      .toPromise()
      .then(response => this.unloadedClassesMetric = response!)
      .catch(_ => this.snackBar.open('Unloaded classes metric not loaded.', 'OK', {duration: 1000}));

    this.statisticsService.getStatistics()
      .toPromise()
      .then(response => this.statistics = response!.data)
      .catch(_ => this.snackBar.open('Statistics not loaded.', 'OK', {duration: 1000}));

    this.statisticsService.getItemsPerCategoriesStatistics()
      .toPromise()
      .then(result => this.initiateItemsPerCategoriesChart(result!.data))
      .catch(_ => this.snackBar.open('Statistics not loaded.', 'OK', {duration: 1000}))

    this.statisticsService.getBookingsPerDayStatistics()
      .toPromise()
      .then(result => this.initiateBookingsPerDayChart(result!.data))
      .catch(_ => this.snackBar.open('Statistics not loaded.', 'OK', {duration: 1000}))

    this.statisticsService.getBookingsPerWeekStatistics()
      .toPromise()
      .then(result => this.initiateBookingsPerWeekChart(result!.data))
      .catch(_ => this.snackBar.open('Statistics not loaded.', 'OK', {duration: 1000}))

    this.statisticsService.getBookingsPerMonthStatistics()
      .toPromise()
      .then(result => this.initiateBookingsPerMonthChart(result!.data))
      .catch(_ => this.snackBar.open('Statistics not loaded.', 'OK', {duration: 1000}))
  }

  private initiateItemsPerCategoriesChart(data: ItemsPerCategoryStatisticsModel[]) {
    this.itemsPerCategoriesChartOptions = {
      title: {
        text: "Items per categories",
        align: "center"
      },
      series: data.map(statistics => statistics.items),
      chart: {
        type: 'pie',
        foreColor: 'white',
        toolbar: {
          show: true,
          offsetX: 0,
          offsetY: 0,
          tools: {
            download: true,
            selection: true,
            zoom: true,
            zoomin: true,
            zoomout: true,
            pan: true,
            customIcons: []
          },
          export: {
            csv: {
              filename: 'items_per_categories_report',
              columnDelimiter: ',',
              headerCategory: 'category',
              headerValue: 'value',
              dateFormatter(timestamp?: number): any {
                return new Date(timestamp!).toDateString()
              }
            }
          },
          autoSelected: 'zoom'
        },
      },
      labels: data.map(statistics => statistics.category),
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }
      ]
    }
  }

  private initiateBookingsPerDayChart(data: BookingsPerDayModel[]) {
    this.bookingsPerDayChartOptions = {
      series: [
        {
          name: "Bookings",
          data: data.map(statistics => statistics.bookings)
        }
      ],
      chart: {
        type: 'line',
        foreColor: 'white',
        zoom: {
          enabled: false
        }
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: "straight"
      },
      title: {
        text: "Bookings for today",
        align: "center"
      },
      grid: {
        row: {
          colors: ["#f3f3f3", "transparent"],
          opacity: 0.5
        }
      },
      xaxis: {
        categories: data.map(statistics => statistics.hour)
      }
    };
  }

  private initiateBookingsPerWeekChart(data: BookingsPerWeekModel[]) {
    this.bookingsPerWeekChartOptions = {
      series: [
        {
          name: "Bookings",
          data: data.map(statistics => statistics.bookings)
        }
      ],
      chart: {
        type: 'line',
        foreColor: 'white',
        zoom: {
          enabled: false
        }
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: "straight"
      },
      title: {
        text: "Bookings for this week",
        align: "center"
      },
      grid: {
        row: {
          colors: ["#f3f3f3", "transparent"],
          opacity: 0.5
        }
      },
      xaxis: {
        categories: data.map(statistics => statistics.day)
      }
    };
  }

  private initiateBookingsPerMonthChart(data: BookingsPerMonthModel[]) {
    this.bookingsPerMonthChartOptions = {
      series: [
        {
          name: "Bookings",
          data: data.map(statistics => statistics.bookings)
        }
      ],
      chart: {
        type: 'line',
        foreColor: 'white',
        zoom: {
          enabled: false
        }
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: "straight"
      },
      title: {
        text: "Bookings for this month",
        align: "center"
      },
      grid: {
        row: {
          colors: ["#f3f3f3", "transparent"],
          opacity: 0.5
        }
      },
      xaxis: {
        categories: data.map(statistics => statistics.day)
      }
    };
  }

}
