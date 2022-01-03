import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {MetricModel} from "../dto/metric.model";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class ActuatorService {
  private url = 'http://localhost:8081/actuator/metrics';

  constructor(private http: HttpClient) {
  }

  getCpuCount(): Observable<MetricModel> {
    return this.http.get<MetricModel>(`${this.url}/system.cpu.count`);
  }

  getUptime(): Observable<MetricModel> {
    return this.http.get<MetricModel>(`${this.url}/process.uptime`);
  }

  getServerRequestsCount(): Observable<MetricModel> {
    return this.http.get<MetricModel>(`${this.url}/http.server.requests`);
  }

  getMaxMemory(): Observable<MetricModel> {
    return this.http.get<MetricModel>(`${this.url}/jvm.memory.max`);
  }

  getUsedMemory(): Observable<MetricModel> {
    return this.http.get<MetricModel>(`${this.url}/jvm.memory.used`);
  }

  getLoadedClassesCount(): Observable<MetricModel> {
    return this.http.get<MetricModel>(`${this.url}/jvm.classes.loaded`);
  }

  getNotLoadedClassesCount(): Observable<MetricModel> {
    return this.http.get<MetricModel>(`${this.url}/jvm.classes.unloaded`);
  }
}
