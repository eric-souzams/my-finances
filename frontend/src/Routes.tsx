import { BrowserRouter, Switch, Route, Redirect } from 'react-router-dom';
import { NavBar } from './components/NavBar';
import { AuthContext } from './contexts/AuthContext';

import { Home } from './pages/Home';
import { LandingPage } from './pages/LandingPage';
import { Login } from './pages/Login';
import { NewRelease } from './pages/Releases/NewRelease';
import { Releases } from './pages/Releases/Releases';
import { Signup } from './pages/Signup';

type AuthRouteProps = {
  exact?: boolean;
  path: string;
  component: React.ComponentType<any>;
  isAuthenticatedUser: boolean;
}

function AuthenticatedRoute({ component: Component, isAuthenticatedUser, ...rest }: AuthRouteProps) {
  return (
    <Route {...rest} render={(props) => {
      if (isAuthenticatedUser) {
        return (
          <Component {...props} />
        )
      } else {
        return (
          <Redirect to={{ pathname: '/login', state: { from: props.location } }} />
        );
      }
    }}
    />
  );
}

export function Routes() {
  return (
    <AuthContext.Consumer>
      {(context) => (
        <BrowserRouter>
          <NavBar />
          <Switch>
            <Route path="/" exact component={LandingPage} />
            <Route path="/login" component={Login} />
            <Route path="/signup" component={Signup} />

            <AuthenticatedRoute isAuthenticatedUser={context.isAuthenticated} path="/home" component={Home} />
            <AuthenticatedRoute isAuthenticatedUser={context.isAuthenticated} path="/releases" component={Releases} />
            <AuthenticatedRoute isAuthenticatedUser={context.isAuthenticated} path="/new-release/:id?" component={NewRelease} />
          </Switch>
        </BrowserRouter>
      )}
    </AuthContext.Consumer>
  );
}
