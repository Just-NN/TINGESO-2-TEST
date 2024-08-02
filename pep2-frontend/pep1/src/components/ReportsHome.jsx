import { useNavigate } from 'react-router-dom';
import NavBar from "./NavBar.jsx";
import './theme.css';
import r1Service from "../services/r1.service.js";

const Reports = () => {
    const navigate = useNavigate();

    const goToReportList = () => {
        navigate('/reports/1');
    }
    const goToReportList2 = () => {
        navigate('/reports/2');
    }

    const updateReport = () => {
        r1.getReportById(1) // Fetch the report with idReport = 1
            .then(response => {
                const report = response.data;
                const updatedReport = {...report, updated: true}; // Update the report
                console.log(updatedReport)
                r1Service.updateReport(updatedReport) // Save the updated report
                    .then(response => {
                        console.log('Update Success:', response.data);
                        window.location.reload(); // Add this line to reload the page
                    })
                    .catch(error => {
                        console.error('There was an error!', error);
                    });
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    const createNullReport = () => {
        const report = {
            idreport: 1,
            vehicleRepairIds: []
        };

        r1.saveReport(report)
            .then(response => {
                console.log('Success:', response.data);
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }

    return (
        <div>
            <NavBar></NavBar>
            <h1>Reports</h1>
            <div className="body">
                <div className="options-grid">
                    <div className='option-row'>
                        <div className='option-card'>
                            <button onClick={goToReportList}>
                                <h3>Go to Report List 1</h3>
                            </button>
                            <button onClick={goToReportList2}>
                                <h3>Go to Report List 2</h3>
                            </button>
                            <button onClick={createNullReport}>
                                <h3>Create First Report</h3>
                            </button>
                            <button onClick={updateReport}>
                                <h3>Update Reports</h3>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Reports;