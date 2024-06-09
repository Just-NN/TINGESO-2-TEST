import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import repairService from '../services/repair.service';
import './theme.css'; // Import the CSS file

const RepairDetail = () => {
    const [repair, setRepair] = useState(null);
    const { id: repairId } = useParams(); // get the id from the route params

    useEffect(() => {
        repairService.getRepairById(repairId)
            .then(response => {
                setRepair(response.data);
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }, [repairId]);

    if (!repair) {
        return <div>Loading...</div>;
    }

    return (
        <div className="card">
            <h1 className="card-title">Repair Detail</h1>
            <table className="repair-table">
                <tbody>
                <tr><td>ID Ticket:</td><td>{repair.idTicket}</td></tr>
                <tr><td>License Plate:</td><td>{repair.licensePlate}</td></tr>
                <tr><td>Repair Type:</td><td>{repair.repairType}</td></tr>
                <tr><td>Entry Date:</td><td>{repair.entryDate}</td></tr>
                <tr><td>Exit Date:</td><td>{repair.exitDate}</td></tr>
                <tr><td>Exit Time:</td><td>{repair.exitTime}</td></tr>
                <tr><td>Pickup Date:</td><td>{repair.pickupDate}</td></tr>
                <tr><td>Pickup Time:</td><td>{repair.pickupTime}</td></tr>
                <tr><td>Total Repair Amount:</td><td>{repair.totalRepairAmount}</td></tr>
                <tr><td>KM Surcharge:</td><td>{repair.kmSurcharge}</td></tr>
                <tr><td>Age Surcharge:</td><td>{repair.ageSurcharge}</td></tr>
                <tr><td>Delay Surcharge:</td><td>{repair.delaySurcharge}</td></tr>
                <tr><td>Day Discount:</td><td>{repair.dayDiscount}</td></tr>
                <tr><td>Repairs Discount:</td><td>{repair.repairsDiscount}</td></tr>
                <tr><td>Base Price:</td><td>{repair.basePrice}</td></tr>
                <tr><td>Total Price:</td><td>{repair.totalPrice}</td></tr>
                </tbody>
            </table>
        </div>
    );
};

export default RepairDetail;