export const SCHEMA_DATA = {
  students: {
    title: 'Students CSV Schema',
    fields: [
        { fieldName: 'student_id', dataType: 'String', required: true, unique: true, example: 'S101' },
        { fieldName: 'roll_number', dataType: 'String', required: true, unique: true, example: '21CS001' },
        { fieldName: 'student_name', dataType: 'String', required: true, unique: false, example: 'John Doe' },
        { fieldName: 'email', dataType: 'String', required: true, unique: true, example: 'john.doe@example.com' },
        { fieldName: 'phone_number', dataType: 'String', required: false, unique: true, example: '1234567890' },
        { fieldName: 'semester', dataType: 'Integer', required: true, unique: false, example: '4' },
        { fieldName: 'branch_code', dataType: 'String', required: true, unique: false, example: 'CS' },
    ]
  },
  courses: {
    title: 'Courses CSV Schema',
    fields: [
        { fieldName: 'course_id', dataType: 'String', required: true, unique: true, example: 'C202' },
        { fieldName: 'course_name', dataType: 'String', required: true, unique: false, example: 'Data Structures' },
        { fieldName: 'course_code', dataType: 'String', required: true, unique: true, example: 'CS202' },
        { fieldName: 'semester', dataType: 'Integer', required: true, unique: false, example: '4' },
        { fieldName: 'branch_code', dataType: 'String', required: true, unique: false, example: 'CS' },
        { fieldName: 'credits', dataType: 'Integer', required: true, unique: false, example: '4' },
    ]
  },
  faculties: {
    title: 'Faculties CSV Schema',
    fields: [
        { fieldName: 'faculty_id', dataType: 'String', required: true, unique: true, example: 'F303' },
        { fieldName: 'faculty_name', dataType: 'String', required: true, unique: false, example: 'Dr. Jane Smith' },
        { fieldName: 'email', dataType: 'String', required: true, unique: true, example: 'jane.smith@example.com' },
        { fieldName: 'phone_number', dataType: 'String', required: false, unique: true, example: '0987654321' },
        { fieldName: 'department_code', dataType: 'String', required: true, unique: false, example: 'CS' },
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