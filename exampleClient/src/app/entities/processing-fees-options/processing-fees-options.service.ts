import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IProcessingFeesOptions } from './iprocessing-fees-options';
import { GenericApiService } from 'src/app/common/shared';

@Injectable({
  providedIn: 'root',
})
export class ProcessingFeesOptionsService extends GenericApiService<IProcessingFeesOptions> {
  constructor(protected httpclient: HttpClient) {
    super(httpclient, 'processingFeesOptions');
  }
}
