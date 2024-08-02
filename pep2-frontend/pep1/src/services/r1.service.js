import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/r1';

const getR1ById = (id) => axios.get(`${API_URL}/${id}`);

const saveR1 = (r1) => axios.post(API_URL, r1);

const deleteR1 = (id) => axios.delete(`${API_URL}/${id}`);

const updateR1 = (r1) => axios.put(API_URL, r1);

const deleteAllR1 = () => axios.delete(`${API_URL}/all`);

const initializeValues = (id, month, year) => axios.put(`${API_URL}/init/${id}/${month}/${year}`);

const createEmptyR1 = () => axios.post(`${API_URL}/empty`);

const getVehicleRepairs = (id) => axios.get(`${API_URL}/repairs/${id}`);

const getMonthRepairs = (id, month) => axios.get(`${API_URL}/monthRepairs/${id}`);

export default {
    getR1ById,
    saveR1,
    deleteR1,
    updateR1,
    deleteAllR1,
    initializeValues,
    createEmptyR1,
    getVehicleRepairs,
    getMonthRepairs
};