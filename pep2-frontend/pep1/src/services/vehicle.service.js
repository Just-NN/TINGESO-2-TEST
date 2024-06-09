import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/vehicle';

function saveVehicle(data) {
    return axios.post(API_URL+"/", data);
}
function getVehicles() {
    return axios.get(API_URL + "/");
}
function getVehicle(id) {
    return axios.get(API_URL + "/" + id);
}
function updateVehicle(id, data) {
    return axios.put(API_URL + "/" + id, data);
}
function deleteVehicle(id) {
    return axios.delete(API_URL + "/" + id);
}
export default {
    saveVehicle,
    getVehicles,
    getVehicle,
    updateVehicle,
    deleteVehicle
}
