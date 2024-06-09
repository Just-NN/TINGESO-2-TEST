import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ticketService from '../services/ticket.service.js';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import { formatISO, parseISO } from 'date-fns';
import NavBar from "./NavBar.jsx";

const AddTicket = () => {
    const [idTicket, setIdTicket] = useState(null);
    const [pickupDate, setPickupDate] = useState(formatISO(new Date()));
    const [entryDate, setEntryDate] = useState(formatISO(new Date()));
    const [exitDate, setExitDate] = useState(formatISO(new Date()));
    const [exitTime, setExitTime] = useState('');
    const [pickupTime, setPickupTime] = useState('');
    const [licensePlate, setLicensePlate] = useState('');

    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (id) {
            ticketService.get(id).then((response) => {
                const ticket = response.data;
                setIdTicket(ticket.idTicket);
                setPickupDate(ticket.pickupDate ? formatISO(parseISO(ticket.pickupDate)) : formatISO(new Date()));
                setEntryDate(ticket.entryDate ? formatISO(parseISO(ticket.entryDate)) : formatISO(new Date()));
                setExitDate(ticket.exitDate ? formatISO(parseISO(ticket.exitDate)) : formatISO(new Date()));
                setExitTime(ticket.exitTime);
                setPickupTime(ticket.pickupTime);
                setLicensePlate(ticket.licensePlate);
            });
        }
    }, [id]);

    const handleSubmit = (event) => {
        event.preventDefault();

        let ticket = {
            idTicket,
            pickupDate: pickupDate ? pickupDate : null,
            entryDate: entryDate ? entryDate : null,
            exitDate: exitDate ? exitDate : null,
            exitTime,
            pickupTime,
            licensePlate,
        };

        // First, check if the ticket exists in the database
        ticketService.getTicketById(idTicket).then((response) => {
            // If the ticket exists, perform an update (PUT request)
            console.log("EXISTE")
            ticketService.updateTicket(ticket).then(() => {
                console.log("Ticket updated!")
                navigate('/tickets');
            }).catch(error => {
                console.error('There was an error!', error);
            });
        }).catch(error => {
            // If the server responds with a 404 error, perform a save (POST request)
            if (error.response && error.response.status === 404) {
                console.log("NO EXISTE AUN")
                ticketService.saveTicket(ticket).then(() => {
                    console.log("Ticket saved!")
                    navigate('/tickets');
                }).catch(error => {
                    console.error('There was an error!', error);
                });
            } else {
                console.error('There was an error!', error);
            }
        });
    };

    return (
        <Form onSubmit={handleSubmit}>
            <NavBar />
            <FormGroup>
                <Label for="idTicket">ID Ticket</Label>
                <Input type="number" id="idTicket" value={idTicket || ''} onChange={(e) => setIdTicket(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="pickupDate">Pickup Date</Label>
                <Input type="datetime-local" id="pickupDate" value={pickupDate} onChange={(e) => setPickupDate(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="entryDate">Entry Date</Label>
                <Input type="datetime-local" id="entryDate" value={entryDate} onChange={(e) => setEntryDate(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="exitDate">Exit Date</Label>
                <Input type="datetime-local" id="exitDate" value={exitDate} onChange={(e) => setExitDate(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="licensePlate">License Plate</Label>
                <Input type="number" id="licensePlate" value={licensePlate || ''} onChange={(e) => setLicensePlate(e.target.value)} />
            </FormGroup>
            <Button type="submit" color="primary">Submit</Button>
        </Form>
    );
};

export default AddTicket;