import './App.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./components/Home.jsx";
import Repairs from "./components/RepairsHome.jsx";
import Vehicles from "./components/VehiclesHome.jsx";
import BonusBrands from "./components/BonusBrandsHome.jsx";
import Reports from "./components/ReportsHome.jsx";
import Tickets from "./components/TicketsHome.jsx";
import RepairList from "./components/RepairList.jsx";
import AddEditRepair from "./components/AddEditRepair.jsx";
import RepairDetail from "./components/FindRepairById.jsx";
import AddVehicle from "./components/AddVehicle.jsx";
import ReportList from "./components/ReportList.jsx";
import TicketList from "./components/TicketList.jsx";
import AddBrand from "./components/AddBrand.jsx";
import BonusBrandList from "./components/BonusList.jsx";
import AddTicket from "./components/AddTicket.jsx";
import ReportList2 from "./components/Report2List.jsx"; // Ensure this import is correct


function App() {
    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route path="/home" element={<Home />} />
                    <Route path="/vehicles" element={<Vehicles />} />
                    <Route path="/repairs" element={<Repairs />} />
                    <Route path="/tickets" element={<Tickets />} />
                    <Route path="/brands" element={<BonusBrands />} />
                    <Route path="/reports" element={<Reports />} />
                    <Route path="/repairs/list" element={<RepairList />} />
                    <Route path="/repairs/add" element={<AddEditRepair />} />
                    <Route path="/repairs/:id" element={<RepairDetail />} />
                    <Route path="/vehicles/add" element={<AddVehicle />} />
                    <Route path="/reports/1" element={<ReportList />} />
                    <Route path="/tickets/list" element={<TicketList />} />
                    <Route path="/brands/add" element={<AddBrand />} />
                    <Route path="/brands/list" element={<BonusBrandList />} />
                    <Route path="/tickets/add" element={<AddTicket />} />
                    <Route path="/reports/2" element={<ReportList2 />} />
                    

                </Routes>
            </BrowserRouter>
        </>
    );
}

export default App;