import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/repair';

function getRepairById(id) {
    return axios.get(`${API_URL}/${id}`);
}

function saveRepair(repair) {
    return axios.post(`${API_URL}/`, repair);
}

function deleteRepair(id) {
    return axios.delete(`${API_URL}/${id}`);
}

function updateRepair(repair) {
    return axios.put(`${API_URL}/`, repair);
}

function getAllRepairs() {
    return axios.get(`${API_URL}/`);
}

function getRepairsByTicket(id) {
    return axios.get(`${API_URL}/byticket/${id}`);
}

function setBasePrice(repair) {
    return axios.put(`${API_URL}/setBasePrice`, repair);
}

function calculateKMSurcharge(repair, percentage) {
    return axios.put(`${API_URL}/calculateKMSurcharge/${percentage}`, repair);
}

function calculateAgeSurcharge(repair, percentage) {
    return axios.put(`${API_URL}/calculateAgeSurcharge/${percentage}`, repair);
}

function calculateDelaySurcharge(repair, percentage) {
    return axios.put(`${API_URL}/calculateDelaySurcharge/${percentage}`, repair);
}

function calculateDayDiscount(repair, percentage) {
    return axios.put(`${API_URL}/calculateDayDiscount/${percentage}`, repair);
}

function calculateRepairsDiscount(repair, percentage) {
    return axios.put(`${API_URL}/calculateRepairsDiscount/${percentage}`, repair);
}

function calculateTotalPrice(repair) {
    return axios.put(`${API_URL}/calculateTotalPrice`, repair);
}

export default {
    getRepairById,
    saveRepair,
    deleteRepair,
    updateRepair,
    getAllRepairs,
    getRepairsByTicket,
    setBasePrice,
    calculateKMSurcharge,
    calculateAgeSurcharge,
    calculateDelaySurcharge,
    calculateDayDiscount,
    calculateRepairsDiscount,
    calculateTotalPrice
}