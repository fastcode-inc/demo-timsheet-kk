export interface ITimesheet {
  id: number;
  notes?: string;
  periodendingdate: Date;
  periodstartingdate: Date;

  timesheetstatusDescriptiveField?: string;
  timesheetstatusid: number;
  usersDescriptiveField?: string;
  userid: number;
}
