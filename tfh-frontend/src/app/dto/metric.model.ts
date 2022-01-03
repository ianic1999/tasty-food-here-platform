import {MetricMeasurementModel} from "./metric-measurement.model";

export class MetricModel {
  constructor(public name: string = '',
              public description: string = '',
              public baseUnit: any = null,
              public measurements: MetricMeasurementModel[] = []) {
  }
}
