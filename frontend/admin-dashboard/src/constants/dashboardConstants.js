import { 
  Upload, 
  Users, 
  BookOpen, 
  UserPlus, 
  GraduationCap, 
  BarChart3, 
  Settings,
} from 'lucide-react';

export const schemes = ['CBCS', 'Autonomous', 'University'];
export const branches = ['Computer Science', 'Electronics', 'Mechanical', 'Civil'];
export const semesters = ['1st', '2nd', '3rd', '4th', '5th', '6th', '7th', '8th'];
export const subjects = ['Data Structures', 'Operating Systems', 'Database Systems', 'Computer Networks'];
export const faculties = ['Dr. Smith', 'Prof. Johnson', 'Dr. Brown', 'Prof. Davis'];

export const studentSchema = [
  { field: 'roll_number', type: 'String', required: 'Yes', example: 'CS2021001' },
  { field: 'name', type: 'String', required: 'Yes', example: 'John Doe' },
  { field: 'email', type: 'String', required: 'Yes', example: 'john@email.com' },
  { field: 'branch', type: 'String', required: 'Yes', example: 'Computer Science' },
  { field: 'semester', type: 'String', required: 'Yes', example: '5th' },
  { field: 'phone', type: 'String', required: 'No', example: '9876543210' }
];

export const courseSchema = [
  { field: 'course_code', type: 'String', required: 'Yes', example: 'CS301' },
  { field: 'course_name', type: 'String', required: 'Yes', example: 'Data Structures' },
  { field: 'credits', type: 'Number', required: 'Yes', example: '4' },
  { field: 'branch', type: 'String', required: 'Yes', example: 'Computer Science' },
  { field: 'semester', type: 'String', required: 'Yes', example: '3rd' },
  { field: 'type', type: 'String', required: 'Yes', example: 'Theory/Lab' }
];

export const facultySchema = [
  { field: 'faculty_id', type: 'String', required: 'Yes', example: 'FAC001' },
  { field: 'name', type: 'String', required: 'Yes', example: 'Dr. Smith' },
  { field: 'email', type: 'String', required: 'Yes', example: 'smith@college.edu' },
  { field: 'department', type: 'String', required: 'Yes', example: 'Computer Science' },
  { field: 'phone', type: 'String', required: 'No', example: '9876543210' },
  { field: 'designation', type: 'String', required: 'Yes', example: 'Professor' }
];

export const attendanceData = [
  { roll: 'CS2021001', name: 'John Doe', subject: 'Data Structures', percentage: '85%' },
  { roll: 'CS2021002', name: 'Jane Smith', subject: 'Data Structures', percentage: '92%' },
  { roll: 'CS2021003', name: 'Mike Johnson', subject: 'Data Structures', percentage: '78%' },
  { roll: 'CS2021004', name: 'Sarah Wilson', subject: 'Data Structures', percentage: '96%' }
];

export const navItems = [
  { id: 'bulk-upload', label: 'Bulk Data Upload', icon: Upload },
  { id: 'assign-class', label: 'Assign Class', icon: UserPlus },
  { id: 'assignments', label: 'Assignments', icon: BookOpen },
  { id: 'student-management', label: 'Student Management', icon: Users },
  { id: 'course-management', label: 'Course Management', icon: GraduationCap },
  { id: 'attendance-reports', label: 'Attendance Reports', icon: BarChart3 },
  { id: 'settings', label: 'Settings', icon: Settings }
];