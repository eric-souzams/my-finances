import { BrowserRouter, Switch, Route } from 'react-router-dom';
import { NavBar } from './components/NavBar';

import { Home } from './pages/Home';
import { LandingPage } from './pages/LandingPage';
import { Login } from './pages/Login';
import { Signup } from './pages/Signup';

export function Routes() {
    return (
        <BrowserRouter>
            <NavBar />
            <Switch>
                <Route path="/" exact component={LandingPage} />
                <Route path="/home"  component={Home} />
                <Route path="/login" component={Login} />
                <Route path="/signup" component={Signup} />
            </Switch>
        </BrowserRouter>
    );
}