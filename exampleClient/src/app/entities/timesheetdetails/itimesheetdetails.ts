export interface ITimesheetdetails {
  hours?: number;
  id: number;
  notes?: string;
  workdate: Date;

  taskDescriptiveField?: string;
  taskid?: number;
  timeofftypeDescriptiveField?: string;
  timeofftypeid?: number;
  timesheetDescriptiveField?: number;
  timesheetid: number;
}
