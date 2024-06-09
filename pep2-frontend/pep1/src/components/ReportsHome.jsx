import { useNavigate } from 'react-router-dom';
import NavBar from "./NavBar.jsx";
import './theme.css';
import reportService from "../services/report.service.js";

const Reports = () => {
    const navigate = useNavigate();

    const goToReportList = () => {
        navigate('/reports/1');
    }

    const updateReport = () => {
        reportService.getReportById(1) // Fetch the report with idReport = 1
            .then(response => {
                const report = response.data;
                const updatedReport = {...report, updated: true}; // Update the report
                console.log(updatedReport)
                reportService.updateReport(updatedReport) // Save the updated report
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
            // other report properties set to null
        };

        reportService.saveReport(report)
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
                                <h3>Go to Report List</h3>
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