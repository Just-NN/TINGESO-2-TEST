import axios from 'axios';

const API_URL = 'http://gateway-server-service/api/v1/report';

function getReportById(id) {
    return axios.get(`${API_URL}/${id}`);
}

function saveReport(report) {
    return axios.post(`${API_URL}/`, report);
}

function deleteReport(id) {
    return axios.delete(`${API_URL}/${id}`);
}

function updateReport(report) {
    return axios.put(`${API_URL}/`, report);
}

function saveR1(report) {
    return axios.post(`${API_URL}/r1`, report);
}

function saveR2(report) {
    return axios.post(`${API_URL}/r2`, report);
}

function saveR3(report) {
    return axios.post(`${API_URL}/r3`, report);
}

function saveR4(report) {
    return axios.post(`${API_URL}/r4`, report);
}

function saveInit(report) {
    return axios.put(`${API_URL}/init`, report);
}

export default {
    getReportById,
    saveReport,
    deleteReport,
    updateReport,
    saveR1,
    saveR2,
    saveR3,
    saveR4,
    saveInit
}