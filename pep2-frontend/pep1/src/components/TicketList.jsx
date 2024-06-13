import { useEffect, useState } from 'react';
import ticketService from '../services/ticket.service';
import NavBar from "./NavBar.jsx";
import './theme.css';
import { format } from 'date-fns';

const TicketList = () => {
    const [tickets, setTickets] = useState([]);

    const fetchTickets = () => {
        console.log('Fetching tickets...')
        ticketService.getAllTickets()
            .then(response => {
                setTickets(response.data);
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    const initTickets = () => {
        console.log('Initializing tickets...')
        tickets.forEach(ticket => {
            console.log('Initializing ticket:', ticket.idTicket)
            ticketService.saveInitValues(ticket)
                .then(response => {
                    console.log('Response data:', response.data);
                    // Update the ticket in the state if necessary
                })
                .catch(error => {
                    console.error('There was an error!', error, 'IN TICKET', ticket.idTicket);
                });
        });
        // Fetch the updated tickets after initializing
        fetchTickets();
    }
    const saveInitAllTickets = () => {
        console.log('Initializing all tickets...')
        tickets.forEach(ticket => {
            console.log('Initializing ticket:', ticket.idTicket)
            ticketService.saveInit(ticket)
                .then(response => {
                    console.log('Response data:', response.data);
                    // Update the ticket in the state if necessary
                })
                .catch(error => {
                    console.error('There was an error!', error, 'IN TICKET', ticket.idTicket);
                });
        });
    }

    useEffect(() => {
        fetchTickets();
    }, []);

    return (
        <div className='option-body'>
            <NavBar></NavBar>
            <h1>Ticket List</h1>

            <button className="reload-button" onClick={fetchTickets}>Reload Table</button>
            <button className="init-button" onClick={saveInitAllTickets}>Initialize 1</button>
            <button className="init-button" onClick={initTickets}>Initialize 2</button>

            <table className="repair-table">
                <thead>
                <tr>
                    <th>ID Ticket</th>
                    <th>Brand</th>
                    <th>Model</th>
                    <th>Vehicle Type</th>
                    <th>Year</th>
                    <th>Total Surcharges</th>
                    <th>Total Discounts</th>
                    <th>Sub Total</th>
                    <th>IVA Value</th>
                    <th>Base Price</th>
                    <th>Total Price</th>
                    <th>License Plate</th>
                    <th>Pickup Date</th>
                    <th>Entry Date</th>
                    <th>Exit Date</th>
                    <th>Exit Time</th>
                    <th>Pickup Time</th>
                </tr>
                </thead>
                <tbody>
                {tickets.map((ticket, index) => (
                    <tr key={index}>
                        <td>{ticket.idTicket}</td>
                        <td>{ticket.brand}</td>
                        <td>{ticket.model}</td>
                        <td>{ticket.vehicleType}</td>
                        <td>{ticket.year}</td>
                        <td>{ticket.totalSurcharges}</td>
                        <td>{ticket.totalDiscounts}</td>
                        <td>{ticket.subTotal}</td>
                        <td>{ticket.ivaValue}</td>
                        <td>{ticket.basePrice}</td>
                        <td>{ticket.totalPrice}</td>
                        <td>{ticket.licensePlate}</td>
                        <td>{format(ticket.pickupDate, "yyyy-MM-dd'T'HH:mm")}</td>
                        <td>{format(ticket.entryDate, "yyyy-MM-dd'T'HH:mm")}</td>
                        <td>{format(ticket.exitDate, "yyyy-MM-dd'T'HH:mm")}</td>
                        <td>{ticket.exitTime}</td>
                        <td>{ticket.pickupTime}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default TicketList;