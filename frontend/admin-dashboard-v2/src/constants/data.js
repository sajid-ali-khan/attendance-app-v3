export const SCHEMA_DATA = {
  students: {
    title: 'Students CSV Schema',
    fields: [
        { fieldName: 'roll', dataType: 'String', required: true, unique: true, example: '229x1a2851' },
        { fieldName: 'name', dataType: 'String', required: true, unique: false, example: 'Patan Sajid Ali Khan' },
        { fieldName: 'branch', dataType: 'String', required: true, unique: false, example: '31, 21' },
        { fieldName: 'section', dataType: 'String', required: true, unique: false, example: 'A' },
        { fieldName: 'semester', dataType: 'Integer', required: true, unique: false, example: '4' },
        { fieldName: 'scheme', dataType: 'String', required: true, unique: false, example: '20, 23, 20x' },
        { fieldName: 'degree', dataType: 'String', required: false, unique: false, example: 'Btech(default), Mtech' },
    ]
  },
  courses: {
    title: 'Courses CSV Schema',
    fields: [
        { fieldName: 'degree', dataType: 'String', required: true, unique: false, example: 'Btech' },
        { fieldName: 'scheme', dataType: 'String', required: true, unique: false, example: '20, 23, 20x' },
        { fieldName: 'branch', dataType: 'String', required: true, unique: false, example: '12, 32' },
        { fieldName: 'semester', dataType: 'Integer', required: true, unique: false, example: '4' },
        { fieldName: 'scode', dataType: 'String', required: true, unique: false, example: 'DAA, MEFA' },
        { fieldName: 'subname', dataType: 'String', required: true, unique: false, example: 'Data Structures, Android Development Lab' },
    ]
  },
  faculties: {
    title: 'Faculties CSV Schema',
    fields: [
        { fieldName: 'empid', dataType: 'Integer', required: true, unique: true, example: '1001' },
        { fieldName: 'pwd', dataType: 'String', required: true, unique: false, example: '12345, sajid@1234' },
        { fieldName: 'gender', dataType: 'String', required: false, unique: false, example: 'Female, Male' },
        { fieldName: 'salutation', dataType: 'String', required: false, unique: false, example: 'Dr., Prof.' },
        { fieldName: 'name', dataType: 'String', required: true, unique: false, example: 'Zahoor Ul Haq' },
    ]
  },
};


export const MOCK_DATA = {
    schemes: ['2021-25', '2022-26', '2023-27'],
    branches: ['Computer Science', 'Electronics', 'Mechanical', 'Civil'],
    semesters: ['1', '2', '3', '4', '5', '6', '7', '8'],
    sections: ['A', 'B', 'C'],
    subjects: {
        'Computer Science': ['Data Structures', 'Algorithms', 'Database Systems', 'Operating Systems'],
        'Electronics': ['Circuit Theory', 'Digital Logic', 'Analog Electronics', 'Microprocessors'],
        'Mechanical': ['Thermodynamics', 'Fluid Mechanics', 'Machine Design', 'Manufacturing Process'],
        'Civil': ['Structural Analysis', 'Geotechnical Engineering', 'Transportation Engineering', 'Surveying'],
    },
    faculties: ['Dr. Alan Turing', 'Dr. Marie Curie', 'Dr. Isaac Newton', 'Dr. Albert Einstein', 'Dr. Nikola Tesla', 'Dr. Rosalind Franklin'],
    classAssignments: [
        { id: 1, scheme: '2021-25', branch: 'Computer Science', semester: '4', section: 'A', subject: 'Data Structures', faculty: 'Dr. Alan Turing' },
        { id: 2, scheme: '2021-25', branch: 'Computer Science', semester: '4', section: 'A', subject: 'Algorithms', faculty: 'Dr. Isaac Newton' },
        { id: 3, scheme: '2021-25', branch: 'Computer Science', semester: '5', section: 'B', subject: 'Database Systems', faculty: 'Dr. Marie Curie' },
        { id: 4, scheme: '2022-26', branch: 'Electronics', semester: '3', section: 'A', subject: 'Circuit Theory', faculty: 'Dr. Albert Einstein' },
        { id: 5, scheme: '2022-26', branch: 'Electronics', semester: '3', section: 'B', subject: 'Digital Logic', faculty: 'Dr. Marie Curie' },
    ],
    students: [
        { id: 1, roll: '21CS001', name: 'Alice Johnson', branch: 'Computer Science' },
        { id: 2, roll: '21CS002', name: 'Bob Williams', branch: 'Computer Science' },
        { id: 3, roll: '21EE001', name: 'Charlie Brown', branch: 'Electronics' },
    ],
    courses: [
        { id: 1, code: 'CS201', name: 'Data Structures', branch: 'Computer Science' },
        { id: 2, code: 'CS202', name: 'Algorithms', branch: 'Computer Science' },
        { id: 3, code: 'EE201', name: 'Circuit Theory', branch: 'Electronics' },
    ],
    attendanceReport: [
      { 
        roll: '21CS001', 
        name: 'Alice Johnson', 
        attendance: { 'Data Structures': 92, 'Algorithms': 85, 'Database Systems': 95, 'Operating Systems': 88 } 
      },
      { 
        roll: '21CS002', 
        name: 'Bob Williams', 
        attendance: { 'Data Structures': 88, 'Algorithms': 91, 'Database Systems': 82, 'Operating Systems': 90 } 
      },
      { 
        roll: '21CS003', 
        name: 'Charlie Davis', 
        attendance: { 'Data Structures': 95, 'Algorithms': 76, 'Database Systems': 89, 'Operating Systems': 81 } 
      },
      { 
        roll: '21CS004', 
        name: 'Diana Miller', 
        attendance: { 'Data Structures': 78, 'Algorithms': 81, 'Database Systems': 85, 'Operating Systems': 79 } 
      },
    ]
};