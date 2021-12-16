import { createContext, ReactNode, useState } from "react";
import AuthService from "../services/authService";

type AuthContextType = {
  isAuthenticated: boolean;
  authenticatedUser: User | undefined;
  startSession: (user: User) => void;
  endSession: () => void;
}

type User = {
  id: number;
  nome: string;
  email: string;
  dataCadastro: Array<number>;
}

type AuthContextProviderProps = {
  children: ReactNode;
}

export const AuthContext = createContext({} as AuthContextType);
export const AuthConsumer = AuthContext.Consumer;

export function AuthContextProvider(props: AuthContextProviderProps) {
  const [authenticatedUser, setAuthenticatedUser] = useState<User>();
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  function startSession(user: User) {
    AuthService.signIn(user);
    setIsAuthenticated(true);
    setAuthenticatedUser(user);
  }

  function endSession() {
    AuthService.logout();
    setIsAuthenticated(false);
    setAuthenticatedUser(undefined);
  }

  return (
    <AuthContext.Provider 
      value={{ 
        isAuthenticated, 
        authenticatedUser, 
        startSession, 
        endSession 
      }}
    >
      {props.children}
    </AuthContext.Provider>
  );
}