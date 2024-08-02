import React, { useEffect, useState } from 'react';
import r1Service from '../services/r1.service.js';
import NavBar from "./NavBar.jsx";
import './theme.css';

const ReportList = () => {
    const [report, setReport] = useState(null);
    const [vehicleRepairs, setVehicleRepairs] = useState([]);
    const [inputValue, setInputValue] = useState(0); // State for number input value
    const [monthValue, setMonthValue] = useState(''); // State for month input value
    const [monthInt, setMonthInt] = useState(null); // State for month as integer
    const [yearInt, setYearInt] = useState(null); // State for year as integer

    const fetchReport = () => {
        r1Service.getR1ById(1) // Assuming the report ID is known and static
            .then(response => {
                setReport(response.data);
                fetchVehicleRepairs(); // Fetch vehicle repairs after successfully fetching the report
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    };

    const fetchVehicleRepairs = async () => {
        try {
            const response = await r1Service.getVehicleRepairs(1); // Always use ID 1
            console.log('Response for ID 1:', response.data); // Debugging log
            if (response.data) {
                setVehicleRepairs(response.data); // Assuming response.data is the list of repairs
            } else {
                console.log('No data returned for ID 1'); // Debugging log for empty responses
                setVehicleRepairs([]); // Set to empty if no data returned
            }
        } catch (error) {
            console.error('There was an error fetching the vehicle repair data for ID 1', error);
            setVehicleRepairs([]); // Set to empty in case of error
        }
    };

    const initializeVehicleRepairs = () => {
        console.log('Initializing vehicle repairs...');
        const fixedId = 1; // Always use ID 1
        // Use the monthInt state
        // Use the yearInt state
        r1Service.initializeValues(fixedId, monthInt, yearInt)
            .then(() => {
                console.log('monthInt:', monthInt, 'yearInt:', yearInt);
                console.log('Initialization successful!');
                fetchVehicleRepairs(); // Re-fetch vehicle repairs after initialization
            })
            .catch(error => {
                console.error('There was an error during initialization!', error);
            });
    };
    const handleMonthChange = (e) => {
        const month = e.target.value;
        setMonthValue(month);
        const date = new Date(`${month}-01`); // Ensure the date is parsed correctly
        const monthInt = date.getMonth();
        if (monthInt === 11) {
            setMonthInt(1);
            setYearInt(date.getFullYear() + 1);
        } else {
            setMonthInt(monthInt + 2); // Adjust the month value by adding 1
            setYearInt(date.getFullYear());
        }
        console.log('Month:', monthInt + 1, 'Year:', date.getFullYear());
    };

    useEffect(() => {
        fetchReport();
    }, []);

    if (!report) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <NavBar></NavBar>
            <h1>Report List</h1>
            <button onClick={initializeVehicleRepairs}>Initialize Vehicle Repairs</button>
            <input
                type="month"
                value={monthValue}
                onChange={handleMonthChange}
                placeholder="Enter a month"
                className="monthInput"
            />
            <table className="repair-table">
                <thead>
                <tr>
                    <th>Repair Type</th>
                    <th>Sedan Amount</th>
                    <th>Hatchback Amount</th>
                    <th>SUV Amount</th>
                    <th>Pickup Amount</th>
                    <th>Truck Amount</th>
                    <th>Sedan Price</th>
                    <th>Hatchback Price</th>
                    <th>SUV Price</th>
                    <th>Pickup Price</th>
                    <th>Truck Price</th>
                    <th>Total Amount</th>
                    <th>Total Price</th>
                </tr>
                </thead>
                <tbody>
                {vehicleRepairs.map((repair, index) => (
                    <tr key={index}>
                        <td>{repair.repairType}</td>
                        <td>{repair.sedanAmount}</td>
                        <td>{repair.hatchbackAmount}</td>
                        <td>{repair.suvAmount}</td>
                        <td>{repair.pickupAmount}</td>
                        <td>{repair.truckAmount}</td>
                        <td>{repair.sedanPrice}</td>
                        <td>{repair.hatchbackPrice}</td>
                        <td>{repair.suvPrice}</td>
                        <td>{repair.pickupPrice}</td>
                        <td>{repair.truckPrice}</td>
                        <td>{repair.totalAmount}</td>
                        <td>{repair.totalPrice}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default ReportList;