import { useEffect, useState } from 'react';
import bonusBrandService from '../services/bonusbrand.service.js';
import NavBar from "./NavBar.jsx";
import './theme.css';
import { format } from 'date-fns';

const BonusBrandList = () => {
    const [bonusBrands, setBonusBrands] = useState([]);

    const fetchBonusBrands = () => {
        bonusBrandService.getAllBonusBrands()
            .then(response => {
                const data = response.data.map(bonusBrand => ({
                    ...bonusBrand,
                    entryDate: new Date(bonusBrand.entryDate),
                    exitDate: new Date(bonusBrand.exitDate),
                    pickupDate: new Date(bonusBrand.pickupDate)
                }));
                setBonusBrands(data);
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    useEffect(() => {
        fetchBonusBrands();
    }, []);

    return (
        <div className='option-body'>
            <NavBar></NavBar>
            <h1>Bonus Brand List</h1>

            <button className="reload-button" onClick={fetchBonusBrands}>Reload Table</button>

            <table className="repair-table">
                <thead>
                <tr>
                    <th>Brand</th>
                    <th>Bonus</th>
                    <th>Active</th>
                </tr>
                </thead>
                <tbody>
                {bonusBrands.map((bonusBrand, index) => (
                    <tr key={index}>
                        <td>{bonusBrand.brand}</td>
                        <td>{bonusBrand.bonus}</td>
                        <td>{bonusBrand.active ? 'Yes' : 'No'}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default BonusBrandList;