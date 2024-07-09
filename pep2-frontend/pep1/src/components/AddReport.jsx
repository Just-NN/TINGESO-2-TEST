import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import r1Service from '../services/r1.service.js';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import NavBar from "./NavBar.jsx";

const AddBrandBonus = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (!id) {
            // If there's no ID, we're creating a new report, so no need to fetch existing report details
            console.log("Creating a new report");
        }
    }, [id]);

    const handleSubmit = (event) => {
        event.preventDefault();

        const emptyReport = {
            // R1 is just the list of repairs, so we just need to create an empty list and then fill it when initializing the report
            vehicleRepairIds: []
        };

        if (!id) {
            // Call the service to save a new report with the empty or default values
            r1Service.createEmptyR1(emptyReport).then(() => {
                console.log("Report created");
                navigate('/reports'); // Returning to the reports menu
            }).catch((error) => {
                console.error("Error creating report", error);
            });
        }
    };

    return (
        <Form onSubmit={handleSubmit}>
            <NavBar></NavBar>
            <Button type="submit" color="primary">Submit</Button>
        </Form>
    );
};

export default AddBrandBonus;