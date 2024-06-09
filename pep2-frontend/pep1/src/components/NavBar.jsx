import React from 'react';
import './navbar.css'
import './theme.css';
function Navbar() {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <a className="navbar-brand" href="/home">Home</a>
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarNav">
                <ul className="navbar-nav">
                    <li className="nav-item active">
                        <a className="nav-link" href="/vehicles">Vehicles</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="/repairs">Repairs</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="/tickets">Tickets</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="/brands">Brands</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="/reports">Reports</a>
                    </li>
                </ul>
            </div>
        </nav>
    );
}

export default Navbar;