import { useEffect, useState } from 'react';
import reportService from '../services/report.service';
import NavBar from "./NavBar.jsx";
import './theme.css';

const ReportList = () => {
    const [report, setReport] = useState(null);

    const fetchReport = () => {
        reportService.getReportById(1) // Changed from 1 to 2
            .then(response => {
                setReport(response.data);
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    const updateReport = () => {
        const updatedReport = {...report, updated: true}; // Added new property
        console.log(updatedReport)
        reportService.updateReport(updatedReport)
            .then(response => {
                setReport(response.data);
                window.location.reload(); // Add this line to reload the page
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    const saveInitReport = () => {
        reportService.saveInit(report)
            .then(response => {
                console.log('Initial save successful', response);
                setReport(response.data); // Update the state with the response data
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    useEffect(() => {
        fetchReport();
    }, []);

    if (!report) {
        return <div>Loading...</div>;
    }

    return (
        <div className='option-body'>
            <NavBar></NavBar>
            <h1>Report Details</h1>

            <button className="reload-button" onClick={updateReport}>Update Report</button>
            <button className="reload-button" onClick={saveInitReport}>Save Initial Report</button>

            <table className="report-table">
                <thead>
                <tr>
                    <th>ID Report</th>
                    <th>R1 Details</th>
                </tr>
                </thead>
                <tbody>
                {report.r1Details.split('\n').map((detail, index) => (
                    <tr key={index}>
                        <td>{report.idReport}</td>
                        {detail.split(',').map((item, i) => <td key={i}>{item}</td>)}
                    </tr>
                ))}
                </tbody>
            </table>

            <table className="report-table">
                <thead>
                <tr>
                    <th>ID Report</th>
                    <th>R2 Repairs Vs Vehicles By Total Type</th>
                </tr>
                </thead>
                <tbody>
                {report.r2RepairsVsVehiclesByTotalType.split('\n').map((detail, index) => (
                    <tr key={index}>
                        <td>{report.idReport}</td>
                        {detail.split(',').map((item, i) => <td key={i}>{item}</td>)}
                    </tr>
                ))}
                </tbody>
            </table>

            <table className="report-table">
                <thead>
                <tr>
                    <th>ID Report</th>
                    <th>R3 Average By Brand</th>
                </tr>
                </thead>
                <tbody>
                {report.r3AverageByBrand.split(',').map((detail, index) => (
                    <tr key={index}>
                        <td>{report.idReport}</td>
                        <td>{detail}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <table className="report-table">
                <thead>
                <tr>
                    <th>ID Report</th>
                    <th>R4 Repairs Vs Vehicles By Total Engine</th>
                </tr>
                </thead>
                <tbody>
                {report.r4RepairsVsVehiclesByTotalEngine.split('\n').map((detail, index) => (
                    <tr key={index}>
                        <td>{report.idReport}</td>
                        {detail.split(',').map((item, i) => <td key={i}>{item}</td>)}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default ReportList;