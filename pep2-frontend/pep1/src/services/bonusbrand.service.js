import axios from 'axios';

const API_URL = 'http://gateway-server-service/api/v1/bonusBrand';

function getBonusBrandById(id) {
    return axios.get(`${API_URL}/${id}`);
}

function saveBonusBrand(bonusBrand) {
    return axios.post(`${API_URL}/`, bonusBrand);
}

function deleteBonusBrand(id) {
    return axios.delete(`${API_URL}/${id}`);
}

function updateBonusBrand(bonusBrand) {
    return axios.put(`${API_URL}/`, bonusBrand);
}

function getBonusByBrand(brand) {
    return axios.get(`${API_URL}/bonus/${brand}`);
}
function getAllBonusBrands() {
    return axios.get(`${API_URL}/`);
}

export default {
    getBonusBrandById,
    getAllBonusBrands,
    saveBonusBrand,
    deleteBonusBrand,
    updateBonusBrand,
    getBonusByBrand
}