import { useState } from 'react';
import { useNavigate } from "react-router-dom";
import NavBar from "./NavBar.jsx";
import './theme.css';

const BonusBrands = () => {
    const navigate = useNavigate();
    const [id, setId] = useState(''); // Add a new state variable for the ID

    const handlePageChange = (path) => {
        return () => navigate(path);
    }

    const handleIdSubmit = (event) => {
        event.preventDefault();
        navigate(`/bonusBrands/${id}`);
    }

    return (
        <div>
            <NavBar></NavBar>
            <h1>Brand Bonuses</h1>
            <div className="body">
                <h2>Options</h2>
                <div className="options-grid">
                    <div className='option-row'>
                        <div className='option-card'>
                            <button className="op-button" onClick={handlePageChange("/brands/list")}>
                                <h3>Show All</h3>
                            </button>
                            <p>Show all the bonuses</p>
                        </div>
                    </div>
                    <div className="option-row">
                        <div className='option-card'>
                            <button className="op-button" onClick={handlePageChange("/brands/add")}>
                                <h3>Create a Bonus</h3>
                            </button>
                            <p>Fill the form to create a bonus</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default BonusBrands;