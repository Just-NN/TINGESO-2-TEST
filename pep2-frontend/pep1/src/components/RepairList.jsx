import { useEffect, useState } from 'react';
import repairService from '../services/repair.service';
import NavBar from "./NavBar.jsx";
import './theme.css';

const RepairList = () => {
    const [repairs, setRepairs] = useState([]);

    const fetchRepairs = () => {
        repairService.getAllRepairs()
            .then(response => {
                setRepairs(response.data);
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    const initRepairs = () => {
        console.log('Initializing repairs...')
        repairs.forEach(repair => {
            console.log('Initializing repair:', repair.idRepair)
            repairService.calculateTotalPrice(repair)
                .then(response => {
                    console.log('Response data:', response.data);
                    // Update the repair in the state if necessary
                })
                .catch(error => {
                    console.error('There was an error!', error, 'IN REPAIR', repair.idRepair);
                });
        });
        // Fetch the updated repairs after initializing
        fetchRepairs();
    }

    useEffect(() => {
        fetchRepairs();
    }, []);

    return (
        <div className='option-body'>
            <NavBar></NavBar>
            <h1>Repair List</h1>

            <button className="reload-button" onClick={fetchRepairs}>Reload Table</button>
            <button className="init-button" onClick={initRepairs}>Initialize Repairs</button>

            <table className="repair-table">
                <thead>
                <tr>
                    <th>ID Repair</th>
                    <th>ID Ticket</th>
                    <th>Repair Type</th>
                    <th>KM Surcharge</th>
                    <th>Age Surcharge</th>
                    <th>Delay Surcharge</th>
                    <th>Base Price</th>
                    <th>Total Price</th>
                </tr>
                </thead>
                <tbody>
                {repairs.map((repair, index) => (
                    <tr key={index}>
                        <td>{repair.idRepair}</td>
                        <td>{repair.idTicket}</td>
                        <td>{repair.repairType}</td>
                        <td>{repair.kmSurcharge}</td>
                        <td>{repair.ageSurcharge}</td>
                        <td>{repair.delaySurcharge}</td>
                        <td>{repair.basePrice}</td>
                        <td>{repair.totalPrice}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default RepairList;