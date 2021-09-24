import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IState } from './istate';
import { GenericApiService } from 'src/app/common/shared';

@Injectable({
  providedIn: 'root',
})
export class StateService extends GenericApiService<IState> {
  constructor(protected httpclient: HttpClient) {
    super(httpclient, 'state');
  }
}
