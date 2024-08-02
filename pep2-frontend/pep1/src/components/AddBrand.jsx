import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import brandBonusService from '../services/bonusbrand.service.js';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import NavBar from "./NavBar.jsx";

const AddBrandBonus = () => {
    const [brand, setBrand] = useState(null);
    const [bonus, setBonus] = useState(null);
    const [active, setActive] = useState(true); // Set active to true by default

    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (id) {
            brandBonusService.getBonusBrandById(id).then((response) => {
                const brandBonus = response.data;
                setBrand(brandBonus.brand);
                setBonus(brandBonus.bonus);
                setActive(brandBonus.active);
            });
        }
    }, [id]);

    const handleSubmit = (event) => {
        event.preventDefault();

        const brandBonus = {
            brand,
            bonus,
            active
        };
        console.log(brandBonus)

        if (id) {
            console.log("NO UPDATES ALLOWED");
            navigate('/brands');
        } else {
            brandBonusService.saveBonusBrand(brandBonus).then(() => {
                console.log("Brand Bonus saved")
                navigate('/brands/list');
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
                <Label for="bonus">Bonus</Label>
                <Input type="number" id="bonus" value={bonus || ''} onChange={(e) => setBonus(e.target.value)} />
            </FormGroup>
            <FormGroup check>
                <Label check>
                    <Input type="checkbox" id="active" checked={active} onChange={(e) => setActive(e.target.checked)} />{' '}
                    Active
                </Label>
            </FormGroup>
            <Button type="submit" color="primary" >Submit</Button>
        </Form>
    );
};

export default AddBrandBonus;