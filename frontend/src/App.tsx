import { Routes } from "./Routes";

import 'bootswatch/dist/solar/bootstrap.min.css';
import './styles/global.css';
import 'toastr/build/toastr.min.css'

import 'toastr/build/toastr.min.js'
import { AuthContextProvider } from "./contexts/AuthContext";

function App() {
  return (
    <AuthContextProvider>
      <Routes />
    </AuthContextProvider>
  );
}

export default App;
