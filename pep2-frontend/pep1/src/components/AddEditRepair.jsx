import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import repairService from '../services/repair.service.js';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import { format } from 'date-fns';
import NavBar from "./NavBar.jsx";

const AddEditRepair = () => {
    const [idRepair, setIdRepair] = useState(null);
    const [idTicket, setIdTicket] = useState(null);
    const [repairType, setRepairType] = useState(null);


    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (id) {
            repairService.get(id).then((response) => {
                const repair = response.data;
                setIdRepair(repair.idRepair);
                setIdTicket(repair.idTicket);
                setRepairType(repair.repairType);
            });
        }
    }, [id]);

    const handleSubmit = (event) => {
        event.preventDefault();

        let repair = {
            idTicket,
            repairType  // Add this line
        };

        if (idRepair) {
            repair.idRepair = idRepair;
            repairService.updateRepair(repair).then(() => {
                navigate('/repairs');
            });
        } else {
            console.log(repair)
            repairService.saveRepair(repair).then(() => {
                navigate('/repairs');
            });
        }
    };

    return (
        <Form onSubmit={handleSubmit}>
            <NavBar></NavBar>
            <FormGroup>
                <Label for="idTicket">ID Ticket</Label>
                <Input type="number" id="idTicket" value={idTicket || ''} onChange={(e) => setIdTicket(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="repairType">Repair Type</Label>
                <Input type="number" id="repairType" min="1" max="11" value={repairType || ''} onChange={(e) => setRepairType(e.target.value)} />
            </FormGroup>
            <Button type="submit" color="primary" >Submit</Button>
        </Form>
    );
};

export default AddEditRepair;