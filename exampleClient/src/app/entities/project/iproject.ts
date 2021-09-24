export interface IProject {
  description?: string;
  enddate: Date;
  id: number;
  name: string;
  startdate: Date;

  customerDescriptiveField?: string;
  customerid: number;
}
