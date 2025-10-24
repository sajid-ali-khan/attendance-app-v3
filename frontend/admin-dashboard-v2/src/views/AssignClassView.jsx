// Description: View for assigning subjects to faculty members.
// ====================================================================================
import { AssignClassForm } from './AssignClassForm';
import ViewAssignments from './ViewAssignments';
export const AssignClassView = () => {

    return (
        <div>
            <AssignClassForm />

            <ViewAssignments />
        </div>
    );
};