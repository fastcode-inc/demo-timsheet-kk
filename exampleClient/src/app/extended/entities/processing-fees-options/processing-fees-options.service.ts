import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ProcessingFeesOptionsService } from 'src/app/entities/processing-fees-options/processing-fees-options.service';
@Injectable({
  providedIn: 'root',
})
export class ProcessingFeesOptionsExtendedService extends ProcessingFeesOptionsService {
  constructor(protected httpclient: HttpClient) {
    super(httpclient);
    this.url += '/extended';
  }
}
