// Description: The main dashboard view with summary statistics.
// ====================================================================================
import { Card } from "../components/ui/Card";
import { SectionTitle } from "../components/ui/SectionTitle";
export const DashboardView = () => (
    <div>
        <SectionTitle>Admin Dashboard</SectionTitle>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            <Card className="flex flex-col justify-between">
                <h3 className="font-semibold text-slate-600">Total Students</h3>
                <p className="text-4xl font-bold text-slate-800">1,250</p>
            </Card>
            <Card className="flex flex-col justify-between">
                <h3 className="font-semibold text-slate-600">Total Courses</h3>
                <p className="text-4xl font-bold text-slate-800">48</p>
            </Card>
            <Card className="flex flex-col justify-between">
                <h3 className="font-semibold text-slate-600">Faculties</h3>
                <p className="text-4xl font-bold text-slate-800">75</p>
            </Card>
             <Card className="flex flex-col justify-between">
                <h3 className="font-semibold text-slate-600">Classes Assigned</h3>
                <p className="text-4xl font-bold text-slate-800">112</p>
            </Card>
        </div>
    </div>
);