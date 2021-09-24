export enum ListColumnType {
  String = 'String',
  Number = 'Number',
  Date = 'Date',
  DateTime = 'DateTime',
  Boolean = 'Boolean',
  Array = 'Array',
}

export interface IListColumn {
  column: string;
  searchColumn?: string;
  label: string;
  sort: boolean;
  filter: boolean;
  type: ListColumnType;
  options?: Array<any>;
}

export interface Field {
  name: string;
  label: string;
  isRequired: boolean;
  isAutoGenerated: boolean;
  type: FieldType;
}

export enum FieldType {
  String = 'string',
  Number = 'number',
  Date = 'date',
  DateTime = 'dateTime',
  Boolean = 'boolean',
  FixedPointArray = 'fixedPointArray',
  FloatingPointArray = 'floatingPointArray',
  File = 'file',
}
