import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import repairService from '../services/repair.service';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import { format } from 'date-fns';
import NavBar from "./NavBar.jsx";

const AddEditRepair2 = () => {
    const [idRepair, setIdRepair] = useState(null);
    const [idTicket, setIdTicket] = useState(null);
    const [licensePlate, setLicensePlate] = useState(null);
    const [repairType, setRepairType] = useState(null);
    const [entryDateTime, setEntryDateTime] = useState(new Date());
    const [exitDateTime, setExitDateTime] = useState(new Date());
    const [exitTime, setExitTime] = useState(null);
    const [pickupDateTime, setPickupDateTime] = useState(new Date());
    const [pickupTime, setPickupTime] = useState(null);
    const [totalRepairAmount, setTotalRepairAmount] = useState(null);
    const [kmSurcharge, setKmSurcharge] = useState(null);
    const [ageSurcharge, setAgeSurcharge] = useState(null);
    const [delaySurcharge, setDelaySurcharge] = useState(null);
    const [dayDiscount, setDayDiscount] = useState(null);
    const [repairsDiscount, setRepairsDiscount] = useState(null);
    const [basePrice, setBasePrice] = useState(0);
    const [totalPrice, setTotalPrice] = useState(0);

    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (id) {
            repairService.get(id).then((response) => {
                const repair = response.data;
                setIdRepair(repair.idRepair);
                setIdTicket(repair.idTicket);
                setLicensePlate(repair.licensePlate);
                setRepairType(repair.repairType);
                setEntryDateTime(repair.entryDateTime ? new Date(repair.entryDateTime) : new Date());
                setExitDateTime(repair.exitDateTime ? new Date(repair.exitDateTime) : new Date());
                setExitTime(repair.exitTime);
                setPickupDateTime(repair.pickupDateTime ? new Date(repair.pickupDateTime) : new Date());
                setPickupTime(repair.pickupTime);
                setTotalRepairAmount(repair.totalRepairAmount);
                setKmSurcharge(repair.kmSurcharge);
                setAgeSurcharge(repair.ageSurcharge);
                setDelaySurcharge(repair.delaySurcharge);
                setDayDiscount(repair.dayDiscount);
                setRepairsDiscount(repair.repairsDiscount);
                setBasePrice(repair.basePrice);
                setTotalPrice(repair.totalPrice);
            });
        }
    }, [id]);

    const handleSubmit = (event) => {
        event.preventDefault();

        let repair = {
            licensePlate,
            repairType,
            entryDateTime: entryDateTime instanceof Date && !isNaN(entryDateTime) ? format(entryDateTime, "yyyy-MM-dd'T'HH:mm") : null,
            exitDateTime: exitDateTime instanceof Date && !isNaN(exitDateTime) ? format(exitDateTime, "yyyy-MM-dd'T'HH:mm") : null,
            exitTime,
            pickupDateTime: pickupDateTime instanceof Date && !isNaN(pickupDateTime) ? format(pickupDateTime, "yyyy-MM-dd'T'HH:mm") : null,
            pickupTime,
            totalRepairAmount,
            kmSurcharge,
            ageSurcharge,
            delaySurcharge,
            dayDiscount,
            repairsDiscount,
            basePrice,
            totalPrice
        };

        if (idRepair) {
            repair.idRepair = idRepair;
            repair.idTicket = idTicket;
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
                <Label for="licensePlate">License Plate</Label>
                <Input type="number" id="licensePlate" value={licensePlate || ''} onChange={(e) => setLicensePlate(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="repairType">Repair Type</Label>
                <Input type="number" id="repairType" value={repairType || ''} onChange={(e) => setRepairType(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="entryDateTime">Entry Date and Time</Label>
                <Input type="datetime-local" id="entryDateTime" value={format(entryDateTime, "yyyy-MM-dd'T'HH:mm")} onChange={(e) => setEntryDateTime(new Date(e.target.value))} />
            </FormGroup>
            <FormGroup>
                <Label for="exitDateTime">Exit Date and Time</Label>
                <Input type="datetime-local" id="exitDateTime" value={format(exitDateTime, "yyyy-MM-dd'T'HH:mm")} onChange={(e) => setExitDateTime(new Date(e.target.value))} />
            </FormGroup>
            <FormGroup>
                <Label for="exitTime">Exit Time</Label>
                <Input type="time" id="exitTime" value={exitTime || ''} onChange={(e) => setExitTime(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="pickupDateTime">Pickup Date and Time</Label>
                <Input type="datetime-local" id="pickupDateTime" value={format(pickupDateTime, "yyyy-MM-dd'T'HH:mm")} onChange={(e) => setPickupDateTime(new Date(e.target.value))} />
            </FormGroup>
            <FormGroup>
                <Label for="pickupTime">Pickup Time</Label>
                <Input type="time" id="pickupTime" value={pickupTime || ''} onChange={(e) => setPickupTime(e.target.value)} />
            </FormGroup>
            <Button type="submit" color="primary" >Submit</Button>
        </Form>
    );
};

export default AddEditRepair2;