import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ProcessingFeesOptionsExtendedService } from '../extended/entities/processing-fees-options';
import { StateExtendedService } from '../extended/entities/state';



@Component({
  selector: 'app-demo',
  templateUrl: './demo.component.html',
  styleUrls: ['./demo.component.scss']
})
export class DemoComponent implements OnInit {

  //ATTENTION! All Variables declarations goes here. THIS PART IS NOT EDITABLE. DONOT REMOVE THIS LINE.!!! 
  purposeOfRequestOptions = [{ "id": "1", "label": "A. An individual seeking information for personal use and not for commercial use. If the subject matter of your request is yourself, ICE staff may contact you to verity your identity." }, { "id": "2", "label": "B. Affiliated with an educational or noncommercial scientific institution, and this request is made for a scholarly or scientific purpose and not for commercial use." }, { "id": "3", "label": "C. Affiliated with a private corporation or law firm and seeking this information for use in the company's business." }, { "id": "4", "label": "D. A representative of the news media and this request is made as part of news gathering and not for commercial use." }];

  states;
  getstates() {
    this.stateService.getAll().subscribe(response => {
      this.states = response;
    });
  }

  processingFeesOptions;
  getProcessingFeesOptions() {
    this.processingFeesOptionsService.getAll().subscribe(response => {
      this.processingFeesOptions = response;
    });
  }



  step1632412188923 = new FormGroup({ textArea1632412248896: new FormControl({ value: null, disabled: false }, [Validators.required]), select1632414736219: new FormControl({ value: null, disabled: false }, [Validators.required]), });

  step1632411094943 = new FormGroup({ input1632411303831: new FormControl({ value: null, disabled: false }, [Validators.required]), input1632411477506: new FormControl({ value: null, disabled: false }, [Validators.required]), input1632411496965: new FormControl({ value: null, disabled: false }, [Validators.required]), select1632411587486: new FormControl({ value: null, disabled: false }, [Validators.required]), input1632411636945: new FormControl({ value: null, disabled: false }, [Validators.required]), input1632411667170: new FormControl({ value: null, disabled: false }, [Validators.required]), input1632411730095: new FormControl({ value: null, disabled: false }, []), input1632411759838: new FormControl({ value: null, disabled: false }, []), });

  constructor(private stateService: StateExtendedService, private processingFeesOptionsService: ProcessingFeesOptionsExtendedService) { }
  ngOnInit() {
    this.getstates();
    this.getProcessingFeesOptions();
  }

  save() {
    console.log("save data");
  }
}