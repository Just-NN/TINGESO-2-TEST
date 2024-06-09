import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import vehicleService from '../services/vehicle.service';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import NavBar from "./NavBar.jsx";

const AddVehicle = () => {
    const [licensePlate, setLicensePlate] = useState(null);
    const [brand, setBrand] = useState(null);
    const [model, setModel] = useState(null);
    const [vehicleType, setVehicleType] = useState(null);
    const [mileage, setMileage] = useState(null);
    const [year, setYear] = useState(null);
    const [engineType, setEngineType] = useState(null);
    const [seats, setSeats] = useState(null);

    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (id) {
            vehicleService.getVehicle(id).then((response) => {
                const vehicle = response.data;
                setLicensePlate(vehicle.licensePlate);
                setBrand(vehicle.brand);
                setModel(vehicle.model);
                setVehicleType(vehicle.vehicleType);
                setMileage(vehicle.mileage);
                setYear(vehicle.year);
                setEngineType(vehicle.engineType);
                setSeats(vehicle.seats);
            });
        }
    }, [id]);

    const handleSubmit = (event) => {
        event.preventDefault();

        const vehicle = {
            licensePlate,
            brand,
            model,
            vehicleType,
            mileage,
            year,
            engineType,
            seats
        };
        console.log(vehicle)

        if (id) {
            vehicleService.updateVehicle(vehicle).then(() => {
                navigate('/vehicles');
            });
        } else {
            vehicleService.saveVehicle(vehicle).then(() => {
                navigate('/vehicles');
            });
        }
    };

    return (

        <Form onSubmit={handleSubmit}>
            <NavBar></NavBar>
            <FormGroup>
                <Label for="brand">Brand</Label>
                <Input type="text" id="brand" value={brand || ''} onChange={(e) => setBrand(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="model">Model</Label>
                <Input type="text" id="model" value={model || ''} onChange={(e) => setModel(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="vehicleType">Vehicle Type</Label>
                <Input
                    type="number"
                    id="vehicleType"
                    value={vehicleType || ''}
                    onChange={(e) => {
                        let value = parseInt(e.target.value);
                        if (value >= 0 && value <= 3) {
                            setVehicleType(value);
                        }
                    }}
                />
            </FormGroup>
            <FormGroup>
                <Label for="mileage">KM</Label>
                <Input type="number" id="mileage" value={mileage || ''} onChange={(e) => setMileage(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="year">Year</Label>
                <Input type="number" id="year" value={year || ''} onChange={(e) => setYear(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="engineType">Engine Type</Label>
                <Input type="number" id="engineType" value={engineType || ''} onChange={(e) => setEngineType(e.target.value)} />
            </FormGroup>
            <FormGroup>
                <Label for="seats">Seats</Label>
                <Input type="number" id="seats" value={seats || ''} onChange={(e) => setSeats(e.target.value)} />
            </FormGroup>
            <Button type="submit" color="primary" >Submit</Button>
        </Form>
    );
};

export default AddVehicle;