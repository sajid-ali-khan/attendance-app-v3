// Description: View for assigning subjects to faculty members.
// ====================================================================================
import { useState } from 'react';
import { Card } from '../components/ui/Card';
import { SectionTitle } from '../components/ui/SectionTitle';
import { SelectInput } from '../components/ui/SelectInput';
import { AssignClassForm } from './AssignClassForm';
import { DownloadIcon } from '../components/icons';
import { MOCK_DATA } from '../constants/data';
import ViewAssignments from './ViewAssignments';
export const AssignClassView = () => {

    return (
        <div>
            <AssignClassForm />

            <ViewAssignments />
        </div>
    );
};