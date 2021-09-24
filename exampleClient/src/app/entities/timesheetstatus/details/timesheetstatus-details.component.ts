import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { TimesheetstatusService } from '../timesheetstatus.service';
import { ITimesheetstatus } from '../itimesheetstatus';

import { BaseDetailsComponent, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { GlobalPermissionService } from 'src/app/core/services/global-permission.service';

@Component({
  selector: 'app-timesheetstatus-details',
  templateUrl: './timesheetstatus-details.component.html',
  styleUrls: ['./timesheetstatus-details.component.scss'],
})
export class TimesheetstatusDetailsComponent extends BaseDetailsComponent<ITimesheetstatus> implements OnInit {
  title = 'Timesheetstatus';
  parentUrl = 'timesheetstatus';
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public timesheetstatusService: TimesheetstatusService,
    public pickerDialogService: PickerDialogService,
    public errorService: ErrorService,
    public globalPermissionService: GlobalPermissionService
  ) {
    super(formBuilder, router, route, dialog, pickerDialogService, timesheetstatusService, errorService);
  }

  ngOnInit() {
    this.entityName = 'Timesheetstatus';
    this.setAssociations();
    super.ngOnInit();
    this.setForm();
    this.getItem();
  }

  setForm() {
    this.itemForm = this.formBuilder.group({
      id: [{ value: '', disabled: true }, Validators.required],
      statusname: ['', Validators.required],
    });

    this.fields = [
      {
        name: 'statusname',
        label: 'statusname',
        isRequired: true,
        isAutoGenerated: false,
        type: 'string',
      },
    ];
  }

  onItemFetched(item: ITimesheetstatus) {
    this.item = item;
    this.itemForm.patchValue(item);
  }

  setAssociations() {
    this.associations = [];

    this.childAssociations = this.associations.filter((association) => {
      return association.isParent;
    });

    this.parentAssociations = this.associations.filter((association) => {
      return !association.isParent;
    });
  }

  onSubmit() {
    let timesheetstatus = this.itemForm.getRawValue();
    super.onSubmit(timesheetstatus);
  }
}
