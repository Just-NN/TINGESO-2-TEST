import Navbar from "./NavBar.jsx";
import './theme.css';

const Home = () => {
  return (
      <div className="home-container">
        <div className="navbar-container">
          <Navbar />
        </div>
        <div className="content-container">
          <h1>AutoFix: Cadena de talleres</h1>
          <p>
            AutoFix, una cadena de talleres especializados en el mantenimiento y reparación de
            vehículos (Sedan, Hatchback, SUV, Pickup, y Furgonetas) enfrenta desafíos en la gestión
            eficiente de sus servicios de reparación debido a la creciente demanda y a la diversidad de
            modelos y necesidades de reparación específicas de estos vehículos. La gestión manual de las
            reparaciones ha resultado en tiempos de espera prolongados para los clientes, dificultades en
            el seguimiento de la historia de reparación de los vehículos trayendo como consecuencia
            serios reclamos de los clientes.
          </p>
        </div>
      </div>
  );
};

export default Home;