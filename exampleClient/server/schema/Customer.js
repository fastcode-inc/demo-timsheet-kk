cube(`Customer`, {
  sql: `SELECT * FROM timesheet.customer`,
  
  preAggregations: {
    // Pre-Aggregations definitions go here
    // Learn more here: https://cube.dev/docs/caching/pre-aggregations/getting-started  
  },
  
  joins: {
    
  },
  
  measures: {
		count_name: {
			sql: 'name',
			type: 'count'
		},
		countDistinct_name: {
			sql: 'name',
			type: 'countDistinct'
		},
		countDistinctApprox_name: {
			sql: 'name',
			type: 'countDistinctApprox'
		},
		count_description: {
			sql: 'description',
			type: 'count'
		},
		countDistinct_description: {
			sql: 'description',
			type: 'countDistinct'
		},
		countDistinctApprox_description: {
			sql: 'description',
			type: 'countDistinctApprox'
		},
		count_isactive: {
			sql: 'isactive',
			type: 'count'
		},
		countDistinct_isactive: {
			sql: 'isactive',
			type: 'countDistinct'
		},
		countDistinctApprox_isactive: {
			sql: 'isactive',
			type: 'countDistinctApprox'
		}
  },
  
  dimensions: {
	id: {
		sql: `customerid`,
		type: `number`,
		primaryKey: true
	},

    name: {
      sql: `name`,
      type: `string`
    },
    
    description: {
      sql: `description`,
      type: `string`
    },
    
    isactive: {
      sql: `isactive`,
      type: `string`
    }
  },
  
  dataSource: `default`
});
