import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { StateService } from 'src/app/entities/state/state.service';
@Injectable({
  providedIn: 'root',
})
export class StateExtendedService extends StateService {
  constructor(protected httpclient: HttpClient) {
    super(httpclient);
    this.url += '/extended';
  }
}
